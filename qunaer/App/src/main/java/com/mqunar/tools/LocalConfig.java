package com.mqunar.tools;

import java.util.Arrays;
import java.util.List;

public class LocalConfig {
    public static List<String> allowdSchemes() {
        return Arrays.asList(new String[]{"weixin", "cmblife", "com.qunar.gonglue"});
    }
}
