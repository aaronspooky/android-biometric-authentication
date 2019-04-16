package io.aaronspooky.biometricauthlibrary;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

final public class BiometricCallbackV23 extends FingerprintManagerCompat.AuthenticationCallback {

    private BiometricCallback callback;
    private BiometricDialog dialog;

    public BiometricCallbackV23(BiometricCallback callback, BiometricDialog dialog) {
        this.callback = callback;
        this.dialog = dialog;
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);
        callback.onFailure(errString.toString());
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);
        callback.onFailure(helpString.toString());
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        this.dialog.dismiss();
        callback.onSuccess();
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        callback.onFailure(BiometricsApiError.NOT_RECOGNIZED);
    }
}
