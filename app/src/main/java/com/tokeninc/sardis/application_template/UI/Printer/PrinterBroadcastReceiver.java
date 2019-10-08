package com.tokeninc.sardis.application_template.UI.Printer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PrinterBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "PrinterBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Log.d(TAG, log);

        String errNoString = intent.getStringExtra("errNo") ;
        if(errNoString != null)
        {
            int errNo = Integer.parseInt(errNoString);
            PrinterTests.ToastStatus(context, PrinterDefinitions.PrinterErrorCode.GetValue(errNo));
        }

    }
}