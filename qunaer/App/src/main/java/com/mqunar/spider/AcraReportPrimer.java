package com.mqunar.spider;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources.NotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechConstant;
import com.mqunar.BuildConfig;
import com.mqunar.atomenv.AtomEnvConstants;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.atomenv.OwnerConstant;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.core.QInstrumentation;
import com.mqunar.core.QSpider;
import com.mqunar.core.QunarApkLoader;
import com.mqunar.core.ReflectUtils;
import com.mqunar.core.basectx.activity.Util;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.core.res.ResourcesInfo;
import com.mqunar.core.res.compat.WebViewCompat;
import com.mqunar.dlogin.login.LoginEnvironment;
import com.mqunar.dlogin.login.LoginManager;
import com.mqunar.dlogin.login.PlatformLogin;
import com.mqunar.exception.AtomFileNotFoundException;
import com.mqunar.exception.ResNotFoundException;
import com.mqunar.module.ModuleInfoController;
import com.mqunar.storage.Storage;
import com.mqunar.tools.CheckUtils;
import com.mqunar.tools.DateTimeUtils;
import com.mqunar.tools.log.QLog;
import com.mqunar.tools.log.QLog.GlobalContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.acra.ACRA;
import org.acra.builder.ReportBuilder;
import org.acra.builder.ReportPrimer;
import org.acra.ne.NativeException;
import org.apache.commons.io.IOUtils;

public class AcraReportPrimer implements ReportPrimer {
    public static final String KEY_RESTART_COUNT_BY_NOT_FOUND = "restartByFileNotFound";
    private static String c = "#0x([0-9a-fA-F]{7,8})";
    private static Pattern d = Pattern.compile(c);
    private static final ThreadLocal<String> e = new ThreadLocal();
    private Storage a;
    private Context b;

    private String a(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("app:").append(context.getClassLoader()).append(Contact.NUMBER).append("p:").append(context.getClassLoader().getParent()).append(Contact.NUMBER).append("scl:").append(ClassLoader.getSystemClassLoader()).append(Contact.NUMBER).append("p:").append(ClassLoader.getSystemClassLoader().getParent());
        try {
            ClassLoader classLoader = (ClassLoader) ReflectUtils.invokeMethod("getClassLoader", ReflectUtils.getField(((Application) context).getBaseContext(), "mPackageInfo"), new Class[0], new Object[0]);
            stringBuilder.append(Contact.NUMBER).append("pcl:").append(classLoader).append(Contact.NUMBER).append("p:").append(classLoader.getParent());
        } catch (Throwable th) {
        }
        stringBuilder.append(Contact.NUMBER).append("tcl:").append(Thread.currentThread().getContextClassLoader());
        stringBuilder.append("#ld=").append(QSpider.loadDone).append("#crach_url:").append(QSpider.crashTouchUrl);
        stringBuilder.append(Contact.NUMBER).append("fa:").append(QInstrumentation.firstActivityName);
        stringBuilder.append(Contact.NUMBER).append("ml:");
        stringBuilder.append(ModuleInfoController.modules.copyKeys());
        stringBuilder.append(Contact.NUMBER).append("ll:");
        stringBuilder.append(QunarApkLoader.getLoaderLogStr());
        stringBuilder.append("#al:");
        try {
            List<RunningTaskInfo> runningTasks = ((ActivityManager) QunarApkLoader.getAppContext().getSystemService("activity")).getRunningTasks(1);
            if (runningTasks != null) {
                for (RunningTaskInfo runningTaskInfo : runningTasks) {
                    stringBuilder.append(runningTaskInfo.baseActivity).append("|").append(runningTaskInfo.topActivity).append("|").append(runningTaskInfo.numActivities).append("|").append(runningTaskInfo.numRunning).append(",");
                }
            } else {
                stringBuilder.append("null");
            }
        } catch (Throwable th2) {
            stringBuilder.append(th2.getMessage());
        }
        return stringBuilder.toString();
    }

    private static boolean a(Throwable th) {
        if (th == null) {
            return false;
        }
        if ((th instanceof ClassNotFoundException) || (th instanceof NoClassDefFoundError)) {
            return true;
        }
        return a(th.getCause());
    }

