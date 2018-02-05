package com.mqunar.atomenv.pc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.mqunar.atomenv.pc.formatter.PhoneFormatterCommon;
import com.mqunar.tools.log.QLog;

public final class PhoneCall {
    protected static PhoneCall singleInstance = null;

    public static PhoneCall getInstance() {
        if (singleInstance == null) {
            singleInstance = new PhoneCall();
        }
        return singleInstance;
    }

    private PhoneCall() {
    }

    public void processCall(Context context, String str) {
        processCall(context, str, new PhoneFormatterCommon(), null);
    }

    public void processCall(Context context, String str, IPhoneFormatter iPhoneFormatter) {
        processCall(context, str, iPhoneFormatter, null);
    }

    public void processCall(Context context, String str, AbsCallStat absCallStat) {
        processCall(context, str, new PhoneFormatterCommon(), absCallStat);
    }

    public void processCall(Context context, String str, IPhoneFormatter iPhoneFormatter, AbsCallStat absCallStat) {
        try {
            context.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(iPhoneFormatter.format(str))));
            if (absCallStat != null) {
                PhoneCallStat.getInstance().add(absCallStat);
            }
        } catch (Throwable e) {
            QLog.e(e);
        }
    }
}
