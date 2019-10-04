// ITokenCardService.aidl
package com.tokeninc.cardservice;

interface ITokenCardService {
    // amount:
    // please provide it as 100 x Float amount.
    // example -> For 1 = provide 100, for 0.05 = provide 5
    // timeout:
    // duration as seconds that will present card view and
    // detects any card types
    // config:
    // reserved for future use
    String getCard(int amount, int timeout, String config);
    // Set EMV Configuration as formatted xml file
    int setEMVConfiguration(String config);
    // Get Online PIN data
    String getOnlinePIN(int amount, int minLen, int maxLen, int timeout);
    // Get device Serial Number
    String getDeviceSN();
}