package com.baidu.platform.base;

import com.baidu.mapapi.http.HttpClient.HttpStateError;
import com.baidu.mapapi.http.HttpClient.ProtoResultCallback;

class b extends ProtoResultCallback {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public void onFailed(HttpStateError httpStateError) {
        this.a.d = "{SDK_InnerError:{httpStateError:" + httpStateError + "}}";
        if (this.a.a()) {
            this.a.a.a(this.a.d);
        } else {
            this.a.c.post(new d(this));
        }
    }

    public void onSuccess(String str) {
        this.a.d = str;
        if (!this.a.a()) {
            this.a.c.post(new c(this));
        } else if (this.a.a != null) {
            this.a.a.a(this.a.d);
        }
    }
}
