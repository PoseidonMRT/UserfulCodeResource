package com.mqunar.network;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.iflytek.speech.VoiceWakeuperAidl;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.SM;

public class NetCookieUtils {
    static void a(Context context, String str, HttpUriRequest httpUriRequest) {
        try {
            if (!TextUtils.isEmpty(str) && httpUriRequest != null) {
                CookieManager instance = CookieManager.getInstance();
                newInstanceIfNeed(context);
                String cookie = instance.getCookie(str);
                Header[] headers = httpUriRequest.getHeaders(SM.COOKIE);
                HashSet hashSet = new HashSet();
                if (headers != null) {
                    for (Header value : headers) {
                        a(hashSet, value.getValue());
                    }
                }
                a(hashSet, cookie);
                if (hashSet.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    Iterator it = hashSet.iterator();
                    while (it.hasNext()) {
                        stringBuilder.append((String) it.next()).append("; ");
                    }
                    String stringBuilder2 = stringBuilder.toString();
                    stringBuilder2 = stringBuilder2.substring(0, stringBuilder2.length() - 2);
                    httpUriRequest.removeHeaders(SM.COOKIE);
                    httpUriRequest.addHeader(SM.COOKIE, stringBuilder2);
                }
            }
        } catch (Throwable th) {
        }
    }

    private static void a(Set<String> set, String str) {
        if (!TextUtils.isEmpty(str)) {
            for (String trim : str.split(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                set.add(trim.trim());
            }
        }
    }

    static void a(Context context, String str, HttpResponse httpResponse) {
        if (context != null) {
            try {
                if (!TextUtils.isEmpty(str) && httpResponse != null) {
                    Header[] headers = httpResponse.getHeaders(SM.SET_COOKIE);
                    if (headers != null) {
                        CookieSyncManager.createInstance(context.getApplicationContext());
                        CookieManager instance = CookieManager.getInstance();
                        instance.setAcceptCookie(true);
                        for (Header value : headers) {
                            instance.setCookie(str, value.getValue());
                        }
                        CookieSyncManager.getInstance().sync();
                    }
                }
            } catch (Throwable th) {
            }
        }
    }

    public static void clearSessionCookie(Context context) {
        try {
            CookieSyncManager.createInstance(context);
            CookieManager.getInstance().removeSessionCookie();
        } catch (Throwable th) {
        }
    }

    public static void newInstanceIfNeed(Context context) {
        CookieSyncManager cookieSyncManager = null;
        try {
            cookieSyncManager = CookieSyncManager.getInstance();
        } catch (IllegalStateException e) {
        } catch (Throwable th) {
        }
        if (cookieSyncManager == null) {
            try {
                CookieSyncManager.createInstance(context.getApplicationContext());
            } catch (Throwable th2) {
            }
        }
    }
}
