package com.mqunar.atomenv;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.mqunar.atomenv.env.DelegateEnvironment;
import com.mqunar.atomenv.model.Config;
import com.mqunar.atomenv.model.Dawn;
import com.mqunar.atomenv.model.ServerTime;
import com.mqunar.atomenv.uc.model.GlobalUCUserResult;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.json.JsonUtils;
import com.mqunar.storage.Storage;
import com.mqunar.tools.log.QLog;

public class GlobalEnv implements IEnvironment {
    private static GlobalEnv a = null;
    private IEnvironment b;
    private Storage c;
    private Storage d;
    private boolean e;
    private DeployType f;
    private BuildType g;
    private Runtime h;
    protected Storage storage_global;

    private GlobalEnv() {
        this.b = null;
        this.storage_global = Storage.newStorage(QApplication.getContext(), OwnerConstant.STORAGE_OWNER_GLOBAL);
        this.c = Storage.newStorage(QApplication.getContext(), OwnerConstant.STORAGE_OWNER_SYS);
        this.e = false;
        this.b = EnvInit.newEnv();
    }

    public IEnvironment getEnv() {
        return this.b;
    }

    public static GlobalEnv getInstance() {
        if (a == null) {
            synchronized (GlobalEnv.class) {
                if (a == null) {
                    a = new GlobalEnv();
                }
            }
        }
        return a;
    }

    public void envLock() {
        this.e = true;
    }

    private void a() {
        if (this.e) {
            throw new RuntimeException("can not set ENV when locked!");
        }
    }

    public void setEnv(IEnvironment iEnvironment) {
        a();
        this.b = iEnvironment;
    }

    public void setDelegate(DelegateEnvironment delegateEnvironment) {
        delegateEnvironment.environment = this.b;
        setEnv(delegateEnvironment);
    }

    public synchronized BuildType getBuildType() {
        BuildType buildType;
        if (isRelease()) {
            buildType = BuildType.RELEASE;
        } else {
            if (this.g == null) {
                this.g = BuildType.parseByBuildConfig();
            }
            buildType = this.g;
        }
        return buildType;
    }

    public synchronized DeployType getDeployType() {
        DeployType deployType;
        if (isRelease()) {
            deployType = DeployType.OPS;
        } else {
            if (this.f == null) {
                this.f = DeployType.parseByBuildConfig();
            }
            deployType = this.f;
        }
        return deployType;
    }

    public synchronized Runtime getRuntime() {
        Runtime runtime;
        if (isRelease()) {
            runtime = Runtime.ILLEGAL;
        } else {
            if (this.h == null) {
                this.h = Runtime.parseByBuildConfig();
            }
            runtime = this.h;
        }
        return runtime;
    }

    public String getUid() {
        return this.b.getUid();
    }

    public String getPid() {
        return this.b.getPid();
    }

    public String getUserId() {
        GlobalUCUserResult globalUCUserResult = getGlobalUCUserResult();
        if (globalUCUserResult == null || globalUCUserResult.getData() == null || globalUCUserResult.getData().getUinfo() == null) {
            return "";
        }
        return globalUCUserResult.getData().getUinfo().getUserid();
    }

    public GlobalUCUserResult getGlobalUCUserResult() {
        try {
            Object userResultJson = getUserResultJson();
            if (!TextUtils.isEmpty(userResultJson)) {
                return (GlobalUCUserResult) JsonUtils.parseObject(userResultJson, GlobalUCUserResult.class);
            }
        } catch (Throwable th) {
            QLog.e(th);
        }
        return null;
    }

    public String getUserName() {
        GlobalUCUserResult globalUCUserResult = getGlobalUCUserResult();
        if (globalUCUserResult == null || globalUCUserResult.getData() == null || globalUCUserResult.getData().getUinfo() == null) {
            return "";
        }
        return globalUCUserResult.getData().getUinfo().getUname();
    }

    public String getUUID() {
        GlobalUCUserResult globalUCUserResult = getGlobalUCUserResult();
        if (globalUCUserResult == null || globalUCUserResult.getData() == null || globalUCUserResult.getData().getUinfo() == null) {
            return "";
        }
        return globalUCUserResult.getData().getUinfo().getUuid();
    }

    public String getVid() {
        return this.b.getVid();
    }

    public String getCid() {
        return this.b.getCid();
    }

    public String getRCid() {
        return this.c.getString(AtomEnvConstants.SYS_RCID, getCid());
    }

    public String getSid() {
        return this.b.getSid();
    }

    public String getGid() {
        return this.b.getGid();
    }

    public boolean isAutoSwapImage() {
        return this.b.isAutoSwapImage();
    }

    public void putAutoSwapImage(boolean z) {
        this.b.putAutoSwapImage(z);
    }

    public ServerTime getServerTime() {
        return this.b.getServerTime();
    }

    public void putTint(long j) {
        this.b.putTint(j);
    }

    public void putTstr(String str) {
        this.b.putTstr(str);
    }

