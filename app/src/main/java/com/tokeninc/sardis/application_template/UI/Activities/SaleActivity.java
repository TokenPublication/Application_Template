package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.token.uicomponents.ListMenuFragment.IListMenuItem;
import com.token.uicomponents.ListMenuFragment.ListMenuClickListener;
import com.token.uicomponents.ListMenuFragment.ListMenuFragment;
import com.token.uicomponents.infodialog.InfoDialog;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Entity.CardReadType;
import com.tokeninc.sardis.application_template.Entity.ICCCard;
import com.tokeninc.sardis.application_template.Entity.ICard;
import com.tokeninc.sardis.application_template.Entity.MSRCard;
import com.tokeninc.sardis.application_template.Entity.ResponseCode;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SaleActivity extends BaseActivity implements View.OnClickListener, ListMenuClickListener {

    private int amount = 0;

    private List<IListMenuItem> menuItemList;
    private ICard card;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        amount = getIntent().getExtras().getInt("Amount");

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItemList, this, "Satış Tipi", false);
        addFragment(R.id.container, fragment, false);
    }

    private void prepareData() {
        menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem(1,"Satış"));
        menuItemList.add(new MenuItem(2,"Taksitli Satış"));
        menuItemList.add(new MenuItem(3,"Puan Satış"));
        menuItemList.add(new MenuItem(4,"Kampanya Satış"));
    }

    @Override
    public void onItemClick(IListMenuItem item) {
        readCard();
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
            obj.put("zeroAmount", 1);

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
        InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Connecting, "Bağlanıyor...", false);
        new Handler().postDelayed(() -> {
            dialog.update(InfoDialog.InfoType.Processing, "İşlem yapılıyor...");
            new Handler().postDelayed(() -> {
                dialog.update(InfoDialog.InfoType.Confirmed, "İşlem Başarılı!");
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

            if (type == CardReadType.ICC.value) {
                ICCCard card = new Gson().fromJson(cardData, ICCCard.class);
                this.card = card;
                showInfoDialog();
            } else if (type == CardReadType.MSR.value || type == CardReadType.KeyIn.value) {
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
