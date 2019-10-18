package com.tokeninc.sardis.application_template.Helpers;

public class StringHelper {

    public static String getAmount(int amount) {
        String str=String.valueOf(amount);
        if (str.length() == 1) str = "00" + str;
        else if (str.length() == 2) str = "0" + str;

        String s1=str.substring(0,str.length()-2);
        String s2=str.substring(str.length()-2);
        return s1 + "," + s2 + "₺";
    }
}
