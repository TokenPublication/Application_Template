package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Helpers.StringHelper;
import com.tokeninc.sardis.application_template.R;

public class SlipActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slip);

        Bundle bundle = getIntent().getExtras();
        int amount = bundle.getInt("Amount");
        String uuid = bundle.getString("UUID");
        String slipType = bundle.getString("SlipType");
    }

    private void print(int amount, String uuid, String slipType) {
        //...
    }


    private void onPrintCustomerSlipClicked(int amount) {
        String formattedAmount = StringHelper.getAmount(amount);
        //TODO do print operation
        //PrinterTests.printCustomerSlip(StringHelper.getAmount(formattedAmount));
        //if successful, return result to payment gateway like below
        Intent resultIntentPrinted = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("ResponseCode",0);
        resultIntentPrinted.putExtras(bundle);
        setResult(Activity.RESULT_OK,resultIntentPrinted);
        finish();
    }

    private void onPrintMerchantSlipClicked(int amount) {
        String formattedAmount = StringHelper.getAmount(amount);
        //TODO do print operation
        //PrinterTests.printMerchantSlip(makeAmount(price));
        //if successful, return result to payment gateway like below
        Intent resultIntentPrinted = new Intent();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("ResponseCode",0);
        //bundle2.putBoolean("SlipResponse",isMerchantSlipPrinted&isCustomerSlipPrinted);
        resultIntentPrinted.putExtras(bundle2);
        setResult(Activity.RESULT_OK,resultIntentPrinted);
        finish();
    }

}
