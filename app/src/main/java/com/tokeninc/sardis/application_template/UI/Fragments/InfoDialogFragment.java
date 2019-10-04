package com.tokeninc.sardis.application_template.UI.Fragments;

import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokeninc.sardis.application_template.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class InfoDialogFragment extends DialogFragment {

    private static final String PARAM_TYPE = "TYPE";
    private static final String PARAM_TEXT = "TEXT";

    private TextView mTextView;
    private ImageView mImageView;
    private InfoType type;
    private String text;

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

    /**
     * @param type Animation type to be shown on dialog screen.
     * @param infoText Text to be shown under animation.
     */
    public static InfoDialogFragment newInstance(InfoType type, String infoText) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAM_TYPE, type);
        bundle.putString(PARAM_TEXT, infoText);
        InfoDialogFragment fragment = new InfoDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_NoTitleBar_Fullscreen);

        Bundle args = getArguments();
        if (args != null) {
            type = (InfoType) args.getSerializable(PARAM_TYPE);
            text = args.getString(PARAM_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.component_info_dialog, container, false);
        mImageView = view.findViewById(R.id.ic_anim);
        mTextView = view.findViewById(R.id.tv_info);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        update(type, text);
        return view;
    }

    public void update(InfoType type, String text) {
        this.text = text;
        this.type = type;
        mTextView.setText(text);
        setAnimation();
    }

    private void setAnimation() {
        switch (type) {
            case Confirmed:
            case Warning:
            case Error:
            case Info:
            case Declined:
            case Connecting:
            case Downloading:
            case Uploading:
            case Processing:
                AnimatedVectorDrawable d = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.animated_check);
                mImageView.setImageDrawable(d);
                d.start();
                break;
            case None:
                mImageView.setImageDrawable(null);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onInfoFragmentInteraction();
    }
}
