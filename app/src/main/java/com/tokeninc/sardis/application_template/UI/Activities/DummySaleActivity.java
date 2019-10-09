package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.printertest.IPrinterService;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Entity.ResponseCode;
import com.tokeninc.sardis.application_template.Entity.SampleReceipt;
import com.tokeninc.sardis.application_template.Entity.SlipType;
import com.tokeninc.sardis.application_template.Helpers.StringHelper;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Printer.PrinterDefinitions;
import com.tokeninc.sardis.application_template.UI.Printer.PrinterDefinitions.Alignment;
import com.tokeninc.sardis.application_template.UI.Printer.StyledString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
            onSaleResponseRetrieved(amount, ResponseCode.values()[responseCode], true, SlipType.BOTH_SLIPS);
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

    private void print() {
        SampleReceipt receipt = getSampleReceipt();
        getFormattedText(receipt, SlipType.CARDHOLDER_SLIP);
        getFormattedText(receipt, SlipType.MERCHANT_SLIP);
    }

    private SampleReceipt getSampleReceipt() {
        SampleReceipt receipt = new SampleReceipt();
        receipt.setMerchantName("TOKEN FINTECH");
        receipt.setMerchantID("26854222228");
        receipt.setPosID("000002AC");
        receipt.setCardNo("**** **** **** 2453");
        receipt.setFullName("SERDAR SAMANCIOĞLU");
        receipt.setAmount("100,00 TL");
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

        onSaleResponseRetrieved(amount, code, cbCustomer.isChecked() || cbMerchant.isChecked(), slipType);
    }

    //TODO Data has to be returned to Payment Gateway after sale operation completed via template below using actual data.
    private void onSaleResponseRetrieved(Integer price, ResponseCode code, Boolean hasSlip, SlipType slipType) {
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
            bundle.putString("customerSlipData", getFormattedText(getSampleReceipt(), SlipType.CARDHOLDER_SLIP));
        }
        if (slipType == SlipType.MERCHANT_SLIP || slipType == SlipType.BOTH_SLIPS) {
            bundle.putString("merchantSlipData", getFormattedText(getSampleReceipt(), SlipType.MERCHANT_SLIP));
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

    public String getFormattedText(SampleReceipt receipt, SlipType slipType)
    {
        StyledString styledText = new StyledString();

        styledText.setLineSpacing(0.5f);
        styledText.setFontSize(12);
        styledText.setFontFace(PrinterDefinitions.Font_E.SourceSansPro);
        styledText.addTextToLine(receipt.getMerchantName(), Alignment.Center);

        styledText.newLine();
        styledText.setFontFace(PrinterDefinitions.Font_E.Sans_Semi_Bold);
        styledText.addTextToLine("İŞYERİ NO:", Alignment.Left);
        styledText.setFontFace(PrinterDefinitions.Font_E.SourceSansPro);
        styledText.addTextToLine(receipt.getMerchantID(), Alignment.Right);

        styledText.newLine();
        styledText.setFontFace(PrinterDefinitions.Font_E.Sans_Semi_Bold);
        styledText.addTextToLine("TERMİNAL NO:", Alignment.Left);
        styledText.setFontFace(PrinterDefinitions.Font_E.SourceSansPro);
        styledText.addTextToLine(receipt.getPosID(), Alignment.Right);

        styledText.newLine();
        if (slipType == SlipType.CARDHOLDER_SLIP) {
            styledText.addTextToLine("MÜŞTERİ NÜSHASI", Alignment.Center);
            styledText.newLine();
        }
        else if (slipType == SlipType.MERCHANT_SLIP) {
            styledText.addTextToLine("İŞYERİ NÜSHASI", Alignment.Center);
            styledText.newLine();
        }
        styledText.addTextToLine("SATIŞ", Alignment.Center);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault());
        String time = sdf.format(Calendar.getInstance().getTime());

        styledText.newLine();
        styledText.addTextToLine(time + " " + "C ONLINE", Alignment.Center);

        styledText.newLine();
        styledText.addTextToLine(receipt.getCardNo(), Alignment.Center);

        styledText.newLine();
        styledText.addTextToLine(receipt.getFullName(), Alignment.Center);

        styledText.setLineSpacing(1f);
        styledText.setFontSize(14);
        styledText.setFontFace(PrinterDefinitions.Font_E.Sans_Semi_Bold);
        styledText.newLine();
        styledText.addTextToLine("TUTAR:");
        styledText.addTextToLine(receipt.getAmount(), Alignment.Right);

        styledText.setLineSpacing(0.5f);
        styledText.setFontSize(10);
        styledText.newLine();
        if (slipType == SlipType.CARDHOLDER_SLIP) {
            styledText.addTextToLine("KARŞILIĞI MAL/HİZM ALDIM", Alignment.Center);
        }
        else {
            styledText.addTextToLine("İşlem Şifre Girilerek Yapılmıştır", Alignment.Center);
            styledText.newLine();
            styledText.addTextToLine("İMZAYA GEREK YOKTUR", Alignment.Center);
        }

        styledText.setFontFace(PrinterDefinitions.Font_E.Sans_Bold);
        styledText.setFontSize(12);
        styledText.newLine();
        styledText.addTextToLine("SN: " + "0001");
        styledText.addTextToLine("ONAY KODU: " + "235188", Alignment.Right);

        styledText.setFontSize(8);
        styledText.setFontFace(PrinterDefinitions.Font_E.Sans_Semi_Bold);
        styledText.newLine();
        styledText.addTextToLine("GRUP NO:" + receipt.getGroupNo());

        styledText.newLine();
        styledText.addTextToLine("AID: " + receipt.getAid());

        styledText.newLine();
        styledText.addTextToLine("BU İŞLEM YURT İÇİ KARTLA YAPILMIŞTIR", Alignment.Center);
        styledText.newLine();
        styledText.addTextToLine("BU BELGEYİ SAKLAYINIZ", Alignment.Center);
        styledText.newLine();

        styledText.printBitmap("ykb", 20);
        styledText.addSpace(100);

        //Uncomment this code block to print sample receipt
        /*if (mPrinterService == null) {
            mPrinterService = getPrinterService();
        }
        styledText.print(mPrinterService);*/

        return styledText.toString();
    }
}
