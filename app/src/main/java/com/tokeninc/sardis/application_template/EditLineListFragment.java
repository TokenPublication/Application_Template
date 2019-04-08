package com.tokeninc.sardis.application_template;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokeninc.sardis.application_template.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditLineListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditLineListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditLineListFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private List<Pair<String,String>> mTagHintPairsList;
    private List<String> mEditTextValues;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private Button mButton;
    private EditLineListAdapter mEditLineListAdapter;
    private View mView;


    public EditLineListFragment(List<Pair<String, String>> mTagHintPairsList) {
        this.mTagHintPairsList = mTagHintPairsList;
        this.mEditTextValues = new ArrayList<>(mTagHintPairsList.size());
        for (int i = 0; i < mTagHintPairsList.size(); i++) {
            mEditTextValues.add("");
        }
        Log.d("OK","OK");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_edit_line_list, container, false);
        mRecyclerView = mView.findViewById(R.id.rv_edit_list_menu);
        mButton = mView.findViewById(R.id.bttn_edit_line_list_fragment);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mEditLineListAdapter = new EditLineListAdapter(mTagHintPairsList);
        mEditLineListAdapter.setTextWatcherInterface(new EditLineListAdapter.TextWatcherInterface() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after, int tag) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count, int tag) {
                mEditTextValues.set(tag,String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s, int tag) {

            }
        });
        mRecyclerView.setAdapter(mEditLineListAdapter);
        return mView;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction(this.mEditTextValues);
        }
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
        void onFragmentInteraction(List<String> list);
    }
}