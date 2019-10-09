package com.tokeninc.sardis.application_template.UI.Fragments.InfoDialogFragment;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokeninc.sardis.application_template.R;

public class InfoDialog extends DialogFragment {

    private InfoDialogListener listener;
    private TextView tvTitle;
    private TextView tvInfo;
    private ImageView mImageView;
    private ImageView btnConfirm;
    private ImageView btnCancel;
    private InfoType type;
    private String title;
    private String infoText;
    private InfoDialogButtons buttons;
    private ConstraintLayout confirmationLayout;
    private int arg;

    public enum InfoType {
        Confirmed,
        Warning,
        Error,
        Info,
        Declined,
        Connecting,
        Downloading,
        Uploading,
        Processing,
        None
    }

    public enum InfoDialogButtons {
        None,
        Confirm,
        Cancel,
        Both
    }

    /**
     * @param type Animation type to be shown on dialog screen.
     * @param title Text to be shown under animation.
     */
    public static InfoDialog newInstance(InfoType type, String title) {
        InfoDialog fragment = new InfoDialog();
        fragment.type = type;
        fragment.title = title;
        return fragment;
    }

    /**
     * @param type Animation type to be shown on dialog screen.
     * @param title Text to be shown under animation.
     * @param info Description text inside the confirmation box.
     * @param buttons Button types to be shown inside confirmation box.
     * @param arg Argument passed to the listener callback methods which makes callbacks recognizable. Pass different arg parameters for different confirmations.
     * @param listener The listener object which implements {@link InfoDialogListener} interface.
     */
    public static InfoDialog newInstance(InfoType type, String title, String info, InfoDialogButtons buttons, int arg, InfoDialogListener listener) {
        InfoDialog fragment = InfoDialog.newInstance(type, title);
        fragment.infoText = info;
        fragment.buttons = buttons;
        fragment.listener = listener;
        fragment.arg = arg;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.component_info_dialog, container, false);
        mImageView = view.findViewById(R.id.ic_anim);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvInfo = view.findViewById(R.id.tvInfo);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);
        confirmationLayout = view.findViewById(R.id.confirmationLayout);
        setButtonClickActions();

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        update(type, title);
        setConfirmation(view);

        return view;
    }

    private void setConfirmation(View view) {
        if (infoText != null && !infoText.isEmpty()) {
            confirmationLayout.setVisibility(View.VISIBLE);
            tvInfo.setText(infoText);
        }
        if (buttons != null && buttons != InfoDialogButtons.None) {
            confirmationLayout.setVisibility(View.VISIBLE);
            btnConfirm.setVisibility(buttons == InfoDialogButtons.Both || buttons == InfoDialogButtons.Confirm ? View.VISIBLE : View.GONE);
            btnCancel.setVisibility(buttons == InfoDialogButtons.Both || buttons == InfoDialogButtons.Cancel ? View.VISIBLE : View.GONE);
            view.findViewById(R.id.viewDivider).setVisibility(View.VISIBLE);
        }
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

    /**
     * Call this method to update info dialog after it has been created.
     * Note: This method is only for showing progress dialogs in a row, not for confirmation.
     * @param type: Info dialog icon type to update the old one.
     * @param title: Info dialog title to update the old one.
     */
    public void update(InfoType type, String title) {
        this.title = title;
        this.type = type;
        tvTitle.setText(title);
        setAnimation();
        confirmationLayout.setVisibility(View.GONE);
    }

    private void setAnimation() {
        mImageView.setImageDrawable(null);
        mImageView.setBackground(null);
        switch (type) {
            case Confirmed:
                mImageView.setImageResource(R.drawable.success);
                break;
            case Warning:
                mImageView.setImageResource(R.drawable.warning);
                break;
            case Error:
                mImageView.setImageResource(R.drawable.error);
                break;
            case Info:
                mImageView.setImageResource(R.drawable.info);
                break;
            case Declined:
                mImageView.setImageResource(R.drawable.declined);
                break;
            case Connecting:
                break;
            case Downloading:
                mImageView.setBackgroundResource(R.drawable.downloading);
                break;
            case Uploading:
                mImageView.setBackgroundResource(R.drawable.uploading);
                break;
            case Processing:
                //mImageView.setBackgroundResource(R.drawable.installing);
                break;
            case None:
                break;
        }

        if (mImageView.getBackground() != null && mImageView.getBackground() instanceof AnimationDrawable) {
            ((AnimationDrawable) mImageView.getBackground()).setOneShot(false);
            ((AnimationDrawable) mImageView.getBackground()).start();
        }
    }
}
