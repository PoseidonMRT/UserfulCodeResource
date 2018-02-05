package com.huawei.hwid.openapi.quicklogin.d.a;

import android.content.Context;
import android.content.res.XmlResourceParser;
import com.huawei.hwid.openapi.quicklogin.d.b;
import com.huawei.hwid.openapi.quicklogin.d.c;
import java.util.HashMap;
import java.util.Map;

public class a {
    private static Map a = new HashMap();

    private static void a(Context context) {
        try {
            a = b(context);
        } catch (Throwable e) {
            c.b("AppInfo", "initAppInfos error:" + e.getMessage(), e);
        }
    }

    public static String a(Context context, String str) {
        String a;
        String b = b(context, str);
        String str2 = "";
        if (((com.huawei.hwid.openapi.quicklogin.datatype.a) a.get(b)) != null) {
            a = ((com.huawei.hwid.openapi.quicklogin.datatype.a) a.get(b)).a();
        } else {
            a = str2;
        }
        if (c.b(a)) {
            return "7000000";
        }
        return a;
    }

    private static String b(Context context, String str) {
        if (c.b(str) || "cloud".equalsIgnoreCase(str)) {
            str = "com.huawei.hwid";
        }
        if (a == null || a.isEmpty()) {
            a(context);
        }
        return str;
    }

    private static Map b(Context context) {
        XmlResourceParser xml = context.getResources().getXml(b.b(context, "appinfo"));
        Map hashMap = new HashMap();
        if (xml != null) {
            com.huawei.hwid.openapi.quicklogin.datatype.a aVar = new com.huawei.hwid.openapi.quicklogin.datatype.a();
            for (int eventType = xml.getEventType(); 1 != eventType; eventType = xml.next()) {
                String name = xml.getName();
                switch (eventType) {
                    case 2:
                        if ("appID".equals(name)) {
                            aVar.c(xml.nextText());
                            break;
                        } else if ("reqClientType".equals(name)) {
                            aVar.a(xml.nextText());
                            break;
                        } else {
                            try {
                                if (!"defaultChannel".equals(name)) {
                                    break;
                                }
                                aVar.b(xml.nextText());
                                break;
                            } catch (Throwable e) {
                                c.b("AppInfo", "initAppInfos error:" + e.getMessage(), e);
                                break;
                            } finally {
                                xml.close();
                            }
                        }
                    case 3:
                        if (!"appInfo".equals(name)) {
                            break;
                        }
                        hashMap.put(aVar.b(), aVar);
                        aVar = new com.huawei.hwid.openapi.quicklogin.datatype.a();
                        break;
                    default:
                        break;
                }
            }
        }
        return hashMap;
    }
}
