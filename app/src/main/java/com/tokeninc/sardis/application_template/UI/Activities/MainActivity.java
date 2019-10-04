package com.tokeninc.sardis.application_template.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Adapters.MenuItemAdapter;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;
import com.tokeninc.sardis.application_template.UI.Fragments.ConfirmationDialog;

import java.util.ArrayList;
import java.util.List;

/*
This activity shows how to use List Menu
 */
public class MainActivity extends BaseActivity implements ConfirmationDialog.ConfirmationDialogListener {
    private List<MenuItem> menuItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuItemAdapter menuItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        menuItemAdapter = new MenuItemAdapter(menuItems, new MenuItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item, int position) {
                Log.d("Menu Adapter", item.getTitle() + "-" +String.valueOf(position));
                startActivity(position)
                ;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(menuItemAdapter);
        prepareData();
    }

    private void prepareData() {
        menuItems.add(new MenuItem("EditLineListFragment", 0));
        menuItems.add(new MenuItem("InfoDialogFragment", 0));
        menuItems.add(new MenuItem("ConfirmationDialog", 0));

        menuItemAdapter.notifyDataSetChanged();
    }

    private void startActivity(int menuNo){
        switch (menuNo){
            case 0: {
                Intent myIntent = new Intent(MainActivity.this, EditLineActivity.class);
                startActivity(myIntent);
                break;
            }
            case 1: {
                Intent myIntent = new Intent(MainActivity.this, InfoDialogActivity.class);
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
}
