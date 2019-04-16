package io.aaronspooky.biometricauthlibrary;

import android.annotation.TargetApi;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.P)
final public class BiometricCallbackV28 extends BiometricPrompt.AuthenticationCallback {

    private BiometricCallback callback;

    public BiometricCallbackV28(BiometricCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
    }

    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
    }
}
