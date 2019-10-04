package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;

public class PosTxnActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_txn);
    }

    public void onPosTxnResponse() {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        //bundle.putString("ResponseCode", PosTxnResponse);
        resultIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, resultIntent);//PosTxn_Request_Code:13
        finish();
    }
}
