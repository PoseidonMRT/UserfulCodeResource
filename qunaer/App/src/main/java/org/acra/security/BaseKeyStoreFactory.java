package org.acra.security;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.InputStream;
import java.security.KeyStore;

public abstract class BaseKeyStoreFactory implements c {
    private final String a;

    public enum Type {
        CERTIFICATE,
        KEYSTORE
    }

    protected abstract InputStream a(@NonNull Context context);

    public BaseKeyStoreFactory(String str) {
        this.a = str;
    }

    protected String a() {
        return KeyStore.getDefaultType();
    }

    protected Type b() {
        return Type.CERTIFICATE;
    }

    protected char[] c() {
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.support.annotation.Nullable
    public final java.security.KeyStore b(@android.support.annotation.NonNull android.content.Context r7) {
        /*
        r6 = this;
        r1 = 0;
        r0 = r6.a(r7);
        if (r0 == 0) goto L_0x0049;
    L_0x0007:
        r2 = new java.io.BufferedInputStream;
        r2.<init>(r0);
        r0 = r6.a();	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        r0 = java.security.KeyStore.getInstance(r0);	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        r3 = org.acra.security.BaseKeyStoreFactory.AnonymousClass1.a;	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        r4 = r6.b();	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        r4 = r4.ordinal();	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        r3 = r3[r4];	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        switch(r3) {
            case 1: goto L_0x0027;
            case 2: goto L_0x004b;
            default: goto L_0x0023;
        };
    L_0x0023:
        org.acra.util.g.a(r2);
    L_0x0026:
        return r0;
    L_0x0027:
        r3 = r6.a;	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        r3 = java.security.cert.CertificateFactory.getInstance(r3);	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        r3 = r3.generateCertificate(r2);	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        r4 = 0;
        r5 = 0;
        r0.load(r4, r5);	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        r4 = "ca";
        r0.setCertificateEntry(r4, r3);	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        goto L_0x0023;
    L_0x003c:
        r0 = move-exception;
        r3 = org.acra.ACRA.f;	 Catch:{ all -> 0x007d }
        r4 = org.acra.ACRA.e;	 Catch:{ all -> 0x007d }
        r5 = "Could not load certificate";
        r3.c(r4, r5, r0);	 Catch:{ all -> 0x007d }
        org.acra.util.g.a(r2);
    L_0x0049:
        r0 = r1;
        goto L_0x0026;
    L_0x004b:
        r3 = r6.c();	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        r0.load(r2, r3);	 Catch:{ CertificateException -> 0x003c, KeyStoreException -> 0x0053, NoSuchAlgorithmException -> 0x0061, IOException -> 0x006f }
        goto L_0x0023;
    L_0x0053:
        r0 = move-exception;
        r3 = org.acra.ACRA.f;	 Catch:{ all -> 0x007d }
        r4 = org.acra.ACRA.e;	 Catch:{ all -> 0x007d }
        r5 = "Could not load keystore";
        r3.c(r4, r5, r0);	 Catch:{ all -> 0x007d }
        org.acra.util.g.a(r2);
        goto L_0x0049;
    L_0x0061:
        r0 = move-exception;
        r3 = org.acra.ACRA.f;	 Catch:{ all -> 0x007d }
        r4 = org.acra.ACRA.e;	 Catch:{ all -> 0x007d }
        r5 = "Could not load keystore";
        r3.c(r4, r5, r0);	 Catch:{ all -> 0x007d }
        org.acra.util.g.a(r2);
        goto L_0x0049;
    L_0x006f:
        r0 = move-exception;
        r3 = org.acra.ACRA.f;	 Catch:{ all -> 0x007d }
        r4 = org.acra.ACRA.e;	 Catch:{ all -> 0x007d }
        r5 = "Could not load keystore";
        r3.c(r4, r5, r0);	 Catch:{ all -> 0x007d }
        org.acra.util.g.a(r2);
        goto L_0x0049;
    L_0x007d:
        r0 = move-exception;
        org.acra.util.g.a(r2);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.acra.security.BaseKeyStoreFactory.b(android.content.Context):java.security.KeyStore");
    }
}
