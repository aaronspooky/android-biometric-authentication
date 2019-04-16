package io.aaronspooky.biometricauthlibrary;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.core.os.CancellationSignal;

final public class BiometricDialog extends BottomSheetDialog implements View.OnClickListener {

    private BiometricCallback callback;
    private CancellationSignal cancellationSignal;
    private TextView title, subtitle, description;
    private Button negativeButton;

    public BiometricDialog(Context context) {
        super(context, R.style.Theme_Design_BottomSheetDialog);
        this.showDialog();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setSubtitle(String subtitle) {
        this.subtitle.setText(subtitle);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setNegativeText(String negativeText) {
        this.negativeButton.setText(negativeText);
    }

    public void setCallback(BiometricCallback callback) {
        this.callback = callback;
    }

    public void setCancellationSignal(CancellationSignal signal) {
        this.cancellationSignal = signal;
    }

    private void initUIComponents() {
        this.title = findViewById(R.id.fragment_biometric_fragment_dialog_tv_title);
        this.subtitle = findViewById(R.id.fragment_biometric_fragment_dialog_tv_subtitle);
        this.description = findViewById(R.id.fragment_biometric_fragment_dialog_tv_description);
        this.negativeButton = findViewById(R.id.fragment_biometric_fragment_dialog_btn_negative);
    }

    private void showDialog() {
        View dialog = getLayoutInflater().inflate(R.layout.dialog_biometric_fragment, null);
        this.setCanceledOnTouchOutside(false);
        setContentView(dialog);
        this.initUIComponents();
        this.negativeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        this.cancellationSignal.cancel();
        callback.onFailure(BiometricsApiError.FINGERPRINT_OPERATION_CANCELED);
    }
}
