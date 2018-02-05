package com.iflytek.cloud.thirdparty;

import android.text.TextUtils;
import com.iflytek.cloud.SpeechError;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.util.ByteArrayBuffer;

@Instrumented
public class by extends Thread {
    private int a = 20000;
    private a b = null;
    private volatile boolean c = false;
    private URL d = null;
    private ArrayList<byte[]> e = new ArrayList();
    private String f;
    private Object g = null;
    private int h = 0;

    public interface a {
        void a(SpeechError speechError);

        void a(by byVar, byte[] bArr);
    }

    public static URL a(String str, String str2) {
        if (!(TextUtils.isEmpty(str) || TextUtils.isEmpty(str2))) {
            if (!str.endsWith("?")) {
                str = str + "?";
            }
            str = str + str2;
        }
        return new URL(str);
    }

    private void a(SpeechError speechError) {
        if (this.b != null && !this.c) {
            this.b.a(speechError);
        }
    }

    private byte[] a(InputStream inputStream) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(1024);
        byte[] bArr = new byte[1024];
        while (!this.c) {
            int read = bufferedInputStream.read(bArr, 0, 1024);
            if (read == -1) {
                break;
            }
            byteArrayBuffer.append(bArr, 0, read);
        }
        return byteArrayBuffer.toByteArray();
    }

    private void b(byte[] bArr) {
        if (this.b != null && !this.c) {
            this.b.a(this, bArr);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void c() {
        /*
        r6 = this;
        r2 = 0;
        r0 = "Start connect server";
        com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ Exception -> 0x00cc, all -> 0x00b3 }
        r0 = r6.d;	 Catch:{ Exception -> 0x00cc, all -> 0x00b3 }
        r0 = r0.openConnection();	 Catch:{ Exception -> 0x00cc, all -> 0x00b3 }
        r0 = com.mqunar.necro.agent.instrumentation.HttpInstrumentation.openConnection(r0);	 Catch:{ Exception -> 0x00cc, all -> 0x00b3 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00cc, all -> 0x00b3 }
        r1 = r6.a;	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r0.setConnectTimeout(r1);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r1 = r6.a;	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r0.setReadTimeout(r1);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r1 = "GET";
        r0.setRequestMethod(r1);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r1 = r0.getResponseCode();	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r3.<init>();	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r4 = "responseCode = ";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r3 = r3.append(r1);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        com.iflytek.cloud.thirdparty.cb.a(r3);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r3 != r1) goto L_0x0055;
    L_0x003f:
        r2 = r0.getInputStream();	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r1 = r6.a(r2);	 Catch:{ Exception -> 0x00cf, all -> 0x00c2 }
        r6.b(r1);	 Catch:{ Exception -> 0x00cf, all -> 0x00c2 }
    L_0x004a:
        if (r2 == 0) goto L_0x004f;
    L_0x004c:
        r2.close();	 Catch:{ Exception -> 0x00d5 }
    L_0x004f:
        if (r0 == 0) goto L_0x0054;
    L_0x0051:
        r0.disconnect();	 Catch:{ Exception -> 0x00d5 }
    L_0x0054:
        return;
    L_0x0055:
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r3.<init>();	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r4 = "MscHttpRequest connect error:";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r3 = r3.append(r1);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        com.iflytek.cloud.thirdparty.cb.a(r3);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r3 = new com.iflytek.cloud.SpeechError;	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r1 = java.lang.Math.abs(r1);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r1 = r1 + 12000;
        r3.<init>(r1);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        r6.a(r3);	 Catch:{ Exception -> 0x007a, all -> 0x00c2 }
        goto L_0x004a;
    L_0x007a:
        r1 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r5;
    L_0x007f:
        com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ all -> 0x00c7 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c7 }
        r3.<init>();	 Catch:{ all -> 0x00c7 }
        r4 = "MscHttpRequest error:";
        r3 = r3.append(r4);	 Catch:{ all -> 0x00c7 }
        r0 = r0.toString();	 Catch:{ all -> 0x00c7 }
        r0 = r3.append(r0);	 Catch:{ all -> 0x00c7 }
        r0 = r0.toString();	 Catch:{ all -> 0x00c7 }
        com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ all -> 0x00c7 }
        r0 = new com.iflytek.cloud.SpeechError;	 Catch:{ all -> 0x00c7 }
        r3 = 20003; // 0x4e23 float:2.803E-41 double:9.883E-320;
        r0.<init>(r3);	 Catch:{ all -> 0x00c7 }
        r6.a(r0);	 Catch:{ all -> 0x00c7 }
        if (r1 == 0) goto L_0x00ab;
    L_0x00a8:
        r1.close();	 Catch:{ Exception -> 0x00b1 }
    L_0x00ab:
        if (r2 == 0) goto L_0x0054;
    L_0x00ad:
        r2.disconnect();	 Catch:{ Exception -> 0x00b1 }
        goto L_0x0054;
    L_0x00b1:
        r0 = move-exception;
        goto L_0x0054;
    L_0x00b3:
        r0 = move-exception;
        r1 = r2;
    L_0x00b5:
        if (r2 == 0) goto L_0x00ba;
    L_0x00b7:
        r2.close();	 Catch:{ Exception -> 0x00c0 }
    L_0x00ba:
        if (r1 == 0) goto L_0x00bf;
    L_0x00bc:
        r1.disconnect();	 Catch:{ Exception -> 0x00c0 }
    L_0x00bf:
        throw r0;
    L_0x00c0:
        r1 = move-exception;
        goto L_0x00bf;
    L_0x00c2:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x00b5;
    L_0x00c7:
        r0 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r5;
        goto L_0x00b5;
    L_0x00cc:
        r0 = move-exception;
        r1 = r2;
        goto L_0x007f;
    L_0x00cf:
        r1 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r5;
        goto L_0x007f;
    L_0x00d5:
        r0 = move-exception;
        goto L_0x0054;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.iflytek.cloud.thirdparty.by.c():void");
    }

    private int d() {
        int i = 0;
        for (int i2 = 0; i2 < this.e.size(); i2++) {
            i += ((byte[]) this.e.get(i2)).length;
        }
        return i;
    }

    public void a() {
        this.c = true;
    }

    public void a(int i) {
        this.h = i;
    }

    public void a(a aVar) {
        this.b = aVar;
        start();
    }

    public void a(String str, String str2, byte[] bArr) {
        this.e.clear();
        a(bArr);
        try {
            this.d = a(str, str2);
        } catch (Throwable e) {
            cb.a(e);
        }
    }

    public void a(String str, String str2, byte[] bArr, String str3) {
        this.f = str3;
        a(str, str2, bArr);
    }

    public void a(byte[] bArr) {
        if (bArr != null) {
            this.e.add(bArr);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void b() {
        /*
        r6 = this;
        r2 = 0;
        r0 = "MscHttpRequest start Post";
        com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ Exception -> 0x0126, all -> 0x011c }
        r0 = r6.d;	 Catch:{ Exception -> 0x0126, all -> 0x011c }
        r0 = r0.openConnection();	 Catch:{ Exception -> 0x0126, all -> 0x011c }
        r0 = com.mqunar.necro.agent.instrumentation.HttpInstrumentation.openConnection(r0);	 Catch:{ Exception -> 0x0126, all -> 0x011c }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x0126, all -> 0x011c }
        r1 = 1;
        r0.setDoOutput(r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = 1;
        r0.setDoInput(r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = 0;
        r0.setUseCaches(r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = "POST";
        r0.setRequestMethod(r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = r6.a;	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r0.setConnectTimeout(r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = r6.a;	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r0.setReadTimeout(r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = "Content-length";
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3.<init>();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r4 = "";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r4 = r6.d();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r0.setRequestProperty(r1, r3);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = "Connection";
        r3 = "Keep-Alive";
        r0.setRequestProperty(r1, r3);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = "Charset";
        r3 = "utf-8";
        r0.setRequestProperty(r1, r3);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = "Content-Type";
        r3 = "application/x-gzip";
        r0.setRequestProperty(r1, r3);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = r6.f;	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = android.text.TextUtils.isEmpty(r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        if (r1 != 0) goto L_0x0076;
    L_0x0066:
        r1 = r6.f;	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = r1.getBytes();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3 = 2;
        r1 = android.util.Base64.encodeToString(r1, r3);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3 = "X-Par";
        r0.setRequestProperty(r3, r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
    L_0x0076:
        r3 = r0.getOutputStream();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = r6.e;	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r4 = r1.iterator();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
    L_0x0080:
        r1 = r4.hasNext();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        if (r1 == 0) goto L_0x00c4;
    L_0x0086:
        r1 = r4.next();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = (byte[]) r1;	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3.write(r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        goto L_0x0080;
    L_0x0090:
        r1 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r5;
    L_0x0095:
        r3 = new com.iflytek.cloud.SpeechError;	 Catch:{ all -> 0x011f }
        r4 = 20003; // 0x4e23 float:2.803E-41 double:9.883E-320;
        r3.<init>(r4);	 Catch:{ all -> 0x011f }
        r6.a(r3);	 Catch:{ all -> 0x011f }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x011f }
        r3.<init>();	 Catch:{ all -> 0x011f }
        r4 = "MscHttpRequest error:";
        r3 = r3.append(r4);	 Catch:{ all -> 0x011f }
        r0 = r0.toString();	 Catch:{ all -> 0x011f }
        r0 = r3.append(r0);	 Catch:{ all -> 0x011f }
        r0 = r0.toString();	 Catch:{ all -> 0x011f }
        com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ all -> 0x011f }
        if (r1 == 0) goto L_0x00be;
    L_0x00bb:
        r1.close();	 Catch:{ Exception -> 0x0124 }
    L_0x00be:
        if (r2 == 0) goto L_0x00c3;
    L_0x00c0:
        r2.disconnect();	 Catch:{ Exception -> 0x0124 }
    L_0x00c3:
        return;
    L_0x00c4:
        r3.flush();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3.close();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = r0.getResponseCode();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r3 != r1) goto L_0x00ea;
    L_0x00d2:
        r2 = r0.getInputStream();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = r6.a(r2);	 Catch:{ Exception -> 0x012a, all -> 0x010b }
        r6.b(r1);	 Catch:{ Exception -> 0x012a, all -> 0x010b }
    L_0x00dd:
        if (r2 == 0) goto L_0x00e2;
    L_0x00df:
        r2.close();	 Catch:{ Exception -> 0x00e8 }
    L_0x00e2:
        if (r0 == 0) goto L_0x00c3;
    L_0x00e4:
        r0.disconnect();	 Catch:{ Exception -> 0x00e8 }
        goto L_0x00c3;
    L_0x00e8:
        r0 = move-exception;
        goto L_0x00c3;
    L_0x00ea:
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3.<init>();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r4 = "Http Request Failed.";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3 = r3.append(r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        com.iflytek.cloud.thirdparty.cb.a(r3);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r3 = new com.iflytek.cloud.SpeechError;	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r1 = r1 + 12000;
        r3.<init>(r1);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        r6.a(r3);	 Catch:{ Exception -> 0x0090, all -> 0x010b }
        goto L_0x00dd;
    L_0x010b:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x010f:
        if (r2 == 0) goto L_0x0114;
    L_0x0111:
        r2.close();	 Catch:{ Exception -> 0x011a }
    L_0x0114:
        if (r1 == 0) goto L_0x0119;
    L_0x0116:
        r1.disconnect();	 Catch:{ Exception -> 0x011a }
    L_0x0119:
        throw r0;
    L_0x011a:
        r1 = move-exception;
        goto L_0x0119;
    L_0x011c:
        r0 = move-exception;
        r1 = r2;
        goto L_0x010f;
    L_0x011f:
        r0 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r5;
        goto L_0x010f;
    L_0x0124:
        r0 = move-exception;
        goto L_0x00c3;
    L_0x0126:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0095;
    L_0x012a:
        r1 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r5;
        goto L_0x0095;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.iflytek.cloud.thirdparty.by.b():void");
    }

    public void b(int i) {
        this.a = i;
    }

    public void run() {
        if (this.h == 1) {
            b();
        } else {
            c();
        }
    }
}
