package com.mqunar.libtask;

import com.mqunar.network.NetResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrossConductor extends o {

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

        public CrossConductor create(TaskCallback... taskCallbackArr) {
            CrossConductor crossConductor = new CrossConductor(taskCallbackArr);
            Object[] objArr = new Object[3];
            objArr[0] = this.a;
            objArr[1] = this.c == null ? this.b : this.c;
            objArr[2] = this.d;
            crossConductor.setParams(objArr);
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

    public CrossConductor(TaskCallback... taskCallbackArr) {
        super(taskCallbackArr);
    }
}
