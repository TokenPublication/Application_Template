package com.tokeninc.sardis.application_template.UI.Activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Adapters.MenuItemAdapter;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;
import com.tokeninc.sardis.application_template.UI.Fragments.InfoDialogFragment;

import java.util.ArrayList;
import java.util.List;

/*
This activity shows how to use List Menu
 */
public class InfoDialogActivity extends BaseActivity {
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
                showPopup(position);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(menuItemAdapter);
        //For scrolling performance optimization
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);

        prepareData();
    }

    private void prepareData() {
        menuItems.add(new MenuItem("Confirmed", 0));
        menuItems.add(new MenuItem("Warning", 0));
        menuItems.add(new MenuItem("Error",0));
        menuItems.add(new MenuItem("Info",0));
        menuItems.add(new MenuItem("Declined",0));
        menuItems.add(new MenuItem("Connecting",0));
        menuItems.add(new MenuItem("Downloading",0));
        menuItems.add(new MenuItem("Uploading",0));
        menuItems.add(new MenuItem("Installing",0));

        menuItemAdapter.notifyDataSetChanged();
    }

    private void showPopup(int menuNo){
        switch (menuNo){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7: {
                InfoDialogFragment dialog = showInfoDialog(InfoDialogFragment.InfoType.Confirmed, "Confirmed");
                //Dismiss dialog by calling dialog.dismiss() when needed.
            }
        }
    }
}