    public void primeReport(Context context, ReportBuilder reportBuilder) {
        if (!b(context, reportBuilder.getException())) {
            Object c;
            if (a(reportBuilder.getException()) || b(reportBuilder.getException())) {
                ACRA.getErrorReporter().putCustomData("cl", a(context));
                c = c(reportBuilder.getException());
                if (!TextUtils.isEmpty(c)) {
                    ACRA.getErrorReporter().putCustomData("res_notfound", c);
                }
            }
            this.a = Storage.newStorage(context, OwnerConstant.STORAGE_OWNER_SYS);
            this.a.putInt(KEY_RESTART_COUNT_BY_NOT_FOUND, 0);
            if (TextUtils.isEmpty(ACRA.getErrorReporter().getCustomData(SpeechConstant.ISV_VID))) {
                ACRA.getErrorReporter().putCustomData(SpeechConstant.ISV_VID, this.a.getString(AtomEnvConstants.SYS_VID, "NULL"));
            }
            c = this.a.getString(AtomEnvConstants.SYS_RCID, "");
            if (!TextUtils.isEmpty(c)) {
                ACRA.getErrorReporter().putCustomData("cid", c);
            }
            if (TextUtils.isEmpty(ACRA.getErrorReporter().getCustomData(AIUIConstant.KEY_UID))) {
                ACRA.getErrorReporter().putCustomData(AIUIConstant.KEY_UID, this.a.getString(AtomEnvConstants.SYS_UID, "NULL"));
            }
            ACRA.getErrorReporter().putCustomData("atom", this.a.getString("sys_atom", "{}"));
            ACRA.getErrorReporter().putCustomData("MILESTONE", BuildConfig.MILESTONE);
            ACRA.getErrorReporter().putCustomData("webview_res", WebViewCompat.getLog());
            try {
                if (!(((reportBuilder.getException() instanceof NativeException) && ((NativeException) reportBuilder.getException()).noSendDmp) || reportBuilder.getException().getClass().getSimpleName().contains("Violation"))) {
                    String a = a(ACRA.getErrorReporter().mLastActivityManager.getLastActivity());
                    if (!"".equals(a)) {
                        ACRA.getErrorReporter().putCustomData("page", a);
                    }
                }
            } catch (Throwable th) {
                QLog.e("get page name failed :" + th.toString(), new Object[0]);
            }
            if (!GlobalEnv.getInstance().isRelease() && reportBuilder.getException() != null) {
                try {
                    a(context, reportBuilder.getException());
                } catch (Throwable th2) {
                    QLog.e("betaLogin :" + th2.toString(), new Object[0]);
                }
            }
        }
    }

    private void a(Context context, Throwable th) {
        a();
        this.b = context;
        Storage.newStorage(context, "qunar_cr").putString(b(), a(th, new Object[0]));
    }

    private void a() {
        PlatformLogin loginIMPL = LoginManager.getInstance(QApplication.getContext()).initEnv(LoginEnvironment.Release).getLoginIMPL();
        if (loginIMPL.isLogin()) {
            String qTalkId = loginIMPL.getLoginData().getQTalkId();
            String token = loginIMPL.getLoginData().getToken();
            ACRA.getErrorReporter().putCustomData("qTalkId", qTalkId);
            ACRA.getErrorReporter().putCustomData("token", token);
        }
    }

    private static boolean b(Throwable th) {
        if (th == null) {
            return false;
        }
        if (th instanceof NotFoundException) {
            return true;
        }
        if (th instanceof RuntimeException) {
            String message = th.getMessage();
            if (message != null && (message.startsWith("Font asset not found ") || message.startsWith("native typeface cannot be made"))) {
                return true;
            }
        }
        return b(th.getCause());
    }

