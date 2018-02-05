package com.mqunar.hy.res;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridFile;
import com.mqunar.hy.res.model.HybridInfo;
import com.mqunar.hy.res.utils.AssetUtils;
import com.mqunar.hy.res.utils.CheckQpCompetence;
import com.mqunar.hy.res.utils.DownloadManager;
import com.mqunar.hy.res.utils.ErrorCodeAndMessage;
import com.mqunar.hy.res.utils.HybridIdUtils;
import com.mqunar.hy.res.utils.HybridInfoParser;
import com.mqunar.hy.res.utils.QHepburnMimeTypeUtils;
import com.mqunar.hy.res.utils.QmpFileInputStream;
import com.mqunar.hy.res.utils.QunarUtils;
import com.mqunar.hy.res.utils.RsaDecodeUtil;
import com.mqunar.hy.res.utils.SpCahceUtil;
import com.mqunar.hy.res.utils.SynchronizedList;
import com.mqunar.hy.res.utils.UpgradeInfoCache;
import com.mqunar.hy.res.utils.UriCodec;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import qunar.lego.utils.diffpatch.MD5;

public class HybridManager {
    private static HybridManager instance;
    private static Set<String> mHybrididSet = new HashSet();
    private static boolean parseFlag = false;
    private static Thread parseThread;
    private String DATA_PATH;
    private final Object listLock = new Object();
    private List<HybridInfo> mList = new SynchronizedList(new ArrayList(), this.listLock);
    private SharedPreferences preferences;

    public List<HybridInfo> getHybridInfos() {
        return this.mList;
    }

