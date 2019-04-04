package com.tokeninc.sardis.application_template;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class  ListMenuActivity extends AppCompatActivity {
    private ListMenuAdapter listMenuAdapter;
    private RecyclerView mRecyclerView;
    String[] productNames = {"Geleceği Yazanlar", "Paycell", "Tv+","Dergilik","Bip","GNC","Hesabım","Sim","LifeBox","Merhaba Umut","Yaani","Hayal Ortağım","Goller Cepte","Piri"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menu);

        mRecyclerView = findViewById(R.id.rv_list_menu);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        listMenuAdapter = new ListMenuAdapter(productNames);
        listMenuAdapter.setmClickListener(
                new ListMenuAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Intent intent = new Intent(ListMenuActivity.this,Main2Activity.class);
                        startActivity(intent);

                    }
                }
        );
        mRecyclerView.setAdapter(listMenuAdapter);

    }
}