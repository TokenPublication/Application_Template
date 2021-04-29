package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.token.printerlib.IPrinterService;
import com.token.printerlib.StyledString;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Entity.ResponseCode;
import com.tokeninc.sardis.application_template.Entity.SampleReceipt;
import com.tokeninc.sardis.application_template.Entity.SlipType;
import com.tokeninc.sardis.application_template.Helpers.DataBase.DatabaseHelper;
import com.tokeninc.sardis.application_template.Helpers.PrintHelpers.SalePrintHelper;
import com.tokeninc.sardis.application_template.Helpers.StringHelper;
import com.tokeninc.sardis.application_template.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public class DummySaleActivity extends BaseActivity implements View.OnClickListener {

    int amount = 0;
    public static final int bottomMargin = 120;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_sale);

        databaseHelper = new DatabaseHelper(this);

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
        receipt.setMerchantID(databaseHelper.getMerchantId());
        receipt.setPosID(databaseHelper.getTerminalId());
        receipt.setCardNo(StringHelper.maskCardNumber(cardNo));
        receipt.setFullName(ownerName);
        receipt.setAmount(StringHelper.getAmount(amount));
        receipt.setGroupNo(String.valueOf(databaseHelper.getBatchNo()));
        receipt.setAid("A0000000000031010");
        receipt.setSerialNo(String.valueOf(databaseHelper.getSaleID()));
        receipt.setApprovalCode(StringHelper.GenerateApprovalCode(String.valueOf(databaseHelper.getBatchNo()), String.valueOf(databaseHelper.getTxNo()), String.valueOf(databaseHelper.getSaleID())));
        return receipt;
    }


    public void prepareDummyResponse(ResponseCode code) {
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
    public void onSaleResponseRetrieved(Integer price, ResponseCode code, Boolean hasSlip, SlipType slipType, String cardNo, String ownerName) {

        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("ResponseCode", code.ordinal()); // #1 Response Code

            bundle.putString("CardOwner", SaleActivity.shareCardOwner); // Optional
            bundle.putString("CardNumber", SaleActivity.shareCardNo); // Optional, Card No can be masked
            bundle.putInt("PaymentStatus", 0); // #2 Payment Status
            bundle.putInt("Amount", price); // #3 Amount
            bundle.putInt("Amount2", price);
            bundle.putBoolean("IsSlip", hasSlip);
            bundle.putInt("BatchNo", databaseHelper.getBatchNo());
            if(!SaleActivity.shareCardNo.equals("****")) { // For without card Success response
                bundle.putString("CardNo", StringHelper.MaskTheCardNo(SaleActivity.shareCardNo)); //#5 Card No "MASKED"
            }
            bundle.putString("MID", databaseHelper.getMerchantId()); // #6 Merchant ID
            bundle.putString("TID", databaseHelper.getTerminalId()); // #7 Terminal ID
            bundle.putInt("TxnNo", databaseHelper.getTxNo());
            bundle.putInt("SlipType", slipType.value);

            bundle.putString("RefundInfo", getRefundInfo(ResponseCode.SUCCESS));
            bundle.putString("RefNo", String.valueOf(databaseHelper.getSaleID()));

        if (slipType == SlipType.CARDHOLDER_SLIP || slipType == SlipType.BOTH_SLIPS) {
            bundle.putString("customerSlipData", SalePrintHelper.getFormattedText(getSampleReceipt(SaleActivity.shareCardNo, SaleActivity.shareCardOwner), SlipType.CARDHOLDER_SLIP));
          //  bundle.putByteArray("customerSlipBitmapData",PrintHelper.getBitmap(getApplicationContext()));
        }
        if (slipType == SlipType.MERCHANT_SLIP || slipType == SlipType.BOTH_SLIPS) {
            bundle.putString("merchantSlipData", SalePrintHelper.getFormattedText(getSampleReceipt(SaleActivity.shareCardNo, SaleActivity.shareCardOwner), SlipType.MERCHANT_SLIP));
         //  bundle.putByteArray("merchantSlipBitmapData",PrintHelper.getBitmap(getApplicationContext()));
        }
        bundle.putString("ApprovalCode", getApprovalCode());
        resultIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }

    private String getRefundInfo(ResponseCode response) {
        JSONObject json = new JSONObject();
        try {
            json.put("BatchNo", databaseHelper.getBatchNo());
            json.put("TxnNo", databaseHelper.getTxNo());
            json.put("Amount", amount);
            json.put("RefNo", String.valueOf(databaseHelper.getSaleID()));
            json.put("MID", databaseHelper.getMerchantId());
            json.put("TID", databaseHelper.getTerminalId());
            if(!SaleActivity.shareCardNo.equals("****")) {
                json.put("CardNo", StringHelper.MaskTheCardNo(SaleActivity.shareCardNo));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    private String getApprovalCode() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int approvalCode = sharedPref.getInt("ApprovalCode", 0);
        sharedPref.edit().putInt("ApprovalCode", ++approvalCode).apply();
        return String.format(Locale.ENGLISH, "%06d", approvalCode);
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
