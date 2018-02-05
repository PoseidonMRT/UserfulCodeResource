package com.huawei.hwid.openapi.quicklogin.b;

import android.content.Context;
import com.huawei.hwid.openapi.quicklogin.b.a.b;
import com.huawei.hwid.openapi.quicklogin.b.b.f;

public class a extends c {
    private String c = "https://setting.hicloud.com/AccountServer/IUserInfoMng/getTmpST";
    private String d = "7";
    private String e = "01.01";
    private String f;
    private String g;
    private int h;
    private Context i;

    public a(Context context, String str, int i) {
        this.i = context;
        this.f = context.getPackageName();
        this.g = str;
        this.h = i;
        if (this.f.equalsIgnoreCase("com.huawei.hwid")) {
            this.d = "7";
        } else {
            this.d = "2090";
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.http.HttpEntity a() {
        /*
        r5 = this;
        r1 = 0;
        r2 = new java.io.ByteArrayOutputStream;
        r2.<init>();
        r0 = com.huawei.hwid.openapi.quicklogin.d.e.a(r2);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = "UTF-8";
        r4 = 1;
        r4 = java.lang.Boolean.valueOf(r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r0.startDocument(r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = 0;
        r4 = "GetTmpSTReq";
        r0.startTag(r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = "version";
        r4 = r5.e;	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = "serviceToken";
        r4 = r5.g;	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = "appID";
        r4 = r5.f;	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = "devType";
        r4 = "0";
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = "devID";
        r4 = r5.i;	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r4 = com.huawei.hwid.openapi.quicklogin.d.d.e(r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = "reqClientType";
        r4 = r5.d;	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = "reqClientType";
        r4 = r5.d;	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = "channel";
        r4 = "7000000";
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r3 = 0;
        r4 = "GetTmpSTReq";
        r0.endTag(r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r0.endDocument();	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r0 = "UTF-8";
        r3 = r2.toString(r0);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r0 = new org.apache.http.entity.StringEntity;	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r4 = "UTF-8";
        r0.<init>(r3, r4);	 Catch:{ RuntimeException -> 0x007b, Exception -> 0x0095 }
        r2.close();	 Catch:{ IOException -> 0x0070 }
    L_0x006f:
        return r0;
    L_0x0070:
        r1 = move-exception;
        r2 = a;
        r3 = r1.getMessage();
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(r2, r3, r1);
        goto L_0x006f;
    L_0x007b:
        r0 = move-exception;
        r3 = a;	 Catch:{ all -> 0x00af }
        r4 = r0.toString();	 Catch:{ all -> 0x00af }
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(r3, r4, r0);	 Catch:{ all -> 0x00af }
        r2.close();	 Catch:{ IOException -> 0x008a }
    L_0x0088:
        r0 = r1;
        goto L_0x006f;
    L_0x008a:
        r0 = move-exception;
        r2 = a;
        r3 = r0.getMessage();
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(r2, r3, r0);
        goto L_0x0088;
    L_0x0095:
        r0 = move-exception;
        r3 = a;	 Catch:{ all -> 0x00af }
        r4 = r0.getMessage();	 Catch:{ all -> 0x00af }
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(r3, r4, r0);	 Catch:{ all -> 0x00af }
        r2.close();	 Catch:{ IOException -> 0x00a4 }
    L_0x00a2:
        r0 = r1;
        goto L_0x006f;
    L_0x00a4:
        r0 = move-exception;
        r2 = a;
        r3 = r0.getMessage();
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(r2, r3, r0);
        goto L_0x00a2;
    L_0x00af:
        r0 = move-exception;
        r2.close();	 Catch:{ IOException -> 0x00b4 }
    L_0x00b3:
        throw r0;
    L_0x00b4:
        r1 = move-exception;
        r2 = a;
        r3 = r1.getMessage();
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(r2, r3, r1);
        goto L_0x00b3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hwid.openapi.quicklogin.b.a.a():org.apache.http.HttpEntity");
    }

    public String b() {
        String str = "https://setting.";
        if (this.h >= 1 && this.h <= 999 && this.c.startsWith(str)) {
            this.c = this.c.substring(0, str.length() - 1) + this.h + this.c.substring(str.length() - 1);
        }
        return this.c;
    }

    public d c() {
        return d.XMLType;
    }

    public b a(String str) {
        return f.a(str);
    }
}
