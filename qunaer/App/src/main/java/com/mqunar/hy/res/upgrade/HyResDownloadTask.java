package com.mqunar.hy.res.upgrade;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.mqunar.hy.res.DownloadTaskResult;
import com.mqunar.hy.res.HybridManager;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridInfo;
import com.mqunar.hy.res.model.HybridParam;
import com.mqunar.hy.res.utils.DownloadData;
import com.mqunar.hy.res.utils.DownloadManager;
import com.mqunar.hy.res.utils.ErrorCodeAndMessage;
import com.mqunar.hy.res.utils.HybridInfoParser;
import com.mqunar.hy.res.utils.QunarUtils;
import com.mqunar.hy.res.utils.SpCahceUtil;
import com.mqunar.hy.res.utils.StatisticsUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.HttpHost;
import qunar.lego.utils.diffpatch.DiffPatch;
import qunar.lego.utils.diffpatch.MD5;

public class HyResDownloadTask {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private final File cachePath;
    private Context context;
    private DownloadManager downloadManager = DownloadManager.getInstance();
    private HybridParam localParam;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            DownloadData downloadData;
            switch (message.what) {
                case 1001:
                    downloadData = (DownloadData) message.obj;
                    HyResDownloadTask.this.moveAtom(downloadData);
                    downloadData.savefile.delete();
                    HyResDownloadTask.this.runNext();
                    return;
                case 1002:
                    Timber.w("WTF", new Object[0]);
                    downloadData = (DownloadData) message.obj;
                    HybridInfo hybridInfo = new HybridInfo();
                    hybridInfo.hybridId = downloadData.hybridid;
                    try {
                        hybridInfo.version = Integer.parseInt(downloadData.nversion);
                    } catch (Exception e) {
                        e.printStackTrace();
                        hybridInfo.version = 0;
                    }
                    Set<DownloadTaskResult> downloadTaskResult = HyResDownloadTask.this.downloadManager.getDownloadTaskResult(hybridInfo);
                    synchronized (HyResDownloadTask.this.downloadManager) {
                        for (DownloadTaskResult downloadTaskResult2 : downloadTaskResult) {
                            if (downloadTaskResult2 != null) {
                                downloadTaskResult2.error(hybridInfo, ErrorCodeAndMessage.QP_DOWNLOAD_ERROR_CODE, ErrorCodeAndMessage.QP_DOWNLOAD_ERROR_MESSAGE);
                            }
                        }
                    }
                    HyResDownloadTask.this.runNext();
                    return;
                default:
                    return;
            }
        }
    };
    private List<HybridInfo> mInfos;
    private final File targetPath;
    private DownloadTaskResult<HybridInfo> taskResult = null;

    public HyResDownloadTask(Context context, HybridParam hybridParam) {
        this.context = context;
        this.localParam = hybridParam;
        this.targetPath = new File(QunarUtils.getAppFileDir(context) + "/hybrid/");
        this.cachePath = new File(QunarUtils.getAppFileDir(context) + "/caches/");
        if (!(this.targetPath.exists() || this.targetPath.mkdirs())) {
            Timber.e("make dir failed : %s", this.targetPath);
        }
        if (!this.cachePath.exists() && !this.cachePath.mkdirs()) {
            Timber.e("make dir failed : %s", this.cachePath);
        }
    }

    public void run(List<HybridInfo> list, DownloadTaskResult<HybridInfo> downloadTaskResult) {
        this.taskResult = downloadTaskResult;
        if (list != null) {
            List updateModuleInfo = updateModuleInfo(list);
            if (updateModuleInfo != null && updateModuleInfo.size() != 0) {
                downloadModules(updateModuleInfo);
            }
        }
    }

    private List<HybridInfo> updateModuleInfo(List<HybridInfo> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<HybridInfo> arrayList = new ArrayList();
        boolean isWifi = isWifi(this.context);
        for (HybridInfo hybridInfo : list) {
            if (SpCahceUtil.getInstance().isToDownloadQp(hybridInfo)) {
                if (hybridInfo.url.startsWith(HttpHost.DEFAULT_SCHEME_NAME)) {
                    HybridInfo hybridInfo2;
                    int i = 0;
                    while (this.localParam != null && this.localParam.hlist != null && i < this.localParam.hlist.size()) {
                        if (((HybridInfo) this.localParam.hlist.get(i)).hybridId.equals(hybridInfo.hybridId)) {
                            hybridInfo2 = (HybridInfo) this.localParam.hlist.get(i);
                            break;
                        }
                        i++;
                    }
                    hybridInfo2 = null;
                    if (hybridInfo2 != null) {
                        if (hybridInfo2.version < hybridInfo.version) {
                            if (isWifi) {
                                arrayList.add(hybridInfo);
                            } else if (hybridInfo.QpRequestType == 2 || hybridInfo.QpRequestType == 3 || hybridInfo.QpRequestType == 5 || hybridInfo.QpRequestType == 6) {
                                arrayList.add(hybridInfo);
                                Timber.i("filter>current_has_qp非wifi，没返回patchurl,下载:" + hybridInfo.url, new Object[0]);
                            }
                        }
                    } else if (isWifi) {
                        arrayList.add(hybridInfo);
                    } else if (hybridInfo.QpRequestType == 1 || hybridInfo.QpRequestType == 2 || hybridInfo.QpRequestType == 3 || hybridInfo.QpRequestType == 5 || hybridInfo.QpRequestType == 6) {
                        arrayList.add(hybridInfo);
                        Timber.i("filter>current_no_qp非wifi,下载单个模块:" + hybridInfo.url, new Object[0]);
                    }
                } else if (this.taskResult != null) {
                    this.taskResult.error(hybridInfo, 102, ErrorCodeAndMessage.SERVER_ERROR_URL_MESSAGE);
                }
            } else if (this.taskResult != null) {
                this.taskResult.sucess(hybridInfo);
            }
        }
        return arrayList;
    }

    public static boolean isWifi(Context context) {
        NetworkInfo activeNetworkInfo;
        try {
            activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Throwable e) {
            Timber.e(e, "", new Object[0]);
            activeNetworkInfo = null;
        }
        if (activeNetworkInfo == null || activeNetworkInfo.getType() != 1) {
            return false;
        }
        return true;
    }

    private void downloadModules(List<HybridInfo> list) {
        this.mInfos = list;
        if (this.mInfos != null && this.mInfos.size() != 0) {
            for (HybridInfo hybridInfo : this.mInfos) {
                String str;
                File file;
                String str2;
                DownloadData downloadData;
                String str3 = hybridInfo.url;
                if (!(TextUtils.isEmpty(hybridInfo.patchUrl) || TextUtils.isEmpty(hybridInfo.getEncodedMd5()))) {
                    HybridInfo localModeInfo = getLocalModeInfo(hybridInfo.hybridId);
                    if (!(localModeInfo == null || TextUtils.isEmpty(localModeInfo.path) || !new File(localModeInfo.path).exists())) {
                        str = hybridInfo.patchUrl;
                        if (str != null && str.length() > 0) {
                            if (str.contains("/")) {
                                file = new File(this.targetPath, str.substring(str.lastIndexOf(47) + 1));
                                if (file.exists()) {
                                    if (HybridManager.getInstance().getHybridInfoById(hybridInfo.hybridId) != null) {
                                        file.delete();
                                    } else if (this.taskResult != null) {
                                        this.taskResult.sucess(hybridInfo);
                                    }
                                }
                            }
                            if (hybridInfo.QpRequestType == 1) {
                                DownloadManager.getInstance().add(hybridInfo.hybridId, str, String.valueOf(hybridInfo.version), hybridInfo.QpRequestType, this.mHandler);
                                this.downloadManager.setDownloadTaskResult(hybridInfo, this.taskResult);
                                Timber.w("added url -> " + str, new Object[0]);
                            } else if (DownloadManager.getInstance().isRun(str)) {
                                for (Entry entry : DownloadManager.getInstance().nowDownloadMap.entrySet()) {
                                    str2 = (String) entry.getKey();
                                    downloadData = (DownloadData) entry.getValue();
                                    DownloadManager.getInstance().cancel(str2);
                                    DownloadManager.getInstance().add(downloadData.hybridid, str2, downloadData.nversion, downloadData.type, downloadData.handler);
                                    Timber.w("CURRENNT_NO_QP>remove>now download>当前访问qp下载。暂停正在下的qp:-> " + str2 + "当前模块qp,优先级高：" + str, new Object[0]);
                                }
                                this.downloadManager.add(hybridInfo.hybridId, str, String.valueOf(hybridInfo.version), 1, this.mHandler);
                                this.downloadManager.setDownloadTaskResult(hybridInfo, this.taskResult);
                                this.downloadManager.start(str);
                                Timber.w("CURRENNT_NO_QP>download>new>current>qp 当前访问qp下载:-> " + str, new Object[0]);
                                return;
                            } else {
                                Timber.w("CURRENNT_NO_QP>download>new>current>qp 当前qp正在下载:-> " + str, new Object[0]);
                                return;
                            }
                        }
                    }
                }
                str = str3;
                if (str.contains("/")) {
                    file = new File(this.targetPath, str.substring(str.lastIndexOf(47) + 1));
                    if (file.exists()) {
                        if (HybridManager.getInstance().getHybridInfoById(hybridInfo.hybridId) != null) {
                            file.delete();
                        } else if (this.taskResult != null) {
                            this.taskResult.sucess(hybridInfo);
                        }
                    }
                }
                if (hybridInfo.QpRequestType == 1) {
                    DownloadManager.getInstance().add(hybridInfo.hybridId, str, String.valueOf(hybridInfo.version), hybridInfo.QpRequestType, this.mHandler);
                    this.downloadManager.setDownloadTaskResult(hybridInfo, this.taskResult);
                    Timber.w("added url -> " + str, new Object[0]);
                } else if (DownloadManager.getInstance().isRun(str)) {
                    Timber.w("CURRENNT_NO_QP>download>new>current>qp 当前qp正在下载:-> " + str, new Object[0]);
                    return;
                } else {
                    for (Entry entry2 : DownloadManager.getInstance().nowDownloadMap.entrySet()) {
                        str2 = (String) entry2.getKey();
                        downloadData = (DownloadData) entry2.getValue();
                        DownloadManager.getInstance().cancel(str2);
                        DownloadManager.getInstance().add(downloadData.hybridid, str2, downloadData.nversion, downloadData.type, downloadData.handler);
                        Timber.w("CURRENNT_NO_QP>remove>now download>当前访问qp下载。暂停正在下的qp:-> " + str2 + "当前模块qp,优先级高：" + str, new Object[0]);
                    }
                    this.downloadManager.add(hybridInfo.hybridId, str, String.valueOf(hybridInfo.version), 1, this.mHandler);
                    this.downloadManager.setDownloadTaskResult(hybridInfo, this.taskResult);
                    this.downloadManager.start(str);
                    Timber.w("CURRENNT_NO_QP>download>new>current>qp 当前访问qp下载:-> " + str, new Object[0]);
                    return;
                }
            }
            if (!DownloadManager.getInstance().hasRun()) {
                runNext();
                Timber.w("downloadModules>runNext-> ", new Object[0]);
            }
        }
    }

    private void runNext() {
        if (DownloadManager.getInstance().runNext()) {
            Timber.w("ALL DONE>DOWNLOAD>：" + System.currentTimeMillis(), new Object[0]);
            this.downloadManager.clearDownloadTaskResult();
        }
    }

    private HybridInfo getLocalModeInfo(String str) {
        if (this.localParam == null || this.localParam.hlist == null || TextUtils.isEmpty(str)) {
            return null;
        }
        for (HybridInfo hybridInfo : this.localParam.hlist) {
            if (str.equals(hybridInfo.hybridId)) {
                return hybridInfo;
            }
        }
        return null;
    }

    private void moveAtom(DownloadData downloadData) {
        File file;
        String str = downloadData.url;
        for (final HybridInfo hybridInfo : this.mInfos) {
            if (!str.equals(hybridInfo.patchUrl)) {
                if (str.equals(hybridInfo.url)) {
                    break;
                }
            }
            HybridInfo localModeInfo = getLocalModeInfo(hybridInfo.hybridId);
            if (localModeInfo == null || TextUtils.isEmpty(localModeInfo.path)) {
                Timber.e(new NullPointerException("localInfo/localInfo.path = null"), "合并" + hybridInfo.patchUrl + "过程出错，重新下载", new Object[0]);
                return;
            }
            Timber.w("patch file, downloadData.url = %s,downloadData.savefilePath = %s", downloadData.url, downloadData.savefile);
            try {
                file = new File(this.cachePath, hybridInfo.url.substring(hybridInfo.url.lastIndexOf(47) + 1));
                Timber.w("start bspatch, newPath = %s", file);
                long currentTimeMillis = System.currentTimeMillis();
                DiffPatch.bspatch(localModeInfo.path, file.getAbsolutePath(), downloadData.savefile.getAbsolutePath());
                Timber.w("end bspatch, use time = %s, start calculate file md5", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                currentTimeMillis = System.currentTimeMillis();
                Timber.w("end calculate file md5, use time = %s,newFile md5 = %s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), MD5.getMD5(file));
                hybridInfo.path = file.getAbsolutePath();
                hybridInfo.length = file.length();
                if (HybridInfoParser.checkQPFile(hybridInfo)) {
                    final Set downloadTaskResult = this.downloadManager.getDownloadTaskResult(hybridInfo);
                    handler.post(new Runnable() {
                        public void run() {
                            HyResDownloadTask.this.addNewModule(hybridInfo, downloadTaskResult);
                        }
                    });
                    return;
                }
                Timber.w("md5 equals failed, server return md5 = " + hybridInfo.getEncodedMd5(), new Object[0]);
                Timber.e("bspatch error,patchUrl = " + hybridInfo.patchUrl + ", 合成文件md5 = " + r0 + ",server return md5 = " + hybridInfo.getEncodedMd5(), new Object[0]);
                this.downloadManager.add(hybridInfo.hybridId, hybridInfo.url, String.valueOf(hybridInfo.version), hybridInfo.QpRequestType, this.mHandler);
                Set<DownloadTaskResult> downloadTaskResult2 = this.downloadManager.getDownloadTaskResult(hybridInfo);
                synchronized (this.downloadManager) {
                    for (DownloadTaskResult downloadTaskResult3 : downloadTaskResult2) {
                        this.downloadManager.setDownloadTaskResult(hybridInfo, downloadTaskResult3);
                    }
                }
                runNext();
                return;
            } catch (Throwable e) {
                Timber.e(e, "合并" + hybridInfo.patchUrl + "过程出错，重新下载", new Object[0]);
            }
        }
        HybridInfo hybridInfo2 = null;
        file = new File(this.cachePath, downloadData.savefile.getName().replace("{" + downloadData.nversion + "}", ""));
        Timber.w("moveing atom from %s to %s", downloadData.savefile.getAbsolutePath(), file.getAbsolutePath());
        boolean renameTo = downloadData.savefile.renameTo(file);
        if (!renameTo || hybridInfo2 == null) {
            if (downloadData.savefile.exists()) {
                downloadData.savefile.delete();
            }
            if (file.exists()) {
                file.delete();
            }
            downloadTaskResult2 = this.downloadManager.getDownloadTaskResult(hybridInfo2);
            synchronized (this.downloadManager) {
                for (DownloadTaskResult downloadTaskResult32 : downloadTaskResult2) {
                    if (downloadTaskResult32 != null) {
                        downloadTaskResult32.error(hybridInfo2, 104, ErrorCodeAndMessage.FILE_MOVE__ERROR_MESSAGE);
                    }
                }
            }
            Timber.w("moveatom failed" + downloadData.savefile.getName() + renameTo, new Object[0]);
            return;
        }
        hybridInfo2.path = file.getAbsolutePath();
        hybridInfo2.length = file.length();
        if (HybridInfoParser.checkQPFile(hybridInfo2)) {
            Timber.w("moveatom successed" + downloadData.savefile.getName(), new Object[0]);
            downloadTaskResult = this.downloadManager.getDownloadTaskResult(hybridInfo2);
            handler.post(new Runnable() {
                public void run() {
                    HyResDownloadTask.this.addNewModule(hybridInfo2, downloadTaskResult);
                }
            });
            return;
        }
        downloadTaskResult2 = this.downloadManager.getDownloadTaskResult(hybridInfo2);
        synchronized (this.downloadManager) {
            for (DownloadTaskResult downloadTaskResult322 : downloadTaskResult2) {
                if (downloadTaskResult322 != null) {
                    downloadTaskResult322.error(hybridInfo2, 103, ErrorCodeAndMessage.QP_VERIFY_ERROR_MESSAGE);
                }
            }
        }
        file.delete();
        Timber.w("download>checkQpFile failed" + downloadData.savefile.getName(), new Object[0]);
    }

    private void addNewModule(HybridInfo hybridInfo, Set<DownloadTaskResult<HybridInfo>> set) {
        Timber.w("HyRes 下载校验完成，添加新模块addNewModule:-> type: " + hybridInfo.QpRequestType + "; info:" + hybridInfo.toJsonString(), new Object[0]);
        HybridInfo parserManifest = HybridInfoParser.parserManifest(new File(hybridInfo.path), hybridInfo.getEncodedMd5());
        if (parserManifest != null) {
            hybridInfo.setResource(parserManifest.getActualResource());
            hybridInfo.extra = parserManifest.extra;
            hybridInfo.setManifestJson(parserManifest.getManifestJson());
            if (hybridInfo.QpRequestType == 1 || hybridInfo.QpRequestType == 3 || HybridManager.getInstance().getHybridInfoById(hybridInfo.hybridId) == null || !HybridManager.getInstance().hasUsedHybridInfo(hybridInfo.hybridId)) {
                File file = new File(hybridInfo.path);
                File file2 = new File(this.targetPath, file.getName());
                if (!file.exists()) {
                    return;
                }
                if (file2.exists()) {
                    HybridInfo hybridInfoById = HybridManager.getInstance().getHybridInfoById(hybridInfo.hybridId);
                    if (hybridInfoById == null || !HybridInfoParser.checkQPFile(hybridInfoById)) {
                        file2.delete();
                        if (file.renameTo(file2)) {
                            hybridInfo.path = file2.getAbsolutePath();
                            hybridInfo.length = file2.length();
                            HybridManager.getInstance().addNewModule(hybridInfo, (Set) set);
                            Timber.w("HyRes addNewModule>立即生效" + hybridInfo.toJsonString() + "; path=" + hybridInfo.path, new Object[0]);
                            return;
                        }
                        if (file.exists()) {
                            file.delete();
                        }
                        if (file2.exists()) {
                            file2.delete();
                        }
                        synchronized (this.downloadManager) {
                            for (DownloadTaskResult downloadTaskResult : set) {
                                if (downloadTaskResult != null) {
                                    downloadTaskResult.error(parserManifest, 104, ErrorCodeAndMessage.FILE_MOVE__ERROR_MESSAGE);
                                }
                            }
                        }
                        return;
                    }
                    file.delete();
                    synchronized (this.downloadManager) {
                        for (DownloadTaskResult downloadTaskResult2 : set) {
                            if (downloadTaskResult2 != null) {
                                downloadTaskResult2.sucess(hybridInfoById);
                            }
                        }
                    }
                    return;
                } else if (file.renameTo(file2)) {
                    hybridInfo.path = file2.getAbsolutePath();
                    hybridInfo.length = file2.length();
                    HybridManager.getInstance().addNewModule(hybridInfo, (Set) set);
                    Timber.w("HyRes addNewModule>立即生效" + hybridInfo.toJsonString() + "; path=" + hybridInfo.path, new Object[0]);
                    return;
                } else {
                    if (file.exists()) {
                        file.delete();
                    }
                    if (file2.exists()) {
                        file2.delete();
                    }
                    synchronized (this.downloadManager) {
                        for (DownloadTaskResult downloadTaskResult22 : set) {
                            if (downloadTaskResult22 != null) {
                                downloadTaskResult22.error(parserManifest, 104, ErrorCodeAndMessage.FILE_MOVE__ERROR_MESSAGE);
                            }
                        }
                    }
                    return;
                }
            }
            SpCahceUtil.getInstance().saveCacheHyInfo(hybridInfo);
            Timber.w("HyRes addNewModule>saveCacheHyInfo>缓存目录:" + hybridInfo.toJsonString() + "; Path=" + hybridInfo.path, new Object[0]);
            synchronized (this.downloadManager) {
                for (DownloadTaskResult downloadTaskResult222 : set) {
                    if (downloadTaskResult222 != null) {
                        downloadTaskResult222.sucess(hybridInfo);
                    }
                }
            }
            StatisticsUtil.qpDownloadNotReplace(hybridInfo.hybridId, hybridInfo.version + "");
        }
    }
}
