package com.mqunar.core;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import com.mqunar.module.ModuleInfo;
import java.io.File;
import java.util.Map;
import qunar.lego.utils.diffpatch.MD5;

public class ErrorMsg {
    static synchronized ClassNotFoundException a(Context context, BaseApkLoader baseApkLoader, String str, Map<String, ModuleInfo> map, ModuleInfo moduleInfo, BaseApkLoader baseApkLoader2) {
        ClassNotFoundException classNotFoundException;
        synchronized (ErrorMsg.class) {
            try {
                StringBuilder stringBuilder = new StringBuilder("v3_");
                appendClassMessage(stringBuilder, str, baseApkLoader);
                boolean appendMI = appendMI(context, stringBuilder, moduleInfo);
                appendDexMessage(stringBuilder, QunarApkLoader.getDexCachePath(context), moduleInfo, str);
                appendMaps(stringBuilder, map, appendMI);
                appendAtomLoader(stringBuilder, baseApkLoader, baseApkLoader2, str);
                stringBuilder.append("|ld=").append(QSpider.loadDone).append("|crach_url:").append(QSpider.crashTouchUrl);
                classNotFoundException = new ClassNotFoundException(stringBuilder.toString());
            } catch (Throwable th) {
                classNotFoundException = new ClassNotFoundException("can not found class " + str, th);
            }
        }
        return classNotFoundException;
    }

    public static void appendClassMessage(StringBuilder stringBuilder, String str, BaseApkLoader baseApkLoader) {
        stringBuilder.append("a=");
        stringBuilder.append(str);
        stringBuilder.append("#b=");
        if (baseApkLoader == null) {
            stringBuilder.append("n");
        } else {
            stringBuilder.append(baseApkLoader.getModuleInfo().name);
        }
        stringBuilder.append("#c=");
        stringBuilder.append(Looper.getMainLooper() == Looper.myLooper() ? MapViewConstants.ATTR_Y : "n");
        stringBuilder.append("|");
    }

    public static boolean appendMI(Context context, StringBuilder stringBuilder, ModuleInfo moduleInfo) {
        boolean z;
        stringBuilder.append("a=");
        if (moduleInfo == null) {
            stringBuilder.append("n");
            z = false;
            stringBuilder.append("_mi_");
        } else {
            stringBuilder.append(moduleInfo.fileName);
            stringBuilder.append("_");
            stringBuilder.append(moduleInfo.localVersion);
            stringBuilder.append("_");
            if (moduleInfo.dependency == null) {
                stringBuilder.append("n");
            } else {
                stringBuilder.append(moduleInfo.dependency.checkFlag);
                stringBuilder.append("_");
                stringBuilder.append(moduleInfo.isLoad ? "1" : "-1");
            }
            stringBuilder.append("_");
            File file = new File(moduleInfo.fileName);
            if (file.exists()) {
                String md5;
                try {
                    md5 = MD5.getMD5(file);
                } catch (Throwable th) {
                    md5 = "e";
                }
                stringBuilder.append(md5);
            } else {
                stringBuilder.append(file.canRead());
            }
            z = true;
        }
        stringBuilder.append("|");
        return z;
    }

