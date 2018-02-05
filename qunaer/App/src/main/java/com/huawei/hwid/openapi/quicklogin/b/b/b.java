package com.huawei.hwid.openapi.quicklogin.b.b;

import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;

final class b implements ConnPerRoute {
    b() {
    }

    public int getMaxForRoute(HttpRoute httpRoute) {
        return 8;
    }
}
