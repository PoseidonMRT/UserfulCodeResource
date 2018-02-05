package com.huawei.hwid.openapi.quicklogin.b.b;

import com.huawei.hwid.openapi.quicklogin.a.a;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.e;
import com.iflytek.cloud.SpeechUtility;
import org.xmlpull.v1.XmlPullParser;

public class f {
    private static String a = a.b;

    public static com.huawei.hwid.openapi.quicklogin.b.a.a a(String str) {
        com.huawei.hwid.openapi.quicklogin.b.a.a aVar = new com.huawei.hwid.openapi.quicklogin.b.a.a();
        try {
            XmlPullParser a = e.a(str.getBytes("UTF-8"));
            int eventType = a.getEventType();
            d.a(a, "unPackGetTempSTResp");
            int i = eventType;
            eventType = -1;
            for (int i2 = i; 1 != i2; i2 = a.next()) {
                String name = a.getName();
                switch (i2) {
                    case 2:
                        if (SpeechUtility.TAG_RESOURCE_RESULT.equals(name)) {
                            eventType = Integer.valueOf(a.getAttributeValue(null, "resultCode")).intValue();
                        }
                        if (eventType != 0) {
                            if (!"errorCode".equals(name)) {
                                if (!"errorDesc".equals(name)) {
                                    break;
                                }
                                aVar.e(a.nextText());
                                break;
                            }
                            aVar.a(Integer.valueOf(a.nextText()).intValue());
                            break;
                        }
                        a(aVar, name, a);
                        break;
                    default:
                        break;
                }
            }
            return aVar;
        } catch (RuntimeException e) {
            d.d("GetTempST", e.getMessage());
            return null;
        } catch (Exception e2) {
            d.d("GetTempST", e2.getMessage());
            return null;
        }
    }

    private static void a(com.huawei.hwid.openapi.quicklogin.b.a.a aVar, String str, XmlPullParser xmlPullParser) {
        if ("userID".equals(str)) {
            aVar.a(xmlPullParser.nextText());
        } else if ("tmpST".equals(str)) {
            aVar.c(xmlPullParser.nextText());
        } else if ("expiresIn".equals(str)) {
            aVar.d(xmlPullParser.nextText());
        } else if ("version ".equals(str)) {
            aVar.b(xmlPullParser.nextText());
        }
    }
}
