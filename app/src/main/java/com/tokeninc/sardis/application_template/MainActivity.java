package com.tokeninc.sardis.application_template;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.tokeninc.sardis.application_template.Adapters.MenuItemAdapter;
import com.tokeninc.sardis.application_template.Definitions.MenuItems;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<MenuItems> menuItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuItemAdapter menuItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        menuItemAdapter = new MenuItemAdapter(menuItems, new MenuItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItems item, int position) {
                Log.d("Menu Adapter", item.getTitle() + "-" +String.valueOf(position));
                startActivity(position)
                ;
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(menuItemAdapter);

        prepareData();
    }

    private void prepareData() {
        menuItems.add(new MenuItems("Edit Line Menu",R.drawable.ic_edit_black_24dp));
        menuItems.add(new MenuItems("Settings", R.drawable.ic_settings_blue));
        menuItems.add(new MenuItems("Item 2", R.drawable.ic_favorite));
        menuItems.add(new MenuItems("Item 3",0));
        menuItems.add(new MenuItems("Item 4",R.drawable.ic_settings_blue));
        menuItems.add(new MenuItems("Item 5",0));
        menuItems.add(new MenuItems("Item 6",R.drawable.ic_settings_blue  ));



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
