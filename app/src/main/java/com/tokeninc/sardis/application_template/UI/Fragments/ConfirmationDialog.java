package com.tokeninc.sardis.application_template.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokeninc.sardis.application_template.R;

public class ConfirmationDialog extends DialogFragment {

    private Button btnConfirm;
    private Button btnCancel;
    private ConfirmationDialogListener listener;
    private int arg;
    private String title;
    private String text;
    private String confirmButtonText;
    private String cancelButtonText;

    public static ConfirmationDialog newInstance(String title, String text, String confirmButtonText, String cancelButtonText, int arg, ConfirmationDialogListener listener) {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setCancelable(false);
        dialog.buildDialog(title, text, confirmButtonText, cancelButtonText, arg, listener);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.component_confirmation_dialog, container, false);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnConfirm.setText(confirmButtonText);
        btnCancel.setText(cancelButtonText);
        setButtonClickActions();
        ((TextView)view.findViewById(R.id.dialogTitle)).setText(title);
        ((TextView)view.findViewById(R.id.dialogText)).setText(text);
        return view;
    }

    private void setButtonClickActions() {
        btnConfirm.setOnClickListener((view) -> {
            listener.confirmed(arg);
            this.dismiss();
        });

        btnCancel.setOnClickListener((view) -> {
            listener.canceled(arg);
            this.dismiss();
        });
    }

    private void buildDialog(String title, String text, String confirmButtonText, String cancelButtonText, int arg, ConfirmationDialogListener listener) {
        this.listener = listener;
        this.arg = arg;
        this.title = title;
        this.text = text;
        this.confirmButtonText = confirmButtonText;
        this.cancelButtonText = cancelButtonText;
    }

    public interface ConfirmationDialogListener {
        void confirmed(int arg);
        void canceled(int arg);
    }
}
