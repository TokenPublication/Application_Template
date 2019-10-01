package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokeninc.sardis.application_template.BaseActivity;

public class PosTxnActivity extends BaseActivity {


    public void onPosTxnResponse() {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        //bundle.putString("ResponseCode", PosTxnResponse);
        resultIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, resultIntent);//PosTxn_Request_Code:13
        finish();
    }
}