    public static void appendDexMessage(StringBuilder stringBuilder, String str, ModuleInfo moduleInfo, String str2) {
        stringBuilder.append("a=");
        stringBuilder.append(QunarApkLoader.a());
        stringBuilder.append("#b=");
        stringBuilder.append(str);
        stringBuilder.append("#c=");
        try {
            File file = new File(str);
            if (file.exists()) {
                File[] listFiles = file.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    stringBuilder.append("0");
                    stringBuilder.append("|");
                }
                stringBuilder.append(listFiles.length);
                stringBuilder.append("|");
            }
            stringBuilder.append("n");
            stringBuilder.append("|");
        } catch (Throwable th) {
            stringBuilder.append("e");
        }
    }

    public static void appendMaps(StringBuilder stringBuilder, Map<String, ModuleInfo> map, boolean z) {
        stringBuilder.append("a=");
        if (z) {
            if (map == null || map.size() == 0) {
                stringBuilder.append("f");
            } else {
                stringBuilder.append(map.size());
            }
        } else if (map == null || map.size() == 0) {
            stringBuilder.append("n");
        } else {
            for (ModuleInfo moduleInfo : map.values()) {
                stringBuilder.append(moduleInfo.fileName);
                stringBuilder.append("_");
                stringBuilder.append(moduleInfo.localVersion);
                stringBuilder.append("_");
                stringBuilder.append(moduleInfo.dependency.checkFlag);
                stringBuilder.append("_");
                stringBuilder.append(moduleInfo.isLoad ? "1" : "-1");
            }
        }
        stringBuilder.append('|');
    }

    public static void appendAtomLoader(StringBuilder stringBuilder, BaseApkLoader baseApkLoader, BaseApkLoader baseApkLoader2, String str) {
        stringBuilder.append("a=");
        if (baseApkLoader == baseApkLoader2) {
            stringBuilder.append("s");
        } else if (baseApkLoader2 != null) {
            stringBuilder.append(baseApkLoader2.getModuleInfo().name);
        } else {
            stringBuilder.append("n");
        }
        stringBuilder.append("#b=");
        if (baseApkLoader != baseApkLoader2 || baseApkLoader == null) {
            if (baseApkLoader != null) {
                try {
                    baseApkLoader.loadClassFromCurrent(str);
                    stringBuilder.append("C-SUCCESS!");
                } catch (Throwable th) {
                    stringBuilder.append("c-");
                    stringBuilder.append(th.getLocalizedMessage());
                }
            }
            if (baseApkLoader2 != null) {
                try {
                    baseApkLoader2.loadClassFromCurrent(str);
                    stringBuilder.append("A-SUCCESS!");
                } catch (Throwable th2) {
                    stringBuilder.append("a-");
                    stringBuilder.append(th2.getCause() == null ? "noCause" : th2.getCause().getMessage());
                }
                try {
                    baseApkLoader2.loadClassFromSuper(str);
                    stringBuilder.append("-SUPER-SUCCESS!");
                } catch (Throwable th22) {
                    stringBuilder.append("-super-");
                    stringBuilder.append(Log.getStackTraceString(th22));
                }
            }
        } else {
            try {
                baseApkLoader.loadClassFromCurrent(str);
                stringBuilder.append("SAME-SUCCESS!");
            } catch (Throwable th222) {
                stringBuilder.append("s-");
                stringBuilder.append(th222.getMessage());
            }
        }
        stringBuilder.append("#c=");
        if (baseApkLoader2 != null) {
            File debugOutputPath = baseApkLoader2.getDebugOutputPath();
            if (debugOutputPath == null) {
                stringBuilder.append("n");
            } else {
                stringBuilder.append(MapViewConstants.ATTR_Y);
                if (!debugOutputPath.exists()) {
                    stringBuilder.append("_n_");
                } else if (debugOutputPath.canRead() && debugOutputPath.canWrite()) {
                    stringBuilder.append("_y_");
                } else {
                    stringBuilder.append("_x_");
                }
            }
            try {
                String optimizedPathFor = DexPathList.optimizedPathFor(new File(baseApkLoader2.getModuleInfo().fileName), debugOutputPath);
                stringBuilder.append(optimizedPathFor);
                File file = new File(optimizedPathFor);
                if (file.exists()) {
                    stringBuilder.append(MD5.getMD5(file));
                    return;
                } else {
                    stringBuilder.append("_n_");
                    return;
                }
            } catch (Throwable th2222) {
                stringBuilder.append("t:");
                stringBuilder.append(th2222.getMessage());
                return;
            }
        }
        stringBuilder.append("n");
    }
}
