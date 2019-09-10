package com.tokeninc.sardis.application_template;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tokeninc.sardis.application_template.Definitions.EditTextFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity implements EditLineListFragment.OnFragmentInteractionListener, InfoFragment.OnFragmentInteractionListener {
    private Map<String,String> mValueMap;
    private List<EditTextFormat> mEditLineFormats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mEditLineFormats = new ArrayList<>();

        mEditLineFormats.add(new EditTextFormat("Select Datee","",-2));
        mEditLineFormats.add(new EditTextFormat("Select time","",-1));
        mEditLineFormats.add(new EditTextFormat("Third line","",1));
        mEditLineFormats.add(new EditTextFormat("4th line with hint","Hint", InputType.TYPE_CLASS_NUMBER));


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
    public void onEditLineListFragmentInteraction(Map<String,String> list) {
        this.mValueMap = new HashMap<String,String>(list);
        InfoFragment infoFragment = new InfoFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main2_editLine_list_frame,infoFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onInfoFragmentInteraction() {

    }
    // to override toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar, menu);
        return true;
    }
    // To manage Toolbar items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Toast.makeText(Main2Activity.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);    }
}
