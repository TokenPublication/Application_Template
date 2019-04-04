package com.tokeninc.sardis.application_template;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class  ListMenuActivity extends AppCompatActivity {
    private ListMenuAdapter listMenuAdapter;
    private RecyclerView recyclerView;
    String[] productNames = {"Geleceği Yazanlar", "Paycell", "Tv+","Dergilik","Bip","GNC","Hesabım","Sim","LifeBox","Merhaba Umut","Yaani","Hayal Ortağım","Goller Cepte","Piri"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menu);

        recyclerView = findViewById(R.id.list_menu_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        listMenuAdapter = new ListMenuAdapter(productNames);
        listMenuAdapter.setmClickListener(
                new ListMenuAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Log.d("POSITION",String.valueOf(position));
                        //Log.d("TEXT",String.valueOf();
                    }
                }
        );
        recyclerView.setAdapter(listMenuAdapter);

    }
}