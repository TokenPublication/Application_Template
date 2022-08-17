package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Entity.ComPosKeys;
import com.tokeninc.sardis.application_template.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TriggerActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);

        if (getIntent().getAction().equals("BatchClose_Action")) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        if (getIntent().getAction().equals("Parameter_Action")) {
            // If Parameter is success
            Intent resultIntentMain = new Intent();
            Bundle com_pos_bundle = new Bundle();

            String clConfigFile = "";

            try {
                InputStream xmlCLStream = getApplicationContext().getAssets().open("custom_emv_cl_config.xml");
                BufferedReader rCL = new BufferedReader(new InputStreamReader(xmlCLStream));
                StringBuilder totalCL = new StringBuilder();
                for (String line; (line = rCL.readLine()) != null; ) {
                    totalCL.append(line).append('\n');
                }
                clConfigFile = totalCL.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            com_pos_bundle.putString(ComPosKeys.clConfigFile, clConfigFile);

            // Dummy Data
            String bins = "[{\"cardRangeStart\":\"1111110000000\",\"cardRangeEnd\":\"1111119999999\",\"OwnerShip\":\"ISSUER\",\"CardType\":\"C\"},{\"cardRangeStart\":\"2222220000000\",\"cardRangeEnd\":\"2222229999999\",\"OwnerShip\":\"NONE\",\"CardType\":\"C\"},{\"cardRangeStart\":\"3333330000000\",\"cardRangeEnd\":\"3333339999999\",\"OwnerShip\":\"BRAND\",\"CardType\":\"C\"}]";
            com_pos_bundle.putString(ComPosKeys.BINS, bins);
            com_pos_bundle.putString(ComPosKeys.AllowedOperations, "{" +"\"QrAllowed\"" +":" +"1" +"," +"\"KeyInAllowed\"" +":" +"1" +"}");
            com_pos_bundle.putString(ComPosKeys.SupportedAIDs, "[A0000000031010, A0000000041010, A0000000032010]");
            resultIntentMain.putExtras(com_pos_bundle);

            // Add data to the result
            setResult(Activity.RESULT_OK, resultIntentMain);
            finish();
        }
    }
}
