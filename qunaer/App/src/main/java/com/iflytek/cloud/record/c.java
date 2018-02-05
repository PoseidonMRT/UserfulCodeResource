package com.iflytek.cloud.record;

import android.content.Context;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.thirdparty.bw;
import com.iflytek.cloud.thirdparty.cb;

public class c {
    private boolean A = false;
    private boolean B = false;
    private int C = 0;
    private Handler D = new Handler(this, Looper.getMainLooper()) {
        final /* synthetic */ c a;

        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    if (this.a.f != null) {
                        this.a.f.a((SpeechError) message.obj);
                        this.a.f = null;
                        return;
                    }
                    return;
                case 1:
                    if (this.a.f != null) {
                        this.a.f.a();
                        return;
                    }
                    return;
                case 2:
                    if (this.a.f != null) {
                        this.a.f.b();
                        return;
                    }
                    return;
                case 3:
                    if (this.a.f != null) {
                        this.a.f.a(message.arg1, message.arg2, this.a.C);
                        return;
                    }
                    return;
                case 4:
                    if (this.a.f != null) {
                        this.a.f.c();
                        this.a.f = null;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    OnAudioFocusChangeListener a = new OnAudioFocusChangeListener(this) {
        final /* synthetic */ c a;

        {
            this.a = r1;
        }

        public void onAudioFocusChange(int i) {
            if (i == -2 || i == -3 || i == -1) {
                cb.a("PcmPlayer", "pause start");
                if (this.a.c()) {
                    cb.a("PcmPlayer", "pause success");
                    this.a.l = true;
                    if (this.a.f != null) {
                        this.a.f.a();
                    }
                }
            } else if (i == 1) {
                cb.a("PcmPlayer", "resume start");
                if (this.a.l) {
                    this.a.l = false;
                    if (this.a.d()) {
                        cb.a("PcmPlayer", "resume success");
                        if (this.a.f != null) {
                            this.a.f.b();
                        }
                    }
                }
            }
        }
    };
    private AudioTrack b = null;
    private b c = null;
    private Context d = null;
    private b e = null;
    private a f = null;
    private volatile int g = 0;
    private int h = 3;
    private boolean i = true;
    private int j;
    private boolean k = false;
    private boolean l = false;
    private Object m = new Object();
    private Object n = this;
    private final int o = 2;
    private final int p = 500;
    private final int q = 50;
    private int r = 1600;
    private final float s = 1.0f;
    private final float t = 0.0f;
    private final float u = 0.1f;
    private int v = (this.r * 10);
    private float w = 0.0f;
    private float x = 1.0f;
    private float y = 0.1f;
    private boolean z = false;

    public interface a {
        void a();

        void a(int i, int i2, int i3);

        void a(SpeechError speechError);

        void b();

        void c();
    }

    class b extends Thread {
        final /* synthetic */ c a;
        private int b;

        private b(c cVar) {
            this.a = cVar;
            this.b = this.a.h;
        }

        public int a() {
            return this.b;
        }

        public void a(int i) {
            this.b = i;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r9 = this;
            r8 = 3;
            r7 = 1;
            r6 = 4;
            r5 = 2;
            r4 = 0;
            r0 = "PcmPlayer";
            r1 = "start player";
            com.iflytek.cloud.thirdparty.cb.a(r0, r1);	 Catch:{ Exception -> 0x0169 }
            r0 = "PcmPlayer";
            r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0169 }
            r1.<init>();	 Catch:{ Exception -> 0x0169 }
            r2 = "mAudioFocus= ";
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x0169 }
            r2 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r2 = r2.i;	 Catch:{ Exception -> 0x0169 }
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x0169 }
            r1 = r1.toString();	 Catch:{ Exception -> 0x0169 }
            com.iflytek.cloud.thirdparty.cb.a(r0, r1);	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.i;	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x01d2;
        L_0x0032:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.d;	 Catch:{ Exception -> 0x0169 }
            r1 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = r1.k;	 Catch:{ Exception -> 0x0169 }
            r1 = java.lang.Boolean.valueOf(r1);	 Catch:{ Exception -> 0x0169 }
            r2 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r2 = r2.a;	 Catch:{ Exception -> 0x0169 }
            com.iflytek.cloud.thirdparty.bw.a(r0, r1, r2);	 Catch:{ Exception -> 0x0169 }
        L_0x0049:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.c;	 Catch:{ Exception -> 0x0169 }
            r0.c();	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = r0.n;	 Catch:{ Exception -> 0x0169 }
            monitor-enter(r1);	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ all -> 0x0239 }
            r0 = r0.g;	 Catch:{ all -> 0x0239 }
            if (r0 == r6) goto L_0x006f;
        L_0x0061:
            r0 = r9.a;	 Catch:{ all -> 0x0239 }
            r0 = r0.g;	 Catch:{ all -> 0x0239 }
            if (r0 == r8) goto L_0x006f;
        L_0x0069:
            r0 = r9.a;	 Catch:{ all -> 0x0239 }
            r2 = 2;
            r0.g = r2;	 Catch:{ all -> 0x0239 }
        L_0x006f:
            monitor-exit(r1);	 Catch:{ all -> 0x0239 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0.f();	 Catch:{ Exception -> 0x0169 }
        L_0x0075:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0.k();	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.g;	 Catch:{ Exception -> 0x0169 }
            if (r0 == r5) goto L_0x0092;
        L_0x0082:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.g;	 Catch:{ Exception -> 0x0169 }
            if (r0 == r7) goto L_0x0092;
        L_0x008a:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.z;	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x0343;
        L_0x0092:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.c;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.g();	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x0282;
        L_0x009e:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = 1;
            r2 = 2;
            r0 = r0.a(r1, r2);	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x00c0;
        L_0x00a8:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.D;	 Catch:{ Exception -> 0x0169 }
            r1 = 2;
            r0 = android.os.Message.obtain(r0, r1);	 Catch:{ Exception -> 0x0169 }
            r0.sendToTarget();	 Catch:{ Exception -> 0x0169 }
            r0 = "BUFFERING to PLAYING  fading ";
            com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0.f();	 Catch:{ Exception -> 0x0169 }
        L_0x00c0:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.c;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.d();	 Catch:{ Exception -> 0x0169 }
            r1 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = r1.c;	 Catch:{ Exception -> 0x0169 }
            r1 = r1.e();	 Catch:{ Exception -> 0x0169 }
            if (r1 == 0) goto L_0x00ed;
        L_0x00d6:
            r2 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r3 = r1.d;	 Catch:{ Exception -> 0x0169 }
            r2.C = r3;	 Catch:{ Exception -> 0x0169 }
            r2 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r2 = r2.D;	 Catch:{ Exception -> 0x0169 }
            r3 = 3;
            r1 = r1.c;	 Catch:{ Exception -> 0x0169 }
            r0 = android.os.Message.obtain(r2, r3, r0, r1);	 Catch:{ Exception -> 0x0169 }
            r0.sendToTarget();	 Catch:{ Exception -> 0x0169 }
        L_0x00ed:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.b;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.getPlayState();	 Catch:{ Exception -> 0x0169 }
            if (r0 == r8) goto L_0x0102;
        L_0x00f9:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.b;	 Catch:{ Exception -> 0x0169 }
            r0.play();	 Catch:{ Exception -> 0x0169 }
        L_0x0102:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.A;	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x0145;
        L_0x010a:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.c;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.h();	 Catch:{ Exception -> 0x0169 }
            if (r0 != 0) goto L_0x023c;
        L_0x0116:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.c;	 Catch:{ Exception -> 0x0169 }
            r1 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = r1.v;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.b(r1);	 Catch:{ Exception -> 0x0169 }
            if (r0 != 0) goto L_0x023c;
        L_0x0128:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.x;	 Catch:{ Exception -> 0x0169 }
            r1 = 0;
            r0 = r0 - r1;
            r0 = java.lang.Math.abs(r0);	 Catch:{ Exception -> 0x0169 }
            r1 = 1036831949; // 0x3dcccccd float:0.1 double:5.122630465E-315;
            r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
            if (r0 < 0) goto L_0x023c;
        L_0x013b:
            r0 = "no more size  fading ";
            com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0.g();	 Catch:{ Exception -> 0x0169 }
        L_0x0145:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.z;	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x0152;
        L_0x014d:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0.h();	 Catch:{ Exception -> 0x0169 }
        L_0x0152:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.c;	 Catch:{ Exception -> 0x0169 }
            r1 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = r1.b;	 Catch:{ Exception -> 0x0169 }
            r2 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r2 = r2.r;	 Catch:{ Exception -> 0x0169 }
            r0.a(r1, r2);	 Catch:{ Exception -> 0x0169 }
            goto L_0x0075;
        L_0x0169:
            r0 = move-exception;
            com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ all -> 0x01e8 }
            r0 = r9.a;	 Catch:{ all -> 0x01e8 }
            r0 = r0.D;	 Catch:{ all -> 0x01e8 }
            r1 = 0;
            r2 = new com.iflytek.cloud.SpeechError;	 Catch:{ all -> 0x01e8 }
            r3 = 20011; // 0x4e2b float:2.8041E-41 double:9.8867E-320;
            r2.<init>(r3);	 Catch:{ all -> 0x01e8 }
            r0 = android.os.Message.obtain(r0, r1, r2);	 Catch:{ all -> 0x01e8 }
            r0.sendToTarget();	 Catch:{ all -> 0x01e8 }
            r0 = r9.a;
            r1 = r0.n;
            monitor-enter(r1);
            r0 = r9.a;	 Catch:{ all -> 0x039b }
            r2 = 4;
            r0.g = r2;	 Catch:{ all -> 0x039b }
            monitor-exit(r1);	 Catch:{ all -> 0x039b }
            r0 = r9.a;
            r0 = r0.b;
            if (r0 == 0) goto L_0x01a6;
        L_0x0198:
            r0 = r9.a;
            r0 = r0.b;
            r0.release();
            r0 = r9.a;
            r0.b = r4;
        L_0x01a6:
            r0 = r9.a;
            r0 = r0.i;
            if (r0 == 0) goto L_0x039e;
        L_0x01ae:
            r0 = r9.a;
            r0 = r0.d;
            r1 = r9.a;
            r1 = r1.k;
            r1 = java.lang.Boolean.valueOf(r1);
            r2 = r9.a;
            r2 = r2.a;
            com.iflytek.cloud.thirdparty.bw.b(r0, r1, r2);
        L_0x01c5:
            r0 = r9.a;
            r0.e = r4;
            r0 = "PcmPlayer";
            r1 = "player stopped";
            com.iflytek.cloud.thirdparty.cb.a(r0, r1);
        L_0x01d1:
            return;
        L_0x01d2:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.d;	 Catch:{ Exception -> 0x0169 }
            r1 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = r1.k;	 Catch:{ Exception -> 0x0169 }
            r1 = java.lang.Boolean.valueOf(r1);	 Catch:{ Exception -> 0x0169 }
            r2 = 0;
            com.iflytek.cloud.thirdparty.bw.a(r0, r1, r2);	 Catch:{ Exception -> 0x0169 }
            goto L_0x0049;
        L_0x01e8:
            r0 = move-exception;
            r1 = r9.a;
            r1 = r1.n;
            monitor-enter(r1);
            r2 = r9.a;	 Catch:{ all -> 0x03b3 }
            r3 = 4;
            r2.g = r3;	 Catch:{ all -> 0x03b3 }
            monitor-exit(r1);	 Catch:{ all -> 0x03b3 }
            r1 = r9.a;
            r1 = r1.b;
            if (r1 == 0) goto L_0x020d;
        L_0x01ff:
            r1 = r9.a;
            r1 = r1.b;
            r1.release();
            r1 = r9.a;
            r1.b = r4;
        L_0x020d:
            r1 = r9.a;
            r1 = r1.i;
            if (r1 == 0) goto L_0x03b6;
        L_0x0215:
            r1 = r9.a;
            r1 = r1.d;
            r2 = r9.a;
            r2 = r2.k;
            r2 = java.lang.Boolean.valueOf(r2);
            r3 = r9.a;
            r3 = r3.a;
            com.iflytek.cloud.thirdparty.bw.b(r1, r2, r3);
        L_0x022c:
            r1 = r9.a;
            r1.e = r4;
            r1 = "PcmPlayer";
            r2 = "player stopped";
            com.iflytek.cloud.thirdparty.cb.a(r1, r2);
            throw r0;
        L_0x0239:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0239 }
            throw r0;	 Catch:{ Exception -> 0x0169 }
        L_0x023c:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.g;	 Catch:{ Exception -> 0x0169 }
            if (r5 != r0) goto L_0x0145;
        L_0x0244:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.c;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.h();	 Catch:{ Exception -> 0x0169 }
            if (r0 != 0) goto L_0x0262;
        L_0x0250:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.c;	 Catch:{ Exception -> 0x0169 }
            r1 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = r1.v;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.b(r1);	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x0145;
        L_0x0262:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.x;	 Catch:{ Exception -> 0x0169 }
            r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r0 = r0 - r1;
            r0 = java.lang.Math.abs(r0);	 Catch:{ Exception -> 0x0169 }
            r1 = 1036831949; // 0x3dcccccd float:0.1 double:5.122630465E-315;
            r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
            if (r0 < 0) goto L_0x0145;
        L_0x0276:
            r0 = "has buffer  fading ";
            com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0.f();	 Catch:{ Exception -> 0x0169 }
            goto L_0x0145;
        L_0x0282:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.c;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.f();	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x030f;
        L_0x028e:
            r0 = "play stoped";
            com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = 4;
            r0.g = r1;	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.D;	 Catch:{ Exception -> 0x0169 }
            r1 = 4;
            r0 = android.os.Message.obtain(r0, r1);	 Catch:{ Exception -> 0x0169 }
            r0.sendToTarget();	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = 0;
            r0.z = r1;	 Catch:{ Exception -> 0x0169 }
        L_0x02ad:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.b;	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x02be;
        L_0x02b5:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.b;	 Catch:{ Exception -> 0x0169 }
            r0.stop();	 Catch:{ Exception -> 0x0169 }
        L_0x02be:
            r0 = r9.a;
            r1 = r0.n;
            monitor-enter(r1);
            r0 = r9.a;	 Catch:{ all -> 0x0383 }
            r2 = 4;
            r0.g = r2;	 Catch:{ all -> 0x0383 }
            monitor-exit(r1);	 Catch:{ all -> 0x0383 }
            r0 = r9.a;
            r0 = r0.b;
            if (r0 == 0) goto L_0x02e2;
        L_0x02d4:
            r0 = r9.a;
            r0 = r0.b;
            r0.release();
            r0 = r9.a;
            r0.b = r4;
        L_0x02e2:
            r0 = r9.a;
            r0 = r0.i;
            if (r0 == 0) goto L_0x0386;
        L_0x02ea:
            r0 = r9.a;
            r0 = r0.d;
            r1 = r9.a;
            r1 = r1.k;
            r1 = java.lang.Boolean.valueOf(r1);
            r2 = r9.a;
            r2 = r2.a;
            com.iflytek.cloud.thirdparty.bw.b(r0, r1, r2);
        L_0x0301:
            r0 = r9.a;
            r0.e = r4;
            r0 = "PcmPlayer";
            r1 = "player stopped";
            com.iflytek.cloud.thirdparty.cb.a(r0, r1);
            goto L_0x01d1;
        L_0x030f:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.z;	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x031f;
        L_0x0317:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = 0;
            r0.z = r1;	 Catch:{ Exception -> 0x0169 }
            goto L_0x0075;
        L_0x031f:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r1 = 2;
            r2 = 1;
            r0 = r0.a(r1, r2);	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x033c;
        L_0x0329:
            r0 = "play onpaused!";
            com.iflytek.cloud.thirdparty.cb.a(r0);	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.D;	 Catch:{ Exception -> 0x0169 }
            r1 = 1;
            r0 = android.os.Message.obtain(r0, r1);	 Catch:{ Exception -> 0x0169 }
            r0.sendToTarget();	 Catch:{ Exception -> 0x0169 }
        L_0x033c:
            r0 = 5;
            sleep(r0);	 Catch:{ Exception -> 0x0169 }
            goto L_0x0075;
        L_0x0343:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.g;	 Catch:{ Exception -> 0x0169 }
            if (r0 != r8) goto L_0x0374;
        L_0x034b:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.b;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.getPlayState();	 Catch:{ Exception -> 0x0169 }
            if (r5 == r0) goto L_0x036d;
        L_0x0357:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.b;	 Catch:{ Exception -> 0x0169 }
            r0.pause();	 Catch:{ Exception -> 0x0169 }
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.z;	 Catch:{ Exception -> 0x0169 }
            if (r0 == 0) goto L_0x036d;
        L_0x0368:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0.i();	 Catch:{ Exception -> 0x0169 }
        L_0x036d:
            r0 = 5;
            sleep(r0);	 Catch:{ Exception -> 0x0169 }
            goto L_0x0075;
        L_0x0374:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0 = r0.g;	 Catch:{ Exception -> 0x0169 }
            if (r6 != r0) goto L_0x0075;
        L_0x037c:
            r0 = r9.a;	 Catch:{ Exception -> 0x0169 }
            r0.i();	 Catch:{ Exception -> 0x0169 }
            goto L_0x02ad;
        L_0x0383:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0383 }
            throw r0;
        L_0x0386:
            r0 = r9.a;
            r0 = r0.d;
            r1 = r9.a;
            r1 = r1.k;
            r1 = java.lang.Boolean.valueOf(r1);
            com.iflytek.cloud.thirdparty.bw.b(r0, r1, r4);
            goto L_0x0301;
        L_0x039b:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x039b }
            throw r0;
        L_0x039e:
            r0 = r9.a;
            r0 = r0.d;
            r1 = r9.a;
            r1 = r1.k;
            r1 = java.lang.Boolean.valueOf(r1);
            com.iflytek.cloud.thirdparty.bw.b(r0, r1, r4);
            goto L_0x01c5;
        L_0x03b3:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x03b3 }
            throw r0;
        L_0x03b6:
            r1 = r9.a;
            r1 = r1.d;
            r2 = r9.a;
            r2 = r2.k;
            r2 = java.lang.Boolean.valueOf(r2);
            com.iflytek.cloud.thirdparty.bw.b(r1, r2, r4);
            goto L_0x022c;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.iflytek.cloud.record.c.b.run():void");
        }
    }

    public c(Context context) {
        this.d = context;
    }

    public c(Context context, int i, boolean z, boolean z2, boolean z3) {
        this.d = context;
        this.h = i;
        this.k = z;
        this.B = z2;
        this.A = z3;
    }

    private boolean a(int i, int i2) {
        boolean z = false;
        synchronized (this.n) {
            if (i == this.g) {
                this.g = i2;
                z = true;
            }
        }
        return z;
    }

    private void j() {
        cb.a("PcmPlayer", "createAudio start");
        int a = this.c.a();
        this.j = AudioTrack.getMinBufferSize(a, 2, 2);
        this.r = ((a / 1000) * 2) * 50;
        this.v = this.r * 10;
        if (this.b != null) {
            b();
        }
        cb.a("PcmPlayer", "createAudio || mStreamType = " + this.h + ", buffer size: " + this.j);
        this.b = new AudioTrack(this.h, a, 2, 2, this.j * 2, 1);
        if (this.j == -2 || this.j == -1) {
            throw new Exception();
        }
        cb.a("PcmPlayer", "createAudio end");
    }

    private void k() {
        b bVar = this.e;
        if (this.b == null || !(bVar == null || bVar.a() == this.h)) {
            cb.a("PcmPlayer", "prepAudioPlayer || audiotrack is null or stream type is change.");
            j();
            if (bVar != null) {
                bVar.a(this.h);
            }
        }
    }

    public int a() {
        return this.g;
    }

    public boolean a(b bVar, a aVar) {
        cb.a("PcmPlayer", "play mPlaytate= " + this.g + ",mAudioFocus= " + this.i);
        boolean z = true;
        synchronized (this.n) {
            if (this.g == 4 || this.g == 0 || this.g == 3 || this.e == null) {
                this.c = bVar;
                this.f = aVar;
                this.e = new b();
                this.e.start();
            } else {
                z = false;
            }
        }
        return z;
    }

    public void b() {
        synchronized (this.m) {
            if (this.b != null) {
                if (this.b.getPlayState() == 3) {
                    this.b.stop();
                }
                this.b.release();
                this.b = null;
            }
            cb.a("PcmPlayer", "mAudioTrack released");
        }
    }

    public boolean c() {
        if (this.g == 4 || this.g == 3) {
            return false;
        }
        cb.a("pause start fade out");
        g();
        this.g = 3;
        return true;
    }

    public boolean d() {
        boolean a = a(3, 2);
        bw.a(this.d, Boolean.valueOf(this.k), this.a);
        if (a) {
            cb.a("resume start fade in");
            f();
        }
        return a;
    }

    public void e() {
        if (4 != this.g) {
            cb.a("stop start fade out");
            g();
        }
        synchronized (this.n) {
            this.g = 4;
        }
    }

    public void f() {
        if (this.B) {
            synchronized (this.n) {
                cb.a("start fade in");
                this.z = true;
                this.x = 1.0f;
                this.y = 0.1f;
            }
        }
    }

    public void g() {
        if (this.B) {
            synchronized (this.n) {
                cb.a("start fade out");
                this.z = true;
                this.x = 0.0f;
                this.y = -0.1f;
            }
        }
    }

    public void h() {
        if (this.B) {
            synchronized (this.n) {
                if (Math.abs(this.x - this.w) < 0.1f) {
                    this.w = this.x;
                    this.z = false;
                    cb.a("fading finish");
                } else {
                    this.w += this.y;
                }
            }
            this.b.setStereoVolume(this.w, this.w);
            return;
        }
        this.z = false;
    }

    public void i() {
        cb.a("fading set silence");
        synchronized (this.n) {
            if (Math.abs(0.0f - this.x) < 0.1f) {
                this.w = 0.0f;
                this.z = false;
            }
        }
        this.b.setStereoVolume(this.w, this.w);
    }
}
