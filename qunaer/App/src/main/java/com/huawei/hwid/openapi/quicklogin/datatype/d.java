package com.huawei.hwid.openapi.quicklogin.datatype;

import android.content.Context;
import android.content.pm.PackageManager;
import com.huawei.hwid.openapi.quicklogin.d.a.a;
import com.huawei.hwid.openapi.quicklogin.d.a.c;

public class d {
    private e a;
    private String b;
    private String c = com.huawei.hwid.openapi.quicklogin.d.d.b();
    private String d;

    public d(Context context, e eVar) {
        this.a = eVar;
        a(context);
        if (context.getPackageName().equalsIgnoreCase("com.huawei.hwid")) {
            this.d = a.a(context, "com.huawei.hwid");
        } else {
            this.d = "90002090";
        }
    }

    private void a(Context context) {
        String valueOf = String.valueOf(30301300);
        PackageManager packageManager = context.getPackageManager();
        if ("com.huawei.hwid".equals(context.getPackageName())) {
            try {
                valueOf = packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (Throwable e) {
                c.b("OpLogInfo", e.getMessage(), e);
            }
            this.b = "HwID " + valueOf;
        } else if (valueOf.length() >= 4) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("OpenSDK").append(" ").append(valueOf.charAt(0)).append(".").append(valueOf.charAt(1)).append(".").append(valueOf.substring(2, 4));
            this.b = stringBuilder.toString();
        } else {
            this.b = "OpenSDK";
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String toString() {
        /*
        r6 = this;
        r0 = r6.a();
        if (r0 != 0) goto L_0x0009;
    L_0x0006:
        r0 = "";
    L_0x0008:
        return r0;
    L_0x0009:
        r1 = new java.io.ByteArrayOutputStream;
        r1.<init>();
        r0 = com.huawei.hwid.openapi.quicklogin.d.e.a(r1);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r2 = "UTF-8";
        r3 = 1;
        r3 = java.lang.Boolean.valueOf(r3);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r0.startDocument(r2, r3);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r2 = 0;
        r3 = "OpLogReq";
        r0.startTag(r2, r3);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r2 = "clientVer";
        r3 = r6.b;	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r2, r3);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r2 = "osVersion";
        r3 = r6.c;	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r2, r3);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r2 = "channel";
        r3 = r6.d;	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r2, r3);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r2 = 0;
        r3 = "logList";
        r2 = r0.startTag(r2, r3);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r3 = 0;
        r4 = "size";
        r5 = 1;
        r5 = java.lang.String.valueOf(r5);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r2.attribute(r3, r4, r5);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r2 = "Log";
        r3 = r6.a;	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r3 = r3.toString();	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        com.huawei.hwid.openapi.quicklogin.d.e.a(r0, r2, r3);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r2 = 0;
        r3 = "logList";
        r0.endTag(r2, r3);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r2 = 0;
        r3 = "OpLogReq";
        r0.endTag(r2, r3);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r0.endDocument();	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r0 = "UTF-8";
        r0 = r1.toString(r0);	 Catch:{ RuntimeException -> 0x0078, Exception -> 0x0092 }
        r1.close();	 Catch:{ IOException -> 0x006d }
        goto L_0x0008;
    L_0x006d:
        r1 = move-exception;
        r2 = "OpLogInfo";
        r3 = r1.getMessage();
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(r2, r3, r1);
        goto L_0x0008;
    L_0x0078:
        r0 = move-exception;
        r2 = "OpLogInfo";
        r3 = "toString";
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r2, r3, r0);	 Catch:{ all -> 0x00ad }
        r0 = "";
        r1.close();	 Catch:{ IOException -> 0x0086 }
        goto L_0x0008;
    L_0x0086:
        r1 = move-exception;
        r2 = "OpLogInfo";
        r3 = r1.getMessage();
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(r2, r3, r1);
        goto L_0x0008;
    L_0x0092:
        r0 = move-exception;
        r2 = "OpLogInfo";
        r3 = "toString";
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r2, r3, r0);	 Catch:{ all -> 0x00ad }
        r0 = "";
        r1.close();	 Catch:{ IOException -> 0x00a1 }
        goto L_0x0008;
    L_0x00a1:
        r1 = move-exception;
        r2 = "OpLogInfo";
        r3 = r1.getMessage();
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(r2, r3, r1);
        goto L_0x0008;
    L_0x00ad:
        r0 = move-exception;
        r1.close();	 Catch:{ IOException -> 0x00b2 }
    L_0x00b1:
        throw r0;
    L_0x00b2:
        r1 = move-exception;
        r2 = "OpLogInfo";
        r3 = r1.getMessage();
        com.huawei.hwid.openapi.quicklogin.d.b.d.b(r2, r3, r1);
        goto L_0x00b1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hwid.openapi.quicklogin.datatype.d.toString():java.lang.String");
    }

    private boolean a() {
        return a(this.b) && a(this.c) && a(this.d) && Integer.valueOf(this.a.a()).intValue() > 0 && this.a.b() > 0 && a(this.a.d()) && a(this.a.e()) && a(this.a.f()) && this.a.g() == 0 && a(this.a.k()) && a(this.a.l());
    }

    private boolean a(String str) {
        return (str == null || "".equals(str.trim())) ? false : true;
    }
}
