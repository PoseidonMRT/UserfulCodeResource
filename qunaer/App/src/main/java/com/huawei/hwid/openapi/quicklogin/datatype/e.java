package com.huawei.hwid.openapi.quicklogin.datatype;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import com.huawei.hwid.openapi.OpenHwID;
import com.huawei.hwid.openapi.quicklogin.a.a;
import com.huawei.hwid.openapi.quicklogin.d.a.b;
import com.huawei.hwid.openapi.quicklogin.d.b.c;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import com.huawei.hwid.openapi.quicklogin.d.d;
import com.mqunar.contacts.basis.model.Contact;
import java.security.MessageDigest;

public class e {
    private Context a;
    private String b;
    private long c;
    private String d = "";
    private long e;
    private String f = "";
    private String g;
    private String h;
    private String i;
    private String j;
    private String k;
    private String l;
    private int m;
    private String n;
    private String o;

    public e(Context context, String str) {
        this.a = context;
        this.b = str;
        this.c = System.currentTimeMillis();
        this.d = b.a();
        this.e = 0;
        this.g = "";
        a("no_user");
        this.h = d.a(context, -999);
        this.j = "";
        this.k = m();
        com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogItem", "e*c*y*t:" + k.a(this.k, true));
        this.l = "";
        this.m = 0;
        this.n = d.a();
        this.o = context.getPackageName();
        if (b.a(context)) {
            this.g = b.b(context);
        }
    }

    private String m() {
        Throwable th;
        String str = "";
        str = d.b(this.a);
        try {
            ApplicationInfo applicationInfo = this.a.getPackageManager().getApplicationInfo(this.a.getPackageName(), 128);
            if (applicationInfo.metaData == null || applicationInfo.metaData.getInt("oplog_encrypt") != 1) {
                return str;
            }
            byte[] digest = MessageDigest.getInstance("SHA-256").digest((str + new StringBuffer().append(a.b()).append(com.huawei.hwid.openapi.quicklogin.d.b.a.b()).append(c.b()).append(com.huawei.hwid.openapi.quicklogin.d.c.d(k.b())).toString()).getBytes("UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : digest) {
                String toHexString = Integer.toHexString(b & 255);
                if (toHexString.length() == 1) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(toHexString);
            }
            return stringBuilder.toString();
        } catch (Throwable e) {
            th = e;
            str = "";
            com.huawei.hwid.openapi.quicklogin.d.b.d.b("OpLogItem", th.getMessage(), th);
            return str;
        } catch (Throwable e2) {
            th = e2;
            str = "";
            com.huawei.hwid.openapi.quicklogin.d.b.d.b("OpLogItem", th.getMessage(), th);
            return str;
        }
    }

    public String a() {
        return this.b;
    }

    public long b() {
        return this.c;
    }

    public long c() {
        return this.e;
    }

    public String d() {
        return this.g;
    }

    public String e() {
        return this.h;
    }

    public String f() {
        return this.k;
    }

    public int g() {
        return this.m;
    }

    public String h() {
        return this.i;
    }

    public String i() {
        return this.j;
    }

    public String j() {
        return this.l;
    }

    public String k() {
        return this.n;
    }

    public String l() {
        return this.o;
    }

    public void a(long j) {
        this.e = j;
        this.f = b.a();
    }

    public void a(String str) {
        if (str == null || "".equals(str.trim()) || "no_user".equals(str)) {
            this.i = "no_user";
        }
        if ("no_user".equals(this.i) && !"com.huawei.hwid".equals(this.a.getPackageName())) {
            try {
                this.i = (String) OpenHwID.getUserInfo().get("userName");
            } catch (Throwable e) {
                com.huawei.hwid.openapi.quicklogin.d.b.d.b("OpLogItem", "not hwid and no ****HwID", e);
            }
        }
        if (this.i == null || "".equals(this.i.trim())) {
            this.i = "no_user";
        }
    }

    public void b(String str) {
        this.j = str;
    }

    public void c(String str) {
        this.l = str;
    }

    private String d(String str) {
        int i = 0;
        if (str == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        int length = str.length();
        if (com.huawei.hwid.openapi.quicklogin.d.c.a(str) && length > 5) {
            stringBuilder.append(str.substring(0, length - 5));
            stringBuilder.append("*****");
        } else if (length > 1) {
            stringBuilder.append(str.charAt(0));
            int i2 = length / 2;
            while (i < i2) {
                stringBuilder.append(Contact.OTHER);
                i++;
            }
            stringBuilder.append(str.substring(i2 + 1, length));
        } else if (length == 1) {
            stringBuilder.append(Contact.OTHER);
        } else {
            stringBuilder.append("");
        }
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(this.b).append("|").append(this.d).append("|").append(this.f).append("|").append(this.g).append("|").append(this.h).append("|").append(d(this.i)).append("|").append(this.j).append("|").append(this.k).append("|").append(this.l).append("|").append(this.m).append("|").append(this.n).append("|").append(this.o).append("|");
        return stringBuilder.toString();
    }
}
