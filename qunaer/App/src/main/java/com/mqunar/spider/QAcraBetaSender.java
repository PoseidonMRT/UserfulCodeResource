package com.mqunar.spider;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.aiui.AIUIConstant;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.dlogin.login.LoginEnvironment;
import com.mqunar.dlogin.login.LoginManager;
import com.mqunar.dlogin.login.PlatformLogin;
import com.mqunar.tools.log.QLog;
import java.io.File;
import java.lang.reflect.Method;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.sender.QSenderProxy;
import org.acra.sender.ReportSender;

public class QAcraBetaSender implements ReportSender {
    private Context a;

    public void send(@NonNull Context context, @NonNull CrashReportData crashReportData, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull File file, @NonNull String str, int i) {
        this.a = context;
        try {
            if (!a().getBoolean("avra.temp_not_login", false)) {
                a(str);
            }
        } catch (Throwable th) {
            QLog.e(th.toString(), new Object[0]);
        }
        new QSenderProxy("http://mloganalysts.corp.qunar.com/api/log/clientError", "http://client.qunar.com/pitcher-proxy").send(context, crashReportData, aCRAConfiguration, file, str, i);
    }

    private void a(@NonNull String str) {
        PlatformLogin loginIMPL = LoginManager.getInstance(QApplication.getContext()).initEnv(LoginEnvironment.Release).getLoginIMPL();
        if (!loginIMPL.isLogin()) {
            new Handler(Looper.getMainLooper()).postAtFrontOfQueue(new f(this, loginIMPL, new d(this, loginIMPL, str)));
        }
    }

    private SharedPreferences a() {
        if (this.a != null) {
            return this.a.getSharedPreferences("data_avra", 4);
        }
        return null;
    }

    @TargetApi(19)
    private boolean a(Context context) {
        try {
            Object systemService = context.getSystemService("appops");
            if (systemService == null) {
                return false;
            }
            Method method = systemService.getClass().getMethod("checkOp", new Class[]{Integer.TYPE, Integer.TYPE, String.class});
            if (method == null) {
                return false;
            }
            return ((Integer) method.invoke(systemService, new Object[]{Integer.valueOf(24), Integer.valueOf(Binder.getCallingUid()), context.getPackageName()})).intValue() == 0;
        } catch (Exception e) {
            QLog.e("getAppOps :" + e.toString(), new Object[0]);
            return false;
        }
    }

    private String a(String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("qTalkId", (Object) str);
        jSONObject.put("token", (Object) str2);
        jSONObject.put(AIUIConstant.KEY_UID, GlobalEnv.getInstance().getUid());
        return JSON.toJSONString(jSONObject);
    }
}
