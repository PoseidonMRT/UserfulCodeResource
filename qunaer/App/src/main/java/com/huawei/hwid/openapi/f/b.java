package com.huawei.hwid.openapi.f;

import android.content.Context;
import com.huawei.hwid.openapi.quicklogin.b.b.a;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class b implements a {
    public final boolean a() {
        return false;
    }

    public HttpClient a(Context context) {
        return new DefaultHttpClient(a.a(), null);
    }
}
