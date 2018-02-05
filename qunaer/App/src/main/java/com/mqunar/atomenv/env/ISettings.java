package com.mqunar.atomenv.env;

public interface ISettings {
    void cleanLocation();

    String getBaiduVoiceUrl();

    boolean getBool();

    String getCarAboutTouchUrl();

    String getCarPullUrl();

    String getCid();

    String getHotDogUrl();

    String getHotelUploadPicUrl();

    int getInt1();

    int getInt2();

    String getLatitude();

    String getLocalLifeUrl();

    String getLongitude();

    String getMac();

    String getOuterCarUrl();

    String getPayUrl();

    String getPid();

    String getScheme();

    String getSchemeWap();

    int getSplashAdTime();

    int getSplashTime();

    String getString();

    String getUid();

    String getVid();

    boolean isShowBackButton();

    boolean isShowBaiduVoidLog();

    boolean isShowNetWorkLog();

    boolean isShowUELog();

    void removeInt1();

    void removeInt2();

    void setBaiduVoiceUrl(String str);

    void setBool(boolean z);

    void setCarPullUrl(String str);

    void setCarTouchUrl(String str);

    void setCid(String str);

    void setHotDogUrl(String str);

    void setHotelPicUrl(String str);

    void setInt1(int i);

    void setInt2(int i);

    void setLatitude(String str);

    void setLocalLifeUrl(String str);

    void setLongitude(String str);

    void setMac(String str);

    void setOuterCarUrl(String str);

    void setPayUrl(String str);

    void setPid(String str);

    void setScheme(String str);

    void setSchemeWap(String str);

    void setShowBackButton(boolean z);

    void setShowBaiduVoidLog(boolean z);

    void setShowNetWorkLog(boolean z);

    void setShowUELog(boolean z);

    void setSplashAdTime(int i);

    void setSplashTime(int i);

    void setString(String str);

    void setUid(String str);

    void setVid(String str);
}
