package com.baidu.location.f;

import android.net.wifi.ScanResult;
import android.text.TextUtils;
import com.iflytek.speech.VoiceWakeuperAidl;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class i {
    public List<ScanResult> a = null;
    private long b = 0;
    private long c = 0;
    private boolean d = false;
    private boolean e;

    public i(List<ScanResult> list, long j) {
        this.b = j;
        this.a = list;
        this.c = System.currentTimeMillis();
        i();
    }

    private boolean a(String str) {
        return TextUtils.isEmpty(str) ? false : Pattern.compile("wpa|wep", 2).matcher(str).find();
    }

    private void i() {
        if (a() >= 1) {
            Object obj = 1;
            for (int size = this.a.size() - 1; size >= 1 && r2 != null; size--) {
                int i = 0;
                obj = null;
                while (i < size) {
                    Object obj2;
                    if (((ScanResult) this.a.get(i)).level < ((ScanResult) this.a.get(i + 1)).level) {
                        ScanResult scanResult = (ScanResult) this.a.get(i + 1);
                        this.a.set(i + 1, this.a.get(i));
                        this.a.set(i, scanResult);
                        obj2 = 1;
                    } else {
                        obj2 = obj;
                    }
                    i++;
                    obj = obj2;
                }
            }
        }
    }

    public int a() {
        return this.a == null ? 0 : this.a.size();
    }

    public String a(int i) {
        return a(i, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String a(int r25, boolean r26) {
        /*
        r24 = this;
        r2 = r24.a();
        r3 = 1;
        if (r2 >= r3) goto L_0x0009;
    L_0x0007:
        r2 = 0;
    L_0x0008:
        return r2;
    L_0x0009:
        r3 = 0;
        r17 = new java.util.Random;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r17.<init>();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r18 = new java.lang.StringBuffer;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r0 = r18;
        r0.<init>(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r19 = new java.util.ArrayList;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r19.<init>();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = com.baidu.location.f.k.a();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r5 = r2.i();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r4 = 0;
        r2 = -1;
        if (r5 == 0) goto L_0x0330;
    L_0x0029:
        r6 = r5.getBSSID();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r6 == 0) goto L_0x0330;
    L_0x002f:
        r2 = r5.getBSSID();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r4 = ":";
        r6 = "";
        r4 = r2.replace(r4, r6);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r5.getRssi();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r2 >= 0) goto L_0x032b;
    L_0x0041:
        r2 = -r2;
        r15 = r2;
        r16 = r4;
    L_0x0045:
        r4 = 0;
        r7 = 0;
        r2 = 0;
        r6 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r9 = 17;
        if (r6 < r9) goto L_0x0328;
    L_0x0050:
        r4 = android.os.SystemClock.elapsedRealtimeNanos();	 Catch:{ Error -> 0x009e, Exception -> 0x031a }
        r9 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r4 = r4 / r9;
    L_0x0057:
        r9 = 0;
        r6 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1));
        if (r6 <= 0) goto L_0x0328;
    L_0x005d:
        r2 = 1;
        r13 = r4;
    L_0x005f:
        if (r2 == 0) goto L_0x0325;
    L_0x0061:
        if (r2 == 0) goto L_0x00a2;
    L_0x0063:
        if (r26 == 0) goto L_0x00a2;
    L_0x0065:
        r2 = 1;
    L_0x0066:
        r12 = r2;
    L_0x0067:
        r6 = 0;
        r5 = 0;
        r0 = r24;
        r2 = r0.a;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.size();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r4 = 1;
        r0 = r25;
        if (r2 <= r0) goto L_0x0321;
    L_0x0076:
        r2 = 0;
        r11 = r2;
    L_0x0078:
        r0 = r25;
        if (r11 >= r0) goto L_0x0209;
    L_0x007c:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.get(r11);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.level;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r2 != 0) goto L_0x00a4;
    L_0x008a:
        r2 = r4;
        r4 = r6;
        r23 = r5;
        r5 = r7;
        r7 = r3;
        r3 = r23;
    L_0x0092:
        r8 = r11 + 1;
        r11 = r8;
        r23 = r3;
        r3 = r7;
        r7 = r5;
        r6 = r4;
        r5 = r23;
        r4 = r2;
        goto L_0x0078;
    L_0x009e:
        r4 = move-exception;
        r4 = 0;
        goto L_0x0057;
    L_0x00a2:
        r2 = 0;
        goto L_0x0066;
    L_0x00a4:
        if (r12 == 0) goto L_0x00c7;
    L_0x00a6:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Exception -> 0x018e, Error -> 0x019c }
        r2 = r2.get(r11);	 Catch:{ Exception -> 0x018e, Error -> 0x019c }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Exception -> 0x018e, Error -> 0x019c }
        r9 = r2.timestamp;	 Catch:{ Exception -> 0x018e, Error -> 0x019c }
        r9 = r13 - r9;
        r20 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r9 = r9 / r20;
    L_0x00b9:
        r2 = java.lang.Long.valueOf(r9);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r19;
        r0.add(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1));
        if (r2 <= 0) goto L_0x00c7;
    L_0x00c6:
        r7 = r9;
    L_0x00c7:
        if (r4 == 0) goto L_0x0193;
    L_0x00c9:
        r4 = 0;
        r2 = "&wf=";
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
    L_0x00d1:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.get(r11);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.BSSID;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r2 == 0) goto L_0x01ff;
    L_0x00df:
        r9 = ":";
        r10 = "";
        r9 = r2.replace(r9, r10);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r18;
        r0.append(r9);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r24;
        r2 = r0.a;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.get(r11);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.level;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r2 >= 0) goto L_0x00fb;
    L_0x00fa:
        r2 = -r2;
    L_0x00fb:
        r10 = java.util.Locale.CHINA;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r20 = ";%d;";
        r21 = 1;
        r0 = r21;
        r0 = new java.lang.Object[r0];	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r21 = r0;
        r22 = 0;
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r21[r22] = r2;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r20;
        r1 = r21;
        r2 = java.lang.String.format(r10, r0, r1);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r6 = r6 + 1;
        r2 = 0;
        if (r16 == 0) goto L_0x0141;
    L_0x0121:
        r0 = r16;
        r9 = r0.equals(r9);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r9 == 0) goto L_0x0141;
    L_0x0129:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.get(r11);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.capabilities;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r24;
        r2 = r0.a(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r24;
        r0.e = r2;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = 1;
        r5 = r6;
    L_0x0141:
        if (r2 != 0) goto L_0x01ee;
    L_0x0143:
        if (r3 != 0) goto L_0x01a0;
    L_0x0145:
        r2 = 10;
        r0 = r17;
        r2 = r0.nextInt(r2);	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r9 = 2;
        if (r2 != r9) goto L_0x031e;
    L_0x0150:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.get(r11);	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.SSID;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        if (r2 == 0) goto L_0x031e;
    L_0x015e:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.get(r11);	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.SSID;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.length();	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r9 = 30;
        if (r2 >= r9) goto L_0x031e;
    L_0x0172:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.get(r11);	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.SSID;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r0 = r18;
        r0.append(r2);	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = 1;
    L_0x0184:
        r3 = r5;
        r23 = r4;
        r4 = r6;
        r5 = r7;
        r7 = r2;
        r2 = r23;
        goto L_0x0092;
    L_0x018e:
        r2 = move-exception;
        r9 = 0;
        goto L_0x00b9;
    L_0x0193:
        r2 = "|";
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        goto L_0x00d1;
    L_0x019c:
        r2 = move-exception;
        r2 = 0;
        goto L_0x0008;
    L_0x01a0:
        r2 = 1;
        if (r3 != r2) goto L_0x031e;
    L_0x01a3:
        r2 = 20;
        r0 = r17;
        r2 = r0.nextInt(r2);	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r9 = 1;
        if (r2 != r9) goto L_0x031e;
    L_0x01ae:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.get(r11);	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.SSID;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        if (r2 == 0) goto L_0x031e;
    L_0x01bc:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.get(r11);	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.SSID;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.length();	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r9 = 30;
        if (r2 >= r9) goto L_0x031e;
    L_0x01d0:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.get(r11);	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = r2.SSID;	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r0 = r18;
        r0.append(r2);	 Catch:{ Exception -> 0x01e3, Error -> 0x019c }
        r2 = 2;
        goto L_0x0184;
    L_0x01e3:
        r2 = move-exception;
        r2 = r4;
        r4 = r6;
        r23 = r5;
        r5 = r7;
        r7 = r3;
        r3 = r23;
        goto L_0x0092;
    L_0x01ee:
        r0 = r24;
        r2 = r0.a;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.get(r11);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = (android.net.wifi.ScanResult) r2;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.SSID;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
    L_0x01ff:
        r2 = r4;
        r4 = r6;
        r23 = r5;
        r5 = r7;
        r7 = r3;
        r3 = r23;
        goto L_0x0092;
    L_0x0209:
        if (r4 != 0) goto L_0x0317;
    L_0x020b:
        r2 = new java.lang.StringBuilder;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2.<init>();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r3 = "&wf_n=";
        r2 = r2.append(r3);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.append(r5);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.toString();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r16 == 0) goto L_0x0240;
    L_0x0225:
        r2 = -1;
        if (r15 == r2) goto L_0x0240;
    L_0x0228:
        r2 = new java.lang.StringBuilder;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2.<init>();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r3 = "&wf_rs=";
        r2 = r2.append(r3);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.append(r15);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.toString();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
    L_0x0240:
        r2 = 10;
        r2 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x02c7;
    L_0x0246:
        r2 = r19.size();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r2 <= 0) goto L_0x02c7;
    L_0x024c:
        r2 = 0;
        r0 = r19;
        r2 = r0.get(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = (java.lang.Long) r2;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = r2.longValue();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r6 = 0;
        r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r2 <= 0) goto L_0x02c7;
    L_0x025f:
        r6 = new java.lang.StringBuffer;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r6.<init>(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = "&wf_ut=";
        r6.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r4 = 1;
        r2 = 0;
        r0 = r19;
        r2 = r0.get(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = (java.lang.Long) r2;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r7 = r19.iterator();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
    L_0x0279:
        r3 = r7.hasNext();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r3 == 0) goto L_0x02be;
    L_0x027f:
        r3 = r7.next();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r3 = (java.lang.Long) r3;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r4 == 0) goto L_0x0297;
    L_0x0287:
        r4 = 0;
        r8 = r3.longValue();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r6.append(r8);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r3 = r4;
    L_0x0290:
        r4 = "|";
        r6.append(r4);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r4 = r3;
        goto L_0x0279;
    L_0x0297:
        r8 = r3.longValue();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r10 = r2.longValue();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r8 = r8 - r10;
        r10 = 0;
        r3 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r3 == 0) goto L_0x02bc;
    L_0x02a6:
        r3 = new java.lang.StringBuilder;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r3.<init>();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r10 = "";
        r3 = r3.append(r10);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r3 = r3.append(r8);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r3 = r3.toString();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r6.append(r3);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
    L_0x02bc:
        r3 = r4;
        goto L_0x0290;
    L_0x02be:
        r2 = r6.toString();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
    L_0x02c7:
        r2 = "&wf_st=";
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r24;
        r2 = r0.b;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = "&wf_et=";
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r24;
        r2 = r0.c;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = "&wf_vt=";
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = com.baidu.location.f.j.a;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r5 <= 0) goto L_0x030f;
    L_0x02f7:
        r2 = 1;
        r0 = r24;
        r0.d = r2;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r2 = "&wf_en=";
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        r0 = r24;
        r2 = r0.e;	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        if (r2 == 0) goto L_0x0315;
    L_0x0309:
        r2 = 1;
    L_0x030a:
        r0 = r18;
        r0.append(r2);	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
    L_0x030f:
        r2 = r18.toString();	 Catch:{ Error -> 0x019c, Exception -> 0x031a }
        goto L_0x0008;
    L_0x0315:
        r2 = 0;
        goto L_0x030a;
    L_0x0317:
        r2 = 0;
        goto L_0x0008;
    L_0x031a:
        r2 = move-exception;
        r2 = 0;
        goto L_0x0008;
    L_0x031e:
        r2 = r3;
        goto L_0x0184;
    L_0x0321:
        r25 = r2;
        goto L_0x0076;
    L_0x0325:
        r12 = r2;
        goto L_0x0067;
    L_0x0328:
        r13 = r4;
        goto L_0x005f;
    L_0x032b:
        r15 = r2;
        r16 = r4;
        goto L_0x0045;
    L_0x0330:
        r15 = r2;
        r16 = r4;
        goto L_0x0045;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.f.i.a(int, boolean):java.lang.String");
    }

    public boolean a(i iVar) {
        if (this.a == null || iVar == null || iVar.a == null) {
            return false;
        }
        int size = this.a.size() < iVar.a.size() ? this.a.size() : iVar.a.size();
        for (int i = 0; i < size; i++) {
            if (!((ScanResult) this.a.get(i)).BSSID.equals(((ScanResult) iVar.a.get(i)).BSSID)) {
                return false;
            }
        }
        return true;
    }

    public String b() {
        try {
            return a(com.baidu.location.h.i.O, true);
        } catch (Exception e) {
            return null;
        }
    }

    public String b(int i) {
        if (a() < 1) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(512);
        int size = this.a.size();
        if (size <= i) {
            i = size;
        }
        int i2 = 0;
        int i3 = 1;
        while (i2 < i) {
            if (((ScanResult) this.a.get(i2)).level != 0) {
                if (((ScanResult) this.a.get(i2)).BSSID == null) {
                    size = i3;
                    i2++;
                    i3 = size;
                } else {
                    if (i3 != 0) {
                        i3 = 0;
                    } else {
                        stringBuffer.append("|");
                    }
                    stringBuffer.append(((ScanResult) this.a.get(i2)).BSSID.replace(":", ""));
                    size = ((ScanResult) this.a.get(i2)).level;
                    if (size < 0) {
                        size = -size;
                    }
                    stringBuffer.append(String.format(Locale.CHINA, ";%d;", new Object[]{Integer.valueOf(size)}));
                }
            }
            size = i3;
            i2++;
            i3 = size;
        }
        return i3 == 0 ? stringBuffer.toString() : null;
    }

    public boolean b(i iVar) {
        if (this.a == null || iVar == null || iVar.a == null) {
            return false;
        }
        int size = this.a.size() < iVar.a.size() ? this.a.size() : iVar.a.size();
        for (int i = 0; i < size; i++) {
            String str = ((ScanResult) this.a.get(i)).BSSID;
            int i2 = ((ScanResult) this.a.get(i)).level;
            String str2 = ((ScanResult) iVar.a.get(i)).BSSID;
            int i3 = ((ScanResult) iVar.a.get(i)).level;
            if (!str.equals(str2) || i2 != i3) {
                return false;
            }
        }
        return true;
    }

    public String c() {
        try {
            return a(15);
        } catch (Exception e) {
            return null;
        }
    }

    public String c(int i) {
        int i2 = 0;
        if (i == 0 || a() < 1) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(256);
        int size = this.a.size();
        int i3 = size > com.baidu.location.h.i.O ? com.baidu.location.h.i.O : size;
        int i4 = 1;
        int i5 = 0;
        while (i5 < i3) {
            if ((i4 & i) == 0 || ((ScanResult) this.a.get(i5)).BSSID == null) {
                size = i2;
            } else {
                if (i2 == 0) {
                    stringBuffer.append("&ssid=");
                } else {
                    stringBuffer.append("|");
                }
                stringBuffer.append(((ScanResult) this.a.get(i5)).BSSID.replace(":", ""));
                stringBuffer.append(VoiceWakeuperAidl.PARAMS_SEPARATE);
                stringBuffer.append(((ScanResult) this.a.get(i5)).SSID);
                size = i2 + 1;
            }
            i4 <<= 1;
            i5++;
            i2 = size;
        }
        return stringBuffer.toString();
    }

    public boolean c(i iVar) {
        return j.a(iVar, this, com.baidu.location.h.i.R);
    }

    public int d() {
        for (int i = 0; i < a(); i++) {
            int i2 = -((ScanResult) this.a.get(i)).level;
            if (i2 > 0) {
                return i2;
            }
        }
        return 0;
    }

    public boolean e() {
        return this.d;
    }

    public boolean f() {
        return System.currentTimeMillis() - this.c > 0 && System.currentTimeMillis() - this.c < 5000;
    }

    public boolean g() {
        return System.currentTimeMillis() - this.c > 0 && System.currentTimeMillis() - this.c < 5000;
    }

    public boolean h() {
        return System.currentTimeMillis() - this.c > 0 && System.currentTimeMillis() - this.b < 5000;
    }
}
