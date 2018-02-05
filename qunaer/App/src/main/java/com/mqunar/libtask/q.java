package com.mqunar.libtask;

import android.content.Context;
import android.net.Uri;
import android.net.http.Headers;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.mqunar.necro.agent.instrumentation.TransactionStateUtil;
import com.mqunar.network.NetRequest;
import com.mqunar.tools.log.QLog;
import java.util.List;
import java.util.Map;
import org.apache.http.cookie.SM;

class q {
    public static String a(Context context, NetRequest netRequest) {
        try {
            if (!netRequest.header.containsKey("Pitcher-Url")) {
                return null;
            }
            String str = (String) netRequest.header.get("Pitcher-Url");
            try {
                if (netRequest.header.containsKey(TransactionStateUtil.REQUEST_HEADER_PITCHER_TYPE) && "hotdog".equalsIgnoreCase((String) netRequest.header.get(TransactionStateUtil.REQUEST_HEADER_PITCHER_TYPE))) {
                    Uri parse = Uri.parse(str);
                    str = parse.buildUpon().appendEncodedPath(parse.getQueryParameter("qrt").replaceAll("_", "/")).build().toString();
                }
                CookieManager instance = CookieManager.getInstance();
                a(context);
                Object cookie = instance.getCookie(str);
                if (TextUtils.isEmpty(cookie)) {
                    return str;
                }
                netRequest.addHeader(SM.COOKIE, cookie);
                return str;
            } catch (Throwable th) {
                return str;
            }
        } catch (Throwable th2) {
            return null;
        }
    }

    public static void a(Context context, String str, Map<String, Object> map) {
        if (context != null) {
            try {
                if (!TextUtils.isEmpty(str) && map != null) {
                    Object obj = map.get(Headers.SET_COOKIE);
                    if (obj != null) {
                        CookieSyncManager.createInstance(context.getApplicationContext());
                        CookieManager instance = CookieManager.getInstance();
                        instance.setAcceptCookie(true);
                        if (obj instanceof String) {
                            instance.setCookie(str, (String) obj);
                        } else if (obj instanceof List) {
                            for (String cookie : (List) obj) {
                                instance.setCookie(str, cookie);
                            }
                        }
                        CookieSyncManager.getInstance().sync();
                    }
                }
            } catch (Throwable th) {
            }
        }
    }

    public static void a(Context context) {
        CookieSyncManager cookieSyncManager = null;
        try {
            cookieSyncManager = CookieSyncManager.getInstance();
        } catch (IllegalStateException e) {
        } catch (Throwable th) {
            QLog.e(th);
        }
        if (cookieSyncManager == null) {
            try {
                CookieSyncManager.createInstance(context.getApplicationContext());
            } catch (Throwable th2) {
                QLog.e(th2);
            }
        }
    }
}
