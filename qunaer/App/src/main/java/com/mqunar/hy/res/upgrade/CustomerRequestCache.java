package com.mqunar.hy.res.upgrade;

import android.text.TextUtils;
import com.mqunar.hy.res.model.HybridInfo;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class CustomerRequestCache {
    private Map<String, HybridInfo> requestCacheMap;

    class CustomerRequestCacheHolder {
        private static final CustomerRequestCache INSTANCE = new CustomerRequestCache();

        private CustomerRequestCacheHolder() {
        }
    }

    private CustomerRequestCache() {
        this.requestCacheMap = Collections.synchronizedMap(new HashMap());
    }

    public static CustomerRequestCache getInstance() {
        return CustomerRequestCacheHolder.INSTANCE;
    }

    public void add(String str, HybridInfo hybridInfo) {
        this.requestCacheMap.put(str, hybridInfo);
    }

    public void add(List<HybridInfo> list) {
        for (HybridInfo hybridInfo : list) {
            if (!(hybridInfo == null || TextUtils.isEmpty(hybridInfo.hybridId))) {
                add(hybridInfo.hybridId, hybridInfo);
            }
        }
    }

    public HybridInfo get(String str) {
        return (HybridInfo) this.requestCacheMap.get(str);
    }

    public void remove(String str) {
        this.requestCacheMap.remove(str);
    }

    public void clear() {
        this.requestCacheMap.clear();
    }
}
