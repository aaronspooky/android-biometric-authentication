package io.aaronspooky.biometricauthlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.NonNull;

public class BiometricManager {

    private BiometricBuilder builder;

    private BiometricManager(final BiometricBuilder builder) {
        this.builder = builder;
    }

    public void authenticate(@NonNull final BiometricCallback callback) {
        if (!BiometricsUtils.isSdkVersionSupported()) {
            callback.notSupported(BiometricsApiError.SDK_NOT_SUPPORTED);
            return;
        }

        if (!BiometricsUtils.isHardwareSupported(builder.context)) {
            callback.notSupported(BiometricsApiError.HARDWARE_NOT_SUPPORTED);
            return;
        }

        if (!BiometricsUtils.isPermissionGranted(builder.context)) {
            callback.onFailure(BiometricsApiError.PERMISSION_NOT_GRANTED);
            return;
        }

        if (!BiometricsUtils.isFingerPrintAvailable(builder.context)) {
            callback.onFailure(BiometricsApiError.FINGERPRINT_NOT_AVAILABLE);
            return;
        }

        if (BiometricsUtils.isBiometricPromptEnabled()) {
            this.displayBiometricPrompt(callback);
        } else {
            this.displayBiometricPromptV23(callback);
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    private void displayBiometricPrompt(final BiometricCallback callback) {
        BiometricPrompt biometric = new BiometricPrompt.Builder(this.builder.context)
                .setTitle(builder.title)
                .setSubtitle(builder.subtitle)
                .setDescription(builder.description)
                .setNegativeButton(builder.negativeText, builder.context.getMainExecutor(), (dialog, which) -> callback.onFailure(BiometricsApiError.FINGERPRINT_OPERATION_CANCELED))
                .build();

        biometric.authenticate(new CancellationSignal(), builder.context.getMainExecutor(), new BiometricCallbackV28(callback));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void displayBiometricPromptV23(final BiometricCallback callback) {
        BiometricPromptV23 biometric = new BiometricPromptV23.Builder(this.builder.context, new androidx.core.os.CancellationSignal())
                .setTitle(builder.title)
                .setSubtitle(builder.subtitle)
                .setDescription(builder.description)
                .setNegativeText(builder.negativeText)
                .setCallback(callback)
                .show()
                .build();
        biometric.authenticate();
    }

    public static class BiometricBuilder {
        protected Context context;
        protected String title;
        protected String subtitle;
        protected String description;
        protected String negativeText;

        public BiometricBuilder(final Context context) {
            this.context = context;
        }

        public BiometricBuilder setTitle(@NonNull final String title) {
            this.title = title;
            return this;
        }

        public BiometricBuilder setSubtitle(@NonNull final String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public BiometricBuilder setDescription(@NonNull final String description) {
            this.description = description;
            return this;
        }

        public BiometricBuilder setNegativeButtonText(@NonNull final String text) {
            this.negativeText = text;
            return this;
        }

        public BiometricManager build() {
            return new BiometricManager(this);
        }
    }
}
