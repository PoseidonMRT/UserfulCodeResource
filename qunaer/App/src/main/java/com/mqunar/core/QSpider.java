package com.mqunar.core;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Process;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.iflytek.cloud.SpeechConstant;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.atomenv.SwitchEnv;
import com.mqunar.internal.SpiderPlatfromInfo;
import com.mqunar.json.JsonUtils;
import com.mqunar.libtask.ProxyConfig;
import com.mqunar.module.ModuleInfoController;
import com.mqunar.necro.agent.NewNecro;
import com.mqunar.necro.agent.task.IHttpSender;
import com.mqunar.spider.ModuleMonitor;
import com.mqunar.spider.QNecroSender;
import com.mqunar.splash.SplashUtils;
import com.mqunar.tools.log.QLog;
import com.qunar.avra.AVRA;
import com.qunar.avra.AVRAInstallBuilder;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.acra.ACRA;
import qunar.lego.utils.Goblin;
import qunar.lego.utils.Pitcher;

public class QSpider {
    private static boolean a;
    private static String b;
    public static String crashTouchUrl;
    public static Throwable initThrowable;
    public static volatile boolean loadDone;
    public static long startTime;

    public class EggsPushStartException extends RuntimeException {
        public EggsPushStartException(String str) {
            super(str);
        }
    }

    public static void initSpider(Context context) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        try {
            SpiderPlatfromInfo.init(context);
            if (!(SwitchEnv.getInstance().isShowNetTips() || !ACRA.init((Application) context.getApplicationContext(), getEnv(), getCParam(context.getApplicationContext())) || GlobalEnv.getInstance().isRelease())) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("data_avra", 0);
                AVRAInstallBuilder watchMainProcess = AVRA.watchMainProcess();
                if (sharedPreferences.getBoolean("avra.leak_switch", true)) {
                    watchMainProcess.detectLeakedActivity();
                }
                if (sharedPreferences.getBoolean("avra.block_switch", false)) {
                    watchMainProcess.detectBlock(1000, 2);
                }
                if (sharedPreferences.getBoolean("avra.strictmode_switch", false)) {
                    watchMainProcess.detectFileUriExposure().detectLeakedClosableObjects().detectLeakedRegistrationObjects().detectLeakedSqlLiteObjects().detectResourceMismatch();
                }
                watchMainProcess.buildAndInstall(context);
            }
        } catch (Throwable th) {
            QLog.e("init acra fail : " + th, new Object[0]);
        }
        QunarApkLoader.a(context);
        b = getCurrentProcessName(context);
        if (b == null || b.equalsIgnoreCase(context.getPackageName())) {
            ModuleMonitor.getInstance().appCreateTime = System.currentTimeMillis();
            if (!a) {
                a = true;
                loadDone = false;
                crashTouchUrl = null;
                SplashUtils.setLoadDone(false);
                SplashUtils.setCrashTouchUrl(null);
                if (VERSION.SDK_INT >= 21) {
                    try {
                        Method declaredMethod = Class.forName("android.webkit.WebViewFactory").getDeclaredMethod("getProvider", new Class[0]);
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(null, new Object[0]);
                    } catch (Throwable th2) {
                    }
                }
                QunarApkLoader.init(context);
                try {
                    GlobalEnv.getInstance().envLock();
                    ProxyConfig.getInstance().setDefaultUrl("http://client.qunar.com/pitcher-proxy");
                    Pitcher.lockUrl();
                    if (!SwitchEnv.getInstance().isShowNetTips()) {
                        NewNecro withLogEnabled = NewNecro.withAppToken("token").withPid(GlobalEnv.getInstance().getPid()).withLogEnabled(!GlobalEnv.getInstance().isRelease());
                        ReflectUtils.invokeMethod("withSender", withLogEnabled, new Class[]{IHttpSender.class}, new Object[]{new QNecroSender()});
                        withLogEnabled.start(context.getApplicationContext());
                    }
                } catch (Exception e) {
                    QLog.e("init spider fail " + e.toString(), new Object[0]);
                }
                try {
                    Goblin.loadInfo(context);
                } catch (Throwable th3) {
                    QLog.e("loadInfo fail " + th3.toString(), new Object[0]);
                }
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    ModuleInfoController.registModules(context);
                    QLog.i("initSpider", "registModules:" + (System.currentTimeMillis() - currentTimeMillis) + "ms", new Object[0]);
                    new a(context).start();
                    return;
                } catch (Throwable th32) {
                    loadDone = true;
                    SplashUtils.setLoadDone(true);
                    b(th32, context);
                    return;
                }
            }
            return;
        }
        QLog.e("found other process " + b, new Object[0]);
    }

    public static int getEnv() {
        if (GlobalEnv.getInstance().isBeta()) {
            return 2;
        }
        if (GlobalEnv.getInstance().isDev()) {
            return 3;
        }
        return 1;
    }

    private static void b(Throwable th, Context context) {
        initThrowable = th;
        crashTouchUrl = "http://touch.qunar.com?bd_source=appcrash";
        SplashUtils.setCrashTouchUrl(crashTouchUrl);
        if (GlobalEnv.getInstance().isRelease()) {
            th.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(th);
            a(context);
            return;
        }
        throw new RuntimeException(th);
    }

    private static void a(Context context) {
        ModuleInfoController.clearLocalModules(context);
    }

    public static String getCurrentProcessName(Context context) {
        if (TextUtils.isEmpty(b)) {
            String str = null;
            try {
                Class cls = Class.forName("android.ddm.DdmHandleAppName");
                str = (String) cls.getDeclaredMethod("getAppName", new Class[0]).invoke(cls, new Object[0]);
            } catch (Throwable th) {
            }
            if (TextUtils.isEmpty(str)) {
                int myPid = Process.myPid();
                for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                    if (runningAppProcessInfo.pid == myPid) {
                        str = runningAppProcessInfo.processName;
                        break;
                    }
                }
            }
            b = str;
        }
        return b;
    }

    @NonNull
    public static String getCParam(Context context) {
        try {
            HashMap hashMap = (HashMap) ReflectUtils.invokeStaticMethod("com.mqunar.libtask.HotdogConductor", "cp", new Class[]{Context.class}, new Context[]{context});
            String vid = GlobalEnv.getInstance().getVid();
            if (hashMap != null) {
                hashMap.put(SpeechConstant.ISV_VID, vid);
            }
            return JsonUtils.toJsonString(hashMap);
        } catch (Exception e) {
            return "";
        }
    }
}
