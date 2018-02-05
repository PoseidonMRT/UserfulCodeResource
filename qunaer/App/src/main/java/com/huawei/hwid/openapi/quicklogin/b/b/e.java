package com.huawei.hwid.openapi.quicklogin.b.b;

import android.content.Context;
import android.os.Bundle;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.out.OutReturn.Ret_code;
import com.huawei.hwid.openapi.quicklogin.a.a;
import com.huawei.hwid.openapi.quicklogin.b.c;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import java.io.UnsupportedEncodingException;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.http.HttpResponse;

class e extends Thread {
    private static String d = a.b;
    Context a = null;
    c b = null;
    com.huawei.hwid.openapi.quicklogin.b.b.a.a c = null;

    e(Context context, c cVar, com.huawei.hwid.openapi.quicklogin.b.b.a.a aVar) {
        this.a = context;
        this.b = cVar;
        this.c = aVar;
    }

    public void run() {
        Bundle bundle = null;
        int i = 0;
        while (i < 3) {
            d.a(d, "access UP: times=" + (i + 1));
            try {
                HttpResponse a = com.huawei.hwid.openapi.quicklogin.b.b.b.a.a(this.a, this.b.d());
                int statusCode = a.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    bundle = com.huawei.hwid.openapi.quicklogin.b.b.c.a.a(Ret_code.ERR_UP_400_FAILED, "http error:" + statusCode);
                    i++;
                } else {
                    bundle = this.b.a(a);
                    if (this.b.a(bundle)) {
                        bundle.putInt(ParamStr.RET_CODE, 1);
                    } else {
                        bundle.putInt(ParamStr.RET_CODE, 1000);
                    }
                    d.a(d, "return info:" + k.a(bundle.getString(ParamStr.RET_RES_CONTENT), true));
                    this.c.a(this.b, bundle);
                    return;
                }
            } catch (RuntimeException e) {
                bundle = a(e);
            } catch (Exception e2) {
                bundle = a(e2);
                d.d(d, "up failed==" + k.a(bundle));
            }
        }
        this.c.b(this.b, bundle);
    }

    private Bundle a(RuntimeException runtimeException) {
        Bundle a;
        d.b(d, runtimeException.toString(), runtimeException);
        if ((runtimeException instanceof IllegalArgumentException) || (runtimeException instanceof IllegalStateException)) {
            a = com.huawei.hwid.openapi.quicklogin.b.b.c.a.a(1001, runtimeException.toString());
        } else {
            a = com.huawei.hwid.openapi.quicklogin.b.b.c.a.a(1003, runtimeException.toString());
        }
        d.d(d, "up failed==" + k.a(a));
        return a;
    }

    private Bundle a(Exception exception) {
        d.b(d, exception.getMessage(), exception);
        if ((exception instanceof UnsupportedEncodingException) || (exception instanceof IllegalArgumentException) || (exception instanceof IllegalStateException)) {
            return com.huawei.hwid.openapi.quicklogin.b.b.c.a.a(1001, exception.toString());
        }
        if ((exception instanceof SSLPeerUnverifiedException) || (exception instanceof SSLException) || (exception instanceof SSLHandshakeException)) {
            return com.huawei.hwid.openapi.quicklogin.b.b.c.a.a(1002, exception.toString());
        }
        return com.huawei.hwid.openapi.quicklogin.b.b.c.a.a(1003, exception.toString());
    }
}
