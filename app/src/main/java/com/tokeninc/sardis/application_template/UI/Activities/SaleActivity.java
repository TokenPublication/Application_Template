package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.tokeninc.cardservice.ITokenCardService;
import com.tokeninc.components.ListMenuFragment.IListMenuItem;
import com.tokeninc.components.ListMenuFragment.ListMenuClickListener;
import com.tokeninc.components.ListMenuFragment.ListMenuFragment;
import com.tokeninc.components.infodialog.InfoDialog;
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

    private ITokenCardService emvService;
    private ServiceConnection emvServiceConnection;
    private boolean mBound = false;

    private static final String EMV_SERVICE_PACKAGE_NAME = "com.tokeninc.cardservice";
    private static final String EMV_SERVICE_NAME = "com.tokeninc.cardservice.CardService";

    private int amount = 0;

    private List<IListMenuItem> menuItemList;
    private ICard card;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        amount = getIntent().getExtras().getInt("Amount");

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItemList, this);
        addFragment(R.id.container, fragment, false);
    }

    private void prepareData() {
        menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem("Satış"));
        menuItemList.add(new MenuItem("Taksitli Satış"));
        menuItemList.add(new MenuItem("Puan Satış"));
        menuItemList.add(new MenuItem("Kampanya Satış"));
    }

    @Override
    public void onItemClick(int position, IListMenuItem item) {
        readCard();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            bindService();
        }
    }

    private void bindService() {
        emvServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                Log.d("TCardService", "Connected");
                emvService = ITokenCardService.Stub.asInterface(service);
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d("TCardService", "Disconnected");
                mBound = false;
            }
        };

        Intent emvS = new Intent();
        emvS.setComponent(new ComponentName(EMV_SERVICE_PACKAGE_NAME, EMV_SERVICE_NAME));
        try {
            if (!bindService(emvS, emvServiceConnection, Context.BIND_AUTO_CREATE)) {
                Toast.makeText(getApplicationContext(), "Could not bind to the service", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Log.d("TCardService", "Successfully bound!");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mBound) {
            unbindService(emvServiceConnection);
            mBound = false;
            Log.d("TCardService", "Unbind!");
        }
        super.onDestroy();
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
        if (mBound) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("forceOnline", 0);
                obj.put("iccError", 1);
                obj.put("msrError", 0);
                obj.put("fallback", 0);
                obj.put("keyIn", 0);

                String cardData = emvService.getCard(amount, 40,
                        obj.toString());

                JSONObject json = new JSONObject(cardData);
                int type = json.getInt("mCardReadType");

                if (type == CardReadType.ICC.value) {
                    ICCCard card = new Gson().fromJson(cardData, ICCCard.class);
                    this.card = card;
                    showInfoDialog();
                }
                else if (type == CardReadType.MSR.value || type == CardReadType.KeyIn.value) {
                    MSRCard card = new Gson().fromJson(cardData, MSRCard.class);
                    this.card = card;
                    String pin = emvService.getOnlinePIN(amount, card.getCardNumber(), 0x0A01,0,4, 8, 30);
                    //TODO Do transaction after pin verification
                    showInfoDialog();
                }
                //TODO
                //..check and process other read types
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showInfoDialog() {
        InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.None, "Bağlanıyor...", false);
        customerScreenService.showProgress("Bağlanıyor...");
        new Handler().postDelayed(() -> {
            dialog.update(InfoDialog.InfoType.None, "İşlem yapılıyor...");
            customerScreenService.showProgress("İşlem yapılıyor....");
            new Handler().postDelayed(() -> {
                dialog.update(InfoDialog.InfoType.Confirmed, "İşlem Başarılı!");
                customerScreenService.showSuccess("İşlem Başarılı!");
                new Handler().postDelayed(() -> {
                    dialog.dismiss();
                    finishSale(ResponseCode.SUCCESS);
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
            int setConfigResult = emvService.setEMVConfiguration(total.toString());
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
}