    public String getSplashAdUrl() {
        return this.b.getSplashAdUrl();
    }

    public void putSplashAdUrl(String str) {
        this.b.putSplashAdUrl(str);
    }

    public String getSplashWebUrl() {
        return this.b.getSplashWebUrl();
    }

    public String getHotDogUrl() {
        return this.b.getHotDogUrl();
    }

    public String getCarPullUrl() {
        return this.b.getCarPullUrl();
    }

    public String getBaiduVoiceUrl() {
        return this.b.getBaiduVoiceUrl();
    }

    public String getHotelUploadPicUrl() {
        return this.b.getHotelUploadPicUrl();
    }

    public String getLocalLifeUrl() {
        return this.b.getLocalLifeUrl();
    }

    public String getCarAboutTouchUrl() {
        return this.b.getCarAboutTouchUrl();
    }

    public String getPayUrl() {
        return this.b.getPayUrl();
    }

    public String getOuterCarUrl() {
        return this.b.getOuterCarUrl();
    }

    public String getScheme() {
        return this.b.getScheme();
    }

    public String getSchemeWap() {
        return this.b.getSchemeWap();
    }

    public String getMac() {
        return this.b.getMac();
    }

    public boolean isRelease() {
        return this.b.isRelease();
    }

    public boolean isBeta() {
        return this.b.isBeta();
    }

    public boolean isDev() {
        return this.b.isDev();
    }

    public String getDBPath() {
        return this.b.getDBPath();
    }

    public void putDBPath(String str) {
        this.b.putDBPath(str);
    }

    public String getWXAppId() {
        return this.b.getWXAppId();
    }

    public String getConfigJson() {
        return this.b.getConfigJson();
    }

    public Config getConfig() {
        return this.b.getConfig();
    }

    public String getBetaLongitude() {
        return this.b.getBetaLongitude();
    }

    public String getBetaLatitude() {
        return this.b.getBetaLatitude();
    }

    public String getBetaString() {
        return this.b.getBetaString();
    }

    public void putUserResultJson(String str) {
        QLog.d("UN", str == null ? "null" : str, new Object[0]);
        if (!TextUtils.isEmpty(str)) {
            this.storage_global.putString(AtomEnvConstants.GLOBAL_USERRESULT, str);
            Intent intent = new Intent();
            intent.setAction("com.mqunar.usercenter.MESSAGE_LOGIN_STATE");
            intent.putExtra("state", "login");
            LocalBroadcastManager.getInstance(QApplication.getContext()).sendBroadcast(intent);
        }
    }

    public String getUserResultJson() {
        return this.storage_global.getString(AtomEnvConstants.GLOBAL_USERRESULT, "");
    }

    public void removeUserResultJson() {
        this.storage_global.putString(AtomEnvConstants.GLOBAL_USERRESULT, "");
        Intent intent = new Intent();
        intent.setAction("com.mqunar.usercenter.MESSAGE_LOGIN_STATE");
        intent.putExtra("state", "logout");
        LocalBroadcastManager.getInstance(QApplication.getContext()).sendBroadcast(intent);
    }

    public void putDawn(Dawn dawn) {
        this.storage_global.putSerializable(AtomEnvConstants.GLOBAL_DAWN, dawn);
    }

    public Dawn getDawn() {
        return (Dawn) this.storage_global.getSerializable(AtomEnvConstants.GLOBAL_DAWN);
    }

    public int getUCLoginT() {
        return this.storage_global.getInt(AtomEnvConstants.GLOBAL_UCLOGINT, 0);
    }

    public String getUCparamJsonStr() {
        return this.storage_global.getString(AtomEnvConstants.GLOBAL_UCPARAMJSONSTR, "");
    }

    public void putUCparamJsonStr(String str) {
        this.storage_global.putString(AtomEnvConstants.GLOBAL_UCPARAMJSONSTR, str);
    }

    public void putUCLoginT(int i) {
        this.storage_global.putInt(AtomEnvConstants.GLOBAL_UCLOGINT, i);
    }

    public void removeLoginT() {
        this.storage_global.remove(AtomEnvConstants.GLOBAL_UCLOGINT);
    }

    public synchronized Storage transferDebug() {
        Storage storage;
        if (isRelease()) {
            storage = null;
        } else {
            if (this.d == null) {
                this.d = Storage.newStorage(QApplication.getContext(), OwnerConstant.STORAGE_OWNER_DEBUG);
            }
            storage = this.d;
        }
        return storage;
    }

    public String getBetaSwitch(String str, String str2) {
        if (isRelease()) {
            return str2;
        }
        try {
            Class cls = Class.forName("com.mqunar.atomenv.env.debug.a");
            Object invoke = cls.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            String str3 = (String) cls.getDeclaredMethod("get", new Class[]{String.class}).invoke(invoke, new Object[]{"inter_" + str});
            if (TextUtils.isEmpty(str3)) {
                return str2;
            }
            return str3;
        } catch (Throwable th) {
            return str2;
        }
    }
}
