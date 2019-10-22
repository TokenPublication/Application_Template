// ITokenCardService.aidl
package com.tokeninc.cardservice;



interface ITokenCardService {
        // amount:
        // please provide it as 100 x Float amount.
        // example -> For 1 = provide 100, for 0.05 = provide 5
        //
        // timeout:
        // duration as seconds that will present card view and
        // detects any card types
        //
        // config:
        // reserved for future use
        void getCard(int amount, int timeout, String config, String packageName);

        // action:
        // Online approval					    1
        // Online decline					    2
        // Unable go online					    3
        // Issuer referral approval             4
        // Issuer referral decline              5
        // Unable go online with specific ARC   6
        //
        // authRespCode:
        // Response Code from the host. 2 bytes.
        //
        // issuerAuthData:
        // Issuer Authentication Data from the issuer host
        //
        // issuerAuthDataLen:
        // Length of the Issuer Authentication Data
        //
        // issuerScript:
        // Issuer Script from the issuer host
        //
        // issuerScriptLen:
        // Length of the Issuer Script
        int completeEmvTxn(byte action, in byte[] authRespCode, in byte[] issuerAuthData, int issuerAuthDataLen, in byte[] issuerScript, int issuerScriptLen);

        // config:
        // emv settings xml data
        int setEMVConfiguration(String config);

        // amount:
        // please provide it as 100 x Float amount.
        // example -> For 1 = provide 100, for 0.05 = provide 5
        //
        // pan:
        // PAN of the Card
        //
        // keySet:
        // The keySet that assigned for Bank
        //
        // keyIndex:
        // The index of the key in keySet
        //
        // minLen:
        // min PIN entry len
        //
        // maxLen:
        // max PIN entry len
        //
        // timeout:
        // PIN entry timeout as Sec
        void getOnlinePIN(int amount, String pan, int keySet, int keyIndex, int minLen, int maxLen, int timeout, String packageName);

        String getDeviceSN();
}
