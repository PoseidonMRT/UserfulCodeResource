package com.mqunar.hy.res.libtask;

import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import com.mqunar.hy.res.logger.Timber;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URLEncodedUtils;
import qunar.lego.utils.HttpHeader;

class NetHttpConductor extends AbsConductor {
    private byte[] content = null;
    private boolean cut;
    private NetRequest netRequest;
    private final HttpHeader reqHeader = new HttpHeader();
    private final HttpHeader respHeader = new HttpHeader();
    private String url;

    class CutCallback implements Callback {
        NetHttpConductor conductor;
        NetRequest netRequest;

        CutCallback(NetHttpConductor netHttpConductor) {
            this.conductor = netHttpConductor;
        }

        public void setNetRequest(NetRequest netRequest) {
            this.netRequest = netRequest;
        }

        public boolean handleMessage(Message message) {
            if (this.conductor.getStatus().equals(TaskCode.TASK_CANCEL)) {
                return true;
            }
            NetMsgObj netMsgObj;
            if (message.what == 1) {
                netMsgObj = (NetMsgObj) message.obj;
                if (netMsgObj.id == this.netRequest.id) {
                    this.conductor.buildResult((byte[]) netMsgObj.obj, netMsgObj.arg1, netMsgObj.arg2);
                    this.conductor.msgd.onMessage(TaskCode.TASK_RESULT, this.conductor);
                }
            } else if (message.what == 2) {
                netMsgObj = (NetMsgObj) message.obj;
                if (netMsgObj.id == this.netRequest.id) {
                    this.conductor.requestTotal = netMsgObj.arg1;
                    this.conductor.msgd.onMessage(TaskCode.TASK_REQUEST, this.conductor);
                }
            }
            return false;
        }
    }

    public NetHttpConductor(TaskCallback... taskCallbackArr) {
        super(taskCallbackArr);
    }

