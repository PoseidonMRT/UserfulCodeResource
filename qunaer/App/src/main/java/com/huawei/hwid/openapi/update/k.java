package com.huawei.hwid.openapi.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.text.TextUtils;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.update.a.b;
import com.iflytek.aiui.AIUIConstant;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.http.cookie.ClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class k {
    public static String a(String str, Map map) {
        String string;
        JSONException e;
        String str2 = "1";
        try {
            String str3 = "";
            JSONObject jSONObject = new JSONObject(str);
            string = jSONObject.getString("status");
            try {
                if (jSONObject.has("forcedupdate")) {
                    str3 = jSONObject.getString("forcedupdate");
                }
                if ("0".equals(string)) {
                    JSONArray jSONArray = jSONObject.getJSONArray("components");
                    int length = jSONArray.length();
                    c.b("OtaUtils", "length:" + length);
                    for (int i = 0; i < length; i++) {
                        b bVar = new b();
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                        String string2 = jSONObject2.getString("url");
                        int i2 = jSONObject2.getInt("componentID");
                        bVar.b(i2);
                        bVar.b(jSONObject2.getString(AIUIConstant.KEY_NAME));
                        bVar.c(jSONObject2.getString(ClientCookie.VERSION_ATTR));
                        bVar.a(jSONObject2.getString("versionID"));
                        bVar.d(string2);
                        bVar.a(jSONObject2.getInt("size"));
                        bVar.h(jSONObject2.getString("createTime"));
                        bVar.g(jSONObject2.getString("description"));
                        bVar.f(str3);
                        map.put(Integer.valueOf(i2), bVar);
                    }
                }
            } catch (JSONException e2) {
                e = e2;
                c.d("OtaUtils", "parse version to map error ,error is " + e.getMessage());
                return string;
            }
        } catch (JSONException e3) {
            JSONException jSONException = e3;
            string = str2;
            e = jSONException;
            c.d("OtaUtils", "parse version to map error ,error is " + e.getMessage());
            return string;
        }
        return string;
    }

    public static String a(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        String language = configuration.locale.getLanguage();
        return (language + '-' + configuration.locale.getCountry()).toLowerCase(Locale.getDefault());
    }

    public static boolean a(Context context, long j) {
        if (context.getExternalCacheDir().getUsableSpace() < 3 * j) {
            return false;
        }
        return true;
    }

    public static void a(InputStream inputStream, String str) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                if (TextUtils.isEmpty(str)) {
                    c.d("OtaUtils", e.getMessage());
                } else {
                    c.d(str, e.getMessage());
                }
            }
        }
    }

    public static InputStream a(InputStream inputStream) {
        InputStream pushbackInputStream = new PushbackInputStream(inputStream);
        int read = pushbackInputStream.read();
        if (read != 239) {
            pushbackInputStream.unread(read);
        } else {
            read = pushbackInputStream.read();
            if (read != 187) {
                pushbackInputStream.unread(read);
                pushbackInputStream.unread(239);
            } else if (pushbackInputStream.read() != 191) {
                throw new IOException("UTF-8 file error");
            }
        }
        return pushbackInputStream;
    }

    public static void b(InputStream inputStream, String str) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
                if (TextUtils.isEmpty(str)) {
                    a(e, "OtaUtils");
                } else {
                    a(e, str);
                }
            }
        }
    }

    public static void a(OutputStream outputStream, String str) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (Exception e) {
                if (TextUtils.isEmpty(str)) {
                    a(e, "OtaUtils");
                } else {
                    a(e, str);
                }
            }
        }
    }

    public static void a(Exception exception, String str) {
        if (exception != null && exception.getMessage() != null) {
            c.d(str, exception.getMessage());
        }
    }

    public static String a(String str) {
        if (str == null) {
            return "";
        }
        char[] toCharArray = str.toCharArray();
        int length = toCharArray.length;
        int i = 4;
        while (i < length && i <= 10) {
            toCharArray[i] = '0';
            i++;
        }
        return new String(toCharArray);
    }

    public static boolean b(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent();
            String packageName = context.getPackageName();
            intent.setClassName(packageName, "com.huawei.hwid.openapi.update.OtaFileProvider");
            intent.setPackage(packageName);
            List list = null;
            if (packageManager != null) {
                list = packageManager.queryIntentContentProviders(intent, 0);
            }
            if (list == null) {
                c.b("OtaUtils", "null == resolveInfos");
                return false;
            } else if (list.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            c.d("OtaUtils", "hasOtaFileProvider error:" + e.getMessage());
            return false;
        }
    }
}
