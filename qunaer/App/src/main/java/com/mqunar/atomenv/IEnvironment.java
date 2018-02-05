package com.mqunar.atomenv;

import com.mqunar.atomenv.model.Config;
import com.mqunar.atomenv.model.ServerTime;

public interface IEnvironment {
    String getBaiduVoiceUrl();

    String getBetaLatitude();

    String getBetaLongitude();

    String getBetaString();

    String getCarAboutTouchUrl();

    String getCarPullUrl();

    String getCid();

    Config getConfig();

    String getConfigJson();

    String getDBPath();

    String getGid();

    String getHotDogUrl();

    String getHotelUploadPicUrl();

    String getLocalLifeUrl();

    String getMac();

    String getOuterCarUrl();

    String getPayUrl();

    String getPid();

    String getScheme();

    String getSchemeWap();

    ServerTime getServerTime();

    String getSid();

    String getSplashAdUrl();

    String getSplashWebUrl();

    String getUid();

    String getVid();

    String getWXAppId();

    boolean isAutoSwapImage();

    boolean isBeta();

    boolean isDev();

    boolean isRelease();

    void putAutoSwapImage(boolean z);

    void putDBPath(String str);

    void putSplashAdUrl(String str);

    void putTint(long j);

    void putTstr(String str);
}
