package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.printertest.IPrinterService;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Entity.ResponseCode;
import com.tokeninc.sardis.application_template.Entity.SampleReceipt;
import com.tokeninc.sardis.application_template.Entity.SlipType;
import com.tokeninc.sardis.application_template.Helpers.PrintHelper;
import com.tokeninc.sardis.application_template.Helpers.StringHelper;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Printer.StyledString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DummySaleActivity extends BaseActivity implements View.OnClickListener {

    int amount = 0;
    public static final int bottomMargin = 120;

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
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            int responseCode = data.getIntExtra("ResponseCode", ResponseCode.CANCELLED.ordinal());
            String cardNo = "**** **** **** ";
            String owner = "";
            if (data.hasExtra("CardOwner")) {
                owner = data.getStringExtra("CardOwner");
            }
            if (data.hasExtra("CardNumber")) {
                String number = data.getStringExtra("CardNumber");
                if (number.length() >= 4) {
                    cardNo = cardNo + number.substring(number.length() - 4);
                }
            }

            onSaleResponseRetrieved(amount, ResponseCode.values()[responseCode], true, SlipType.BOTH_SLIPS, cardNo, owner);
        }
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

    /**
     * Use this method only to print dummy slip.
     * This method will NOT be used in production.
     */
    private void print(StyledString styledText) {
        if (mPrinterService == null) {
            mPrinterService = getPrinterService();
        }
        styledText.print(mPrinterService);
    }

    private SampleReceipt getSampleReceipt(String cardNo, String ownerName) {
        SampleReceipt receipt = new SampleReceipt();
        receipt.setMerchantName("TOKEN FINTECH");
        receipt.setMerchantID("26854222228");
        receipt.setPosID("000002AC");
        receipt.setCardNo(cardNo);
        receipt.setFullName(ownerName);
        receipt.setAmount(StringHelper.getAmount(amount));
        receipt.setGroupNo("0001");
        receipt.setAid("A0000000000031010");
        return receipt;
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

        onSaleResponseRetrieved(amount, code, cbCustomer.isChecked() || cbMerchant.isChecked(), slipType, "**** **** **** ****", "OWNER NAME");
    }

    //TODO Data has to be returned to Payment Gateway after sale operation completed via template below using actual data.
    private void onSaleResponseRetrieved(Integer price, ResponseCode code, Boolean hasSlip, SlipType slipType, String cardNo, String ownerName) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("ResponseCode", code.ordinal());
        bundle.putInt("PaymentStatus",0);
        bundle.putInt("Amount",price);
        bundle.putBoolean("IsSlip", hasSlip);
        bundle.putInt("BatchNo",0);
        bundle.putInt("TxnNo",0);
        bundle.putInt("Amount2", price);
        bundle.putInt("SlipType", slipType.value);

        if (slipType == SlipType.CARDHOLDER_SLIP || slipType == SlipType.BOTH_SLIPS) {
            bundle.putString("customerSlipData", PrintHelper.getFormattedText(getSampleReceipt(cardNo, ownerName), SlipType.CARDHOLDER_SLIP));
        }
        if (slipType == SlipType.MERCHANT_SLIP || slipType == SlipType.BOTH_SLIPS) {
            bundle.putString("merchantSlipData", PrintHelper.getFormattedText(getSampleReceipt(cardNo, ownerName), SlipType.MERCHANT_SLIP));
        }
        resultIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    IPrinterService mPrinterService = null;

    private IPrinterService getPrinterService() {
        IPrinterService mService = null;
        Method method = null;
        try {
            method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, "PrinterService");
            if (binder != null) {
                mService = IPrinterService.Stub.asInterface(binder);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return mService;
    }


}
