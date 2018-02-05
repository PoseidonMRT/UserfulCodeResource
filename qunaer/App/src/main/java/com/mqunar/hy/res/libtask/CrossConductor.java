package com.mqunar.hy.res.libtask;

import java.util.HashMap;
import java.util.Map;

public class CrossConductor extends NetHttpConductor {

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

        public CrossConductor create(TaskCallback... taskCallbackArr) {
            CrossConductor crossConductor = new CrossConductor(taskCallbackArr);
            crossConductor.setParams(new Object[]{this.url, this.content, this.header});
            return crossConductor;
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

    public CrossConductor(TaskCallback... taskCallbackArr) {
        super(taskCallbackArr);
    }
}
