package com.tokeninc.sardis.application_template;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements EditLineListFragment.OnFragmentInteractionListener, InfoFragment.OnFragmentInteractionListener {
    private List<String> mValueList;
    private List<EditTextFormat> mEditLineFormats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mEditLineFormats = new ArrayList<>();

        mEditLineFormats.add(new EditTextFormat("Hello","",0));

        EditLineListFragment editLineListFragment = new EditLineListFragment();
        editLineListFragment.setArguments(mEditLineFormats);
        fragmentTransaction.add(R.id.main2_editLine_list_frame, editLineListFragment);
        fragmentTransaction.commit();

/*
        InfoFragment infoFragment = new InfoFragment();
        fragmentTransaction.add(R.id.main2_editLine_list_frame,infoFragment);
        fragmentTransaction.commit();*/
    }

    @Override
    public void onEditLineListFragmentInteraction(List<String> list) {
        this.mValueList = list;
    }

    @Override
    public void onInfoFragmentInteraction() {

    }
}
