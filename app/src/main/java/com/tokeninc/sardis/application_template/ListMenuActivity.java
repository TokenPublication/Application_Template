package com.tokeninc.sardis.application_template;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tokeninc.sardis.application_template.UI.Adapters.ListMenuAdapter;

public class  ListMenuActivity extends AppCompatActivity {
    private ListMenuAdapter listMenuAdapter;
    private RecyclerView mRecyclerView;
    String[] productNames = {"Geleceği Yazanlar", "Paycell", "Tv+","Dergilik","Bip","GNC","Hesabım","Sim","LifeBox","Merhaba Umut","Yaani","Hayal Ortağım","Goller Cepte","Piri"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menu);
    }
}