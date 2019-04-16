package io.aaronspooky.biometricauthlibrary;

public interface BiometricCallback {
    void onSuccess();
    void onFailure(String error);
    void notSupported(String error);
}
