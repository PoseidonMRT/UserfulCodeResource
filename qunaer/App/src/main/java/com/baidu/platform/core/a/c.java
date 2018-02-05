package com.baidu.platform.core.a;

import com.baidu.platform.base.g;
import com.baidu.platform.domain.b;
import com.iflytek.aiui.AIUIConstant;

public class c extends g {
    public c(String str) {
        a(str);
    }

    private void a(String str) {
        this.a.a("qt", "ext");
        this.a.a("num", "1000");
        this.a.a("l", "10");
        this.a.a("ie", "utf-8");
        this.a.a("oue", "1");
        this.a.a("res", "api");
        this.a.a("fromproduct", "android_map_sdk");
        this.a.a(AIUIConstant.KEY_UID, str);
    }

    public String a(b bVar) {
        return bVar.o();
    }
}
