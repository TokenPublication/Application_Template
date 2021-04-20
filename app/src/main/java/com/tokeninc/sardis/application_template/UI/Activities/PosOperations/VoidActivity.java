package com.tokeninc.sardis.application_template.UI.Activities.PosOperations;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

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
import com.tokeninc.sardis.application_template.R;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.IOException;

public class VoidActivity extends BaseActivity {

    private ICard card;
    BinDbHelper binDbHelper;

    String card_no;
    String CardNo, card_type_info, card_range_start, card_range_end, list_ID, pan_length, payment_system, bank_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_void);

        binDbHelper = new BinDbHelper(this);
        try {
            binDbHelper.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readCard();
    }

    public void matchCard(){
        SQLiteDatabase db = binDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM bkm_binlist WHERE " +CardNo +" BETWEEN cardrangestart AND cardrangeend" , null);

        if (cursor.moveToNext()) {
            card_type_info = cursor.getString(cursor.getColumnIndexOrThrow("cardtype_açıklama"));
            card_range_start = cursor.getString(cursor.getColumnIndexOrThrow("cardrangestart"));
            card_range_end = cursor.getString(cursor.getColumnIndexOrThrow("cardrangeend"));
            list_ID = cursor.getString(cursor.getColumnIndexOrThrow("ID"));
            pan_length = cursor.getString(cursor.getColumnIndexOrThrow("panlength"));
            payment_system = cursor.getString(cursor.getColumnIndexOrThrow("paymentsystem"));
            bank_id = cursor.getString(cursor.getColumnIndexOrThrow("bankid"));
            cursor.close();
        }
        db.close();
    }

    private void readCard() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("forceOnline", 1);
            obj.put("zeroAmount", 0);
            obj.put("fallback", 1);
            obj.put("showAmount", 0);
           // obj.put("cardReadTypes", 4);
            cardServiceBinding.getCard(0, 40, obj.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInfoDialog() {

        finishRead(ResponseCode.SUCCESS);

        matchCard();

        showInfoDialog(InfoDialog.InfoType.Confirmed,
                "Card No: " +card_no
                        +"\nRange Start: " +card_range_start
                        +"\nRange End:   " +card_range_end
                        +"\nCard Type: " +card_type_info
                        +"\nPan Length: " +pan_length
                        +"\nPayment System: "+payment_system
                        +"\nBank ID: " +bank_id
                        +"\nID: " +list_ID, true);
    }

    private void finishRead(ResponseCode code) {
        Bundle bundle = new Bundle();
        bundle.putInt("ResponseCode", code.ordinal());
        if (card != null) {
            bundle.putString("CardOwner", card.getOwnerName());
            bundle.putString("CardNumber", card.getCardNumber());
            card_no = String.valueOf(card.getCardNumber());

            String CardNoFirstTen = StringUtils.left(card_no, 10);
            String ZeroS = StringUtils.repeat('0', 3);
            CardNo = CardNoFirstTen + ZeroS;
        }
        Intent result = new Intent();
        result.putExtras(bundle);
        setResult(Activity.RESULT_OK, result);
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
                cardServiceBinding.getOnlinePIN(0, card.getCardNumber(), 0x0A01, 0, 4, 8, 30);
            }
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
        finishRead(ResponseCode.SUCCESS);
    }

}
