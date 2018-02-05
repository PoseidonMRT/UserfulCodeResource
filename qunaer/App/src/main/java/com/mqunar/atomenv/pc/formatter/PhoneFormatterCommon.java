package com.mqunar.atomenv.pc.formatter;

import com.mqunar.atomenv.pc.IPhoneFormatter;

public class PhoneFormatterCommon implements IPhoneFormatter {
    public String format(String str) {
        return "tel:" + str.replace("-", "").replace(" ", "").replace("转", "p");
    }
}