    protected void checkTicket(Ticket ticket) {
        this.cut = ticket.multiTrasnfer;
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
                        this.url = (String) obj;
                    } else if (i2 == 1) {
                        if (obj != null && (obj instanceof byte[])) {
                            this.content = (byte[]) obj;
                        }
                    } else if (i2 != 2) {
                        continue;
                    } else {
                        if (obj != null) {
                            try {
                                if (obj instanceof Map) {
                                    this.reqHeader.addHeaders((Map) obj);
                                }
                            } catch (Exception e) {
                                throw new IllegalArgumentException("input params must be String, byte[]/List, Map ");
                            }
                        }
                        if (obj != null && (obj instanceof HttpHeader)) {
                            this.reqHeader.addHeaders((HttpHeader) obj);
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
        if (cancel && this.netRequest != null) {
            this.netRequest.cancel();
        }
        return cancel;
    }

    public void beforeAdd() {
        super.beforeAdd();
        String str = (String) ReflectUtils.invokeStaticMethod("com.mqunar.qav.uelog.QAVLog", "getRequestId", null, null);
        if (!TextUtils.isEmpty(str)) {
            this.reqHeader.addHeader("qrid", str);
        }
    }

    protected void doingTask() {
        if (this.status.get() != TaskCode.TASK_CANCEL) {
            prepareParams();
            this.netRequest = newNetRequstObj();
            if (!onInterceptRequest(this.netRequest)) {
                try {
                    NetResponse request = NetRequestManager.getInstance().request(this.netRequest, this.train.context);
                    Map map = request.headers;
                    if (map != null) {
                        for (String str : map.keySet()) {
                            this.respHeader.addHeader(str, (String) map.get(str));
                        }
                    }
                    if (!onInterceptResponse(request) && this.status.get() != TaskCode.TASK_CANCEL) {
                        switch (request.error) {
                            case 0:
                                Timber.v("response code", "code=%d", Integer.valueOf(request.code));
                                if (request.code >= HttpStatus.SC_BAD_REQUEST) {
                                    this.error = -1;
                                    break;
                                }
                                this.status.set(TaskCode.TASK_RESULT);
                                try {
                                    buildResult(request.result, request.total, request.resultLen);
                                    this.msgd.onMessage(getStatus(), this);
                                    return;
                                } catch (Throwable e) {
                                    Timber.e(e, new Object[0]);
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
                        if (request.e != null) {
                            Timber.e(request.e, this.url, new Object[0]);
                        }
                        this.status.set(TaskCode.TASK_ERROR);
                        this.msgd.onMessage(TaskCode.TASK_ERROR, this);
                    }
                } catch (Throwable e2) {
                    if (this.status.get() != TaskCode.TASK_CANCEL) {
                        this.error = -3;
                        Timber.e(e2, new Object[0]);
                        this.status.set(TaskCode.TASK_ERROR);
                        this.msgd.onMessage(TaskCode.TASK_ERROR, this);
                    }
                }
            }
        }
    }

    protected void prepareParams() {
    }

    private boolean interceptRequest(NetRequest netRequest) {
        boolean z = false;
        if (onInterceptRequest(netRequest)) {
            z = true;
        }
        if (onInterceptRequest()) {
            return true;
        }
        return z;
    }

    private boolean interceptResponse(NetResponse netResponse) {
        return onInterceptResponse(netResponse);
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
        if (this.cut) {
            Callback cutCallback = new CutCallback(this);
            NetRequest netRequest2 = new NetRequest(this.url, this.content, cutCallback);
            cutCallback.setNetRequest(netRequest2);
            netRequest = netRequest2;
        } else {
            netRequest = new NetRequest(this.url, this.content, null);
        }
        if (!this.reqHeader.hasHeader("Content-Type")) {
            this.reqHeader.setHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE);
        }
        Iterator it = this.reqHeader.iterator();
        while (it.hasNext()) {
            SimpleEntry simpleEntry = (SimpleEntry) it.next();
            netRequest.addHeader((String) simpleEntry.getKey(), (String) simpleEntry.getValue());
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
        return this.url;
    }

    public <T extends AbsConductor> boolean sameAs(T t) {
        boolean z = true;
        if (!(t instanceof NetHttpConductor)) {
            return false;
        }
        String str = ((NetHttpConductor) t).url;
        if (this.url == null) {
            return str == null;
        } else if (!this.url.equals(str)) {
            return false;
        } else {
            if (Arrays.equals(this.content, ((NetHttpConductor) t).content)) {
                z = false;
            }
            return z;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NetHttpConductor)) {
            return false;
        }
        NetHttpConductor netHttpConductor = (NetHttpConductor) obj;
        if (!Arrays.equals(this.content, netHttpConductor.content)) {
            return false;
        }
        if (this.url != null) {
            if (this.url.equals(netHttpConductor.url)) {
                return true;
            }
        } else if (netHttpConductor.url == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        if (this.url != null) {
            hashCode = this.url.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode *= 31;
        if (this.content != null) {
            i = Arrays.hashCode(this.content);
        }
        return hashCode + i;
    }

    public Object findCache(boolean z) {
        return null;
    }

    protected void setUrl(String str) {
        this.url = str;
    }

    protected String getUrl() {
        return this.url;
    }

    protected void setContent(byte[] bArr) {
        this.content = bArr;
    }

    protected byte[] getContent() {
        return this.content;
    }

    protected void setReqHeader(HttpHeader httpHeader) {
        if (httpHeader != null) {
            this.reqHeader.addHeaders(httpHeader);
        }
    }

    protected void setReqHeader(Map<String, Object> map) {
        if (map != null) {
            this.reqHeader.addHeaders((Map) map);
        }
    }

    protected Map<String, Object> getReqHeader() {
        return this.reqHeader.getHeadersMap();
    }

    public Map<String, Object> getRespHeader() {
        return this.respHeader.getHeadersMap();
    }
}
