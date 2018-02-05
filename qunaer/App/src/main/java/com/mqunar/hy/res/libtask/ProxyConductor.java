package com.mqunar.hy.res.libtask;

import java.util.HashMap;
import java.util.Map;
import qunar.lego.utils.HttpHeader;
import qunar.lego.utils.Pitcher;

public class ProxyConductor extends NetHttpConductor {
    private String queryName;
    private String queryValue;

    public class Builder {
        private byte[] content = null;
        private Map<String, String> header;
        private String url;

        public Builder setUrl(String str) {
            this.url = str;
            return this;
        }

        public Builder setContent(byte[] bArr) {
            this.content = bArr;
            return this;
        }

        public Builder addHeader(String str, String str2) {
            if (this.header == null) {
                this.header = new HashMap();
            }
            this.header.put(str, str2);
            return this;
        }

        public ProxyConductor create(TaskCallback... taskCallbackArr) {
            ProxyConductor proxyConductor = new ProxyConductor(taskCallbackArr);
            proxyConductor.setParams(new Object[]{this.url, this.content, this.header});
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
        Pitcher pitcher = new Pitcher(this.train.context, getUrl(), getContent(), new HttpHeader(getReqHeader()));
        setReqHeader(new HttpHeader(pitcher.getReqHeader()));
        setContent(pitcher.getContent());
        setUrl(buildProxyUrl(pitcher));
    }

    private String buildProxyUrl(Pitcher pitcher) {
        String proxyUrl = pitcher.getProxyUrl();
        return (this.queryName == null || this.queryValue == null) ? proxyUrl : proxyUrl + "?" + this.queryName + "=" + this.queryValue;
    }

    protected void buildResult(byte[] bArr, long j, int i) {
        byte[] buildResult = Pitcher.buildResult(bArr);
        super.buildResult(buildResult, j, buildResult.length);
    }

    public void addQuery(String str, String str2) {
        this.queryName = str;
        this.queryValue = str2;
    }
}
