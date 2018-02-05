package qunar.lego.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechConstant;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Goblin {
    private static final Object javaLock = new Object();
    private static String javaString;
    private static String nativeJson;
    private static final Object nativeJsonLock = new Object();
    private static String ua;

    final class AnonymousClass1 implements Runnable {
        final /* synthetic */ Context val$context;

        AnonymousClass1(Context context) {
            this.val$context = context;
        }

        public void run() {
            String str = null;
            try {
                str = Goblin.getJavaInfo(this.val$context);
                if (str == null) {
                    str = "";
                }
                synchronized (Goblin.javaLock) {
                    Goblin.javaString = str;
                    Goblin.javaLock.notify();
                }
            } catch (Throwable th) {
                if (str == null) {
                    str = "";
                }
                synchronized (Goblin.javaLock) {
                    Goblin.javaString = str;
                    Goblin.javaLock.notify();
                }
            }
        }
    }

    public static native String SHR();

    public static native String d(String str, String str2);

    public static native String dPoll(String str);

    public static native byte[] da(byte[] bArr);

    public static native String dn(byte[] bArr, String str);

    public static native byte[] dn1(byte[] bArr, String str);

    public static native byte[] drift(byte[] bArr);

    public static native String duch(String str);

    public static native String e(String str, String str2);

    public static native String ePoll(String str);

    public static native byte[] ea(byte[] bArr);

    public static native byte[] eg(byte[] bArr);

    public static native String es(String str);

    public static native String espirt(String str);

    public static native int getCrc32(String str);

    public static native String getInfoNative();

    public static native String getPayKey();

    public static native String report(String str);

    public static native String roller(String str);

    public static native byte[] sand(byte[] bArr);

    public static native String ve(String str);

    public static native String version();

    static {
        try {
            System.loadLibrary("goblin_3_1_7");
        } catch (UnsatisfiedLinkError e) {
            Log.d("JNI", "failed");
        }
    }

    public static String getNativeJson() {
        String str;
        synchronized (nativeJsonLock) {
            if (nativeJson == null) {
                try {
                    nativeJsonLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (nativeJson == null) {
                str = "";
            } else {
                str = nativeJson;
            }
        }
        return str;
    }

    private static String getJavaString(int i) {
        String str;
        synchronized (javaLock) {
            if (javaString == null && i == 1) {
                try {
                    javaLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (javaString == null) {
                str = "";
            } else {
                str = javaString;
            }
        }
        return str;
    }

    @TargetApi(11)
    public static void loadInfo(Context context) {
        try {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new AnonymousClass1(context));
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                public void run() {
                    String infoNative = Goblin.getInfoNative();
                    synchronized (Goblin.nativeJsonLock) {
                        Goblin.nativeJson = infoNative;
                        Goblin.nativeJsonLock.notify();
                    }
                }
            });
        } catch (Throwable th) {
        }
    }

    @TargetApi(11)
    private static String getJavaInfo(Context context) {
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        try {
            for (PackageInfo packageInfo : context.getApplicationContext().getPackageManager().getInstalledPackages(0)) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("pn", packageInfo.packageName);
                jSONObject2.put("vc", packageInfo.versionCode);
                jSONObject2.put("lt", packageInfo.lastUpdateTime);
                jSONObject2.put("ft", packageInfo.firstInstallTime);
                jSONObject2.put("il", packageInfo.applicationInfo.sourceDir);
                jSONObject2.put("vn", packageInfo.versionName);
                jSONObject2.put("f", packageInfo.applicationInfo.flags);
                jSONArray.put(jSONObject2);
            }
        } catch (Throwable th) {
        }
        try {
            jSONObject.put("apps", jSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ua = getCurrentUserAgent(context);
            jSONObject.put("ua", ua);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            jSONObject.put("gid", GetInfoUtils.getInstance().getGid());
            jSONObject.put(SpeechConstant.IST_SESSION_ID, GetInfoUtils.getInstance().getSid());
            jSONObject.put("pid", GetInfoUtils.getInstance().getPid());
            jSONObject.put(SpeechConstant.ISV_VID, GetInfoUtils.getInstance().getVid());
            jSONObject.put("cid", GetInfoUtils.getInstance().getCid());
            jSONObject.put(AIUIConstant.KEY_UID, GetInfoUtils.getInstance().getUid());
            jSONObject.put("usid", GetInfoUtils.getInstance().getUSid());
            jSONObject.put("adid", GetInfoUtils.getInstance().getADID(context));
            jSONObject.put("apn", GetInfoUtils.getInstance().getApn(context));
            jSONObject.put("bat", GetInfoUtils.getInstance().getBattery(context));
            jSONObject.put("build", GetInfoUtils.getInstance().getBuild());
            jSONObject.put("imsi", GetInfoUtils.getInstance().getImsi(context));
            jSONObject.put("ip", GetInfoUtils.getInstance().getIp());
            jSONObject.put("loc", GetInfoUtils.getInstance().getLocation());
            jSONObject.put("mac", GetInfoUtils.getInstance().getMacAddress());
            jSONObject.put("mno", GetInfoUtils.getInstance().getMno(context));
            jSONObject.put("model", GetInfoUtils.getInstance().getModel());
            jSONObject.put("network", GetInfoUtils.getInstance().getNetwork(context));
            jSONObject.put("osVersion", GetInfoUtils.getInstance().getOsVersion());
            jSONObject.put("platform", "adr");
            jSONObject.put("ssn", GetInfoUtils.getInstance().getSsn(context));
            jSONObject.put("wh", GetInfoUtils.getInstance().getWh(context));
            jSONObject.put(SpeechConstant.VOLUME, GetInfoUtils.getInstance().getVolume(context));
            jSONObject.put("wifi", GetInfoUtils.getInstance().getLinkedWifi(context));
            jSONObject.put("bsid", GetInfoUtils.getInstance().getBaseStationId(context));
            jSONObject.put("cpu", GetInfoUtils.getInstance().getCpuName());
            jSONObject.put("image", GetInfoUtils.getInstance().getImageCount(context));
            jSONObject.put("tel", GetInfoUtils.getInstance().getTel(context));
            jSONObject.put("wifis", GetInfoUtils.getInstance().getWifiList(context, 10));
            jSONObject.put("gyroscope", GetInfoUtils.getInstance().getGyroscopeSensorData(context));
            jSONObject.put("light", GetInfoUtils.getInstance().getLightSensorData(context));
            jSONObject.put("orientation", GetInfoUtils.getInstance().getOrientationSensorData(context));
            return jSONObject.toString();
        } catch (Throwable th2) {
            ACRAErrorReporterBridge.getInstance().handleSilentException(th2);
            return "{}";
        }
    }

    public static String getRiskControlInfo(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("gid", GetInfoUtils.getInstance().getGid());
            jSONObject.put(SpeechConstant.IST_SESSION_ID, GetInfoUtils.getInstance().getSid());
            jSONObject.put("pid", GetInfoUtils.getInstance().getPid());
            jSONObject.put(SpeechConstant.ISV_VID, GetInfoUtils.getInstance().getVid());
            jSONObject.put("cid", GetInfoUtils.getInstance().getCid());
            jSONObject.put(AIUIConstant.KEY_UID, GetInfoUtils.getInstance().getUid());
            jSONObject.put("usid", GetInfoUtils.getInstance().getUSid());
            jSONObject.put("apn", GetInfoUtils.getInstance().getApn(context));
            jSONObject.put("bat", GetInfoUtils.getInstance().getBattery(context));
            jSONObject.put("imsi", GetInfoUtils.getInstance().getImsi(context));
            jSONObject.put("ip", GetInfoUtils.getInstance().getIp());
            jSONObject.put("loc", GetInfoUtils.getInstance().getLocation());
            jSONObject.put("mac", GetInfoUtils.getInstance().getMacAddress());
            jSONObject.put("mno", GetInfoUtils.getInstance().getMno(context));
            jSONObject.put("model", GetInfoUtils.getInstance().getModel());
            jSONObject.put("network", GetInfoUtils.getInstance().getNetwork(context));
            jSONObject.put("osVersion", GetInfoUtils.getInstance().getOsVersion());
            jSONObject.put("platform", "adr");
            jSONObject.put("wh", GetInfoUtils.getInstance().getWh(context));
            jSONObject.put(SpeechConstant.VOLUME, GetInfoUtils.getInstance().getVolume(context));
            jSONObject.put("wifi", GetInfoUtils.getInstance().getLinkedWifi(context));
            jSONObject.put("bsid", GetInfoUtils.getInstance().getBaseStationId(context));
            jSONObject.put("cpu", GetInfoUtils.getInstance().getCpuName());
            jSONObject.put("image", GetInfoUtils.getInstance().getImageCount(context));
            jSONObject.put("tel", GetInfoUtils.getInstance().getTel(context));
            jSONObject.put("wifis", GetInfoUtils.getInstance().getWifiList(context, 5));
            jSONObject.put("gyroscope", GetInfoUtils.getInstance().getGyroscopeSensorData(context));
            jSONObject.put("light", GetInfoUtils.getInstance().getLightSensorData(context));
            jSONObject.put("orientation", GetInfoUtils.getInstance().getOrientationSensorData(context));
            return jSONObject.toString();
        } catch (Throwable th) {
            ACRAErrorReporterBridge.getInstance().handleSilentException(th);
            return "{}";
        }
    }

    private static String getCurrentUserAgent(Context context) {
        String country;
        Locale locale = Locale.getDefault();
        StringBuilder stringBuilder = new StringBuilder();
        String str = VERSION.RELEASE;
        if (str.length() > 0) {
            stringBuilder.append(str);
        } else {
            stringBuilder.append("1.0");
        }
        stringBuilder.append("; ");
        str = locale.getLanguage();
        if (str != null) {
            stringBuilder.append(str.toLowerCase());
            country = locale.getCountry();
            if (country != null) {
                stringBuilder.append("-");
                stringBuilder.append(country.toLowerCase());
            }
        } else {
            stringBuilder.append("en");
        }
        if ("REL".equals(VERSION.CODENAME)) {
            country = Build.MODEL;
            if (country.length() > 0) {
                stringBuilder.append("; ");
                stringBuilder.append(country);
            }
        }
        country = Build.ID;
        if (country.length() > 0) {
            stringBuilder.append(" Build/");
            stringBuilder.append(country);
        }
        Resources resources = context.getResources();
        return String.format(resources.getText(resources.getIdentifier("android:string/web_user_agent", "string", "android")).toString(), new Object[]{stringBuilder, "Mobile "});
    }
}
