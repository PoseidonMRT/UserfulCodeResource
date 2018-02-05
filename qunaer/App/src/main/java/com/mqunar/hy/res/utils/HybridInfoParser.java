package com.mqunar.hy.res.utils;

import android.text.TextUtils;
import com.mqunar.hy.res.HybridManager;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridFile;
import com.mqunar.hy.res.model.HybridInfo;
import com.mqunar.hy.res.model.HybridManifest;
import com.mqunar.hy.res.model.HybridManifest.ResItem;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import qunar.lego.utils.diffpatch.MD5;

public class HybridInfoParser {
    public static synchronized HybridInfo parseAndCheck(String str, List<HybridInfo> list, String str2) {
        HybridInfo hybridInfo;
        synchronized (HybridInfoParser.class) {
            if (TextUtils.isEmpty(str)) {
                hybridInfo = null;
            } else {
                int i = 0;
                while (list != null) {
                    if (i >= list.size()) {
                        break;
                    }
                    hybridInfo = (HybridInfo) list.get(i);
                    if (str.equals(hybridInfo.path)) {
                        break;
                    }
                    i++;
                }
                hybridInfo = null;
                File file = new File(str);
                if (hybridInfo != null) {
                    hybridInfo.checked = false;
                    hybridInfo.errorChanged = false;
                    if (!checkQPFile(hybridInfo)) {
                        file.delete();
                        list.remove(hybridInfo);
                        hybridInfo = null;
                    }
                } else {
                    HybridInfo parserManifest = parserManifest(file, str2);
                    if (parserManifest == null) {
                        file.delete();
                        hybridInfo = null;
                    } else {
                        if (list != null) {
                            for (int size = list.size() - 1; size > -1; size--) {
                                hybridInfo = (HybridInfo) list.get(size);
                                if (!TextUtils.isEmpty(parserManifest.hybridId) && parserManifest.hybridId.equals(hybridInfo.hybridId)) {
                                    if (hybridInfo.version > parserManifest.version) {
                                        new File(parserManifest.path).delete();
                                        hybridInfo = null;
                                        break;
                                    }
                                    new File(hybridInfo.path).delete();
                                    list.remove(hybridInfo);
                                }
                            }
                        }
                        if (list != null) {
                            list.add(parserManifest);
                        }
                        hybridInfo = parserManifest;
                    }
                }
            }
        }
        return hybridInfo;
    }

    public static boolean checkQPFile(HybridInfo hybridInfo) {
        try {
            if (hybridInfo.errorChanged) {
                return false;
            }
            if (hybridInfo.checked) {
                return true;
            }
            File file = new File(hybridInfo.path);
            if (file.length() == hybridInfo.length) {
                long currentTimeMillis = System.currentTimeMillis();
                String md5 = MD5.getMD5(file);
                Timber.i("CheckQPFile>MD5>TIME:" + (System.currentTimeMillis() - currentTimeMillis) + " file:" + file.getAbsolutePath(), new Object[0]);
                if (RsaDecodeUtil.equals(md5, hybridInfo.getMd5())) {
                    hybridInfo.checked = true;
                    FileObserverManager.addHybridInfoObserver(hybridInfo);
                    return true;
                }
            }
            hybridInfo.setMd5("***md5 error****");
            hybridInfo.version = 0;
            hybridInfo.errorChanged = true;
            if (file.exists()) {
                file.delete();
            }
            if (HybridManager.getInstance().getHybridInfos().contains(hybridInfo)) {
                HybridManager.getInstance().getHybridInfos().remove(hybridInfo);
            }
            HybridManager.getInstance().getDefaultHybridInfo(hybridInfo.hybridId).QpRequestType = 3;
            Timber.i("md5 error:sendSingleUpdateRequest" + hybridInfo.toJsonString(), new Object[0]);
            return false;
        } catch (Throwable th) {
            Timber.i("md5 error:exception no sendSingleUpdateRequest" + hybridInfo.toJsonString(), new Object[0]);
            return false;
        }
    }

    public static HybridInfo parserManifest(File file, String str) {
        Throwable e;
        FileInputStream fileInputStream;
        if (file == null || !file.exists() || file.length() < ((long) 6)) {
            return null;
        }
        HybridInfo hybridInfo = new HybridInfo();
        hybridInfo.path = file.getAbsolutePath();
        hybridInfo.length = file.length();
        hybridInfo.setMd5(str);
        FileInputStream fileInputStream2;
        try {
            fileInputStream2 = new FileInputStream(file);
            try {
                byte[] bArr = new byte[6];
                fileInputStream2.read(bArr, 0, 6);
                if ("QP".equalsIgnoreCase(new String(bArr, 0, 2, "UTF-8"))) {
                    int i = getInt(bArr, 2);
                    bArr = new byte[i];
                    if (fileInputStream2.read(bArr, 0, i) != -1) {
                        Timber.d(new String(bArr), new Object[0]);
                        HybridManifest hybridManifest = new HybridManifest(new String(bArr, 0, i, "UTF-8"));
                        hybridInfo.setManifestJson(hybridManifest.getManifestJson());
                        hybridInfo.version = hybridManifest.getVersion();
                        hybridInfo.hybridId = hybridManifest.getHybridid();
                        hybridInfo.extra = hybridManifest.getExtra();
                        List<ResItem> files = hybridManifest.getFiles();
                        if (files != null && files.size() > 0) {
                            Map hashMap = new HashMap(files.size());
                            for (ResItem resItem : files) {
                                hashMap.put(UriCodec.getUrlWithOutQueryAndHash(resItem.getUrl()), new HybridFile(resItem, 6 + i));
                            }
                            hybridInfo.setResource(hashMap);
                        }
                        if (fileInputStream2 != null) {
                            try {
                                fileInputStream2.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        return hybridInfo;
                    }
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    return null;
                }
                if (fileInputStream2 != null) {
                    try {
                        fileInputStream2.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                }
                return null;
            } catch (Exception e3) {
                e = e3;
                fileInputStream = fileInputStream2;
                try {
                    Timber.e(e, new Object[0]);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e2222) {
                            e2222.printStackTrace();
                        }
                    }
                    return null;
                } catch (Throwable th) {
                    e = th;
                    fileInputStream2 = fileInputStream;
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw e;
                }
            } catch (Throwable th2) {
                e = th2;
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
                throw e;
            }
        } catch (Exception e5) {
            e = e5;
            fileInputStream = null;
            Timber.e(e, new Object[0]);
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return null;
        } catch (Throwable th3) {
            e = th3;
            fileInputStream2 = null;
            if (fileInputStream2 != null) {
                fileInputStream2.close();
            }
            throw e;
        }
    }

    public static int getInt(byte[] bArr, int i) {
        return (((convertbytetoint(bArr[i + 3]) << 24) + (convertbytetoint(bArr[i + 2]) << 16)) + (convertbytetoint(bArr[i + 1]) << 8)) + convertbytetoint(bArr[i]);
    }

    public static int convertbytetoint(byte b) {
        return b & 255;
    }
}
