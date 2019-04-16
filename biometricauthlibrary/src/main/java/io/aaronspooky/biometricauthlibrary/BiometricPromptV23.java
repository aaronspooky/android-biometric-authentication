package io.aaronspooky.biometricauthlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

@TargetApi(Build.VERSION_CODES.M)
class BiometricPromptV23 {

    private static final String KEY_NAME = UUID.randomUUID().toString();

    private Builder builder;
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManagerCompat.CryptoObject cryptoObject;

    private BiometricPromptV23(Builder builder) {
        this.builder = builder;
    }

    public void authenticate() {
        generateKey();

        if (initCipher()) {
            cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
            FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(builder.context);
            fingerprintManagerCompat.authenticate(cryptoObject, 0, builder.cancellationSignal, new BiometricCallbackV23(builder.callback, builder.dialog), null);
        }
    }

    private void generateKey() {
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
        }
    }

    private boolean initCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;


        } catch (KeyPermanentlyInvalidatedException e) {
            return false;

        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {

            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public static class Builder {
        protected Context context;
        protected BiometricCallback callback;
        protected CancellationSignal cancellationSignal;
        protected BiometricDialog dialog;

        public Builder(Context context, CancellationSignal cancellationSignal) {
            this.context = context;
            this.cancellationSignal = cancellationSignal;
            this.dialog = new BiometricDialog(context);
            this.dialog.setCancellationSignal(cancellationSignal);
        }

        public Builder setTitle(String title) {
            dialog.setTitle(title);
            return this;
        }

        public Builder setSubtitle(String subtitle) {
            dialog.setSubtitle(subtitle);
            return this;
        }

        public Builder setDescription(String description) {
            dialog.setDescription(description);
            return this;
        }

        public Builder setNegativeText(String negativeText) {
            dialog.setNegativeText(negativeText);
            return this;
        }

        public Builder setCallback(BiometricCallback callback) {
            dialog.setCallback(callback);
            this.callback = callback;
            return this;
        }

        public Builder show() {
            dialog.show();
            return this;
        }

        public BiometricPromptV23 build() {
            return new BiometricPromptV23(this);
        }
    }
}
