package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.token.uicomponents.ListMenuFragment.IListMenuItem;
import com.token.uicomponents.ListMenuFragment.ListMenuFragment;
import com.token.uicomponents.ListMenuFragment.MenuItemClickListener;
import com.token.uicomponents.infodialog.InfoDialog;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Entity.CardReadType;
import com.tokeninc.sardis.application_template.Entity.ICCCard;
import com.tokeninc.sardis.application_template.Entity.ICard;
import com.tokeninc.sardis.application_template.Entity.MSRCard;
import com.tokeninc.sardis.application_template.Entity.ResponseCode;
import com.tokeninc.sardis.application_template.Helpers.DataBase.DataModel;
import com.tokeninc.sardis.application_template.Helpers.DataBase.DatabaseHelper;
import com.tokeninc.sardis.application_template.Helpers.PrintHelpers.DateUtil;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SaleActivity extends BaseActivity implements View.OnClickListener {

    private int amount = 0;

    private List<IListMenuItem> menuItemList;
    private ICard card;

    DatabaseHelper databaseHelper;
    String card_no, sale_amount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        //Prevent screen from turning of when sale is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        databaseHelper = new DatabaseHelper(this);

        amount = getIntent().getExtras().getInt("Amount");
        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItemList, "Sale Type", false, null);
        addFragment(R.id.container, fragment, false);
    }

    private void prepareData() {
        menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem("Sale", new MenuItemClickListener() {
            @Override
            public void onClick(IListMenuItem menuItem) {
                readCard();
            }
        }));
        menuItemList.add(new MenuItem("Installment Sale", (menuItem) -> readCard()));
        menuItemList.add(new MenuItem("Loyalty Sale", (menuItem) -> readCard()));
        menuItemList.add(new MenuItem("Campaign Sale", (menuItem) -> readCard()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetConfig:
                setConfig();
                break;
        }
    }

    /**
     * Read card data and return result with data back to payment gateway.
     * @see DummySaleActivity onSaleResponseRetrieved(Integer, ResponseCode, Boolean, SlipType)
     *
     */
    private void readCard() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("forceOnline", 1);
            obj.put("zeroAmount", 0);
            obj.put("fallback", 1);

            cardServiceBinding.getCard(amount, 40, obj.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takeOutICC() {
        cardServiceBinding.takeOutICC(40);
    }

    private void showInfoDialog() {
        InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Progress, "Connecting", false);
        new Handler().postDelayed(() -> {
            dialog.update(InfoDialog.InfoType.Confirmed, "Success \n Approval Code: 000002");
            new Handler().postDelayed(() -> {
                dialog.update(InfoDialog.InfoType.Progress, "Printing the receipt");
                new Handler().postDelayed(() -> {
                    dialog.dismiss();
                    if (card instanceof ICCCard)
                        takeOutICC();
                    else {
                        finishSale(ResponseCode.SUCCESS);
                    }
                }, 2000);
            }, 2000);
        }, 2000);
    }

    public String MaskTheCardNo(String cardNoSTR){
        // CREATE A MASKED CARD NO
        // First 6 and Last 4 digit is visible, others are masked with '*' Card No can be 16,17,18 Digits...
        // 123456******0987
        String CardNoFirstSix = StringUtils.left(cardNoSTR, 6);
        String CardNoLastFour =  cardNoSTR.substring(cardNoSTR.length() - 4);
        int LenCardNo = cardNoSTR.length();
        int astrixNo = LenCardNo - 10;
        String AstrixS = StringUtils.repeat('*', astrixNo);
        String CardNoMasked = CardNoFirstSix + AstrixS + CardNoLastFour;

        return CardNoMasked;
    }

    public void finishSale(ResponseCode code) {
        Bundle bundle = new Bundle();
        bundle.putInt("ResponseCode", code.ordinal()); // #1 Response Code
        if (card != null) {
            bundle.putString("CardOwner", card.getOwnerName()); // Optional
            bundle.putString("CardNumber", card.getCardNumber()); // Optional, Card No can be masked
            bundle.putInt("PaymentStatus",0); // #2 Payment Status
            bundle.putInt("Amount", amount); // #3 Amount
            bundle.putBoolean("IsSlip", true); // #4 Slip
            bundle.putInt("BatchNo", databaseHelper.getBatchNo());
            bundle.putString("CardNo", MaskTheCardNo(card.getCardNumber())); //#5 Card No "MASKED"
            bundle.putString("MID", databaseHelper.getMerchantId()); //#6 Merchant ID
            bundle.putString("TID", databaseHelper.getTerminalId()); //#7 Terminal ID
            bundle.putInt("TxnNo", databaseHelper.getTxNo());

            bundle.putString("RefundInfo", String.valueOf(databaseHelper.getSaleID()));

            // if Response Code is success && Transaction Code is not Loyalty Sale
            bundle.putInt("RefNo", databaseHelper.getSaleID());
        }
        Intent result = new Intent();
        result.putExtras(bundle);
        setResult(Activity.RESULT_OK, result);

        // ADD Sale Data to DB
        card_no = String.valueOf(card.getCardNumber());
        sale_amount = String.valueOf(amount);
        databaseHelper.SaveSaleToDB(card_no, sale_amount);
        finish();
    }

    private void setConfig() {
        try {
            InputStream xmlStream = getApplicationContext().getAssets().open("custom_emv_config.xml");
            //String conf = xmlStream.toString();
            //Log.d(TAG, "conf string: " + conf);
            BufferedReader r = new BufferedReader(new InputStreamReader(xmlStream));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                Log.d("emv_config", "conf line: " + line);
                total.append(line).append('\n');
            }
            //Log.d(TAG, "conf string: " + total.toString());
            int setConfigResult = cardServiceBinding.setEMVConfiguration(total.toString());
            Toast.makeText(getApplicationContext(), "setEMVConfiguration res=" + setConfigResult, Toast.LENGTH_SHORT).show();
            Log.d("emv_config", "setEMVConfiguration: " + setConfigResult);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onCardDataReceived(String cardData) {

        try {
            JSONObject json = new JSONObject(cardData);
            int type = json.getInt("mCardReadType");

            if (type == CardReadType.CLCard.value) {
                ICCCard card = new Gson().fromJson(cardData, ICCCard.class);
                this.card = card;
                showInfoDialog();
            }

            if (type == CardReadType.ICC.value) {
                ICCCard card = new Gson().fromJson(cardData, ICCCard.class);
                this.card = card;
                showInfoDialog();
            }if (type == CardReadType.ICC2MSR.value || type == CardReadType.MSR.value || type == CardReadType.KeyIn.value) {
                MSRCard card = new Gson().fromJson(cardData, MSRCard.class);
                this.card = card;
                cardServiceBinding.getOnlinePIN(amount, card.getCardNumber(), 0x0A01, 0, 4, 8, 30);
                //TODO Do transaction after pin verification
            }
            //TODO
            //..check and process other read types
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPinReceived(String pin) {
        showInfoDialog();
    }

    @Override
    public void onICCTakeOut() {
        finishSale(ResponseCode.SUCCESS);
    }
}
