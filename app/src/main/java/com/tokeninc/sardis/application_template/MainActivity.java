package com.tokeninc.sardis.application_template;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tokeninc.sardis.application_template.UI.Adapters.MenuItemAdapter;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import java.util.ArrayList;
import java.util.List;

/*
This activity shows how to use List Menu
 */
public class MainActivity extends AppCompatActivity {
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
        menuItems.add(new MenuItem("Edit Line Menu",R.drawable.ic_edit_black_24dp));
        menuItems.add(new MenuItem("Settings", R.drawable.ic_settings_blue));
        menuItems.add(new MenuItem("Item 2", R.drawable.ic_favorite));
        menuItems.add(new MenuItem("Item 3",0));
        menuItems.add(new MenuItem("Item 4",R.drawable.ic_settings_blue));
        menuItems.add(new MenuItem("Item 5",0));
        menuItems.add(new MenuItem("Item 6",R.drawable.ic_settings_blue  ));



        menuItemAdapter.notifyDataSetChanged();
    }

    void startActivity(int menuNo){
        switch (menuNo){
            case 0:{
                Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                MainActivity.this.startActivity(myIntent);

                break;
            }
            default:{
                Log.d("Main", "No menu");
            }
        }

    }
}
