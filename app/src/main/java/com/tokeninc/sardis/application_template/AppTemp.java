package com.tokeninc.sardis.application_template;

import android.app.Application;
import android.util.Log;

import com.tokeninc.deviceinfo.DeviceInfo;
import com.tokeninc.sardis.application_template.Entity.DeviceMode;

public class AppTemp extends Application {
    private int currentDeviceMode = DeviceMode.NONE.mode;
    private String currentFiscalID = null;

    @Override
    public void onCreate() {
        super.onCreate();
        startDeviceInfo();
    }

    public Integer getCurrentDeviceMode() {
        return currentDeviceMode;
    }

    public void setCurrentDeviceMode(Integer currentDeviceMode) {
        this.currentDeviceMode = currentDeviceMode;
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
                    this.setCurrentDeviceMode(Integer.valueOf(fields[1]));

                    deviceInfo.unbind();
                },
                DeviceInfo.Field.FISCAL_ID, DeviceInfo.Field.DEVICE_MODE
        );
    }
}
