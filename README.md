# Android Biometric Authentication
Authentication using biometrics with AndroidX

## Getting started
MinSDK: 23

MaxSDK: 28

For lower versions the ``notSupported`` method is invoked because the ``Fingerprint sensor`` was implemented from Android 6.

Step 1. Add the JitPack repository to your gradle file 

```gradle
allprojects {
  repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
  
```

Step 2. Add the dependency

```gradle
dependencies {
	        implementation 'com.github.aaronspooky:android-biometric-authentication:0.10'
}
```

## Usage

Step 1. Add ``BiometricCallback`` interface
```java
public class MainActivity extends AppCompatActivity implements BiometricCallback {
}
```

Step 2. Add the following methods

```java
// region Biometric Callbacks

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure(String error) {
    }

    @Override
    public void notSupported(String error) {
    }

// endregion
```

Step 3. Add the ``BiometricManager`` builder

```java
new BiometricManager.BiometricBuilder(this)
                    .setTitle("Any text")
                    .setSubtitle("Any text")
                    .setDescription("Any text")
                    .setNegativeButtonText("Cancel")
                    .build()
                    .authenticate(this);
```
