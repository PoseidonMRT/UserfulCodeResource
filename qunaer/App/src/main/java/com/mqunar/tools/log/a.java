package com.mqunar.tools.log;

import com.mqunar.tools.log.QLog.Clerk;
import java.util.HashMap;
import java.util.Map;

final class a implements Clerk {
    a() {
    }

    public void v(String str, Object... objArr) {
        for (Clerk v : QLog.a) {
            v.v(str, objArr);
        }
    }

    public void d(String str, Object... objArr) {
        for (Clerk d : QLog.a) {
            d.d(str, objArr);
        }
    }

    public void i(String str, Object... objArr) {
        for (Clerk i : QLog.a) {
            i.i(str, objArr);
        }
    }

    public void w(String str, Object... objArr) {
        for (Clerk w : QLog.a) {
            w.w(str, objArr);
        }
    }

    public void e(String str, Object... objArr) {
        for (Clerk e : QLog.a) {
            e.e(str, objArr);
        }
    }

    public void e(Throwable th, String str, Object... objArr) {
        for (Clerk e : QLog.a) {
            e.e(th, str, objArr);
        }
    }

    public void e(Throwable th) {
        for (Clerk e : QLog.a) {
            e.e(th);
        }
    }

    public void crash(Throwable th, String str) {
        for (Clerk crash : QLog.a) {
            crash.crash(th, str);
        }
    }

    public Map<String, String> getCrashes() {
        Map hashMap = new HashMap();
        for (Clerk crashes : QLog.a) {
            Map crashes2 = crashes.getCrashes();
            if (crashes2 != null) {
                hashMap.putAll(crashes2);
            }
        }
        return hashMap;
    }

    public void removeCrash(String str) {
        for (Clerk removeCrash : QLog.a) {
            removeCrash.removeCrash(str);
        }
    }
}
