package com.mqunar.qutui;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Looper;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechConstant;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.cock.utils.AndroidUtils;
import com.mqunar.cock.utils.ReflectUtils;
import com.mqunar.qutui.model.Caf;
import com.mqunar.qutui.model.LogModel;
import com.mqunar.tools.ArrayUtils;
import com.mqunar.tools.DateTimeUtils;
import com.mqunar.tools.log.QLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import qunar.lego.utils.FormPart;
import qunar.lego.utils.Goblin;
import qunar.lego.utils.HttpHeader;
import qunar.lego.utils.Pitcher;
import qunar.lego.utils.PitcherResponse;

public class QutuiLog {
    static List<String> a = new ArrayList();
    private static String e;
    private static String f;
    private static long g;
    private static QutuiLog h;
    private static m i;
    private static long j;
    private static List<Caf> k = new ArrayList();
    private static Map<String, PendingIntent> m = new HashMap();
    String b;
    String c;
    String d;
    private Context l;

    static {
        e = "http://miniclient.qunar.com/pitcher-proxy";
        f = "http://mwhale.corp.qunar.com/alive";
        g = DateTimeUtils.ONE_DAY;
        try {
            if (!GlobalEnv.getInstance().isRelease()) {
                e = "http://front.pitcher.beta.qunar.com/pitcher-proxy";
                f = "http://l-client4.wap.beta.cn0.qunar.com:9088/alive";
                g = 300000;
            }
        } catch (Exception e) {
        }
    }

    public void setIds(String str, String str2, String str3) {
        this.b = str;
        this.c = str2;
        this.d = str3;
    }

    private QutuiLog(Context context) {
        if (i == null) {
            i = m.a(context);
        }
        this.l = getSafeContext(context);
        try {
            setIds(GlobalEnv.getInstance().getPid(), GlobalEnv.getInstance().getCid(), GlobalEnv.getInstance().getVid());
        } catch (Throwable e) {
            QLog.e(e);
        }
    }

    public static Context getSafeContext(Context context) {
        if (context == null) {
            throw new NullPointerException("context is empty!");
        } else if (context instanceof Application) {
            return context;
        } else {
            Context applicationContext = context.getApplicationContext();
            if (applicationContext != null) {
                return applicationContext;
            }
            return context;
        }
    }

    public static QutuiLog getInstance(Context context) {
        if (h == null) {
            synchronized (QutuiLog.class) {
                if (h == null) {
                    h = new QutuiLog(context);
                }
            }
        }
        return h;
    }

