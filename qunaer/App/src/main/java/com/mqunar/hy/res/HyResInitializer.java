package com.mqunar.hy.res;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.text.TextUtils;
import com.mqunar.BuildConfig;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.logger.Timber.DebugTree;
import com.mqunar.hy.res.model.CommonParam;
import com.mqunar.hy.res.model.HybridInfo;
import java.util.Map;

public class HyResInitializer {
    public static final String SP_NAME = "qunar_hy_res";
    public static final String UPGRADE_10010 = "http://exbizcom.qunar.com/hybridUpgrade";
    public static final String UPGRADE_10010_BETA = "http://l-client3.wap.beta.cn6.qunar.com:9334/hybridUpgrade";
    public static final String UPGRADE_OTHER = "http://hybrid.qunar.com/hybridUpgrade";
    public static final String UPGRADE_OTHER_BETA = "http://l-wap8.wap.beta.cn6.qunar.com:8038/hybridUpgrade";
    private static final String UPGRADE_URL = "upgrade_url";
    private static CommonParam cParam = new CommonParam();
    private static Application context;
    private static boolean debug = false;
    private static Map<String, String> module;
    private static boolean offlineWork = true;
    private static String serverUrl = UPGRADE_10010;
    private static HyResInitializer singleInstance = null;

    private HyResInitializer() {
    }

    public void setServerUrl(String str) {
        if (debug && !isOnline() && !TextUtils.isEmpty(str)) {
            serverUrl = str;
            saveCacheUpgradeUrl();
        }
    }

    public static String getServerUrl() {
        if (!debug || isOnline()) {
            return serverUrl;
        }
        return getCacheUpgradeUrl();
    }

    public static CommonParam getCParam() {
        return cParam;
    }

    public void setCParam(CommonParam commonParam) {
        if (commonParam != null) {
            cParam = commonParam;
            if ("10010".equals(commonParam.pid) || "_10010".equals(commonParam.pid)) {
                serverUrl = UPGRADE_10010;
            } else {
                serverUrl = UPGRADE_OTHER;
            }
        }
    }

    public void setDebug(boolean z) {
        debug = z;
        Timber.uprootAll();
        if (z) {
            Timber.plant(new DebugTree());
        }
    }

    public static void setOfflineWork(boolean z) {
        offlineWork = z;
    }

    public static boolean getOfflineWork() {
        return offlineWork;
    }

    public static boolean isDebug() {
        return debug;
    }

    public void setModules(Map<String, String> map) {
        module = map;
    }

    public static Map<String, String> getModules() {
        return module;
    }

    public static HyResInitializer getInstance(Application application) {
        if (singleInstance == null) {
            singleInstance = new HyResInitializer();
            context = application;
        }
        return singleInstance;
    }

    public static HyResInitializer getInstance() {
        if (singleInstance == null) {
            singleInstance = new HyResInitializer();
        }
        return singleInstance;
    }

    public static void setContext(Application application) {
        context = application;
    }

    public static Application getContext() {
        return context;
    }

    public void sendUpdateRequest() {
        new AutoDownloadControler().startUpdateRequest();
        Timber.i("sendUpdateRequest>全量更新>time:" + System.currentTimeMillis(), new Object[0]);
    }

    public void sendSingleUpdateRequest(HybridInfo hybridInfo) {
        new AutoDownloadControler().startUpdateRequest(hybridInfo);
    }

    public void regiestNewModuleFromAssert(AssetManager assetManager, String str, String str2) {
        HybridManager.getInstance().addNewModuleFromAssert(assetManager, str, str2);
    }

    public void addNewModuleFromPath(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            HybridManager.getInstance().addNewModule(str, str2);
        }
    }

    public HybridInfo addNewModuleFromCache(String str) {
        return null;
    }

    public void startDownload(String str) {
    }

    public void downloadOnly(HybridInfo hybridInfo) {
    }

    private static String getCacheUpgradeUrl() {
        return context.getSharedPreferences(SP_NAME, 0).getString(UPGRADE_URL, serverUrl);
    }

    public static boolean isOnline() {
        try {
            if (((Boolean) getBuildConfigValue(context, "DEBUG")).booleanValue()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static void saveCacheUpgradeUrl() {
        Editor edit = context.getSharedPreferences(SP_NAME, 0).edit();
        edit.putString(UPGRADE_URL, serverUrl);
        edit.apply();
    }

    private static Object getBuildConfigValue(Context context, String str) {
        try {
            if (BuildConfig.APPLICATION_ID.equals(context.getPackageName())) {
                return Class.forName("com.mqunar.BuildConfig").getField(str).get(null);
            }
            return Boolean.valueOf(debug);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
