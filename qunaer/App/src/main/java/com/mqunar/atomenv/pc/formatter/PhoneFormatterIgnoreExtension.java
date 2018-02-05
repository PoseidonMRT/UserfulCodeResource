package com.mqunar.atomenv.pc.formatter;

import com.mqunar.atomenv.pc.IPhoneFormatter;

public class PhoneFormatterIgnoreExtension implements IPhoneFormatter {
    public String format(String str) {
        String str2 = "tel:" + str.replace("-", "").replace(" ", "").replace("转", "p");
        try {
            return str2.split("p")[0];
        } catch (Exception e) {
            return str2;
        }
    }
}