    static boolean a(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private String e() {
        Object invokeStaticMethod = ReflectUtils.invokeStaticMethod("com.mqunar.libtask.HotdogConductor", "cp", new Class[]{Context.class}, new Object[]{this.l});
        JSONObject jSONObject;
        if (invokeStaticMethod != null && (invokeStaticMethod instanceof HashMap)) {
            HashMap hashMap = (HashMap) invokeStaticMethod;
            jSONObject = new JSONObject(hashMap);
            try {
                if (!hashMap.containsKey(SpeechConstant.ISV_VID)) {
                    jSONObject.put(SpeechConstant.ISV_VID, this.d);
                }
                jSONObject.put("ke", System.currentTimeMillis());
                jSONObject.put("v", Goblin.version());
            } catch (Throwable e) {
                QLog.e(e);
            }
            return jSONObject.toString();
        } else if (TextUtils.isEmpty(this.b)) {
            throw new RuntimeException("pid 不能为空");
        } else {
            jSONObject = new JSONObject();
            try {
                String packageName = this.l.getPackageName();
                PackageInfo packageInfo = this.l.getPackageManager().getPackageInfo(packageName, 0);
                jSONObject.put(SpeechConstant.ISV_VID, this.d);
                jSONObject.put("vname", packageInfo.versionName);
                if (TextUtils.isEmpty(this.c)) {
                    jSONObject.put("cid", this.c);
                }
                jSONObject.put(AIUIConstant.KEY_UID, AndroidUtils.getIMEI(this.l));
                jSONObject.put("model", Build.MODEL);
                jSONObject.put("osVersion", VERSION.RELEASE + "_" + VERSION.SDK_INT);
                jSONObject.put("pkg", packageName);
                jSONObject.put("pid", this.b);
                jSONObject.put("ma", AndroidUtils.getMac());
                jSONObject.put("adid", AndroidUtils.getADID(this.l));
                jSONObject.put("nt", AndroidUtils.getApnName(this.l));
                jSONObject.put("mno", AndroidUtils.getSimOperator(this.l));
                jSONObject.put("tsv", String.valueOf(this.l.getApplicationInfo().targetSdkVersion));
                jSONObject.put("ke", String.valueOf(System.currentTimeMillis()));
            } catch (Throwable e2) {
                QLog.e(e2, e2.getMessage(), new Object[0]);
            }
            return jSONObject.toString();
        }
    }

    public void sendLog(String str) {
        Throwable th;
        if (a(this.l) && !TextUtils.isEmpty(str)) {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                throw new RuntimeException("Count sendLog on Main Thread!");
            } else if (!a.contains(str)) {
                a.add(str);
                ArrayList arrayList = new ArrayList();
                String e = e();
                FormPart formPart = new FormPart("c", e);
                formPart.addHeader("X-ClientEncoding", "none");
                arrayList.add(formPart);
                formPart = new FormPart("log", str);
                formPart.addHeader("X-ClientEncoding", "none");
                arrayList.add(formPart);
                QLog.i("request url = " + f + "/uploadLog" + ", cparam = " + e + ", content = " + str, new Object[0]);
                PitcherResponse request = a(f + "/uploadLog", arrayList).request();
                if (request.e != null) {
                    QLog.e(request.e, "send error, response.respcode=" + request.respcode, new Object[0]);
                    i.a(String.valueOf(((LogModel) JSON.parseObject(str, LogModel.class)).startTime), str);
                } else if (request.respcode > HttpStatus.SC_BAD_REQUEST) {
                    QLog.e("send fail, response.respcode=" + request.respcode, new Object[0]);
                    i.a(String.valueOf(((LogModel) JSON.parseObject(str, LogModel.class)).startTime), str);
                } else {
                    String str2 = null;
                    if (request.content != null) {
                        try {
                            e = new String(request.content, "utf-8");
                            try {
                                if (!(TextUtils.isEmpty(e) || new JSONObject(e).optInt("status", -1) == 0)) {
                                    i.a(String.valueOf(((LogModel) JSON.parseObject(str, LogModel.class)).startTime), str);
                                }
                                str2 = e;
                            } catch (Throwable e2) {
                                Throwable th2 = e2;
                                str2 = e;
                                th = th2;
                                QLog.e(th);
                                QLog.i("request url = " + f + "/uploadLog" + ", respcode = [" + request.respcode + "], response = [" + str2 + "]", new Object[0]);
                                a.remove(str);
                            }
                        } catch (Exception e3) {
                            th = e3;
                            QLog.e(th);
                            QLog.i("request url = " + f + "/uploadLog" + ", respcode = [" + request.respcode + "], response = [" + str2 + "]", new Object[0]);
                            a.remove(str);
                        }
                    }
                    QLog.i("request url = " + f + "/uploadLog" + ", respcode = [" + request.respcode + "], response = [" + str2 + "]", new Object[0]);
                }
                a.remove(str);
            }
        }
    }

    private Pitcher a(String str, ArrayList<FormPart> arrayList) {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("X-ClientEncoding", "none");
        httpHeader.addHeader("qrid", System.currentTimeMillis() + "");
        Pitcher pitcher = new Pitcher(this.l, str, (List) arrayList, httpHeader);
        pitcher.setProxyUrl(e);
        return pitcher;
    }

    List<Caf> a() {
        if (System.currentTimeMillis() - j > g) {
            new Thread(new k(this)).start();
        }
        return k;
    }

    private void f() {
        if (!ArrayUtils.isEmpty(k)) {
            if (m != null && m.size() > 0) {
                for (Entry entry : m.entrySet()) {
                    AlarmManager alarmManager = (AlarmManager) this.l.getSystemService("alarm");
                    if (alarmManager != null) {
                        alarmManager.cancel((PendingIntent) entry.getValue());
                    }
                    m.remove(entry.getKey());
                }
            }
            List arrayList = new ArrayList();
            Map hashMap = new HashMap();
            for (Caf caf : k) {
                PackageInfo a = i.a(this.l, caf.packageName);
                if (a != null) {
                    arrayList.add(caf);
                    QLog.i("find app : " + caf.packageName, new Object[0]);
                }
                hashMap.put(caf.packageName, Boolean.valueOf(a != null));
            }
            new Thread(new l(this, arrayList, hashMap)).start();
        }
    }

    void a(String str, boolean z) {
        i.a(str, z);
    }

    void a(Map<String, Boolean> map, Map<String, Boolean> map2) {
        i.a((Map) map, (Map) map2);
    }
}
