package com.mqunar.hy.res.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.mqunar.hy.res.HyResInitializer;
import com.mqunar.hy.res.HybridManager;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SpCahceUtil {
    private static final String SP_CACHE_INFO_NAME = "hybrid_cache_infos";
    private static final String SP_DELETE_INFO_NAME = "hybrid_delete_infos";
    private static SpCahceUtil spCahceUtil = new SpCahceUtil();
    private List<HybridInfo> deletList = Collections.synchronizedList(new ArrayList());
    private List<HybridInfo> mCacheList = Collections.synchronizedList(new ArrayList());
    private SharedPreferences sp = HyResInitializer.getContext().getSharedPreferences(HyResInitializer.SP_NAME, 0);

    private SpCahceUtil() {
    }

    public static SpCahceUtil getInstance() {
        return spCahceUtil;
    }

    public void saveCacheHyInfo(HybridInfo hybridInfo) {
        Timber.i("cache>添加离线包,filpath=" + hybridInfo.path + ",hybridid=" + hybridInfo.hybridId, new Object[0]);
        if (hybridInfo != null) {
            int i;
            HybridInfo hybridInfoById = HybridManager.getInstance().getHybridInfoById(hybridInfo.hybridId);
            if (hybridInfoById != null) {
                if (hybridInfoById.version < hybridInfo.version) {
                    this.deletList.add(hybridInfoById);
                } else if (hybridInfoById.version > hybridInfo.version) {
                    File file = new File(hybridInfoById.path);
                    if (file.exists() && file.length() == hybridInfoById.length) {
                        this.deletList.add(hybridInfo);
                        return;
                    }
                    this.deletList.add(hybridInfoById);
                } else if (HybridInfoParser.checkQPFile(hybridInfoById)) {
                    new File(hybridInfo.path).delete();
                    return;
                }
            }
            Iterator it = this.mCacheList.iterator();
            while (it.hasNext()) {
                hybridInfoById = (HybridInfo) it.next();
                if (hybridInfoById.hybridId.equals(hybridInfo.hybridId)) {
                    if (hybridInfoById.version < hybridInfo.version) {
                        if (new File(hybridInfoById.path).exists()) {
                            new File(hybridInfoById.path).delete();
                        }
                        it.remove();
                        i = 1;
                    } else {
                        i = 0;
                    }
                    if (i != 0) {
                        this.mCacheList.add(hybridInfo);
                    }
                    saveHyInfoList(this.mCacheList, this.sp, SP_CACHE_INFO_NAME);
                    saveHyInfoList(this.deletList, this.sp, SP_DELETE_INFO_NAME);
                }
            }
            i = 1;
            if (i != 0) {
                this.mCacheList.add(hybridInfo);
            }
            saveHyInfoList(this.mCacheList, this.sp, SP_CACHE_INFO_NAME);
            saveHyInfoList(this.deletList, this.sp, SP_DELETE_INFO_NAME);
        }
    }

    public void deleteCacheHyInfo(String str) {
        if (!TextUtils.isEmpty(str) && this.mCacheList != null) {
            HybridInfo hybridInfo;
            Iterator it = this.mCacheList.iterator();
            while (it.hasNext()) {
                hybridInfo = (HybridInfo) it.next();
                if (hybridInfo != null && str.equals(hybridInfo.hybridId)) {
                    it.remove();
                    break;
                }
            }
            if (this.deletList != null) {
                it = this.deletList.iterator();
                while (it.hasNext()) {
                    hybridInfo = (HybridInfo) it.next();
                    if (hybridInfo != null && str.equals(hybridInfo.hybridId)) {
                        it.remove();
                        break;
                    }
                }
                saveHyInfoList(this.mCacheList, this.sp, SP_CACHE_INFO_NAME);
                saveHyInfoList(this.deletList, this.sp, SP_DELETE_INFO_NAME);
            }
        }
    }

    public HybridInfo getCacheHybridInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (this.mCacheList == null) {
            return null;
        }
        for (HybridInfo hybridInfo : this.mCacheList) {
            if (hybridInfo != null && str.equals(hybridInfo.hybridId)) {
                return hybridInfo;
            }
        }
        return null;
    }

    public HybridInfo getdeletHybridInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (this.deletList == null) {
            return null;
        }
        for (HybridInfo hybridInfo : this.deletList) {
            if (hybridInfo != null && str.equals(hybridInfo.hybridId)) {
                return hybridInfo;
            }
        }
        return null;
    }

    private void saveHyInfoList(List<HybridInfo> list, SharedPreferences sharedPreferences, String str) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HybridInfo hybridInfo = (HybridInfo) it.next();
            hybridInfo.QpRequestType = 0;
            if (TextUtils.isEmpty(hybridInfo.path) || !new File(hybridInfo.path).exists()) {
                it.remove();
            }
        }
        try {
            Editor edit = sharedPreferences.edit();
            edit.putString(str, JSON.toJSONString(list));
            edit.apply();
        } catch (Throwable e) {
            Timber.e(e, new Object[0]);
        }
    }

    private void deleteOldQp() {
        String string = this.sp.getString(SP_DELETE_INFO_NAME, null);
        Timber.i("deleteOldQp>sp>" + string, new Object[0]);
        Collection parseArray = JSON.parseArray(string, HybridInfo.class);
        if (parseArray != null && parseArray.size() > 0) {
            this.deletList.addAll(parseArray);
        }
        if (this.deletList != null && this.deletList.size() > 0) {
            for (HybridInfo hybridInfo : this.deletList) {
                File file = new File(hybridInfo.path);
                if (file.exists()) {
                    Timber.i("deleteOldQp>path>" + file.getAbsolutePath(), new Object[0]);
                    file.delete();
                }
            }
            this.deletList.clear();
            this.sp.edit().putString(SP_DELETE_INFO_NAME, JSON.toJSONString(this.deletList)).apply();
        }
    }

    public List<HybridInfo> getCacheList() {
        ArrayList arrayList = new ArrayList();
        String string = this.sp.getString(SP_CACHE_INFO_NAME, null);
        Timber.i("moveCacheList>chache>" + string, new Object[0]);
        try {
            return JSON.parseArray(string, HybridInfo.class);
        } catch (Throwable e) {
            Throwable th = e;
            List<HybridInfo> arrayList2 = new ArrayList();
            Timber.e(th, new Object[0]);
            return arrayList2;
        }
    }

    public List<HybridInfo> moveCacheList() {
        List<HybridInfo> cacheList = getCacheList();
        this.mCacheList.clear();
        this.sp.edit().putString(SP_CACHE_INFO_NAME, JSON.toJSONString(this.mCacheList)).apply();
        deleteOldQp();
        return cacheList;
    }

    public boolean isToDownloadQp(HybridInfo hybridInfo) {
        for (HybridInfo hybridInfo2 : this.mCacheList) {
            if (hybridInfo2.hybridId.equals(hybridInfo.hybridId) && hybridInfo2.version >= hybridInfo.version && new File(hybridInfo2.path).exists()) {
                Timber.i("isToDownloadQp>CacheHybridid:false:" + hybridInfo2.hybridId + hybridInfo2.version + "now Hybridid:" + hybridInfo.hybridId + hybridInfo.version, new Object[0]);
                return false;
            }
        }
        return true;
    }
}
