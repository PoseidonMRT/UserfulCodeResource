package com.huawei.hwid.openapi.update.a;

import android.content.Context;
import com.huawei.hwid.openapi.update.c;
import com.huawei.hwid.openapi.update.k;
import java.io.File;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class b {
    private int a;
    private String b = null;
    private String c = null;
    private String d = null;
    private String e = null;
    private String f = null;
    private String g = null;
    private String h = null;
    private String i = null;
    private String j = null;
    private String k = null;
    private String l = null;
    private String m = null;
    private String n = null;
    private String o = null;
    private String p = null;
    private String q = null;
    private String r = "";
    private int s;
    private boolean t = false;
    private boolean u = false;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(java.io.InputStream r7) {
        /*
        r6 = this;
        r0 = 0;
        r0 = com.huawei.hwid.openapi.update.k.a(r7);	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a, all -> 0x0068 }
        r1 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        r2 = r1.newPullParser();	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        r1 = "UTF-8";
        r2.setInput(r0, r1);	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        r1 = "";
        r1 = "NewVersionInfo";
        r3 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        r3.<init>();	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        r4 = "pullParser: ";
        r3 = r3.append(r4);	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        r4 = r2.toString();	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        r3 = r3.append(r4);	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        r3 = r3.toString();	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r1, r3);	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        r1 = r2.getEventType();	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
    L_0x0034:
        r3 = 1;
        if (r1 == r3) goto L_0x0046;
    L_0x0037:
        r3 = r2.getName();	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        r4 = 2;
        if (r1 != r4) goto L_0x0041;
    L_0x003e:
        r6.a(r2, r3);	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
    L_0x0041:
        r1 = r2.next();	 Catch:{ XmlPullParserException -> 0x004c, IOException -> 0x005a }
        goto L_0x0034;
    L_0x0046:
        r1 = "NewVersionInfo";
        com.huawei.hwid.openapi.update.k.b(r0, r1);
    L_0x004b:
        return;
    L_0x004c:
        r1 = move-exception;
        r1 = "NewVersionInfo";
        r2 = "parse filelist to versioninfo error";
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r1, r2);	 Catch:{ all -> 0x0072 }
        r1 = "NewVersionInfo";
        com.huawei.hwid.openapi.update.k.b(r0, r1);
        goto L_0x004b;
    L_0x005a:
        r1 = move-exception;
        r1 = "NewVersionInfo";
        r2 = "parse filelist to versioninfo error";
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r1, r2);	 Catch:{ all -> 0x0072 }
        r1 = "NewVersionInfo";
        com.huawei.hwid.openapi.update.k.b(r0, r1);
        goto L_0x004b;
    L_0x0068:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x006c:
        r2 = "NewVersionInfo";
        com.huawei.hwid.openapi.update.k.b(r1, r2);
        throw r0;
    L_0x0072:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x006c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hwid.openapi.update.a.b.a(java.io.InputStream):void");
    }

    public void a(Context context, InputStream inputStream) {
        Throwable th;
        InputStream inputStream2 = null;
        InputStream a;
        try {
            a = k.a(inputStream);
            try {
                XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
                newPullParser.setInput(a, "UTF-8");
                String a2 = k.a(context);
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                Object obj = null;
                for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                    str = newPullParser.getName();
                    if (eventType == 2) {
                        if ("language".equalsIgnoreCase(str)) {
                            obj = newPullParser.getAttributeValue(0);
                        } else if ("feature".equalsIgnoreCase(str)) {
                            if (a2.equals(obj)) {
                                this.r += newPullParser.nextText().trim() + c.a;
                            }
                            if ("en-us".equals(obj)) {
                                stringBuffer.append("".equals(this.r) ? newPullParser.nextText().trim() + c.a : this.r);
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    }
                }
                if ("".equals(this.r)) {
                    this.r = stringBuffer.toString();
                }
                k.b(a, "NewVersionInfo");
            } catch (XmlPullParserException e) {
            } catch (Exception e2) {
            }
        } catch (XmlPullParserException e3) {
            a = null;
            try {
                com.huawei.hwid.openapi.quicklogin.d.a.c.d("NewVersionInfo", "parse changelog to versioninfo error");
                k.b(a, "NewVersionInfo");
            } catch (Throwable th2) {
                Throwable th3 = th2;
                inputStream2 = a;
                th = th3;
                k.b(inputStream2, "NewVersionInfo");
                throw th;
            }
        } catch (Exception e4) {
            a = null;
            com.huawei.hwid.openapi.quicklogin.d.a.c.d("NewVersionInfo", "parse changelog to versioninfo error");
            k.b(a, "NewVersionInfo");
        } catch (Throwable th4) {
            th = th4;
            k.b(inputStream2, "NewVersionInfo");
            throw th;
        }
    }

    private void a(XmlPullParser xmlPullParser, String str) {
        if ("spath".equalsIgnoreCase(str)) {
            this.b = xmlPullParser.nextText();
            com.huawei.hwid.openapi.quicklogin.d.a.c.b("NewVersionInfo", "this.sPath: " + this.b);
        } else if ("dpath".equalsIgnoreCase(str)) {
            this.c = xmlPullParser.nextText();
            com.huawei.hwid.openapi.quicklogin.d.a.c.b("NewVersionInfo", "this.dPath: " + this.c);
        } else if ("md5".equalsIgnoreCase(str)) {
            this.g = xmlPullParser.nextText();
            com.huawei.hwid.openapi.quicklogin.d.a.c.b("NewVersionInfo", "this.strMd5: " + this.g);
        } else if ("size".equalsIgnoreCase(str)) {
            this.a = Integer.parseInt(xmlPullParser.nextText());
            com.huawei.hwid.openapi.quicklogin.d.a.c.b("NewVersionInfo", "this.totalSize: " + this.a);
        } else if ("packageName".equalsIgnoreCase(str)) {
            this.m = xmlPullParser.nextText();
            com.huawei.hwid.openapi.quicklogin.d.a.c.b("NewVersionInfo", "this.packageName: " + this.m);
        } else if ("versionName".equalsIgnoreCase(str)) {
            this.o = xmlPullParser.nextText();
            com.huawei.hwid.openapi.quicklogin.d.a.c.b("NewVersionInfo", "this.versionName: " + this.o);
        } else if ("versionCode".equalsIgnoreCase(str)) {
            this.n = xmlPullParser.nextText();
            com.huawei.hwid.openapi.quicklogin.d.a.c.b("NewVersionInfo", "this.versionCode: " + this.n);
        }
    }

    public void a(String str) {
        this.f = str;
    }

    public void b(String str) {
        this.h = str;
    }

    public void c(String str) {
        this.i = str;
    }

    public void d(String str) {
        this.k = str;
    }

    public String a() {
        return this.n;
    }

    public String b() {
        return this.o;
    }

    public int c() {
        return this.a;
    }

    public void a(int i) {
        this.a = i;
    }

    public String d() {
        return this.b;
    }

    public String e() {
        this.d = this.k + "full" + File.separator + this.b;
        return this.d;
    }

    public String f() {
        return this.e;
    }

    public void e(String str) {
        this.e = str;
    }

    public String g() {
        return this.f;
    }

    public String h() {
        return this.g;
    }

    public String i() {
        return this.k;
    }

    public void f(String str) {
        this.q = str;
    }

    public void g(String str) {
        this.j = str;
    }

    public void h(String str) {
        this.l = str;
    }

    public void b(int i) {
        this.s = i;
    }

    public void a(boolean z) {
        this.t = z;
    }
}
