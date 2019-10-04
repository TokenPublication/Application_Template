package com.tokeninc.sardis.application_template.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.IListMenuItem;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;
import com.tokeninc.sardis.application_template.UI.Fragments.ConfirmationDialog;
import com.tokeninc.sardis.application_template.UI.Fragments.ListMenuFragment;

import java.util.ArrayList;
import java.util.List;

/*
This activity shows how to use List Menu
 */
public class MainActivity extends BaseActivity implements ConfirmationDialog.ConfirmationDialogListener, ListMenuFragment.ListMenuClickListener {

    private List<IListMenuItem> menuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItems, this);
        addFragment(R.id.container, fragment, false);
    }

    private void prepareData() {
        menuItems.add(new MenuItem("EditLineListFragment"));
        menuItems.add(new MenuItem("InfoDialogFragment"));
        menuItems.add(new MenuItem("ConfirmationDialog"));
    }

    private void startActivity(int menuNo){
        switch (menuNo){
            case 0: {
                Intent myIntent = new Intent(MainActivity.this, EditLineActivity.class);
                startActivity(myIntent);
                break;
            }
            case 1: {
                Intent myIntent = new Intent(MainActivity.this, PosTxnActivity.class);
                startActivity(myIntent);
                break;
            }
            case 2: {
                showConfirmationDialog("Confirmation", "Are you sure?", "Yes", "No", 999, this);
            }
        }
    }

    @Override
    public void confirmed(int arg) {
        Toast.makeText(this, "Confirmed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void canceled(int arg) {
        Toast.makeText(this, "Canceled.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position, IListMenuItem item) {
        startActivity(position);
    }
}
