package com.tokeninc.sardis.application_template.Entity;

public class MSRCard {
    int resultCode;
    int mCardReadType;
    String mCardNumber;
    String mTrack2Data;
    String mExpireDate;
    int mTranAmount1;
    String mTrack1CustomerName;

    public int getResultCode() {
        return resultCode;
    }

    public int getmCardReadType() {
        return mCardReadType;
    }

    public String getmCardNumber() {
        return mCardNumber;
    }

    public String getmTrack2Data() {
        return mTrack2Data;
    }

    public String getmExpireDate() {
        return mExpireDate;
    }

    public int getmTranAmount1() {
        return mTranAmount1;
    }

    public String getmTrack1CustomerName() {
        return mTrack1CustomerName;
    }
}
