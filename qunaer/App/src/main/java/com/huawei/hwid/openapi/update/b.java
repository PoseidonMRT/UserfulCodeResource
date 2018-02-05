package com.huawei.hwid.openapi.update;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.client.ClientProtocolException;

public class b extends Thread {
    private g a;
    private Handler b;
    private Context c;
    private int d;
    private boolean e = false;
    private Map f = null;

    b(Context context, g gVar, int i, Handler handler) {
        this.a = gVar;
        this.b = handler;
        this.c = context;
        this.d = i;
    }

    public void a() {
        this.e = true;
    }

    private void a(int i) {
        Message obtainMessage = this.b.obtainMessage(i);
        obtainMessage.obj = this.f;
        obtainMessage.sendToTarget();
    }

    public void run() {
        c.b("OtaCheckVersionThread", "CheckVersionThread run");
        ByteArrayOutputStream a = a(this.a, this.d, 0);
        if (a == null) {
            a(7);
            c.d("OtaCheckVersionThread", "check response error");
            return;
        }
        String str = new String(a.toByteArray(), Charset.forName("UTF-8"));
        try {
            a.close();
        } catch (IOException e) {
            c.d("OtaCheckVersionThread", "outputStream close IOException");
        }
        c.b("OtaCheckVersionThread", "responseStr");
        String str2 = "1";
        this.f = Collections.synchronizedMap(new HashMap(5));
        Map hashMap = new HashMap(5);
        if (TextUtils.isEmpty(str)) {
            a(7);
            c.d("OtaCheckVersionThread", "parse response error");
            return;
        }
        str = k.a(str, hashMap);
        if ("1".equals(str)) {
            c.b("OtaCheckVersionThread", "no new version");
            a(2);
        } else if ("-1".equals(str) || "2".equals(str)) {
            c.d("OtaCheckVersionThread", "tcs server error,response code is " + str);
            a(7);
        } else {
            Iterator it = hashMap.entrySet().iterator();
            boolean hasNext = it.hasNext();
            while (hasNext) {
                com.huawei.hwid.openapi.update.a.b bVar = (com.huawei.hwid.openapi.update.a.b) ((Entry) it.next()).getValue();
                if (TextUtils.isEmpty(bVar.i())) {
                    a(7);
                    return;
                }
                ByteArrayOutputStream a2 = a(this.a, bVar.i() + "full" + "/filelist.xml", 0);
                if (a2 != null) {
                    bVar.a(new ByteArrayInputStream(a2.toByteArray()));
                    try {
                        a2.close();
                    } catch (IOException e2) {
                        c.d("OtaCheckVersionThread", "outputStreamFileList close IOException");
                    }
                    a(this.c, bVar);
                    hasNext = it.hasNext();
                } else {
                    c.d("OtaCheckVersionThread", "tcs server error,response code is " + str);
                    a(7);
                    return;
                }
            }
            if (!this.e) {
                this.f = hashMap;
            }
            a(1);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(android.content.Context r6, com.huawei.hwid.openapi.update.a.b r7) {
        /*
        r5 = this;
        r0 = 0;
        r1 = r5.a;	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048, all -> 0x0058 }
        r2 = new java.lang.StringBuilder;	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048, all -> 0x0058 }
        r2.<init>();	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048, all -> 0x0058 }
        r3 = r7.i();	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048, all -> 0x0058 }
        r2 = r2.append(r3);	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048, all -> 0x0058 }
        r3 = "full";
        r2 = r2.append(r3);	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048, all -> 0x0058 }
        r3 = "/changelog.xml";
        r2 = r2.append(r3);	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048, all -> 0x0058 }
        r2 = r2.toString();	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048, all -> 0x0058 }
        r0 = r1.a(r2);	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048, all -> 0x0058 }
        if (r0 == 0) goto L_0x0032;
    L_0x0026:
        r1 = new java.io.ByteArrayInputStream;	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048 }
        r2 = r0.toByteArray();	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048 }
        r1.<init>(r2);	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048 }
        r7.a(r6, r1);	 Catch:{ ClientProtocolException -> 0x0038, IOException -> 0x0048 }
    L_0x0032:
        if (r0 == 0) goto L_0x0037;
    L_0x0034:
        r0.close();	 Catch:{ IOException -> 0x0062 }
    L_0x0037:
        return;
    L_0x0038:
        r1 = move-exception;
        r1 = "OtaCheckVersionThread";
        r2 = "get apk changelog error";
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r1, r2);	 Catch:{ all -> 0x0066 }
        if (r0 == 0) goto L_0x0037;
    L_0x0042:
        r0.close();	 Catch:{ IOException -> 0x0046 }
        goto L_0x0037;
    L_0x0046:
        r0 = move-exception;
        goto L_0x0037;
    L_0x0048:
        r1 = move-exception;
        r1 = "OtaCheckVersionThread";
        r2 = "get apk changelog error";
        com.huawei.hwid.openapi.quicklogin.d.a.c.d(r1, r2);	 Catch:{ all -> 0x0066 }
        if (r0 == 0) goto L_0x0037;
    L_0x0052:
        r0.close();	 Catch:{ IOException -> 0x0056 }
        goto L_0x0037;
    L_0x0056:
        r0 = move-exception;
        goto L_0x0037;
    L_0x0058:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x005c:
        if (r1 == 0) goto L_0x0061;
    L_0x005e:
        r1.close();	 Catch:{ IOException -> 0x0064 }
    L_0x0061:
        throw r0;
    L_0x0062:
        r0 = move-exception;
        goto L_0x0037;
    L_0x0064:
        r1 = move-exception;
        goto L_0x0061;
    L_0x0066:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
        goto L_0x005c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hwid.openapi.update.b.a(android.content.Context, com.huawei.hwid.openapi.update.a.b):void");
    }

    private ByteArrayOutputStream a(g gVar, int i, int i2) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        if (i2 >= 3) {
            return byteArrayOutputStream;
        }
        int i3 = i2 + 1;
        try {
            byteArrayOutputStream = gVar.a(i);
        } catch (IllegalArgumentException e) {
            c.d("OtaCheckVersionThread", "sendRequestToServer IllegalArgumentException startFromTimes: " + i3);
        } catch (IllegalStateException e2) {
            c.d("OtaCheckVersionThread", "sendRequestToServer IllegalStateException startFromTimes: " + i3);
        } catch (IOException e3) {
            c.d("OtaCheckVersionThread", "sendRequestToServer IOException startFromTimes: " + i3);
        } catch (Exception e4) {
            c.d("OtaCheckVersionThread", "sendRequestToServer Exception startFromTimes: " + i3);
        }
        if (byteArrayOutputStream != null) {
            return byteArrayOutputStream;
        }
        c.d("OtaCheckVersionThread", "sendRequestToServer fail startFromTimes: " + i3);
        return a(gVar, i, i3);
    }

    private ByteArrayOutputStream a(g gVar, String str, int i) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        if (i >= 3) {
            return byteArrayOutputStream;
        }
        int i2 = i + 1;
        try {
            byteArrayOutputStream = gVar.b(str);
        } catch (ClientProtocolException e) {
            c.d("OtaCheckVersionThread", "getFileListFromServer ClientProtocolException startFromTimes: " + i2);
        } catch (IOException e2) {
            c.d("OtaCheckVersionThread", "getFileListFromServer ClientProtocolException startFromTimes: " + i2);
        } catch (Exception e3) {
            c.d("OtaCheckVersionThread", "getFileListFromServer Exception startFromTimes: " + i2);
        }
        if (byteArrayOutputStream != null) {
            return byteArrayOutputStream;
        }
        c.d("OtaCheckVersionThread", "getFileListFromServer fail startFromTimes: " + i2);
        return a(gVar, str, i2);
    }
}
