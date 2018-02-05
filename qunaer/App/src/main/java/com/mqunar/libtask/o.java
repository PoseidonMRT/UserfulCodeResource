package com.mqunar.libtask;

import android.os.Handler.Callback;
import android.text.TextUtils;
import com.mqunar.network.Headers;
import com.mqunar.network.NetRequest;
import com.mqunar.network.NetRequestManager;
import com.mqunar.network.NetResponse;
import com.mqunar.tools.CheckUtils;
import com.mqunar.tools.log.QLog;
import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URLEncodedUtils;

class o extends AbsConductor {
    private String a;
    private byte[] b = null;
    private List<FormPart> c = null;
    private boolean d;
    private final HttpHeader e = new HttpHeader();
    private final HttpHeader f = new HttpHeader();
    private NetRequest g;
    private NetResponse h;

    public o(TaskCallback... taskCallbackArr) {
        super(taskCallbackArr);
    }

    protected void checkTicket(Ticket ticket) {
        this.d = ticket.multiTrasnfer;
    }

    public void setParams(Object... objArr) {
        super.setParams(objArr);
        if (objArr != null) {
            int i = -1;
            while (true) {
                int i2 = i + 1;
                if (objArr.length > i2) {
                    Object obj = objArr[i2];
                    if (i2 == 0) {
                        this.a = (String) obj;
                    } else if (i2 == 1) {
                        if (obj != null && (obj instanceof byte[])) {
                            this.b = (byte[]) obj;
                        } else if (obj != null) {
                            try {
                                if (obj instanceof List) {
                                    this.c = (List) obj;
                                }
                            } catch (Exception e) {
                                throw new IllegalArgumentException("input params must be String, byte[]/List, Map ");
                            }
                        } else {
                            continue;
                        }
                    } else if (i2 == 2) {
                        if (obj != null && (obj instanceof Map)) {
                            this.e.addHeaders((Map) obj);
                        } else if (obj != null && (obj instanceof HttpHeader)) {
                            this.e.addHeaders((HttpHeader) obj);
                        }
                    }
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    public boolean cancel(boolean z) {
        boolean cancel = super.cancel(z);
        if (cancel && this.g != null) {
            this.g.cancel();
        }
        return cancel;
    }

    public void beforeAdd() {
        super.beforeAdd();
        String str = (String) r.a("com.mqunar.qav.uelog.QAVLog", "getRequestId", null, null);
        if (!TextUtils.isEmpty(str)) {
            this.e.addHeader("qrid", str);
        }
    }

    protected void doingTask() {
        if (this.status.get() != TaskCode.TASK_CANCEL) {
            prepareParams();
            this.g = newNetRequstObj();
            if (!onInterceptRequest(this.g)) {
                this.h = null;
                try {
                    putExtraData(Statistics.KEY_TASK_RUN_BEGIN, Long.valueOf(System.currentTimeMillis()));
                    this.h = NetRequestManager.getInstance().request(this.g, this.train.context);
                    putExtraData(Statistics.KEY_TASK_RUN_END, Long.valueOf(System.currentTimeMillis()));
                    Headers headers = this.h.headers;
                    if (headers != null) {
                        for (int i = 0; i < headers.size(); i++) {
                            this.f.addHeader(headers.name(i), headers.value(i));
                        }
                    }
                    if (!onInterceptResponse(this.h) && this.status.get() != TaskCode.TASK_CANCEL) {
                        switch (this.h.error) {
                            case 0:
                                QLog.v("response code", "code=%d", Integer.valueOf(this.h.code));
                                if (this.h.code >= HttpStatus.SC_BAD_REQUEST) {
                                    this.error = -1;
                                    break;
                                }
                                this.status.set(TaskCode.TASK_RESULT);
                                try {
                                    buildResult(this.h.result, this.h.total, this.h.resultLen);
                                    this.msgd.onMessage(getStatus(), this);
                                    return;
                                } catch (Throwable e) {
                                    QLog.e(e);
                                    this.error = -1;
                                    break;
                                }
                            case 1:
                            case 2:
                            case 3:
                                this.error = -2;
                                break;
                            default:
                                this.error = -3;
                                break;
                        }
                        if (this.h.e != null) {
                            QLog.e(this.h.e, this.a, new Object[0]);
                        }
                        this.status.set(TaskCode.TASK_ERROR);
                        this.msgd.onMessage(TaskCode.TASK_ERROR, this);
                    }
                } catch (Throwable e2) {
                    if (this.status.get() != TaskCode.TASK_CANCEL) {
                        this.error = -3;
                        QLog.e(e2);
                        this.status.set(TaskCode.TASK_ERROR);
                        this.msgd.onMessage(TaskCode.TASK_ERROR, this);
                    }
                }
            }
        }
    }

    protected void prepareParams() {
    }

    @Deprecated
    protected boolean onInterceptRequest() {
        return false;
    }

    protected boolean onInterceptRequest(NetRequest netRequest) {
        return false;
    }

    protected boolean onInterceptResponse(NetResponse netResponse) {
        return false;
    }

    protected NetRequest newNetRequstObj() {
        NetRequest netRequest;
        if (this.d) {
            Callback pVar = new p(this);
            NetRequest netRequest2 = new NetRequest(this.a, this.b, pVar);
            pVar.a(netRequest2);
            netRequest = netRequest2;
        } else {
            netRequest = new NetRequest(this.a, this.b, null);
        }
        if (!this.e.hasHeader("Content-Type") && CheckUtils.isEmpty(this.c)) {
            this.e.setHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE);
        }
        Iterator it = this.e.iterator();
        while (it.hasNext()) {
            SimpleEntry simpleEntry = (SimpleEntry) it.next();
            netRequest.addHeader((String) simpleEntry.getKey(), (String) simpleEntry.getValue());
        }
        if (this.c != null) {
            try {
                for (FormPart formPart : this.c) {
                    String str = formPart.name;
                    String str2 = formPart.value;
                    if (str2 != null) {
                        netRequest.addFormDataPart(str, str2, formPart.header.getHeaders());
                    } else {
                        netRequest.addFormDataPart(str, new File(formPart.file), formPart.metaData, formPart.header.getHeaders());
                    }
                }
            } catch (Throwable e) {
                throw new IllegalArgumentException("multiPart params error", e);
            }
        }
        return netRequest;
    }

    protected void buildResult(byte[] bArr, long j, int i) {
        this.result = bArr;
        this.resultTotal = j;
        this.currentLength = i;
    }

    protected int getEmpId() {
        return hashCode();
    }

    protected String getEmpName() {
        return this.a;
    }

    public <T extends AbsConductor> boolean sameAs(T t) {
        boolean z = true;
        if (!(t instanceof o)) {
            return false;
        }
        String str = ((o) t).a;
        if (this.a == null) {
            return str == null;
        } else if (!this.a.equals(str)) {
            return false;
        } else {
            if (Arrays.equals(this.b, ((o) t).b)) {
                z = false;
            }
            return z;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof o)) {
            return false;
        }
        o oVar = (o) obj;
        if (!Arrays.equals(this.b, oVar.b)) {
            return false;
        }
        if (this.c == null ? oVar.c != null : !this.c.equals(oVar.c)) {
            return false;
        }
        if (this.a != null) {
            if (this.a.equals(oVar.a)) {
                return true;
            }
        } else if (oVar.a == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        if (this.a != null) {
            hashCode = this.a.hashCode();
        } else {
            hashCode = 0;
        }
        int i2 = hashCode * 31;
        if (this.b != null) {
            hashCode = Arrays.hashCode(this.b);
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode + i2) * 31;
        if (this.c != null) {
            i = this.c.hashCode();
        }
        return hashCode + i;
    }

    public Object findCache(boolean z) {
        return null;
    }

    protected void setUrl(String str) {
        this.a = str;
    }

    protected String getUrl() {
        return this.a;
    }

    protected void setContent(byte[] bArr) {
        this.b = bArr;
    }

    protected byte[] getContent() {
        return this.b;
    }

    protected void setReqHeader(HttpHeader httpHeader) {
        if (httpHeader != null) {
            this.e.addHeaders(httpHeader);
        }
    }

    protected void setReqHeader(Map<String, Object> map) {
        if (map != null) {
            this.e.addHeaders((Map) map);
        }
    }

    protected Map<String, Object> getReqHeader() {
        return this.e.getHeadersMap();
    }

    protected void setFormParts(List<FormPart> list) {
        this.c = list;
    }

    public List<FormPart> getFormParts() {
        return this.c;
    }

    public Map<String, Object> getRespHeader() {
        return this.f.getHeadersMap();
    }

    public NetResponse getOriginResponse() {
        return this.h;
    }
}
