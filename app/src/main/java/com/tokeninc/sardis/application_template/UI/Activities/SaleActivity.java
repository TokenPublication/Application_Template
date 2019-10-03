package com.tokeninc.sardis.application_template.UI.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tokeninc.cardservice.ITokenCardService;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Entity.CardReadType;
import com.tokeninc.sardis.application_template.Entity.ICCCard;
import com.tokeninc.sardis.application_template.Entity.MSRCard;
import com.tokeninc.sardis.application_template.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SaleActivity extends BaseActivity implements View.OnClickListener {

    private ITokenCardService emvService;
    private ServiceConnection emvServiceConnection;
    private boolean mBound = false;

    private static final String EMV_SERVICE_PACKAGE_NAME = "com.tokeninc.cardservice";
    private static final String EMV_SERVICE_NAME = "com.tokeninc.cardservice.CardService";

    private int amount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        amount = getIntent().getExtras().getInt("Amount");

        emvServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                emvService = ITokenCardService.Stub.asInterface(service);
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBound = false;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent emvS = new Intent();
        emvS.setComponent(new ComponentName(EMV_SERVICE_PACKAGE_NAME, EMV_SERVICE_NAME));

        try {
            if (!bindService(emvS, emvServiceConnection, Context.BIND_AUTO_CREATE)) {
                Toast.makeText(getApplicationContext(), "Could not bind to the service", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(emvServiceConnection);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSale:
                readCard();
                break;
            case R.id.btnSetConfig:
                setConfig();
                break;
        }

    }

    private void readCard() {
        if (mBound) {
            try {
                String cardData = emvService.getCard(amount, 40, "Get Card is called by alien application");

                JSONObject json = new JSONObject(cardData);
                int type = json.getInt("mCardReadType");

                if (type == CardReadType.ICC.value) {
                    ICCCard card = new Gson().fromJson(cardData, ICCCard.class);
                }
                else if (type == CardReadType.MSR.value) {
                    MSRCard card = new Gson().fromJson(cardData, MSRCard.class);
                }
                //TODO
                //..check and process other read types
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
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


}
