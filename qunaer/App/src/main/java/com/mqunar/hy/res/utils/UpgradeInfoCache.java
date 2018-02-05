package com.mqunar.hy.res.utils;

import android.text.TextUtils;
import com.mqunar.hy.res.model.HybridInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeInfoCache {
    private static Map<String, HybridInfo> hybridInfos = new HashMap();

    class UpgradeInfoCacheHolder {
        private static final UpgradeInfoCache INSTANCE = new UpgradeInfoCache();

        private UpgradeInfoCacheHolder() {
        }
    }

    private UpgradeInfoCache() {
    }

    public static UpgradeInfoCache getInstance() {
        return UpgradeInfoCacheHolder.INSTANCE;
    }

    public HybridInfo getHybridInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return (HybridInfo) hybridInfos.get(str);
    }

    public void addHybridInfo(HybridInfo hybridInfo) {
        if (hybridInfo != null && !TextUtils.isEmpty(hybridInfo.hybridId)) {
            hybridInfos.put(hybridInfo.hybridId, hybridInfo);
        }
    }

    public void addHybridInfos(List<HybridInfo> list) {
        if (list != null) {
            for (HybridInfo addHybridInfo : list) {
                addHybridInfo(addHybridInfo);
            }
        }
    }

    public void clear() {
        hybridInfos.clear();
    }
}
