package com.huawei.hwid.openapi.quicklogin.d;

import android.content.Context;
import com.huawei.hwid.openapi.quicklogin.d.b.b;
import com.huawei.hwid.openapi.quicklogin.d.b.c;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class a {
    public static String a(Context context, String str) {
        String stringBuffer = new StringBuffer().append(com.huawei.hwid.openapi.quicklogin.a.a.a()).append(com.huawei.hwid.openapi.quicklogin.d.b.a.a()).append(c.a()).append(c.d(k.a())).toString();
        String str2 = null;
        try {
            str2 = b.a(c.c(str), 0, c.c(stringBuffer), 0);
        } catch (Throwable e) {
            d.b("PasswordEncrypter", "InvalidKeyException / " + e.getMessage(), e);
        } catch (Throwable e2) {
            d.b("PasswordEncrypter", "BadPaddingException / " + e2.getMessage(), e2);
        } catch (Throwable e22) {
            d.b("PasswordEncrypter", "IllegalBlockSizeException / " + e22.getMessage(), e22);
        } catch (Throwable e222) {
            d.b("PasswordEncrypter", "NoSuchAlgorithmException / " + e222.getMessage(), e222);
        } catch (Throwable e2222) {
            d.b("PasswordEncrypter", "NoSuchPaddingException / " + e2222.getMessage(), e2222);
        }
        return str2;
    }

    public static String b(Context context, String str) {
        Throwable e;
        Throwable th;
        String stringBuffer = new StringBuffer().append(com.huawei.hwid.openapi.quicklogin.a.a.a()).append(com.huawei.hwid.openapi.quicklogin.d.b.a.a()).append(c.a()).append(c.d(k.a())).toString();
        d.a("PasswordEncrypter", "decrypter begin");
        String str2 = "";
        try {
            byte[] a = b.a(str, c.c(stringBuffer), 0);
            if (a != null) {
                stringBuffer = new String(a, "UTF-8");
                try {
                    Arrays.fill(a, (byte) 0);
                } catch (InvalidKeyException e2) {
                    e = e2;
                    d.b("PasswordEncrypter", "InvalidKeyException / " + e.getMessage(), e);
                    d.a("PasswordEncrypter", "decrypter end");
                    return stringBuffer;
                } catch (BadPaddingException e3) {
                    e = e3;
                    d.b("PasswordEncrypter", "BadPaddingException / " + e.getMessage(), e);
                    d.a("PasswordEncrypter", "decrypter end");
                    return stringBuffer;
                } catch (IllegalBlockSizeException e4) {
                    e = e4;
                    d.b("PasswordEncrypter", "IllegalBlockSizeException / " + e.getMessage(), e);
                    d.a("PasswordEncrypter", "decrypter end");
                    return stringBuffer;
                } catch (NoSuchAlgorithmException e5) {
                    e = e5;
                    d.b("PasswordEncrypter", "NoSuchAlgorithmException / " + e.getMessage(), e);
                    d.a("PasswordEncrypter", "decrypter end");
                    return stringBuffer;
                } catch (NoSuchPaddingException e6) {
                    e = e6;
                    d.b("PasswordEncrypter", "NoSuchPaddingException / " + e.getMessage(), e);
                    d.a("PasswordEncrypter", "decrypter end");
                    return stringBuffer;
                } catch (UnsupportedEncodingException e7) {
                    e = e7;
                    d.b("PasswordEncrypter", "UnsupportedEncodingException / " + e.getMessage(), e);
                    d.a("PasswordEncrypter", "decrypter end");
                    return stringBuffer;
                }
            }
            stringBuffer = str2;
        } catch (Throwable e8) {
            th = e8;
            stringBuffer = str2;
            e = th;
            d.b("PasswordEncrypter", "InvalidKeyException / " + e.getMessage(), e);
            d.a("PasswordEncrypter", "decrypter end");
            return stringBuffer;
        } catch (Throwable e82) {
            th = e82;
            stringBuffer = str2;
            e = th;
            d.b("PasswordEncrypter", "BadPaddingException / " + e.getMessage(), e);
            d.a("PasswordEncrypter", "decrypter end");
            return stringBuffer;
        } catch (Throwable e822) {
            th = e822;
            stringBuffer = str2;
            e = th;
            d.b("PasswordEncrypter", "IllegalBlockSizeException / " + e.getMessage(), e);
            d.a("PasswordEncrypter", "decrypter end");
            return stringBuffer;
        } catch (Throwable e8222) {
            th = e8222;
            stringBuffer = str2;
            e = th;
            d.b("PasswordEncrypter", "NoSuchAlgorithmException / " + e.getMessage(), e);
            d.a("PasswordEncrypter", "decrypter end");
            return stringBuffer;
        } catch (Throwable e82222) {
            th = e82222;
            stringBuffer = str2;
            e = th;
            d.b("PasswordEncrypter", "NoSuchPaddingException / " + e.getMessage(), e);
            d.a("PasswordEncrypter", "decrypter end");
            return stringBuffer;
        } catch (Throwable e822222) {
            th = e822222;
            stringBuffer = str2;
            e = th;
            d.b("PasswordEncrypter", "UnsupportedEncodingException / " + e.getMessage(), e);
            d.a("PasswordEncrypter", "decrypter end");
            return stringBuffer;
        }
        d.a("PasswordEncrypter", "decrypter end");
        return stringBuffer;
    }
}
