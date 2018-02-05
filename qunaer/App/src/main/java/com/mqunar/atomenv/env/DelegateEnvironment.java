package com.mqunar.atomenv.env;

import com.mqunar.atomenv.IEnvironment;
import com.mqunar.atomenv.model.Config;
import com.mqunar.atomenv.model.ServerTime;

public class DelegateEnvironment implements IEnvironment {
    public IEnvironment environment;

    public DelegateEnvironment(IEnvironment iEnvironment) {
        this.environment = iEnvironment;
    }

    public String getUid() {
        return this.environment.getUid();
    }

    public String getPid() {
        return this.environment.getPid();
    }

    public String getVid() {
        return this.environment.getVid();
    }

    public String getCid() {
        return this.environment.getCid();
    }

    public String getSid() {
        return this.environment.getSid();
    }

    public String getGid() {
        return this.environment.getGid();
    }

    public boolean isAutoSwapImage() {
        return this.environment.isAutoSwapImage();
    }

    public void putAutoSwapImage(boolean z) {
        this.environment.putAutoSwapImage(z);
    }

    public ServerTime getServerTime() {
        return this.environment.getServerTime();
    }

    public void putTint(long j) {
        this.environment.putTint(j);
    }

    public void putTstr(String str) {
        this.environment.putTstr(str);
    }

    public String getSplashAdUrl() {
        return this.environment.getSplashAdUrl();
    }

    public void putSplashAdUrl(String str) {
        this.environment.putSplashAdUrl(str);
    }

    public String getSplashWebUrl() {
        return this.environment.getSplashWebUrl();
    }

    public String getHotDogUrl() {
        return this.environment.getHotDogUrl();
    }

    public String getCarPullUrl() {
        return this.environment.getCarPullUrl();
    }

    public String getBaiduVoiceUrl() {
        return this.environment.getBaiduVoiceUrl();
    }

    public String getHotelUploadPicUrl() {
        return this.environment.getHotelUploadPicUrl();
    }

    public String getLocalLifeUrl() {
        return this.environment.getLocalLifeUrl();
    }

    public String getCarAboutTouchUrl() {
        return this.environment.getCarAboutTouchUrl();
    }

    public String getPayUrl() {
        return this.environment.getPayUrl();
    }

    public String getOuterCarUrl() {
        return this.environment.getOuterCarUrl();
    }

    public String getScheme() {
        return this.environment.getScheme();
    }

    public String getSchemeWap() {
        return this.environment.getSchemeWap();
    }

    public String getMac() {
        return this.environment.getMac();
    }

    public boolean isRelease() {
        return this.environment.isRelease();
    }

    public boolean isBeta() {
        return this.environment.isBeta();
    }

    public boolean isDev() {
        return this.environment.isDev();
    }

    public String getDBPath() {
        return this.environment.getDBPath();
    }

    public void putDBPath(String str) {
        this.environment.putDBPath(str);
    }

    public String getWXAppId() {
        return this.environment.getWXAppId();
    }

    public String getConfigJson() {
        return this.environment.getConfigJson();
    }

    public Config getConfig() {
        return this.environment.getConfig();
    }

    public String getBetaLongitude() {
        return this.environment.getBetaLongitude();
    }

    public String getBetaLatitude() {
        return this.environment.getBetaLatitude();
    }

    public String getBetaString() {
        return this.environment.getBetaString();
    }
}
