package com.tokeninc.sardis.application_template;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokeninc.sardis.application_template.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class InfoFragment extends Fragment {
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;
    private TextView mAmountTitle;
    private TextView mAmount;
    private TextView mProgressText;
    private ProgressBar mProgressBar;
    private ImageView mTickAnimation;
    private ImageView mAnim;
    private Button mButton;

    public InfoFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info, container, false);

        mButton = view.findViewById(R.id.denemebuton);
        mAnim = view.findViewById(R.id.iv_animation);
        mProgressBar = view.findViewById(R.id.progress_bar_indeterminate);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnim.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                mAnim.setImageResource(R.drawable.animated_check);
                ((Animatable) mAnim.getDrawable()).start();
            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void startProgress(){
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);
    }

    public void stopProgress(){
        mProgressBar.setVisibility(View.INVISIBLE);
    }


    /**
     *
     * @param type 0 For Check Mark, 1 for Red X Mark
     */
    public void showAnim(int type){
        mAnim.setVisibility(View.VISIBLE);
        if(type == 0){
            mAnim.setImageResource(R.drawable.animated_check);
        }else{
            mAnim.setImageResource(R.drawable.animated_cross);
        }
        ((Animatable) mAnim.getDrawable()).start();

    }

    public void setTitle(String text){
        mAmountTitle.setText(text);
    }

    public void setAmount(String text){
        mAmount.setText(text);
    }

    public void setmProgressText(String text){
        mProgressText.setText(text);
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
