package com.mqunar.hy.res.utils;

import android.content.Context;
import com.alibaba.fastjson.JSONObject;
import com.mqunar.hy.res.HyResInitializer;
import com.mqunar.hy.res.logger.Timber;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import qunar.sdk.mapapi.utils.MapConstant.QUNAR_GPS_FIELD;

public class StatisticsUtil {
    public static void qpPatchSuccess(String str, String str2, String str3, String str4) {
        qpPatch("1", str, str2, str3, str4);
    }

    public static void qpPatchError(String str, String str2, String str3, String str4) {
        qpPatch("0", str, str2, str3, str4);
    }

    public static void qpPatch(String str, String str2, String str3, String str4, String str5) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("status", (Object) str);
        jSONObject.put("hybridid", (Object) str2);
        jSONObject.put("md5", (Object) str3);
        jSONObject.put("oldver", (Object) str4);
        jSONObject.put("newver", (Object) str5);
        new ArrayList().add("qp");
        invokeQAV("patch:" + jSONObject.toString());
    }

    public static void qpFullSuccess(String str, String str2, String str3, String str4) {
        qpFull("1", str, str2, str3, str4);
    }

    public static void qpFullError(String str, String str2, String str3, String str4) {
        qpFull("0", str, str2, str3, str4);
    }

    public static void qpFull(String str, String str2, String str3, String str4, String str5) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("status", (Object) str);
        jSONObject.put("hybridid", (Object) str2);
        jSONObject.put("md5", (Object) str3);
        jSONObject.put("oldver", (Object) str4);
        jSONObject.put("newver", (Object) str5);
        invokeQAV("full:" + jSONObject.toString());
    }

    public static void qpFullDownloadTime(String str, double d) {
        qpDownloadTime(str, 0, d);
    }

    public static void qpPatchDownloadTime(String str, double d) {
        qpDownloadTime(str, 1, d);
    }

    public static void qpDownloadTime(String str, int i, double d) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("hybridid", (Object) str);
        jSONObject.put("patch", Integer.valueOf(i));
        jSONObject.put(QUNAR_GPS_FIELD.TIME_FIELD, Double.valueOf(d / 1000.0d));
        invokeQAV("downloadtime:" + jSONObject.toString());
    }

    public static void qpForceDownloadTimeout(String str, String str2) {
        qpForceDownload("1", str, str2);
    }

    public static void qpForceDownload(String str, String str2, String str3) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("status", (Object) str);
        jSONObject.put("hybridid", (Object) str2);
        jSONObject.put("ver", (Object) str3);
        invokeQAV("forcedownload:" + jSONObject.toString());
    }

    public static void qpDownloadNotReplace(String str, String str2) {
        qpReplace("1", str, str2);
    }

    public static void qpReplace(String str, String str2, String str3) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("status", (Object) str);
        jSONObject.put("hybridid", (Object) str2);
        jSONObject.put("ver", (Object) str3);
        invokeQAV("replace:" + jSONObject.toString());
    }

    private static void invokeQAV(String str) {
        try {
            Class cls = Class.forName("com.mqunar.qav.uelog.QAVLog");
            Object invoke = cls.getMethod("getInstance", new Class[]{Context.class}).invoke(null, new Object[]{HyResInitializer.getContext()});
            Method method = cls.getMethod("logForHybrid", new Class[]{Array.newInstance(String.class, 0).getClass()});
            Object newInstance = Array.newInstance(String.class, 2);
            Array.set(newInstance, 0, "qp");
            Array.set(newInstance, 1, str);
            method.invoke(invoke, new Object[]{newInstance});
        } catch (Throwable th) {
            Timber.e(th, new Object[0]);
        }
    }
}
