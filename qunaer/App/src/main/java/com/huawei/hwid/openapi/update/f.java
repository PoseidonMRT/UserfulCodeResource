package com.huawei.hwid.openapi.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import com.huawei.hwid.openapi.update.a.b;
import com.huawei.hwid.openapi.update.b.a;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

@Instrumented
public class f extends Thread {
    private Context a;
    private Handler b;
    private int c;
    private boolean d = false;

    public f(Context context, Handler handler, int i) {
        this.a = context;
        this.b = handler;
        this.c = i;
    }

    public void a() {
        this.d = true;
    }

    private void a(int i, int i2) {
        Message obtainMessage = this.b.obtainMessage(i);
        obtainMessage.obj = Integer.valueOf(i2);
        obtainMessage.sendToTarget();
    }

    private void b(int i, int i2) {
        Message obtainMessage = this.b.obtainMessage(3);
        obtainMessage.arg1 = i / 1000;
        obtainMessage.arg2 = i2 / 1000;
        obtainMessage.obj = Integer.valueOf(this.c);
        obtainMessage.sendToTarget();
    }

    public boolean a(b bVar) {
        String h = bVar.h();
        String a = a.a(bVar.f());
        if (a == null) {
            c.b("OtaDownloadThread", "checkMd5 md5Hex: is null");
        }
        if (h == null || a == null) {
            return false;
        }
        return h.equals(a);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r15 = this;
        r8 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r1 = 0;
        r3 = 4;
        r7 = 0;
        r0 = "OtaDownloadThread";
        r2 = "startDownloadVersion run";
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r0, r2);
        r0 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        r9 = new byte[r0];
        r0 = com.huawei.hwid.openapi.update.e.a();
        r2 = r15.c;
        r10 = r0.b(r2);
        if (r10 != 0) goto L_0x0029;
    L_0x001c:
        r0 = "OtaDownloadThread";
        r1 = "no new version to download";
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r0, r1);
        r0 = r15.c;
        r15.a(r3, r0);
    L_0x0028:
        return;
    L_0x0029:
        r0 = 1;
        r10.a(r0);
        r0 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r2 = r10.c();
        r15.b(r0, r2);
        r0 = r10.e();	 Catch:{ SocketException -> 0x02c5, IOException -> 0x02f3, Exception -> 0x0310, all -> 0x033e }
        r2 = "https:";
        r2 = r0.startsWith(r2);	 Catch:{ SocketException -> 0x02c5, IOException -> 0x02f3, Exception -> 0x0310, all -> 0x033e }
        if (r2 == 0) goto L_0x004a;
    L_0x0042:
        r2 = "https:";
        r3 = "http:";
        r0 = r0.replaceFirst(r2, r3);	 Catch:{ SocketException -> 0x02c5, IOException -> 0x02f3, Exception -> 0x0310, all -> 0x033e }
    L_0x004a:
        r3 = new java.net.URL;	 Catch:{ SocketException -> 0x02c5, IOException -> 0x02f3, Exception -> 0x0310, all -> 0x033e }
        r3.<init>(r0);	 Catch:{ SocketException -> 0x02c5, IOException -> 0x02f3, Exception -> 0x0310, all -> 0x033e }
        r0 = r3.openConnection();	 Catch:{ NumberFormatException -> 0x00c6, Exception -> 0x00dd, SocketException -> 0x02c5, IOException -> 0x02f3, all -> 0x033e }
        r0 = com.mqunar.necro.agent.instrumentation.HttpInstrumentation.openConnection(r0);	 Catch:{ NumberFormatException -> 0x00c6, Exception -> 0x00dd, SocketException -> 0x02c5, IOException -> 0x02f3, all -> 0x033e }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ NumberFormatException -> 0x00c6, Exception -> 0x00dd, SocketException -> 0x02c5, IOException -> 0x02f3, all -> 0x033e }
        r2 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r0.setConnectTimeout(r2);	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r2 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r0.setReadTimeout(r2);	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r2 = r0.getResponseCode();	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        if (r2 == r8) goto L_0x008e;
    L_0x0069:
        r4 = "OtaDownloadThread";
        r5 = new java.lang.StringBuilder;	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r5.<init>();	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r6 = "server response code is not 200,code is ";
        r5 = r5.append(r6);	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r2 = r5.append(r2);	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r2 = r2.toString();	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r4, r2);	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r2 = 4;
        r4 = r15.c;	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r15.a(r2, r4);	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r10.a(r7);
        r15.a(r1, r1, r0, r1);
        goto L_0x0028;
    L_0x008e:
        r2 = "Content-Length";
        r2 = r0.getHeaderField(r2);	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r2 = r2.intValue();	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r10.a(r2);	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r3 = r0.getInputStream();	 Catch:{ NumberFormatException -> 0x03ee, Exception -> 0x03ea, SocketException -> 0x03a8, IOException -> 0x037f, all -> 0x0349 }
        r5 = r1;
        r6 = r0;
    L_0x00a5:
        r11 = r10.c();	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r0 = r10.d();	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r2 = r15.a;	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        if (r2 != 0) goto L_0x0197;
    L_0x00b1:
        r0 = "OtaDownloadThread";
        r2 = "mContext is null";
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r0, r2);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r0 = 4;
        r2 = r15.c;	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r15.a(r0, r2);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r10.a(r7);
        r15.a(r3, r1, r6, r5);
        goto L_0x0028;
    L_0x00c6:
        r0 = move-exception;
        r0 = r1;
    L_0x00c8:
        r2 = "OtaDownloadThread";
        r3 = "NumberFormatException";
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r2, r3);	 Catch:{ SocketException -> 0x03a8, IOException -> 0x037f, Exception -> 0x036a, all -> 0x0349 }
        r2 = 4;
        r3 = r15.c;	 Catch:{ SocketException -> 0x03a8, IOException -> 0x037f, Exception -> 0x036a, all -> 0x0349 }
        r15.a(r2, r3);	 Catch:{ SocketException -> 0x03a8, IOException -> 0x037f, Exception -> 0x036a, all -> 0x0349 }
        r10.a(r7);
        r15.a(r1, r1, r0, r1);
        goto L_0x0028;
    L_0x00dd:
        r0 = move-exception;
        r2 = r1;
    L_0x00df:
        r0 = "OtaDownloadThread";
        r4 = "Exception";
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r0, r4);	 Catch:{ SocketException -> 0x03b0, IOException -> 0x0386, Exception -> 0x0370, all -> 0x034f }
        r0 = r3.openConnection();	 Catch:{ NumberFormatException -> 0x013f, Exception -> 0x016b, SocketException -> 0x03b0, IOException -> 0x0386, all -> 0x034f }
        r0 = com.mqunar.necro.agent.instrumentation.HttpInstrumentation.openConnection(r0);	 Catch:{ NumberFormatException -> 0x013f, Exception -> 0x016b, SocketException -> 0x03b0, IOException -> 0x0386, all -> 0x034f }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ NumberFormatException -> 0x013f, Exception -> 0x016b, SocketException -> 0x03b0, IOException -> 0x0386, all -> 0x034f }
        r3 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r0.setConnectTimeout(r3);	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r3 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r0.setReadTimeout(r3);	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r3 = r0.getResponseCode();	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        if (r3 == r8) goto L_0x0126;
    L_0x0100:
        r4 = "OtaDownloadThread";
        r5 = new java.lang.StringBuilder;	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r5.<init>();	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r6 = "server response code is not 200,code is ";
        r5 = r5.append(r6);	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r3 = r5.append(r3);	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r3 = r3.toString();	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r4, r3);	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r3 = 4;
        r4 = r15.c;	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r15.a(r3, r4);	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r10.a(r7);
        r15.a(r1, r1, r2, r0);
        goto L_0x0028;
    L_0x0126:
        r3 = "Content-Length";
        r3 = r0.getHeaderField(r3);	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r3 = r3.intValue();	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r10.a(r3);	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r3 = r0.getInputStream();	 Catch:{ NumberFormatException -> 0x03e4, Exception -> 0x03de, SocketException -> 0x03b7, IOException -> 0x038d, all -> 0x0354 }
        r5 = r0;
        r6 = r2;
        goto L_0x00a5;
    L_0x013f:
        r0 = move-exception;
        r3 = r1;
    L_0x0141:
        r4 = "OtaDownloadThread";
        r5 = new java.lang.StringBuilder;	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r5.<init>();	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r6 = "get download content-length failure,error is ";
        r5 = r5.append(r6);	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r0 = r0.getMessage();	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r0 = r5.append(r0);	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r0 = r0.toString();	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r4, r0);	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r0 = 4;
        r4 = r15.c;	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r15.a(r0, r4);	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r10.a(r7);
        r15.a(r1, r1, r2, r3);
        goto L_0x0028;
    L_0x016b:
        r0 = move-exception;
        r3 = r1;
    L_0x016d:
        r4 = "OtaDownloadThread";
        r5 = new java.lang.StringBuilder;	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r5.<init>();	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r6 = "http Exception";
        r5 = r5.append(r6);	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r0 = r0.getMessage();	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r0 = r5.append(r0);	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r0 = r0.toString();	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r4, r0);	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r0 = 4;
        r4 = r15.c;	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r15.a(r0, r4);	 Catch:{ SocketException -> 0x03bf, IOException -> 0x0394, Exception -> 0x0375, all -> 0x035a }
        r10.a(r7);
        r15.a(r1, r1, r2, r3);
        goto L_0x0028;
    L_0x0197:
        r2 = r15.a;	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r2 = a(r2);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        if (r2 != 0) goto L_0x01b4;
    L_0x019f:
        r0 = "OtaDownloadThread";
        r2 = "externalCacheDir is null";
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r0, r2);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r0 = 4;
        r2 = r15.c;	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r15.a(r0, r2);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r10.a(r7);
        r15.a(r3, r1, r6, r5);
        goto L_0x0028;
    L_0x01b4:
        r4 = new java.lang.StringBuilder;	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r4.<init>();	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r2 = r4.append(r2);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r0 = r2.append(r0);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r12 = r0.toString();	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r0 = "OtaDownloadThread";
        r2 = new java.lang.StringBuilder;	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r2.<init>();	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r4 = "downloadPath: ";
        r2 = r2.append(r4);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r4 = com.huawei.hwid.openapi.quicklogin.d.b.k.a(r12);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r2 = r2.append(r4);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r2 = r2.toString();	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r0, r2);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r2 = new java.io.File;	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r2.<init>(r12);	 Catch:{ SocketException -> 0x03c6, IOException -> 0x039b, Exception -> 0x037a }
        r4 = new java.io.FileOutputStream;	 Catch:{ SocketException -> 0x03cd, IOException -> 0x039f, Exception -> 0x037a }
        r0 = new java.io.File;	 Catch:{ SocketException -> 0x03cd, IOException -> 0x039f, Exception -> 0x037a }
        r0.<init>(r12);	 Catch:{ SocketException -> 0x03cd, IOException -> 0x039f, Exception -> 0x037a }
        r4.<init>(r0);	 Catch:{ SocketException -> 0x03cd, IOException -> 0x039f, Exception -> 0x037a }
        r10.e(r12);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r0 = 6000; // 0x1770 float:8.408E-42 double:2.9644E-320;
        r1 = r10.c();	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r15.b(r0, r1);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r0 = r3.read(r9);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r1 = r0;
        r8 = r7;
        r0 = r7;
    L_0x0203:
        r13 = -1;
        if (r1 == r13) goto L_0x025e;
    L_0x0206:
        r13 = r15.d;	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        if (r13 == 0) goto L_0x022e;
    L_0x020a:
        r0 = "OtaDownloadThread";
        r1 = "cancledownload is true";
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r0, r1);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r0 = "OtaDownloadThread";
        com.huawei.hwid.openapi.update.k.a(r3, r0);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r0 = "OtaDownloadThread";
        com.huawei.hwid.openapi.update.k.a(r4, r0);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r0 = r15.b;	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r1 = 11;
        r0 = r0.obtainMessage(r1);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r0.sendToTarget();	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r10.a(r7);
        r15.a(r3, r4, r6, r5);
        goto L_0x0028;
    L_0x022e:
        r13 = 0;
        r4.write(r9, r13, r1);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r8 = r8 + r1;
        r0 = r0 + 1;
        r1 = r0 % 200;
        if (r1 == 0) goto L_0x023f;
    L_0x0239:
        r1 = r10.c();	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        if (r8 != r1) goto L_0x0259;
    L_0x023f:
        r1 = r15.b;	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r13 = 3;
        r1 = r1.obtainMessage(r13);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r13 = r8 / 1000;
        r1.arg1 = r13;	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r13 = r11 / 1000;
        r1.arg2 = r13;	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r13 = r15.c;	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r13 = java.lang.Integer.valueOf(r13);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r1.obj = r13;	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r1.sendToTarget();	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
    L_0x0259:
        r1 = r3.read(r9);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        goto L_0x0203;
    L_0x025e:
        r0 = r15.b(r10);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        if (r0 != 0) goto L_0x0284;
    L_0x0264:
        r15.a(r2);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r0 = "4";
        r1 = r10.g();	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r8 = r10.b();	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r9 = "download error, file md5 check failure";
        r15.a(r0, r1, r8, r9);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r0 = 4;
        r1 = r15.c;	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r15.a(r0, r1);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r10.a(r7);
        r15.a(r3, r4, r6, r5);
        goto L_0x0028;
    L_0x0284:
        r0 = "OtaDownloadThread";
        r1 = new java.lang.StringBuilder;	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r1.<init>();	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r9 = "downloadSize: ";
        r1 = r1.append(r9);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r9 = r10.c();	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r9 = java.lang.Integer.valueOf(r9);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r9 = com.huawei.hwid.openapi.quicklogin.d.b.k.a(r9);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r1 = r1.append(r9);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r9 = " downloadSize";
        r1 = r1.append(r9);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r9 = java.lang.Integer.valueOf(r8);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r9 = com.huawei.hwid.openapi.quicklogin.d.b.k.a(r9);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r1 = r1.append(r9);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r1 = r1.toString();	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        com.huawei.hwid.openapi.quicklogin.d.a.c.b(r0, r1);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r15.a(r8, r10, r2, r12);	 Catch:{ SocketException -> 0x03d6, IOException -> 0x03a4, Exception -> 0x037c }
        r10.a(r7);
        r15.a(r3, r4, r6, r5);
        goto L_0x0028;
    L_0x02c5:
        r0 = move-exception;
        r2 = r1;
        r3 = r1;
        r4 = r1;
        r5 = r1;
    L_0x02ca:
        r6 = "OtaDownloadThread";
        r8 = "download error, error is socketException";
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r6, r8);	 Catch:{ all -> 0x0364 }
        r15.a(r1);	 Catch:{ all -> 0x0364 }
        r1 = "4";
        r6 = r10.g();	 Catch:{ all -> 0x0364 }
        r8 = r10.b();	 Catch:{ all -> 0x0364 }
        r0 = r0.toString();	 Catch:{ all -> 0x0364 }
        r15.a(r1, r6, r8, r0);	 Catch:{ all -> 0x0364 }
        r0 = 4;
        r1 = r15.c;	 Catch:{ all -> 0x0364 }
        r15.a(r0, r1);	 Catch:{ all -> 0x0364 }
        r10.a(r7);
        r15.a(r2, r3, r5, r4);
        goto L_0x0028;
    L_0x02f3:
        r0 = move-exception;
        r3 = r1;
        r4 = r1;
        r5 = r1;
        r6 = r1;
    L_0x02f8:
        r0 = "OtaDownloadThread";
        r2 = "download error, error is ioexception";
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r0, r2);	 Catch:{ all -> 0x0361 }
        r15.a(r1);	 Catch:{ all -> 0x0361 }
        r0 = 4;
        r1 = r15.c;	 Catch:{ all -> 0x0361 }
        r15.a(r0, r1);	 Catch:{ all -> 0x0361 }
        r10.a(r7);
        r15.a(r3, r4, r6, r5);
        goto L_0x0028;
    L_0x0310:
        r0 = move-exception;
        r3 = r1;
        r5 = r1;
        r6 = r1;
    L_0x0314:
        r2 = "OtaDownloadThread";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x035f }
        r4.<init>();	 Catch:{ all -> 0x035f }
        r8 = "download Exception: ";
        r4 = r4.append(r8);	 Catch:{ all -> 0x035f }
        r0 = r0.getMessage();	 Catch:{ all -> 0x035f }
        r0 = r4.append(r0);	 Catch:{ all -> 0x035f }
        r0 = r0.toString();	 Catch:{ all -> 0x035f }
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r2, r0);	 Catch:{ all -> 0x035f }
        r0 = 4;
        r2 = r15.c;	 Catch:{ all -> 0x035f }
        r15.a(r0, r2);	 Catch:{ all -> 0x035f }
        r10.a(r7);
        r15.a(r3, r1, r6, r5);
        goto L_0x0028;
    L_0x033e:
        r0 = move-exception;
        r3 = r1;
        r5 = r1;
        r6 = r1;
    L_0x0342:
        r10.a(r7);
        r15.a(r3, r1, r6, r5);
        throw r0;
    L_0x0349:
        r2 = move-exception;
        r3 = r1;
        r5 = r1;
        r6 = r0;
        r0 = r2;
        goto L_0x0342;
    L_0x034f:
        r0 = move-exception;
        r3 = r1;
        r5 = r1;
        r6 = r2;
        goto L_0x0342;
    L_0x0354:
        r3 = move-exception;
        r5 = r0;
        r6 = r2;
        r0 = r3;
        r3 = r1;
        goto L_0x0342;
    L_0x035a:
        r0 = move-exception;
        r5 = r3;
        r6 = r2;
        r3 = r1;
        goto L_0x0342;
    L_0x035f:
        r0 = move-exception;
        goto L_0x0342;
    L_0x0361:
        r0 = move-exception;
        r1 = r4;
        goto L_0x0342;
    L_0x0364:
        r0 = move-exception;
        r1 = r3;
        r6 = r5;
        r5 = r4;
        r3 = r2;
        goto L_0x0342;
    L_0x036a:
        r2 = move-exception;
        r3 = r1;
        r5 = r1;
        r6 = r0;
        r0 = r2;
        goto L_0x0314;
    L_0x0370:
        r0 = move-exception;
        r3 = r1;
        r5 = r1;
        r6 = r2;
        goto L_0x0314;
    L_0x0375:
        r0 = move-exception;
        r5 = r3;
        r6 = r2;
        r3 = r1;
        goto L_0x0314;
    L_0x037a:
        r0 = move-exception;
        goto L_0x0314;
    L_0x037c:
        r0 = move-exception;
        r1 = r4;
        goto L_0x0314;
    L_0x037f:
        r2 = move-exception;
        r3 = r1;
        r4 = r1;
        r5 = r1;
        r6 = r0;
        goto L_0x02f8;
    L_0x0386:
        r0 = move-exception;
        r3 = r1;
        r4 = r1;
        r5 = r1;
        r6 = r2;
        goto L_0x02f8;
    L_0x038d:
        r3 = move-exception;
        r3 = r1;
        r4 = r1;
        r5 = r0;
        r6 = r2;
        goto L_0x02f8;
    L_0x0394:
        r0 = move-exception;
        r4 = r1;
        r5 = r3;
        r6 = r2;
        r3 = r1;
        goto L_0x02f8;
    L_0x039b:
        r0 = move-exception;
        r4 = r1;
        goto L_0x02f8;
    L_0x039f:
        r0 = move-exception;
        r4 = r1;
        r1 = r2;
        goto L_0x02f8;
    L_0x03a4:
        r0 = move-exception;
        r1 = r2;
        goto L_0x02f8;
    L_0x03a8:
        r2 = move-exception;
        r3 = r1;
        r4 = r1;
        r5 = r0;
        r0 = r2;
        r2 = r1;
        goto L_0x02ca;
    L_0x03b0:
        r0 = move-exception;
        r3 = r1;
        r4 = r1;
        r5 = r2;
        r2 = r1;
        goto L_0x02ca;
    L_0x03b7:
        r3 = move-exception;
        r4 = r0;
        r5 = r2;
        r0 = r3;
        r2 = r1;
        r3 = r1;
        goto L_0x02ca;
    L_0x03bf:
        r0 = move-exception;
        r4 = r3;
        r5 = r2;
        r2 = r1;
        r3 = r1;
        goto L_0x02ca;
    L_0x03c6:
        r0 = move-exception;
        r2 = r3;
        r4 = r5;
        r3 = r1;
        r5 = r6;
        goto L_0x02ca;
    L_0x03cd:
        r0 = move-exception;
        r4 = r5;
        r5 = r6;
        r14 = r1;
        r1 = r2;
        r2 = r3;
        r3 = r14;
        goto L_0x02ca;
    L_0x03d6:
        r0 = move-exception;
        r1 = r2;
        r2 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x02ca;
    L_0x03de:
        r3 = move-exception;
        r14 = r3;
        r3 = r0;
        r0 = r14;
        goto L_0x016d;
    L_0x03e4:
        r3 = move-exception;
        r14 = r3;
        r3 = r0;
        r0 = r14;
        goto L_0x0141;
    L_0x03ea:
        r2 = move-exception;
        r2 = r0;
        goto L_0x00df;
    L_0x03ee:
        r2 = move-exception;
        goto L_0x00c8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hwid.openapi.update.f.run():void");
    }

    private boolean b(b bVar) {
        String str = null;
        PackageInfo packageArchiveInfo = this.a.getPackageManager().getPackageArchiveInfo(bVar.f(), 1);
        if (packageArchiveInfo != null) {
            str = packageArchiveInfo.applicationInfo.packageName;
        }
        return a(str);
    }

    private boolean a(String str) {
        return !TextUtils.isEmpty(str) && "com.huawei.hwid".equals(str);
    }

    private void a(InputStream inputStream, FileOutputStream fileOutputStream, HttpURLConnection httpURLConnection, HttpURLConnection httpURLConnection2) {
        k.a(inputStream, "OtaDownloadThread");
        k.a((OutputStream) fileOutputStream, "OtaDownloadThread");
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        if (httpURLConnection2 != null) {
            httpURLConnection2.disconnect();
        }
    }

    private void a(int i, b bVar, File file, String str) {
        if (i == bVar.c() && a(bVar)) {
            c.b("OtaDownloadThread", "downloadSize == versionInfo.getTotalSize() versionName: " + k.a(bVar.b()));
            j.a(this.a).a(this.a, Integer.toString(this.c), bVar.b(), str);
            a(5, this.c);
            a("3", bVar.g(), bVar.b(), "update success");
            return;
        }
        c.d("OtaDownloadThread", "download error, file md5 or rsa check failure");
        a(file);
        a("4", bVar.g(), bVar.b(), "download error, file md5 check failure");
        a(4, this.c);
    }

    private boolean a(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        return file.delete();
    }

    private boolean a(String str, String str2, String str3, String str4) {
        c.b("OtaDownloadThread", "send updateReport");
        g gVar = new g();
        boolean z = false;
        try {
            com.huawei.hwid.openapi.update.a.c cVar = new com.huawei.hwid.openapi.update.a.c();
            cVar.a(str);
            cVar.c(str3);
            cVar.d(str4);
            cVar.b(str2);
            z = gVar.a(cVar);
        } catch (IOException e) {
            c.d("OtaDownloadThread", "send update report error ,error is " + e.getMessage());
        } catch (Exception e2) {
            c.d("OtaDownloadThread", "send update report Exception ,Exception is " + e2.getMessage());
        }
        return z;
    }

    public static String a(Context context) {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null) {
            stringBuilder.append(externalCacheDir.getAbsolutePath()).append(File.separator);
        } else {
            stringBuilder.append(Environment.getExternalStorageDirectory().getPath()).append("/Android/data/").append(context.getPackageName()).append("/cache/");
        }
        return stringBuilder.toString();
    }
}
