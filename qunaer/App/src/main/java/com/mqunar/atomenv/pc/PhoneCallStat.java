package com.mqunar.atomenv.pc;

import com.mqunar.atomenv.pc.internal.PhoneCallSto;

public class PhoneCallStat {
    protected static IPhoneCall singleInstance = null;

    public static IPhoneCall getInstance() {
        if (singleInstance == null) {
            singleInstance = new PhoneCallSto();
        }
        return singleInstance;
    }

    private PhoneCallStat() {
    }
}
