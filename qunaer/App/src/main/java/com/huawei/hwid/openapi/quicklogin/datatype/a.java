package com.huawei.hwid.openapi.quicklogin.datatype;

public class a {
    private String a;
    private String b;
    private String c;
    private boolean d = false;
    private boolean e = false;

    public void a(String str) {
        this.b = str;
    }

    public String a() {
        return this.c;
    }

    public void b(String str) {
        this.c = str;
    }

    public void c(String str) {
        this.a = str;
    }

    public String b() {
        return this.a;
    }

    public String toString() {
        return "mAppID:" + this.a + " ;mReqClientType:" + this.b + " ;mDefaultChannel:" + this.c + " ;popLogin:" + String.valueOf(this.d) + " ;chooseAccount:" + String.valueOf(this.e);
    }
}
