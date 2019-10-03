package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Entity.ResponseCode;
import com.tokeninc.sardis.application_template.Entity.SlipType;
import com.tokeninc.sardis.application_template.Helpers.StringHelper;
import com.tokeninc.sardis.application_template.R;

public class DummySaleActivity extends BaseActivity implements View.OnClickListener {

    int amount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dummy_sale);

        //get data from payment gateway and process
        Bundle bundle = getIntent().getExtras();
        amount = bundle.getInt("Amount");
        TextView tvAmount = findViewById(R.id.tvAmount);
        tvAmount.setText(StringHelper.getAmount(amount));
        Object cardReadType = bundle.get("CardReadType"); //Could be any type
        Object cardData = bundle.get("CardData"); //indeterminate for the moment.
    }

    private void doSale(Object cardReadType, Object cardData) {
        Intent intent = new Intent(this, SaleActivity.class);
        intent.putExtra("Amount", amount);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSale:
                doSale(null, null);
                break;
            case R.id.btnSuccess:
                prepareDummyResponse(ResponseCode.SUCCESS);
                break;
            case R.id.btnError:
                prepareDummyResponse(ResponseCode.ERROR);
                break;
            case R.id.btnCancel:
                prepareDummyResponse(ResponseCode.CANCELLED);
                break;
            case R.id.btnOffline:
                prepareDummyResponse(ResponseCode.OFFLINE_DECLINE);
                break;
            case R.id.btnUnable:
                prepareDummyResponse(ResponseCode.UNABLE_DECLINE);
                break;
            case R.id.btnOnlineDecline:
                prepareDummyResponse(ResponseCode.ONLINE_DECLINE);
                break;
        }
    }

    private void prepareDummyResponse(ResponseCode code) {
        CheckBox cbMerchant = findViewById(R.id.cbMerchant);
        CheckBox cbCustomer = findViewById(R.id.cbCustomer);

        SlipType slipType = SlipType.NO_SLIP;
        if (cbMerchant.isChecked() && cbCustomer.isChecked())
            slipType = SlipType.BOTH_SLIPS;
        else if (cbMerchant.isChecked())
            slipType = SlipType.MERCHANT_SLIP;
        else if (cbCustomer.isChecked())
            slipType = SlipType.CARDHOLDER_SLIP;

        onSaleResponseRetrieved(1, code, cbCustomer.isChecked() || cbMerchant.isChecked(), slipType);
    }

    //TODO Data has to be returned to Payment Gateway after sale operation completed via template below using actual data.
    private void onSaleResponseRetrieved(Integer price, ResponseCode code, Boolean hasSlip, SlipType slipType) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("ResponseCode", code.ordinal());
        bundle.putInt("PaymentStatus",0);
        bundle.putInt("Amount",price);
        bundle.putInt("IsSlip", hasSlip ? 1 : 0);
        bundle.putInt("BatchNo",0);
        bundle.putInt("TxnNo",0);
        bundle.putInt("Amount2", price);
        bundle.putInt("SlipType", slipType.value);
        resultIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }
}
