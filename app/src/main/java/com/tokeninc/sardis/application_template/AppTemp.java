package com.tokeninc.sardis.application_template;

import android.app.Application;
import android.util.Log;

import com.tokeninc.deviceinfo.DeviceInfo;

public class AppTemp extends Application {
    private String currentFiscalID = null;

    @Override
    public void onCreate() {
        super.onCreate();
        startDeviceInfo();
    }

    public String getCurrentFiscalID() {
        return currentFiscalID;
    }

    public void setCurrentFiscalID(String currentFiscalID) {
        this.currentFiscalID = currentFiscalID;
    }

    private void startDeviceInfo(){
        DeviceInfo deviceInfo = new DeviceInfo(this);
        deviceInfo.getFields(
                fields -> {
                    if (fields == null) return;
                    // fields is the string array that contains info in the requested order

                    this.setCurrentFiscalID(fields[0]);

                    deviceInfo.unbind();
                },
                DeviceInfo.Field.FISCAL_ID
        );
    }
}
