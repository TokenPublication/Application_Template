package com.tokeninc.sardis.application_template.Helpers;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class StringHelper {

    public static String getAmount(int amount) {
        String Lang = Locale.getDefault().getDisplayLanguage();
        String currency;

        if(Lang.equals("Türkçe")){
         currency = "₺";
        }
        else{
            currency ="€";
        }

        String str=String.valueOf(amount);
        if (str.length() == 1) str = "00" + str;
        else if (str.length() == 2) str = "0" + str;

        String s1=str.substring(0,str.length()-2);
        String s2=str.substring(str.length()-2);
        return s1 + "," + s2 + currency;
    }

    public static String MaskTheCardNo(String cardNoSTR){
        // CREATE A MASKED CARD NO
        // First 6 and Last 4 digit is visible, others are masked with '*' Card No can be 16,17,18 Digits...
        // 123456******0987
        String CardNoFirstSix = StringUtils.left(cardNoSTR, 6);
        String CardNoLastFour =  cardNoSTR.substring(cardNoSTR.length() - 4);
        int LenCardNo = cardNoSTR.length();
        int astrixNo = LenCardNo - 10;
        String AstrixS = StringUtils.repeat('*', astrixNo);
        String CardNoMasked = CardNoFirstSix + AstrixS + CardNoLastFour;

        return CardNoMasked;
    }


}
