package com.mqunar.libtask;

import com.mqunar.network.NetRequest;
import com.mqunar.network.NetResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import qunar.lego.utils.FormPart;
import qunar.lego.utils.HttpHeader;
import qunar.lego.utils.Pitcher;

public class ProxyConductor extends o {
    private String a;
    private String b;
    private String c;

    public class Builder {
        private String a;
        private byte[] b = null;
        private List<FormPart> c = null;
        private Map<String, String> d;

        public Builder setUrl(String str) {
            this.a = str;
            return this;
        }

        public Builder setContent(byte[] bArr) {
            this.b = bArr;
            return this;
        }

        public Builder setFormParts(List<FormPart> list) {
            this.c = list;
            return this;
        }

        public Builder addHeader(String str, String str2) {
            if (this.d == null) {
                this.d = new HashMap();
            }
            this.d.put(str, str2);
            return this;
        }

        public ProxyConductor create(TaskCallback... taskCallbackArr) {
            ProxyConductor proxyConductor = new ProxyConductor(taskCallbackArr);
            Object[] objArr = new Object[3];
            objArr[0] = this.a;
            objArr[1] = this.c == null ? this.b : this.c;
            objArr[2] = this.d;
            proxyConductor.setParams(objArr);
            return proxyConductor;
        }
    }

    public /* bridge */ /* synthetic */ void beforeAdd() {
        super.beforeAdd();
    }

    public /* bridge */ /* synthetic */ boolean cancel(boolean z) {
        return super.cancel(z);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ Object findCache(boolean z) {
        return super.findCache(z);
    }

    public /* bridge */ /* synthetic */ List getFormParts() {
        return super.getFormParts();
    }

    public /* bridge */ /* synthetic */ NetResponse getOriginResponse() {
        return super.getOriginResponse();
    }

    public /* bridge */ /* synthetic */ Map getRespHeader() {
        return super.getRespHeader();
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean sameAs(AbsConductor absConductor) {
        return super.sameAs(absConductor);
    }

    public /* bridge */ /* synthetic */ void setParams(Object[] objArr) {
        super.setParams(objArr);
    }

    public ProxyConductor(TaskCallback... taskCallbackArr) {
        super(taskCallbackArr);
    }

    protected void prepareParams() {
        Pitcher pitcher;
        if (getFormParts() != null) {
            List arrayList = new ArrayList();
            for (FormPart formPart : getFormParts()) {
                if (formPart.value == null) {
                    arrayList.add(new FormPart(formPart.name, formPart.file, formPart.metaData));
                } else {
                    arrayList.add(new FormPart(formPart.name, formPart.value));
                }
            }
            pitcher = new Pitcher(this.train.context, getUrl(), arrayList, new HttpHeader(getReqHeader()));
        } else {
            pitcher = new Pitcher(this.train.context, getUrl(), getContent(), new HttpHeader(getReqHeader()));
        }
        setReqHeader(new HttpHeader(pitcher.getReqHeader()));
        setContent(pitcher.getContent());
        setUrl(a(pitcher));
    }

    private String a(Pitcher pitcher) {
        String proxyUrl = pitcher.getProxyUrl();
        return (this.a == null || this.b == null) ? proxyUrl : proxyUrl + "?" + this.a + "=" + this.b;
    }

    protected void buildResult(byte[] bArr, long j, int i) {
        byte[] buildResult = Pitcher.buildResult(bArr);
        super.buildResult(buildResult, j, buildResult.length);
    }

    public void addQuery(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    protected boolean onInterceptRequest(NetRequest netRequest) {
        super.onInterceptRequest(netRequest);
        this.c = q.a(this.train.context, netRequest);
        return false;
    }

    protected boolean onInterceptResponse(NetResponse netResponse) {
        super.onInterceptResponse(netResponse);
        q.a(this.train.context, this.c, getRespHeader());
        return false;
    }
}
