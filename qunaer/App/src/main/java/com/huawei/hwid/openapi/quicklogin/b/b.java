package com.huawei.hwid.openapi.quicklogin.b;

import com.huawei.hwid.openapi.quicklogin.a.a;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import com.huawei.hwid.openapi.quicklogin.d.e;
import com.iflytek.cloud.SpeechUtility;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.xmlpull.v1.XmlPullParser;

public class b extends c {
    private String c = "https://setting.hicloud.com/AccountServer/IUserInfoMng/opLog";
    private String d;

    public HttpEntity a() {
        try {
            return new StringEntity(this.d, "UTF-8");
        } catch (Exception e) {
            d.d(a.b, k.a(e.getMessage()));
            return null;
        }
    }

    public com.huawei.hwid.openapi.quicklogin.b.a.b a(String str) {
        com.huawei.hwid.openapi.quicklogin.b.a.b bVar = new com.huawei.hwid.openapi.quicklogin.b.a.b();
        d.a("OpLogRequest", "Resolve Response");
        try {
            XmlPullParser a = e.a(str.getBytes("UTF-8"));
            int eventType = a.getEventType();
            d.a("OpLogRequest", "start XmlPullParser");
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
                                bVar.e(a.nextText());
                                break;
                            }
                            bVar.a(Integer.valueOf(a.nextText()).intValue());
                            break;
                        }
                        continue;
                    default:
                        break;
                }
            }
            d.a("OpLogRequest", "XmlPullParser finish");
            return bVar;
        } catch (RuntimeException e) {
            d.d("OpLogRequest", k.a(e.getMessage()));
            return null;
        } catch (Exception e2) {
            d.d("OpLogRequest", k.a(e2.getMessage()));
            return null;
        }
    }

    public b(String str) {
        this.d = str;
    }

    public String b() {
        return this.c;
    }

    public d c() {
        return d.XMLType;
    }
}
