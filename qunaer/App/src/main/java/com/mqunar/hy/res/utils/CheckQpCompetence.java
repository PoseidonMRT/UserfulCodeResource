package com.mqunar.hy.res.utils;

import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mqunar.hy.res.HyResInitializer;
import com.mqunar.hy.res.HybridManager;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckQpCompetence {
    private static final String CHECK_QP_URL = "https://qpadjust.qunar.com/qpadjust.json?hybridid=qpadjust";
    private static final String HYBRID_ID = "qpadjust";
    private static final String OFFLINE_QP_INFO = "offline_qp_info";
    private boolean hasInit;
    private JSONObject onlineCheckJson;
    private JSONObject qpCheckJson;
    private Set<String> usedHybrididSet;

    class CheckQpCompetenceHolder {
        private static final CheckQpCompetence INSTANCE = new CheckQpCompetence();

        private CheckQpCompetenceHolder() {
        }
    }

    private CheckQpCompetence() {
        this.qpCheckJson = new JSONObject();
        this.onlineCheckJson = new JSONObject();
        this.hasInit = false;
        this.usedHybrididSet = Collections.synchronizedSet(new HashSet());
    }

    public static CheckQpCompetence getInstance() {
        return CheckQpCompetenceHolder.INSTANCE;
    }

    public synchronized void init() {
        WebResourceResponse resByUrl = HybridManager.getInstance().getResByUrl(CHECK_QP_URL);
        if (resByUrl != null) {
            String stringFromStream = getStringFromStream(resByUrl.getData());
            if (stringFromStream != null) {
                try {
                    Timber.i("HyRes qpadjust info = " + stringFromStream, new Object[0]);
                    this.qpCheckJson = JSON.parseObject(stringFromStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.onlineCheckJson = getCacheOfflineHybridInfo();
            Timber.i("HyRes 下线包缓存信息 = " + this.onlineCheckJson, new Object[0]);
        }
    }

    private String getStringFromStream(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        char[] cArr = new char[2048];
        while (true) {
            try {
                int read = inputStreamReader.read(cArr);
                if (read == -1) {
                    break;
                }
                stringBuilder.append(cArr, 0, read);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            } catch (Throwable th) {
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        String stringBuilder2 = stringBuilder.toString();
        if (inputStreamReader != null) {
            try {
                inputStreamReader.close();
            } catch (IOException e22) {
                e22.printStackTrace();
            }
        }
        return stringBuilder2;
    }

    public void setUsedHybridid(String str) {
        this.usedHybrididSet.add(str);
    }

    public synchronized boolean isQpCanUse(HybridInfo hybridInfo) {
        boolean z = true;
        synchronized (this) {
            if (HyResInitializer.getOfflineWork() && hybridInfo != null) {
                if (!HYBRID_ID.equals(hybridInfo.hybridId)) {
                    if (!this.hasInit) {
                        init();
                        this.hasInit = true;
                    }
                    CharSequence charSequence = hybridInfo.hybridId;
                    if (!TextUtils.isEmpty(charSequence)) {
                        if (this.usedHybrididSet.contains(charSequence)) {
                            Timber.i("HyRes qp包已经被使用 info = " + hybridInfo.toJsonString(), new Object[0]);
                        } else {
                            if (hybridInfo.version < this.qpCheckJson.getIntValue(hybridInfo.hybridId)) {
                                Timber.i("HyRes qpadjust qp包判断下线 info = " + hybridInfo.toJsonString(), new Object[0]);
                                z = false;
                            } else if (this.onlineCheckJson.getIntValue(hybridInfo.hybridId) == hybridInfo.version) {
                                Timber.i("HyRes qpadjust 服务端判断下线 info = " + hybridInfo.toJsonString(), new Object[0]);
                                z = false;
                            }
                        }
                    }
                }
            }
        }
        return z;
    }

    public synchronized void updateOfflineHybridInfo(List<HybridInfo> list) {
        if (!this.hasInit) {
            init();
            this.hasInit = true;
        }
        for (HybridInfo hybridInfo : list) {
            this.onlineCheckJson.put(hybridInfo.hybridId, Integer.valueOf(hybridInfo.version));
            Timber.i("HyRes qpadjust qp包下线 info = " + hybridInfo.toJsonString(), new Object[0]);
        }
        saveCacheOfflineHybridInfo(this.onlineCheckJson);
    }

    private JSONObject getCacheOfflineHybridInfo() {
        String string = HyResInitializer.getContext().getSharedPreferences(HyResInitializer.SP_NAME, 0).getString(OFFLINE_QP_INFO, null);
        if (string != null) {
            return JSON.parseObject(string);
        }
        return new JSONObject();
    }

    private void saveCacheOfflineHybridInfo(JSONObject jSONObject) {
        Editor edit = HyResInitializer.getContext().getSharedPreferences(HyResInitializer.SP_NAME, 0).edit();
        edit.putString(OFFLINE_QP_INFO, jSONObject.toString());
        edit.apply();
    }
}
