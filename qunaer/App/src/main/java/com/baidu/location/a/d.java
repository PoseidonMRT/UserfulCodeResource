package com.baidu.location.a;

import android.os.Handler;
import android.os.Message;
import com.baidu.location.BDLocation;
import com.baidu.location.Jni;
import com.baidu.location.f;
import com.baidu.location.f.c;
import com.baidu.location.f.i;
import java.util.HashMap;
import java.util.Locale;

public abstract class d {
    public static String c = null;
    public i a = null;
    public com.baidu.location.f.a b = null;
    final Handler d = new a(this);
    private boolean e = true;
    private boolean f = false;

    public class a extends Handler {
        final /* synthetic */ d a;

        public a(d dVar) {
            this.a = dVar;
        }

        public void handleMessage(Message message) {
            if (f.isServing) {
                switch (message.what) {
                    case 21:
                        this.a.a(message);
                        return;
                    case 62:
                    case 63:
                        this.a.a();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    class b extends com.baidu.location.h.f {
        String a;
        String b;
        final /* synthetic */ d c;

        public b(d dVar) {
            this.c = dVar;
            this.a = null;
            this.b = null;
            this.k = new HashMap();
        }

        public void a() {
            this.h = com.baidu.location.h.i.c();
            this.h = "http://" + com.baidu.location.h.i.g + "/sdk.php";
            String encodeTp4 = Jni.encodeTp4(this.b);
            this.b = null;
            if (this.a == null) {
                this.a = j.b();
            }
            this.k.put("bloc", encodeTp4);
            if (this.a != null) {
                this.k.put("up", this.a);
            }
            StringBuffer stringBuffer = new StringBuffer(512);
            stringBuffer.append(String.format(Locale.CHINA, "&ki=%s&sn=%s", new Object[]{com.baidu.location.h.a.b(f.getServiceContext()), com.baidu.location.h.a.a(f.getServiceContext())}));
            if (stringBuffer.length() > 0) {
                this.k.put("ext", Jni.encode(stringBuffer.toString()));
            }
            this.k.put("trtm", String.format(Locale.CHINA, "%d", new Object[]{Long.valueOf(System.currentTimeMillis())}));
        }

        public void a(String str) {
            this.b = str;
            e();
        }

        public void a(boolean z) {
            Message obtainMessage;
            if (!z || this.j == null) {
                obtainMessage = this.c.d.obtainMessage(63);
                obtainMessage.obj = "HttpStatus error";
                obtainMessage.sendToTarget();
            } else {
                try {
                    BDLocation bDLocation;
                    String str = this.j;
                    d.c = str;
                    try {
                        bDLocation = new BDLocation(str);
                        bDLocation.setOperators(c.a().h());
                        if (f.a().f()) {
                            bDLocation.setDirection(f.a().h());
                        }
                    } catch (Exception e) {
                        bDLocation = new BDLocation();
                        bDLocation.setLocType(0);
                    }
                    this.a = null;
                    if (bDLocation.getLocType() == 0 && bDLocation.getLatitude() == Double.MIN_VALUE && bDLocation.getLongitude() == Double.MIN_VALUE) {
                        obtainMessage = this.c.d.obtainMessage(63);
                        obtainMessage.obj = "HttpStatus error";
                        obtainMessage.sendToTarget();
                    } else {
                        Message obtainMessage2 = this.c.d.obtainMessage(21);
                        obtainMessage2.obj = bDLocation;
                        obtainMessage2.sendToTarget();
                    }
                } catch (Exception e2) {
                    obtainMessage = this.c.d.obtainMessage(63);
                    obtainMessage.obj = "HttpStatus error";
                    obtainMessage.sendToTarget();
                }
            }
            if (this.k != null) {
                this.k.clear();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String a(java.lang.String r10) {
        /*
        r9 = this;
        r1 = 0;
        r8 = 1;
        r7 = 0;
        r0 = r9.b;
        if (r0 == 0) goto L_0x000f;
    L_0x0007:
        r0 = r9.b;
        r0 = r0.a();
        if (r0 != 0) goto L_0x0019;
    L_0x000f:
        r0 = com.baidu.location.f.c.a();
        r0 = r0.f();
        r9.b = r0;
    L_0x0019:
        r0 = r9.a;
        if (r0 == 0) goto L_0x0025;
    L_0x001d:
        r0 = r9.a;
        r0 = r0.f();
        if (r0 != 0) goto L_0x002f;
    L_0x0025:
        r0 = com.baidu.location.f.k.a();
        r0 = r0.k();
        r9.a = r0;
    L_0x002f:
        r0 = com.baidu.location.f.f.a();
        r0 = r0.i();
        if (r0 == 0) goto L_0x0111;
    L_0x0039:
        r0 = com.baidu.location.f.f.a();
        r0 = r0.g();
    L_0x0041:
        r2 = r9.b;
        if (r2 == 0) goto L_0x004d;
    L_0x0045:
        r2 = r9.b;
        r2 = r2.c();
        if (r2 == 0) goto L_0x005c;
    L_0x004d:
        r2 = r9.a;
        if (r2 == 0) goto L_0x0059;
    L_0x0051:
        r2 = r9.a;
        r2 = r2.a();
        if (r2 != 0) goto L_0x005c;
    L_0x0059:
        if (r0 != 0) goto L_0x005c;
    L_0x005b:
        return r1;
    L_0x005c:
        r1 = com.baidu.location.a.a.a();
        r2 = r1.c();
        r1 = com.baidu.location.f.k.a();
        r1 = r1.h();
        if (r1 == 0) goto L_0x00d9;
    L_0x006e:
        r1 = "&cn=32";
    L_0x0070:
        r3 = r9.e;
        if (r3 == 0) goto L_0x00f3;
    L_0x0074:
        r9.e = r7;
        r3 = com.baidu.location.c.f.a();
        r3 = r3.b();
        r3.a(r8);
        r3 = com.baidu.location.f.k.a();
        r3 = r3.m();
        r4 = android.text.TextUtils.isEmpty(r3);
        if (r4 != 0) goto L_0x00a6;
    L_0x008f:
        r4 = ":";
        r5 = "";
        r3 = r3.replace(r4, r5);
        r4 = java.util.Locale.CHINA;
        r5 = "%s&mac=%s";
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r6[r7] = r1;
        r6[r8] = r3;
        r1 = java.lang.String.format(r4, r5, r6);
    L_0x00a6:
        r3 = android.os.Build.VERSION.SDK_INT;
        r4 = 17;
        if (r3 <= r4) goto L_0x00ac;
    L_0x00ac:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r1 = r3.append(r1);
        r1 = r1.append(r2);
        r1 = r1.toString();
        if (r10 == 0) goto L_0x00d0;
    L_0x00bf:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r2 = r2.append(r10);
        r1 = r2.append(r1);
        r1 = r1.toString();
    L_0x00d0:
        r2 = r9.b;
        r3 = r9.a;
        r1 = com.baidu.location.h.i.a(r2, r3, r0, r1, r7);
        goto L_0x005b;
    L_0x00d9:
        r1 = java.util.Locale.CHINA;
        r3 = "&cn=%d";
        r4 = new java.lang.Object[r8];
        r5 = com.baidu.location.f.c.a();
        r5 = r5.e();
        r5 = java.lang.Integer.valueOf(r5);
        r4[r7] = r5;
        r1 = java.lang.String.format(r1, r3, r4);
        goto L_0x0070;
    L_0x00f3:
        r3 = r9.f;
        if (r3 != 0) goto L_0x00ac;
    L_0x00f7:
        r3 = com.baidu.location.a.j.f();
        if (r3 == 0) goto L_0x010e;
    L_0x00fd:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r1 = r4.append(r1);
        r1 = r1.append(r3);
        r1 = r1.toString();
    L_0x010e:
        r9.f = r8;
        goto L_0x00ac;
    L_0x0111:
        r0 = r1;
        goto L_0x0041;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.a.d.a(java.lang.String):java.lang.String");
    }

    public abstract void a();

    public abstract void a(Message message);
}
