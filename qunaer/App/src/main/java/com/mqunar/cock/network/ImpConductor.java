package com.mqunar.cock.network;

import android.net.http.Headers;
import com.mqunar.cock.model.BaseParam;
import com.mqunar.imp.Imp;
import com.mqunar.json.JsonUtils;
import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.CrossConductor;
import com.mqunar.libtask.TaskCallback;
import com.mqunar.tools.DateTimeUtils;
import com.mqunar.tools.log.QLog;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ImpConductor extends CrossConductor {
    private String a;
    private BaseParam b;
    private HashMap<String, Object> c = new HashMap();
    private byte[] d;

    public ImpConductor(TaskCallback... taskCallbackArr) {
        super(taskCallbackArr);
        this.c.put(Headers.CONN_DIRECTIVE, "keep-alive");
        this.c.put("Content-Type", "application/octet-stream");
    }

    public void setParams(Object... objArr) {
        initParam(objArr);
        if (objArr != null) {
            int i = -1;
            while (true) {
                try {
                    int i2 = i + 1;
                    if (objArr.length > i2) {
                        Object obj = objArr[i2];
                        if (i2 == 0) {
                            this.a = (String) obj;
                        } else if (i2 == 1) {
                            this.b = (BaseParam) obj;
                        }
                        i = i2;
                    } else {
                        this.d = DateTimeUtils.printCalendarByPattern(DateTimeUtils.getCurrentDateTime(), DateTimeUtils.yyyyMMddHHmmss).getBytes();
                        return;
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("input params must be url:String,req:QImBaseParam");
                }
            }
        }
    }

    protected void prepareParams() {
        byte[] bArr = new byte[0];
        try {
            bArr = Imp.e(JsonUtils.toJsonString(this.b).getBytes("utf-8"), this.d);
            this.a += "/" + this.b.t;
            QLog.d("OneKeyCremation", this.a, new Object[0]);
        } catch (UnsupportedEncodingException e) {
        }
        setContent(bArr);
        setUrl(this.a);
        setReqHeader(this.c);
    }

    protected void buildResult(byte[] bArr, long j, int i) {
        if (bArr == null || ((long) bArr.length) != j) {
            throw new IllegalStateException("can not parse result");
        }
        try {
            this.result = Imp.d(bArr);
        } catch (Exception e) {
            throw new IllegalStateException("can not parse result");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        if (r4 != r5) goto L_0x0006;
    L_0x0004:
        r1 = r0;
    L_0x0005:
        return r1;
    L_0x0006:
        r2 = r5 instanceof com.mqunar.cock.network.ImpConductor;
        if (r2 == 0) goto L_0x0005;
    L_0x000a:
        r2 = super.equals(r5);
        if (r2 == 0) goto L_0x0005;
    L_0x0010:
        r5 = (com.mqunar.cock.network.ImpConductor) r5;
        r2 = r4.a;
        if (r2 == 0) goto L_0x003f;
    L_0x0016:
        r2 = r4.a;
        r3 = r5.a;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0020:
        r2 = r4.b;
        if (r2 == 0) goto L_0x0044;
    L_0x0024:
        r2 = r4.b;
        r3 = r5.b;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x002e:
        r2 = r4.c;
        if (r2 == 0) goto L_0x0049;
    L_0x0032:
        r2 = r4.c;
        r3 = r5.c;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x003d;
    L_0x003c:
        r0 = r1;
    L_0x003d:
        r1 = r0;
        goto L_0x0005;
    L_0x003f:
        r2 = r5.a;
        if (r2 == 0) goto L_0x0020;
    L_0x0043:
        goto L_0x0005;
    L_0x0044:
        r2 = r5.b;
        if (r2 == 0) goto L_0x002e;
    L_0x0048:
        goto L_0x0005;
    L_0x0049:
        r2 = r5.c;
        if (r2 != 0) goto L_0x003c;
    L_0x004d:
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mqunar.cock.network.ImpConductor.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int hashCode2 = super.hashCode() * 31;
        if (this.a != null) {
            hashCode = this.a.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode + hashCode2) * 31;
        if (this.b != null) {
            hashCode = this.b.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode + hashCode2) * 31;
        if (this.c != null) {
            i = this.c.hashCode();
        }
        return hashCode + i;
    }

    public <T extends AbsConductor> boolean sameAs(T t) {
        if (this == t) {
            return true;
        }
        if (!(t instanceof ImpConductor)) {
            return false;
        }
        ImpConductor impConductor = (ImpConductor) t;
        if (!this.a.equals(impConductor.a)) {
            return false;
        }
        if (this.b.t != null) {
            return this.b.t.equals(impConductor.b.t);
        }
        if (impConductor.b.t != null) {
            return false;
        }
        return true;
    }

    public BaseParam getReqParam() {
        return this.b;
    }
}
