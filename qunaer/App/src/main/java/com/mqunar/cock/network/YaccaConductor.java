package com.mqunar.cock.network;

import com.mqunar.cock.model.RequestHeader;
import com.mqunar.cock.model.TcpRequest;
import com.mqunar.cock.model.TcpResult;
import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.FormPart;
import com.mqunar.libtask.HttpHeader;
import com.mqunar.libtask.TaskCallback;
import com.mqunar.libtask.TaskCode;
import com.mqunar.tools.CheckUtils;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.client.utils.URLEncodedUtils;

public class YaccaConductor extends AbsConductor {
    private String a;
    private byte[] b = null;
    private List<FormPart> c = null;
    private final HttpHeader d = new HttpHeader();
    private final HttpHeader e = new HttpHeader();

    public YaccaConductor(TaskCallback... taskCallbackArr) {
        super(taskCallbackArr);
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
                            this.d.addHeaders((Map) obj);
                        } else if (obj != null && (obj instanceof HttpHeader)) {
                            this.d.addHeaders((HttpHeader) obj);
                        }
                    }
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    protected void doingTask() {
        TcpRequest tcpRequest = new TcpRequest();
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.pitcherType = "hotdog";
        requestHeader.pitcherUrl = this.a;
        requestHeader.header = new HashMap();
        if (!this.d.hasHeader("Content-Type") && CheckUtils.isEmpty(this.c)) {
            this.d.setHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE);
        }
        Iterator it = this.d.iterator();
        while (it.hasNext()) {
            SimpleEntry simpleEntry = (SimpleEntry) it.next();
            requestHeader.header.put(simpleEntry.getKey(), simpleEntry.getValue());
        }
        tcpRequest.requestHeader = requestHeader;
        tcpRequest.bytes = this.b;
        TcpResult sendRequest = OneKeyCremation.getInstance().sendRequest(tcpRequest);
        if (this.status.get() != TaskCode.TASK_CANCEL) {
            switch (sendRequest.resultcode) {
                case 200:
                    this.status.set(TaskCode.TASK_RESULT);
                    this.result = sendRequest.bytes;
                    this.msgd.onMessage(getStatus(), this);
                    return;
                case 300:
                    this.status.set(TaskCode.TASK_ERROR);
                    this.msgd.onMessage(TaskCode.TASK_ERROR, this);
                    return;
                default:
                    this.status.set(TaskCode.TASK_ERROR);
                    this.msgd.onMessage(TaskCode.TASK_ERROR, this);
                    return;
            }
        }
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
        if (!(t instanceof YaccaConductor)) {
            return false;
        }
        String str = ((YaccaConductor) t).a;
        if (this.a == null) {
            return str == null;
        } else if (!this.a.equals(str)) {
            return false;
        } else {
            if (Arrays.equals(this.b, ((YaccaConductor) t).b)) {
                z = false;
            }
            return z;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof YaccaConductor)) {
            return false;
        }
        YaccaConductor yaccaConductor = (YaccaConductor) obj;
        if (!Arrays.equals(this.b, yaccaConductor.b)) {
            return false;
        }
        if (this.c == null ? yaccaConductor.c != null : !this.c.equals(yaccaConductor.c)) {
            return false;
        }
        if (this.a != null) {
            if (this.a.equals(yaccaConductor.a)) {
                return true;
            }
        } else if (yaccaConductor.a == null) {
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
            this.d.addHeaders(httpHeader);
        }
    }

    protected void setReqHeader(Map<String, Object> map) {
        if (map != null) {
            this.d.addHeaders((Map) map);
        }
    }

    protected Map<String, Object> getReqHeader() {
        return this.d.getHeadersMap();
    }

    protected void setFormParts(List<FormPart> list) {
        this.c = list;
    }

    public List<FormPart> getFormParts() {
        return this.c;
    }

    public Map<String, Object> getRespHeader() {
        return this.e.getHeadersMap();
    }
}