    private String copyAssertToSdcard(AssetManager assetManager, String str) {
        IOException e;
        FileOutputStream fileOutputStream;
        Throwable th;
        Object obj;
        String str2 = null;
        if (!(TextUtils.isEmpty(str) || assetManager == null)) {
            FileOutputStream fileOutputStream2 = null;
            InputStream open;
            try {
                String substring = str.substring(str.lastIndexOf(File.separator) + 1);
                open = assetManager.open(str);
                try {
                    File file = new File(this.DATA_PATH, substring);
                    if (file.exists() && file.length() == ((long) open.available())) {
                        if (open != null) {
                            try {
                                open.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (str2 != null) {
                            try {
                                fileOutputStream2.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                    } else {
                        fileOutputStream = new FileOutputStream(file);
                        try {
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int read = open.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, read);
                            }
                            fileOutputStream.flush();
                            str2 = file.getAbsolutePath();
                            if (open != null) {
                                try {
                                    open.close();
                                } catch (IOException e32) {
                                    e32.printStackTrace();
                                }
                            }
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e322) {
                                    e322.printStackTrace();
                                }
                            }
                        } catch (IOException e4) {
                            e322 = e4;
                            try {
                                e322.printStackTrace();
                                if (open != null) {
                                    try {
                                        open.close();
                                    } catch (IOException e3222) {
                                        e3222.printStackTrace();
                                    }
                                }
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e32222) {
                                        e32222.printStackTrace();
                                    }
                                }
                                return str2;
                            } catch (Throwable th2) {
                                th = th2;
                                if (open != null) {
                                    try {
                                        open.close();
                                    } catch (IOException e322222) {
                                        e322222.printStackTrace();
                                    }
                                }
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e3222222) {
                                        e3222222.printStackTrace();
                                    }
                                }
                                throw th;
                            }
                        }
                    }
                } catch (IOException e5) {
                    e3222222 = e5;
                    obj = str2;
                    e3222222.printStackTrace();
                    if (open != null) {
                        open.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    return str2;
                } catch (Throwable th3) {
                    obj = str2;
                    th = th3;
                    if (open != null) {
                        open.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e3222222 = e6;
                obj = str2;
                Object obj2 = str2;
                e3222222.printStackTrace();
                if (open != null) {
                    open.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                return str2;
            } catch (Throwable th32) {
                fileOutputStream = str2;
                open = str2;
                th = th32;
                if (open != null) {
                    open.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw th;
            }
        }
        return str2;
    }

    public void addNewModule(HybridInfo hybridInfo, Set<DownloadTaskResult<HybridInfo>> set) {
        if (hybridInfo != null) {
            Timber.d("HyRes 添加了离线资源包,filpath=" + hybridInfo.path + ",hybridid=" + hybridInfo.hybridId, new Object[0]);
            HybridInfo hybridInfoById = getHybridInfoById(hybridInfo.hybridId);
            if (hybridInfoById != null) {
                Timber.d("HyRes 新旧文件信息 oldInfo = " + hybridInfoById.toJsonString() + " <--> newInfo = " + hybridInfo.toJsonString(), new Object[0]);
                if (hybridInfoById.version < hybridInfo.version) {
                    new File(hybridInfoById.path).delete();
                    this.mList.remove(hybridInfoById);
                } else if (hybridInfoById.version > hybridInfo.version) {
                    File file = new File(hybridInfoById.path);
                    if (file.exists() && file.length() == hybridInfoById.length) {
                        new File(hybridInfo.path).delete();
                        synchronized (DownloadManager.getInstance()) {
                            for (DownloadTaskResult downloadTaskResult : set) {
                                if (downloadTaskResult != null) {
                                    downloadTaskResult.error(hybridInfo, 105, ErrorCodeAndMessage.QP_DOWNLOAD_QP_LESS_USED_ERROR_MESSAGE);
                                }
                            }
                        }
                        return;
                    }
                    new File(hybridInfoById.path).delete();
                    this.mList.remove(hybridInfoById);
                } else {
                    this.mList.remove(hybridInfoById);
                    if (!hybridInfoById.path.equals(hybridInfo.path)) {
                        new File(hybridInfoById.path).delete();
                    }
                }
            } else {
                Timber.d("HyRes 没有旧文件 info = " + hybridInfo.toJsonString(), new Object[0]);
            }
            this.mList.add(hybridInfo);
            if (HyResInitializer.isDebug() && !HyResInitializer.isOnline()) {
                for (int i = 0; i < this.mList.size(); i++) {
                    Timber.d("HyRes list[" + i + "] info = " + ((HybridInfo) this.mList.get(i)).toJsonString(), new Object[0]);
                }
            }
            saveDatas();
            Timber.d("HyRes 保存缓存文件 info = " + hybridInfo.toJsonString(), new Object[0]);
            synchronized (DownloadManager.getInstance()) {
                for (DownloadTaskResult downloadTaskResult2 : set) {
                    if (downloadTaskResult2 != null) {
                        downloadTaskResult2.sucess(hybridInfo);
                    }
                }
            }
        }
    }

    public void addNewModule(String str, String str2) {
        Timber.d("HyRes 添加了离线资源包,filpath=" + str, new Object[0]);
        parseHyRes(str, str2);
    }

    private void parseHyRes(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            HybridInfoParser.parseAndCheck(str, this.mList, str2);
            saveDatas();
        }
    }

    public void addNewModuleFromAssert(AssetManager assetManager, String str, String str2) {
        Timber.d("HyRes 添加了离线资源包,assertname=" + str, new Object[0]);
        if (!TextUtils.isEmpty(str) && isCopyNewQp(str)) {
            long currentTimeMillis = System.currentTimeMillis();
            Timber.e(str + "begin:" + currentTimeMillis, new Object[0]);
            String copyAssertToSdcard = copyAssertToSdcard(assetManager, str);
            if (!TextUtils.isEmpty(copyAssertToSdcard)) {
                String assetFileToStr = AssetUtils.getAssetFileToStr(assetManager, str2);
                HybridInfo hybridInfoByFilePath = getHybridInfoByFilePath(copyAssertToSdcard);
                if (!(hybridInfoByFilePath != null && hybridInfoByFilePath.length == new File(copyAssertToSdcard).length() && RsaDecodeUtil.equals(MD5.getMD5(copyAssertToSdcard), hybridInfoByFilePath.getEncodedMd5()))) {
                    parseHyRes(copyAssertToSdcard, assetFileToStr);
                }
                Timber.e(str + "end:" + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            }
        }
    }

    private boolean isCopyNewQp(String str) {
        try {
            String substring = str.substring(0, str.lastIndexOf("_"));
            String substring2 = substring.substring(0, substring.lastIndexOf("_"));
            substring = substring.substring(substring.lastIndexOf("_") + 1, substring.length());
            HybridInfo hybridInfoById = getHybridInfoById(substring2);
            if (hybridInfoById == null) {
                Timber.e("IsCopyNewQp:true assertName:" + str, new Object[0]);
                return true;
            }
            if (hybridInfoById.version >= Integer.parseInt(substring)) {
                Timber.e("IsCopyNewQp:false assertName:" + str + " version:" + hybridInfoById.version, new Object[0]);
                return false;
            }
            Timber.e("IsCopyNewQp:" + true + " assertName:" + str, new Object[0]);
            return true;
        } catch (Throwable th) {
            Timber.e("IsCopyNewQp Exception! QP name is illegal!" + th.getMessage(), new Object[0]);
        }
    }

    private void saveDatas() {
        synchronized (this.listLock) {
            Iterator it = this.mList.iterator();
            while (it.hasNext()) {
                HybridInfo hybridInfo = (HybridInfo) it.next();
                hybridInfo.QpRequestType = 0;
                if (TextUtils.isEmpty(hybridInfo.path) || !new File(hybridInfo.path).exists()) {
                    it.remove();
                }
            }
            try {
                Editor edit = this.preferences.edit();
                edit.putString("hybrid_infos", JSON.toJSONString(this.mList));
                edit.apply();
            } catch (Throwable e) {
                Timber.e(e, "", new Object[0]);
            }
        }
    }

    public boolean hasUsedHybridInfo(String str) {
        if (TextUtils.isEmpty(str) || mHybrididSet == null) {
            return true;
        }
        return mHybrididSet.contains(str);
    }

    public HybridInfo getHybridInfoById(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (this.mList == null || this.mList.size() == 0) {
            return null;
        }
        for (HybridInfo hybridInfo : this.mList) {
            if (str.equals(hybridInfo.hybridId)) {
                return hybridInfo;
            }
        }
        return null;
    }

    public HybridInfo getHybridInfoByFilePath(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (this.mList == null || this.mList.size() == 0) {
            return null;
        }
        for (HybridInfo hybridInfo : this.mList) {
            if (str.equals(hybridInfo.path)) {
                return hybridInfo;
            }
        }
        return null;
    }

    public static HybridManager getInstance() {
        if (instance == null) {
            instance = new HybridManager();
        }
        return instance;
    }

    private HybridManager() {
        Timber.i("HybridManager>new>begin>parserHybridInfos" + System.currentTimeMillis(), new Object[0]);
        initManager();
        Timber.i("HybridManager>new>end>parserHybridInfos" + System.currentTimeMillis(), new Object[0]);
    }

    private void initManager() {
        if (TextUtils.isEmpty(this.DATA_PATH)) {
            this.DATA_PATH = QunarUtils.getAppFileDir(HyResInitializer.getContext()) + "/hybrid/";
            File file = new File(this.DATA_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        this.preferences = HyResInitializer.getContext().getSharedPreferences(HyResInitializer.SP_NAME, 0);
        parserHybridInfos();
    }

    private void parserHybridInfos() {
        mHybrididSet.clear();
        this.mList.clear();
        String string = this.preferences.getString("hybrid_infos", null);
        Timber.i("HyRes parserHybridInfos>old>" + string, new Object[0]);
        try {
            List parseArray = JSON.parseArray(string, HybridInfo.class);
        } catch (Exception e) {
            Object arrayList = new ArrayList();
        }
        List<HybridInfo> moveCacheList = SpCahceUtil.getInstance().moveCacheList();
        Timber.i("HyRes parserHybridInfos>rename>begin" + System.currentTimeMillis(), new Object[0]);
        if (moveCacheList != null && moveCacheList.size() > 0) {
            for (HybridInfo hybridInfo : moveCacheList) {
                if (moveToWorkspace(hybridInfo) != null) {
                    r1.add(hybridInfo);
                }
            }
        }
        Timber.i("HyRes parserHybridInfos>rename>end" + System.currentTimeMillis(), new Object[0]);
        final List arrayList2 = new ArrayList();
        if (r1 != null && r1.size() > 0) {
            for (HybridInfo hybridInfo2 : r1) {
                if (!(hybridInfo2 == null || TextUtils.isEmpty(hybridInfo2.path) || !new File(hybridInfo2.path).exists())) {
                    if (hybridInfo2.length == new File(hybridInfo2.path).length()) {
                        hybridInfo2.QpRequestType = 0;
                        hybridInfo2.checked = false;
                        arrayList2.add(hybridInfo2);
                        this.mList.add(hybridInfo2);
                    } else {
                        new File(hybridInfo2.path).delete();
                    }
                }
            }
        }
        saveDatas();
        parseThread = new Thread() {
            public void run() {
                super.run();
                long currentTimeMillis = System.currentTimeMillis();
                for (HybridInfo hybridInfo : arrayList2) {
                    HybridInfo parserManifest = HybridInfoParser.parserManifest(new File(hybridInfo.path), hybridInfo.getEncodedMd5());
                    if (parserManifest != null) {
                        hybridInfo.setResource(parserManifest.getActualResource());
                        hybridInfo.extra = parserManifest.extra;
                        hybridInfo.setManifestJson(parserManifest.getManifestJson());
                    }
                    Timber.i("Hyres parserManifest:>hybrid>::" + hybridInfo.hybridId + " time>::" + System.currentTimeMillis(), new Object[0]);
                }
                HybridManager.this.saveDatas();
                Timber.i("Hyres parserManifest:all>time>" + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
                HybridManager.parseFlag = true;
            }
        };
        parseFlag = false;
        parseThread.start();
    }

    public HybridInfo moveToWorkspace(HybridInfo hybridInfo) {
        File file = new File(hybridInfo.path);
        File file2 = new File(this.DATA_PATH, file.getName());
        if (file.exists()) {
            if (file.renameTo(file2)) {
                hybridInfo.path = file2.getAbsolutePath();
                hybridInfo.length = file2.length();
                return hybridInfo;
            } else if (file.exists()) {
                file.delete();
            }
        }
        return null;
    }

    public void addCacheList() {
        getInstance();
        List cacheList = SpCahceUtil.getInstance().getCacheList();
        if (cacheList == null || cacheList.size() <= 0) {
            Timber.i("HybridManager>addCacheList:no cache", new Object[0]);
            return;
        }
        Timber.i("HybridManager>begin>addCacheList" + System.currentTimeMillis(), new Object[0]);
        parserHybridInfos();
        Timber.i("HybridManager>end>addCacheList" + System.currentTimeMillis(), new Object[0]);
    }

    public String getHybrididByUrl(String str) {
        List hybridInfos = getHybridInfos();
        int i = 0;
        while (hybridInfos != null && i < hybridInfos.size()) {
            HybridInfo hybridInfo = (HybridInfo) hybridInfos.get(i);
            if (hybridInfo != null && hybridInfo.getActualResource().containsKey(UriCodec.getUrlWithOutQueryAndHash(str))) {
                return hybridInfo.hybridId;
            }
            i++;
        }
        return null;
    }

    public String getHybrididByUrlandParam(String str) {
        String queryParameter = QunarUtils.getQueryParameter(Uri.parse(str), "hybridid");
        if (!TextUtils.isEmpty(queryParameter)) {
            return queryParameter;
        }
        queryParameter = getHybrididByUrl(str);
        if (TextUtils.isEmpty(queryParameter)) {
            return HybridIdUtils.getHybrididByUrl(str);
        }
        return queryParameter;
    }

    public WebResourceResponse getResByUrl(String str) {
        return getResByUrlAndHyId(str, null);
    }

    public WebResourceResponse getResByUrlAndHyId(String str, String str2) {
        waitParseThread();
        Uri parse = Uri.parse(str);
        if (parse.isOpaque()) {
            return null;
        }
        HybridInfo hybridInfo;
        HybridFile hybridFile;
        CharSequence obtainMimeType = QHepburnMimeTypeUtils.obtainMimeType(str);
        if (str2 == null) {
            str2 = QunarUtils.getQueryParameter(parse, "hybridid");
        }
        if (TextUtils.isEmpty(str2)) {
            HybridFile hybridFile2;
            List hybridInfos = getHybridInfos();
            int i = 0;
            HybridFile hybridFile3 = null;
            HybridInfo hybridInfo2 = null;
            while (hybridInfos != null && i < hybridInfos.size()) {
                hybridInfo2 = (HybridInfo) hybridInfos.get(i);
                if (hybridInfo2 != null) {
                    hybridFile3 = hybridInfo2.getHybridFileByUrl(str);
                    if (hybridFile3 != null) {
                        mHybrididSet.add(hybridInfo2.hybridId);
                        hybridFile2 = hybridFile3;
                        hybridInfo = hybridInfo2;
                        hybridFile = hybridFile2;
                        break;
                    }
                }
                i++;
            }
            hybridFile2 = hybridFile3;
            hybridInfo = hybridInfo2;
            hybridFile = hybridFile2;
            if (hybridInfo == null || r0 == null) {
                if ("text/html".equals(obtainMimeType) || TextUtils.isEmpty(obtainMimeType)) {
                    String hybrididByUrl = HybridIdUtils.getHybrididByUrl(str);
                    Timber.i("HybridIdUtils.getHybrididByUrl(url):url:" + str + "; hybridId:" + hybrididByUrl, new Object[0]);
                    if (!TextUtils.isEmpty(hybrididByUrl)) {
                        mHybrididSet.add(hybrididByUrl);
                        hybridInfo2 = getHybridInfoById(hybrididByUrl);
                        if (hybridInfo2 == null) {
                            hybridInfo2 = getDefaultHybridInfo(hybrididByUrl);
                            Timber.i("CURRENNT_NO_QP,本地无qp,url无hybridid,请求qp:" + hybrididByUrl + "url:" + str, new Object[0]);
                        }
                        Timber.i("uppdate" + hybrididByUrl + "url:" + str, new Object[0]);
                        HyResInitializer.getInstance().sendSingleUpdateRequest(hybridInfo2);
                    }
                }
                return null;
            }
        }
        mHybrididSet.add(str2);
        hybridInfo = getHybridInfoById(str2);
        if (hybridInfo == null) {
            if ("text/html".equals(obtainMimeType) || TextUtils.isEmpty(obtainMimeType)) {
                Timber.i("CURRENNT_NO_QP,本地无qp,url有hybridid,请求qp:" + str2 + "url:" + str, new Object[0]);
                HyResInitializer.getInstance().sendSingleUpdateRequest(getDefaultHybridInfo(str2));
            }
            return null;
        }
        hybridFile = hybridInfo.getHybridFileByUrl(str);
        if (hybridFile == null) {
            if ("text/html".equals(obtainMimeType) || TextUtils.isEmpty(obtainMimeType)) {
                Timber.i("CURRENNT_HAS_QP,本地有当前的qp包，但是没有指定的url,请求qp:" + str2 + "url:" + str, new Object[0]);
                if (new File(hybridInfo.path).exists()) {
                    hybridInfo.QpRequestType = 2;
                    HyResInitializer.getInstance().sendSingleUpdateRequest(hybridInfo);
                }
            }
            return null;
        }
        File file = new File(hybridInfo.path);
        if (file.exists() && hybridInfo.length == file.length()) {
            if ("text/html".equals(hybridFile.mimeType) || TextUtils.isEmpty(hybridFile.mimeType)) {
                hybridInfo.QpRequestType = 2;
                Timber.i("CURRENNT_HAS_QP,本地有qp,检测更新,请求qp:" + hybridInfo.hybridId + "url:" + str, new Object[0]);
                HyResInitializer.getInstance().sendSingleUpdateRequest(hybridInfo);
            }
            try {
                Timber.v(String.format("HyRes 准备使用离线资源 url=%s,hybrid=%s", new Object[]{str, str2}), new Object[0]);
                if (CheckQpCompetence.getInstance().isQpCanUse(hybridInfo)) {
                    Timber.v("HyRes QP包" + hybridInfo.hybridId + " isQpCanUse = true", new Object[0]);
                    CheckQpCompetence.getInstance().setUsedHybridid(hybridInfo.hybridId);
                    return new WebResourceResponse(TextUtils.isEmpty(hybridFile.mimeType) ? "text/html" : hybridFile.mimeType, "utf-8", new BufferedInputStream(new QmpFileInputStream(hybridInfo.path, hybridFile.start, hybridFile.length), 32768));
                }
                Timber.v("HyRes QP包" + hybridInfo.hybridId + " isQpCanUse = false", new Object[0]);
                return null;
            } catch (Exception e) {
                Timber.e("HyRes hy_res getresponse", e);
                return null;
            }
        }
        hybridInfo.setMd5("***file deleted****");
        if (file.exists()) {
            file.delete();
        }
        this.mList.remove(hybridInfo);
        Timber.i("CURRENNT_NO_QP,本地无qp,文件被删除,请求qp:" + hybridInfo.hybridId + "url:" + str, new Object[0]);
        HyResInitializer.getInstance().sendSingleUpdateRequest(getDefaultHybridInfo(hybridInfo.hybridId));
        return null;
    }

    public HybridInfo getDefaultHybridInfo(String str) {
        HybridInfo hybridInfo = new HybridInfo();
        hybridInfo.hybridId = str;
        hybridInfo.QpRequestType = 1;
        hybridInfo.version = 0;
        hybridInfo.setMd5("");
        return hybridInfo;
    }

    public HybridInfo getHybridInfoAndStatus(String str) {
        HybridInfo hybridInfoById = getInstance().getHybridInfoById(str);
        if (hybridInfoById == null) {
            return getDefaultHybridInfo(str);
        }
        hybridInfoById.QpRequestType = 2;
        return hybridInfoById;
    }

    public HybridInfo getCustomerHybridInfo(String str) {
        HybridInfo hybridInfoById = getInstance().getHybridInfoById(str);
        HybridInfo hybridInfo = new HybridInfo();
        hybridInfo.hybridId = str;
        hybridInfo.QpRequestType = 5;
        if (hybridInfoById != null) {
            hybridInfo.version = hybridInfoById.version;
            hybridInfo.setMd5(hybridInfoById.getMd5());
        } else {
            hybridInfo.version = 0;
            hybridInfo.setMd5("");
        }
        return hybridInfo;
    }

    public List<String> getUrlsByHyId(String str) {
        HybridInfo hybridInfoById = getHybridInfoById(str);
        if (hybridInfoById == null) {
            return null;
        }
        List<String> arrayList = new ArrayList();
        if (hybridInfoById.errorChanged || hybridInfoById.getActualResource() == null) {
            return arrayList;
        }
        arrayList.addAll(hybridInfoById.getActualResource().keySet());
        return arrayList;
    }

    public JSONObject getExtraByHyId(String str) {
        HybridInfo hybridInfoById = getHybridInfoById(str);
        return hybridInfoById == null ? null : hybridInfoById.extra;
    }

    public void waitParseThread() {
        if (!parseFlag) {
            try {
                if (parseThread != null) {
                    parseThread.join();
                }
            } catch (Throwable e) {
                Timber.e(e, "", new Object[0]);
            }
        }
    }

    public boolean isForceUpgrade(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        HybridInfo hybridInfo = UpgradeInfoCache.getInstance().getHybridInfo(str);
        if (hybridInfo == null || !hybridInfo.isForce()) {
            return false;
        }
        HybridInfo hybridInfoById = getHybridInfoById(str);
        if (hybridInfoById == null) {
            return true;
        }
        if (hybridInfo.version > hybridInfoById.version) {
            return true;
        }
        return false;
    }
}
