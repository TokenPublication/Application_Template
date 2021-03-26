package com.tokeninc.sardis.application_template.UI.Activities.PosOperations;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Helpers.DataBase.DataModel;
import com.tokeninc.sardis.application_template.Helpers.DataBase.DatabaseHelper;
import com.tokeninc.sardis.application_template.Helpers.DataBase.RecycleAdapter;
import com.tokeninc.sardis.application_template.R;

import java.util.ArrayList;
import java.util.List;

public class SalesActivity extends BaseActivity implements View.OnClickListener {
    DatabaseHelper database;
    List<DataModel> dataModel;

    private RecyclerView rvTransactions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_void);

        rvTransactions = findViewById(R.id.rvTransactions);
        dataModel =new ArrayList<DataModel>();
        readDataSQLite();
    }

    public void readDataSQLite(){
        database = new DatabaseHelper(SalesActivity.this);
        dataModel =  database.getData();
        RecycleAdapter adapter = new RecycleAdapter(dataModel);
        rvTransactions.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

    }
}
