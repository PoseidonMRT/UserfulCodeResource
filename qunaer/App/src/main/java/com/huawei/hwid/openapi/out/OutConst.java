package com.huawei.hwid.openapi.out;

import java.util.Arrays;
import java.util.List;

public class OutConst {
    public static final List ABILITIES = Arrays.asList(new String[]{"basic,quicklogin"});
    public static final String version = "3.3.1.300";

    public static boolean hasAbility(String str) {
        return ABILITIES.contains(str);
    }

    public static String getIterfaceVersion() {
        return version;
    }
}