    private static String c(Throwable th) {
        CharSequence message = th.getMessage();
        if (TextUtils.isEmpty(message) || !d.matcher(message).find()) {
            return null;
        }
        int i;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ctx:").append(QApplication.getContext()).append(",").append(QunarApkLoader.getAppContext()).append("|").append("res1:").append(QApplication.getContext().getResources()).append("|");
        try {
            List field = ReflectUtils.getField(QApplication.getContext().getClass(), "mResources");
            stringBuilder.append("fs:").append(field.size()).append("|");
            for (i = 0; i < field.size(); i++) {
                stringBuilder.append("fs[").append(i).append("]:").append(((Field) field.get(i)).get(QApplication.getContext())).append("|");
            }
        } catch (Exception e) {
            stringBuilder.append("getFieldOjbect failure...." + e).append("|");
        }
        Object baseContext = ((Application) QApplication.getContext()).getBaseContext();
        stringBuilder.append("br:").append(ReflectUtils.getField(baseContext, "mResources")).append("|");
        stringBuilder.append("pir:").append(ReflectUtils.getField(ReflectUtils.getField(baseContext, "mPackageInfo"), "mResources")).append("|").append("qr:").append(ResourcesInfo.qResources).append("|").append("qsr:").append(ResourcesInfo.sysResources).append("|").append("qcr:").append(ResourcesInfo.getCurrentResources()).append("|");
        SparseArray sparseArray = (SparseArray) ReflectUtils.invokeMethod("getAssignedPackageIdentifiers", QApplication.getContext().getAssets(), new Class[0], new Object[0]);
        if (sparseArray != null) {
            stringBuilder.append("[");
            for (i = 0; i < sparseArray.size(); i++) {
                stringBuilder.append((String) sparseArray.valueAt(i)).append(",");
            }
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }

    private static boolean b(Context context, Throwable th) {
        if (th != null) {
            if (th instanceof ResNotFoundException) {
                if (QunarApkLoader.shouldRestartWhenResNotFound(((ResNotFoundException) th).resId)) {
                    return restart2WhenFileNotFound(context);
                }
            } else if (th instanceof AtomFileNotFoundException) {
                return restart2WhenFileNotFound(context);
            } else {
                if (th.getCause() != null) {
                    return b(context, th.getCause());
                }
            }
        }
        return false;
    }

    public static boolean restart2WhenFileNotFound(Context context) {
        if (context == null) {
            return false;
        }
        Storage newStorage = Storage.newStorage(context, OwnerConstant.STORAGE_OWNER_SYS);
        int i = newStorage.getInt(KEY_RESTART_COUNT_BY_NOT_FOUND, 0);
        if (i < 0) {
            i = 0;
        }
        if (i + 1 >= 3) {
            return false;
        }
        newStorage.putInt(KEY_RESTART_COUNT_BY_NOT_FOUND, i + 1);
        Util.restart2(context);
        return true;
    }

    private static String b() {
        String str = (String) e.get();
        if (str == null) {
            return String.valueOf(System.currentTimeMillis());
        }
        e.remove();
        return str;
    }

    private String a(Throwable th, Object... objArr) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n======__=======\n");
        try {
            PackageInfo packageInfo = this.b.getPackageManager().getPackageInfo(this.b.getPackageName(), 0);
            stringBuilder.append(packageInfo.packageName);
            stringBuilder.append("-");
            stringBuilder.append(DateTimeUtils.printCalendarByPattern(DateTimeUtils.getCurrentDateTime(), DateTimeUtils.yyyy_MM_dd_HH_mm_ss));
            stringBuilder.append("-");
            stringBuilder.append(packageInfo.versionCode);
            stringBuilder.append("-");
            stringBuilder.append(packageInfo.versionName);
            stringBuilder.append("-");
        } catch (Throwable th2) {
            stringBuilder.append("packageInfoError-");
            stringBuilder.append(th2.getMessage());
            stringBuilder.append('-');
        }
        try {
            stringBuilder.append(this.a.getString(AtomEnvConstants.SYS_PID, "pid"));
            stringBuilder.append("-");
            stringBuilder.append(this.a.getString(AtomEnvConstants.SYS_VID, SpeechConstant.ISV_VID));
            stringBuilder.append("-");
            stringBuilder.append(this.a.getString(AtomEnvConstants.SYS_RCID, "cid"));
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuilder.append("imei:");
            stringBuilder.append(this.a.getString(AtomEnvConstants.SYS_UID, AIUIConstant.KEY_UID));
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
        } catch (Throwable th22) {
            stringBuilder.append("platformInfoError-");
            stringBuilder.append(th22.getMessage());
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        try {
            String str = (String) Class.forName("com.mqunar.BuildConfig").getDeclaredField("MILESTONE").get(null);
            if (!CheckUtils.isEmpty(str)) {
                stringBuilder.append("milestone:");
                stringBuilder.append(str);
                stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
            }
        } catch (Throwable th3) {
            stringBuilder.append("milestone:");
            stringBuilder.append("notfound");
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        try {
            stringBuilder.append("atom:");
            stringBuilder.append(this.a.getString("sys_atom", "{}"));
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
        } catch (Throwable th4) {
            stringBuilder.append("atom:");
            stringBuilder.append(ParamStr.RET_RES_ERROR);
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        try {
            Locale locale = this.b.getResources().getConfiguration().locale;
            for (Field field : Build.class.getDeclaredFields()) {
                field.setAccessible(true);
                stringBuilder.append(field.getName().toLowerCase(locale));
                stringBuilder.append("=");
                stringBuilder.append(field.get(null).toString());
                stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
            }
            Field[] declaredFields = VERSION.class.getDeclaredFields();
            int length = declaredFields.length;
            while (i < length) {
                Field field2 = declaredFields[i];
                field2.setAccessible(true);
                stringBuilder.append(field2.getName().toLowerCase(locale));
                stringBuilder.append("=");
                stringBuilder.append(field2.get(null).toString());
                stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
                i++;
            }
            stringBuilder.append("country=").append(locale.getCountry()).append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuilder.append("language=").append(locale.getLanguage()).append(IOUtils.LINE_SEPARATOR_UNIX);
        } catch (Throwable th5) {
        }
        try {
            stringBuilder.append("process=").append(QSpider.getCurrentProcessName(GlobalContext.getAppContext())).append(IOUtils.LINE_SEPARATOR_UNIX);
        } catch (Exception e) {
        }
        try {
            stringBuilder.append(Log.getStackTraceString(th));
        } catch (Throwable th6) {
        }
        return stringBuilder.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String a(@android.support.annotation.Nullable android.app.Activity r7) {
        /*
        r6 = this;
        if (r7 != 0) goto L_0x0005;
    L_0x0002:
        r1 = "";
    L_0x0004:
        return r1;
    L_0x0005:
        r0 = r7.getClass();
        r1 = r0.getSimpleName();
        r0 = "com.mqunar.core.basectx.launcherfragment.TransparentFragmentActivityBase";
        r0 = java.lang.Class.forName(r0);	 Catch:{ Throwable -> 0x009e }
        r2 = "com.mqunar.core.basectx.launcherfragment.LauncherFragmentActivityBase";
        r2 = java.lang.Class.forName(r2);	 Catch:{ Throwable -> 0x009e }
        r3 = r7.getClass();	 Catch:{ Throwable -> 0x009e }
        r3 = r0.isAssignableFrom(r3);	 Catch:{ Throwable -> 0x009e }
        if (r3 == 0) goto L_0x007c;
    L_0x0023:
        r2 = "_fragmentName";
        r0 = r0.getDeclaredField(r2);	 Catch:{ Throwable -> 0x009e }
        r2 = 1;
        r0.setAccessible(r2);	 Catch:{ Throwable -> 0x009e }
        r0 = r0.get(r7);	 Catch:{ Throwable -> 0x009e }
        r0 = (java.lang.String) r0;	 Catch:{ Throwable -> 0x009e }
        r2 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Throwable -> 0x009e }
        if (r2 != 0) goto L_0x009c;
    L_0x0039:
        r1 = r7.getClass();
        r1 = r6.a(r1);
        r2 = android.text.TextUtils.isEmpty(r1);
        if (r2 == 0) goto L_0x00ac;
    L_0x0047:
        r2 = ".";
        r2 = r0.indexOf(r2);
        if (r2 <= 0) goto L_0x00ac;
    L_0x004f:
        r2 = java.lang.Class.forName(r0);	 Catch:{ Throwable -> 0x00a3 }
        r1 = r6.a(r2);	 Catch:{ Throwable -> 0x00a3 }
        r0 = r2.getSimpleName();	 Catch:{ Throwable -> 0x00a3 }
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x005e:
        r2 = android.text.TextUtils.isEmpty(r0);
        if (r2 != 0) goto L_0x0004;
    L_0x0064:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r2.append(r0);
        r2 = ".";
        r0 = r0.append(r2);
        r0 = r0.append(r1);
        r1 = r0.toString();
        goto L_0x0004;
    L_0x007c:
        r0 = r7.getClass();	 Catch:{ Throwable -> 0x009e }
        r0 = r2.isAssignableFrom(r0);	 Catch:{ Throwable -> 0x009e }
        if (r0 == 0) goto L_0x009c;
    L_0x0086:
        r0 = "_fragmentName";
        r0 = r2.getDeclaredField(r0);	 Catch:{ Throwable -> 0x009e }
        r2 = 1;
        r0.setAccessible(r2);	 Catch:{ Throwable -> 0x009e }
        r0 = r0.get(r7);	 Catch:{ Throwable -> 0x009e }
        r0 = (java.lang.String) r0;	 Catch:{ Throwable -> 0x009e }
        r2 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Throwable -> 0x009e }
        if (r2 == 0) goto L_0x0039;
    L_0x009c:
        r0 = r1;
        goto L_0x0039;
    L_0x009e:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x009c;
    L_0x00a3:
        r2 = move-exception;
        r3 = "found class crash";
        r4 = 0;
        r4 = new java.lang.Object[r4];
        com.mqunar.tools.log.QLog.e(r2, r3, r4);
    L_0x00ac:
        r5 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x005e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mqunar.spider.AcraReportPrimer.a(android.app.Activity):java.lang.String");
    }

    private String a(Class cls) {
        try {
            Class cls2 = Class.forName("com.mqunar.core.QunarApkLoader");
            Method declaredMethod = cls2.getDeclaredMethod("getPkgName", new Class[]{String.class});
            if (declaredMethod == null) {
                declaredMethod = cls2.getDeclaredMethod("getPackageName", new Class[]{String.class});
            }
            String str = (String) declaredMethod.invoke(null, new Object[]{cls.getName()});
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            return str.replace("com.mqunar.", "").replace("atom.", "");
        } catch (Throwable th) {
            QLog.e("getAtomName failed: " + th, new Object[0]);
            return null;
        }
    }
}
