package com.mqunar.spider;

import android.content.SharedPreferences;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.dlogin.login.ILogin;
import com.mqunar.dlogin.login.ILogin.LoginCallback;
import com.mqunar.dlogin.login.PlatformLogin;
import com.mqunar.dlogin.login.PlatformLogin.LoginData;
import com.mqunar.dlogin.login.PlatformLogin.LoginError;
import com.mqunar.tools.log.QLog;
import java.util.ArrayList;
import java.util.List;
import qunar.lego.utils.FormPart;
import qunar.lego.utils.HttpHeader;
import qunar.lego.utils.Pitcher;

class d implements LoginCallback<LoginData, LoginError> {
    final /* synthetic */ PlatformLogin a;
    final /* synthetic */ String b;
    final /* synthetic */ QAcraBetaSender c;

    d(QAcraBetaSender qAcraBetaSender, PlatformLogin platformLogin, String str) {
        this.c = qAcraBetaSender;
        this.a = platformLogin;
        this.b = str;
    }

    public /* synthetic */ void onFailure(ILogin.LoginError loginError) {
        a((LoginError) loginError);
    }

    public /* synthetic */ void onSuccess(ILogin.LoginData loginData) {
        a((LoginData) loginData);
    }

    public void a(LoginData loginData) {
        List arrayList = new ArrayList();
        FormPart formPart = new FormPart("beta_login", this.c.a(this.a.getLoginData().getQTalkId(), loginData.getToken()));
        formPart.addHeader("X-ClientEncoding", "none");
        arrayList.add(formPart);
        FormPart formPart2 = new FormPart("c", this.b);
        formPart2.addHeader("X-ClientEncoding", "none");
        arrayList.add(formPart2);
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("X-ClientEncoding", "none");
        Pitcher pitcher = new Pitcher(QApplication.getContext(), "http://mloganalysts.corp.qunar.com/api/errorLog/betaLogin", arrayList, httpHeader);
        pitcher.setProxyUrl("http://client.qunar.com/pitcher-proxy");
        new e(this, pitcher).start();
    }

    public void a(LoginError loginError) {
    }

    public void onCancel() {
        QLog.d("暂不登录！！！", new Object[0]);
        SharedPreferences a = this.c.a();
        if (a != null && !a.getBoolean("avra.temp_not_login", false)) {
            a.edit().putBoolean("avra.temp_not_login", true).apply();
        }
    }
}
