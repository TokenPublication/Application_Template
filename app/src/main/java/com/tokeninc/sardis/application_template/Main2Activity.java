package com.tokeninc.sardis.application_template;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements EditLineListFragment.OnFragmentInteractionListener {
    private List<String> mValueList;
    private List<Pair<String,String>> mPairsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mPairsList = new ArrayList<>();

        mPairsList.add(new Pair<String, String>("Hello",""));
        mPairsList.add(new Pair<String, String>("Bye","Hello"));
        EditLineListFragment editLineListFragment = new EditLineListFragment(mPairsList);
        fragmentTransaction.add(R.id.main2_editLine_list_frame, editLineListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(List<String> list) {
        this.mValueList = list;
    }
}
