package com.mqunar.cock.network;

import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.mqunar.Manifest.permission;
import com.mqunar.cock.model.ResponseHttpMessage;
import com.mqunar.cock.network.callback.OnPubSyncListener;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.imp.Imp;
import com.mqunar.tools.ArrayUtils;
import com.mqunar.tools.DateTimeUtils;
import com.mqunar.tools.log.QLog;
import com.mqunar.yacca.Cmd.Builder;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

public class CockHandler {
    static final /* synthetic */ boolean b = (!CockHandler.class.desiredAssertionStatus());
    private static volatile CockHandler d;
    int[] a = new int[1];
    private final String c = "CockHandler";
    private OnIMMessage e;
    private ArrayList<d> f = new ArrayList();
    private Map<String, MessageListener> g = new HashMap();
    private Map<String, OnPubSyncListener> h;
    private ArrayList<ImListener> i = new ArrayList();
    private final String j = "com.mqunar.atom.qutui.ACTION_MESSAGE_RECEIVED";
    private final String k = "com.mqunar.atom.qutui.ACTION_MESSAGE_TRANSPARENT";
    private final String l = "com.mqunar.atom.qutui.ACTION_MESSAGE_IM";
    private final String m = "com.mqunar.atom.qutui.OBJ_MESSAGE";
    private final String n = permission.Qunar;
    private String o;
    private boolean p;
    private boolean q = true;
    private OnCockMessage r;
    private Map<String, Timer> s = new HashMap();
    private int t = 0;
    private boolean u;

    public void setYid(String str) {
        this.o = str;
    }

    public String getYid() {
        return this.o;
    }

    public boolean isRelease() {
        return this.p;
    }

    public void setRelease(boolean z) {
        this.p = z;
    }

    public void setPush(boolean z) {
        QLog.d("CockHandler", "startpush", new Object[0]);
        this.q = z;
    }

    public void registerImListener(ImListener imListener) {
        this.i.add(imListener);
    }

    public void unregisterImListener(ImListener imListener) {
        this.i.remove(imListener);
    }

    public void registerPubSyncListener(String str, OnPubSyncListener onPubSyncListener) {
        this.h.put(str, onPubSyncListener);
    }

    public void unregisterPubSyncListener(String str) {
        this.h.remove(str);
    }

    public void setOnCockMessageListener(OnCockMessage onCockMessage) {
        this.r = onCockMessage;
    }

    public static CockHandler getInstance() {
        if (d == null) {
            synchronized (CockHandler.class) {
                if (d == null) {
                    d = new CockHandler();
                }
            }
        }
        return d;
    }

    private CockHandler() {
    }

    public void listen(String str, int i, String str2) {
        Iterator it;
        QLog.d("CockHandler", "listen", new Object[0]);
        if (!ArrayUtils.isEmpty(this.f)) {
            it = this.f.iterator();
            while (it.hasNext()) {
                d dVar = (d) it.next();
                dVar.stop();
                this.f.remove(dVar);
            }
            this.u = false;
        }
        this.f.add(new d(str, i));
        it = this.f.iterator();
        while (it.hasNext()) {
            dVar = (d) it.next();
            dVar.setMsgCallback(new a(this));
            dVar.setYaccaCallback(new c(this));
            if (!this.u) {
                dVar.listen();
            }
            dVar.send(Builder.protocal_version((byte) 1, (byte) 1));
            dVar.send(Builder.set_id(hexStr2Bytes(getInstance().getYid().replace("-", ""))));
        }
    }

    public void stop(boolean z) {
        if (!ArrayUtils.isEmpty(this.f)) {
            Iterator it = this.f.iterator();
            while (it.hasNext()) {
                d dVar = (d) it.next();
                dVar.a = z;
                dVar.stop();
            }
        }
        this.u = false;
    }

    private void a(String str, String str2) {
        Intent intent = new Intent();
        intent.setAction(str);
        if (!TextUtils.isEmpty(str2)) {
            intent.putExtra("com.mqunar.atom.qutui.OBJ_MESSAGE", str2);
        }
        QApplication.getContext().sendBroadcast(intent, permission.Qunar);
    }

    public void response(int i, ResponseHttpMessage responseHttpMessage) {
        this.a[0] = i;
    }

    public void sendRequest(byte[] bArr, String str) {
        if (str.equals("QuTui")) {
            ByteBuffer allocate = ByteBuffer.allocate(bArr.length + 2);
            allocate.putShort((short) 1);
            allocate.put(bArr);
            ((d) this.f.get(OneKeyCremation.getmReqid() % 4)).send(Builder.send(str, (byte) 0, allocate.array()));
        } else if (str.equals("OChat")) {
            ((d) this.f.get(OneKeyCremation.getmReqid() % 4)).send(Builder.send(str, (byte) 0, bArr));
        }
    }

    public byte[] serialize(Object obj) {
        try {
            return Imp.e(JSON.toJSONString(obj).getBytes("utf-8"), DateTimeUtils.printCalendarByPattern(DateTimeUtils.getCurrentDateTime(), DateTimeUtils.yyyyMMddHHmmss).getBytes());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unsupported encoding...utf-8");
        }
    }

    public static byte[] hexStr2Bytes(String str) {
        String toUpperCase = str.trim().replace(" ", "").toUpperCase(Locale.US);
        int length = toUpperCase.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = (i * 2) + 1;
            bArr[i] = (byte) (Integer.decode("0x" + toUpperCase.substring(i * 2, i2) + toUpperCase.substring(i2, i2 + 1)).intValue() & 255);
        }
        return bArr;
    }
}
