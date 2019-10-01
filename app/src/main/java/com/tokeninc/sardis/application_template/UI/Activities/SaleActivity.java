package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.tokeninc.sardis.application_template.BaseActivity;

public class SaleActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //get data from payment gateway and process
        Bundle bundle = getIntent().getExtras();
        int amount = bundle.getInt("Amount");
        Object cardReadType = bundle.get("CardReadType"); //Could be any type
        Object cardData = bundle.get("CardData"); //indeterminate for the moment.
        doSale(amount, cardReadType, cardData);
    }

    private void doSale(int amount, Object cardReadType, Object cardData) {
        //TODO fill
    }


    //TODO Data has to be returned to Payment Gateway after sale operation completed via template below using actual data.
    private void onSaleResponseRetrieved(Integer price) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("ResponseCode",0);
        bundle.putInt("PaymentStatus",0);
        bundle.putInt("Amount",price);
        bundle.putInt("IsSlip",0);
        bundle.putInt("BatchNo",0);
        bundle.putInt("TxnNo",0);
        bundle.putInt("Amount2",0);
        resultIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }

}
