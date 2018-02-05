package com.mqunar.libtask;

import com.mqunar.atomenv.GlobalEnv;
import qunar.lego.utils.Pitcher;

public class ProxyConfig {
    private static ProxyConfig a = null;

    private ProxyConfig() {
        a();
    }

    private void a() {
        if (!GlobalEnv.getInstance().isRelease()) {
            Object a = r.a("get", r.a("com.mqunar.atomenv.env.debug.BetaSetting", "getInstance", null, null), new Class[]{String.class}, new String[]{"pitcher"});
            if (a != null) {
                Pitcher.setDefaultProxyUrl((String) a);
            }
        }
    }

    public static ProxyConfig getInstance() {
        if (a == null) {
            synchronized (ProxyConfig.class) {
                if (a == null) {
                    a = new ProxyConfig();
                }
            }
        }
        return a;
    }

    public void setProxyUrl(String str) {
        if (!GlobalEnv.getInstance().isRelease()) {
            Pitcher.setDefaultProxyUrl(str);
        }
    }

    public void setDefaultUrl(String str) {
        Pitcher.setDefaultProxyUrl(str);
    }
}
