package com.tokeninc.sardis.application_template.UI.Activities.PosOperations;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.token.uicomponents.infodialog.InfoDialog;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Entity.CardReadType;
import com.tokeninc.sardis.application_template.Entity.ICCCard;
import com.tokeninc.sardis.application_template.Entity.ICard;
import com.tokeninc.sardis.application_template.Entity.MSRCard;
import com.tokeninc.sardis.application_template.Entity.ResponseCode;
import com.tokeninc.sardis.application_template.Helpers.DataBase.BinDbHelper;
import com.tokeninc.sardis.application_template.Helpers.DataBase.DatabaseHelper;
import com.tokeninc.sardis.application_template.R;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.IOException;

public class VoidActivity extends BaseActivity {

    private ICard card;
    private String amount = "";
    DatabaseHelper databaseHelper;
    String  batch_no;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_void);

        databaseHelper = new DatabaseHelper(this);

        getSaleData("8");
    }

    public void getSaleData(String myCode){
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String currentBatchNo = String.valueOf(databaseHelper.getBatchNo());

        Cursor cursor = db.rawQuery("SELECT * FROM sale_table WHERE sale_id = " + myCode, null);

        if (cursor.moveToNext()) {
            batch_no = cursor.getString(cursor.getColumnIndexOrThrow("batch_no"));
            amount = cursor.getString(cursor.getColumnIndexOrThrow("sale_amount"));
            cursor.close();
        }

        if (!currentBatchNo.equals(batch_no)){
            /**
             * İADE
             */
            showVoid();

        }
        else{
            /**
             * İPTAL
             */
            showRefund();
        }
        db.close();

    }

    private void readCard() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("forceOnline", 1);
            obj.put("zeroAmount", 0);
            obj.put("fallback", 1);

            //obj.put("cardReadTypes", 5);

            if(amount.equals("")){
                obj.put("showAmount", 0);
                cardServiceBinding.getCard(0, 40, obj.toString());
            }
            else{
                obj.put("showAmount", 1);
                cardServiceBinding.getCard(Integer.parseInt(amount), 40, obj.toString());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showVoid(){
        InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Progress, "İşlem iptal ediliyor", false);
        new Handler().postDelayed(() -> {
            dialog.dismiss();
            readCard();
        }, 3000);
    }

    public void showRefund(){
        InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Progress, "İşlem iade ediliyor", false);
        new Handler().postDelayed(() -> {
            dialog.dismiss();
            readCard();
        }, 3000);
    }


    private void takeOutICC() {
        cardServiceBinding.takeOutICC(40);
    }

    private void showInfoDialog() {
        InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Progress, "Bağlanıyor", false);
        new Handler().postDelayed(() -> {
            dialog.update(InfoDialog.InfoType.Confirmed, "İşlem Başarılı \n Onay kodu: 002301");
            new Handler().postDelayed(() -> {
                dialog.update(InfoDialog.InfoType.Progress, "Belge Oluşturuluyor");
                new Handler().postDelayed(() -> {
                    dialog.dismiss();
                    if (card instanceof ICCCard)
                        takeOutICC();
                    else {
                        finishSale(ResponseCode.SUCCESS);

                    }
                }, 3000);
            }, 3000);
        }, 3000);
    }

    private void finishSale(ResponseCode code) {
        Bundle bundle = new Bundle();
        bundle.putInt("ResponseCode", code.ordinal());
        if (card != null) {
            bundle.putString("CardOwner", card.getOwnerName());
            bundle.putString("CardNumber", card.getCardNumber());
        }
        Intent result = new Intent();
        result.putExtras(bundle);
        setResult(Activity.RESULT_OK, result);

        finish();
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
                showInfoDialog();
            }

            if (type == CardReadType.ICC.value) {
                ICCCard card = new Gson().fromJson(cardData, ICCCard.class);
                this.card = card;
                showInfoDialog();
            }if (type == CardReadType.ICC2MSR.value || type == CardReadType.MSR.value || type == CardReadType.KeyIn.value) {
                MSRCard card = new Gson().fromJson(cardData, MSRCard.class);
                this.card = card;
                cardServiceBinding.getOnlinePIN(Integer.parseInt(amount), card.getCardNumber(), 0x0A01, 0, 4, 8, 30);
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
