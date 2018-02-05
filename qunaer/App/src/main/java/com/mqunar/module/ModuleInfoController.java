package com.mqunar.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.core.DexPathList;
import com.mqunar.core.ModuleParser;
import com.mqunar.core.QunarApkLoader;
import com.mqunar.core.dependency.AtomNode;
import com.mqunar.core.dependency.AtomNode.DeleteSoCallback;
import com.mqunar.core.dependency.Dependency;
import com.mqunar.dispatcher.SchemeManager;
import com.mqunar.json.JsonUtils;
import com.mqunar.module.ModuleInfo.DexInfo;
import com.mqunar.storage.Storage;
import com.mqunar.tools.ArrayUtils;
import com.mqunar.tools.log.QLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModuleInfoController {
    public static String LIB_PATH;
    private static List<MInfo> a;
    private static String b;
    private static String c;
    private static String d;
    private static BroadcastReceiver e;
    private static Dependency f;
    private static boolean g;
    public static Modules modules = new Modules();
    public static String[] preLoad;

    public static List<MInfo> getMInfoList() {
        if (!g) {
            g = true;
            if (ArrayUtils.isEmpty(a)) {
                Storage newStorage = Storage.newStorage(QunarApkLoader.getAppContext(), "spider_minfo");
                if (newStorage != null) {
                    Object string = newStorage.getString("minfo_json", "");
                    if (!TextUtils.isEmpty(string) && ArrayUtils.isEmpty(a)) {
                        try {
                            a = JsonUtils.parseArray(string, MInfo.class);
                        } catch (Throwable e) {
                            QLog.crash(e, "parse minfo json error!");
                        }
                    }
                    b = newStorage.getString("minfo_vid", "");
                }
            }
        }
        return a;
    }

    public static boolean isWifi(Context context) {
        NetworkInfo activeNetworkInfo;
        try {
            activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception e) {
            activeNetworkInfo = null;
        }
        if (activeNetworkInfo == null) {
            return false;
        }
        if (activeNetworkInfo.getType() == 1) {
            return true;
        }
        return false;
    }

    public static MInfo getMInfo(ModuleInfo moduleInfo) {
        if (moduleInfo != null) {
            return getMInfo(moduleInfo.name);
        }
        return null;
    }

    public static MInfo getMInfo(String str) {
        List<MInfo> mInfoList = getMInfoList();
        if (!(mInfoList == null || str == null)) {
            for (MInfo mInfo : mInfoList) {
                if (str.equals(mInfo.packageName)) {
                    return mInfo;
                }
            }
        }
        return null;
    }

    public static String updateModuleInfo(String str) {
        Storage newStorage = Storage.newStorage(QunarApkLoader.getAppContext(), "spider_minfo");
        newStorage.putString("minfo_json", str);
        newStorage.putString("minfo_vid", GlobalEnv.getInstance().getVid());
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        a = JsonUtils.parseArray(str, MInfo.class);
        b = GlobalEnv.getInstance().getVid();
        boolean isWifi = isWifi(QunarApkLoader.getAppContext());
        Set hashSet = new HashSet();
        if (a != null) {
            for (MInfo mInfo : a) {
                ModuleInfo moduleInfo = (ModuleInfo) modules.get(mInfo.packageName);
                if (moduleInfo != null) {
                    mInfo.fileName = moduleInfo.fileName;
                    if (moduleInfo.localVersion < mInfo.version) {
                        if (isWifi) {
                            mInfo.downloadFlag = (byte) -1;
                            hashSet.add(mInfo);
                        } else if (mInfo.restart == (byte) 1) {
                            mInfo.downloadFlag = (byte) -1;
                            hashSet.add(mInfo);
                        } else if (!TextUtils.isEmpty(mInfo.patchUrl) && !TextUtils.isEmpty(mInfo.md5)) {
                            mInfo.downloadFlag = (byte) -1;
                            hashSet.add(mInfo);
                        } else if (preLoad != null) {
                            for (String equals : preLoad) {
                                if (equals.equals(moduleInfo.name)) {
                                    mInfo.downloadFlag = (byte) -1;
                                    hashSet.add(mInfo);
                                    break;
                                }
                            }
                        }
                    }
                } else if (isWifi) {
                    mInfo.downloadFlag = (byte) -1;
                    hashSet.add(mInfo);
                }
            }
        }
        return JsonUtils.toJsonString(hashSet);
    }

    public static String getDownloadJson(ModuleInfo moduleInfo) {
        Collection<MInfo> mInfoList = getMInfoList();
        if (ArrayUtils.isEmpty((Collection) mInfoList)) {
            return null;
        }
        boolean isWifi = isWifi(QunarApkLoader.getAppContext());
        ArrayList arrayList = new ArrayList();
        for (MInfo mInfo : mInfoList) {
            if (mInfo.downloadFlag != (byte) -1) {
                if (!moduleInfo.name.equals(mInfo.packageName)) {
                    ModuleInfo moduleInfo2 = (ModuleInfo) modules.get(mInfo.packageName);
                    if (moduleInfo2 != null) {
                        if (moduleInfo2.localVersion < mInfo.version) {
                            mInfo.fileName = moduleInfo2.fileName;
                            if (!TextUtils.isEmpty(mInfo.patchUrl) && !TextUtils.isEmpty(mInfo.md5)) {
                                mInfo.downloadFlag = (byte) -1;
                                arrayList.add(mInfo);
                            } else if (isWifi) {
                                mInfo.downloadFlag = (byte) -1;
                                arrayList.add(mInfo);
                            }
                        }
                    } else if (isWifi) {
                        mInfo.downloadFlag = (byte) -1;
                        arrayList.add(mInfo);
                    }
                } else if (moduleInfo.localVersion < mInfo.version) {
                    mInfo.fileName = moduleInfo.fileName;
                    mInfo.downloadFlag = (byte) -1;
                    arrayList.add(mInfo);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return JsonUtils.toJsonString(arrayList);
    }

    private ModuleInfoController() {
    }

    private static boolean b(ModuleInfo moduleInfo) {
        if (moduleInfo == null || TextUtils.isEmpty(moduleInfo.fileName) || !ModuleParser.isAtom(moduleInfo.fileName)) {
            return false;
        }
        if (moduleInfo.fileName.indexOf("libq_lib_") > 0) {
            moduleInfo.type = (byte) 1;
        }
        moduleInfo.initDexList();
        return QunarApkLoader.registerModule(moduleInfo);
    }

    public static void registModules(Context context) {
        if (modules.size() == 0) {
            File file;
            if (TextUtils.isEmpty(LIB_PATH)) {
                LIB_PATH = context.getApplicationInfo().nativeLibraryDir;
                if (!new File(LIB_PATH).exists()) {
                    LIB_PATH = context.getApplicationInfo().dataDir + "/lib/";
                }
            }
            if (c == null) {
                c = Storage.getAppFileDir(context) + "/qlib/";
                file = new File(c);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            if (TextUtils.isEmpty(d)) {
                d = Storage.getAppFileDir(context) + "/data/";
                file = new File(d);
                if (!(file.exists() || file.mkdirs())) {
                    QLog.crash(new RuntimeException("Fatal error! can not create " + d + ",versionName is " + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName), "");
                }
            }
            e = new a();
            long currentTimeMillis = System.currentTimeMillis();
            b(context);
            QLog.d("WASTE", "registLocalModules " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.mqunar.spider.module.download");
            LocalBroadcastManager.getInstance(context).registerReceiver(e, intentFilter);
            if (modules.size() == 0) {
                throw new Exception("Fatal error, no module found!");
            }
        }
    }

    private static List<AtomNode> b(List<ModuleInfo> list) {
        List<AtomNode> arrayList = new ArrayList(list.size());
        for (ModuleInfo moduleInfo : list) {
            AtomNode atomNode;
            for (AtomNode atomNode2 : arrayList) {
                Object obj;
                for (ModuleInfo moduleInfo2 : atomNode2.versionList) {
                    if (moduleInfo2.dependency.packageName.equals(moduleInfo.dependency.packageName)) {
                        obj = 1;
                        continue;
                        break;
                    }
                }
                obj = null;
                continue;
                if (obj != null) {
                    break;
                }
            }
            AtomNode atomNode3 = null;
            if (atomNode3 == null) {
                atomNode3 = new AtomNode();
                atomNode3.versionList = new ArrayList(2);
                arrayList.add(atomNode3);
                atomNode = atomNode3;
            } else {
                atomNode = atomNode3;
            }
            int size = atomNode.versionList.size();
            int i = 0;
            while (i < atomNode.versionList.size()) {
                if (Dependency.checkVersion(((ModuleInfo) atomNode.versionList.get(i)).dependency.versionCode, moduleInfo.dependency.versionCode) <= 0) {
                    break;
                }
                i++;
            }
            i = size;
            atomNode.versionList.add(i, moduleInfo);
        }
        return arrayList;
    }

    public static void deleteSo(Context context, ModuleInfo moduleInfo) {
        moduleInfo.freeZipFile();
        new File(moduleInfo.fileName).delete();
    }

    private static Set<ModuleInfo> a(Context context, List<ModuleInfo> list, List<ModuleInfo> list2) {
        Object obj;
        Set<ModuleInfo> hashSet = new HashSet(list.size() * 2);
        List<AtomNode> b = b((List) list2);
        int i = 0;
        while (i < b.size()) {
            AtomNode atomNode = (AtomNode) b.get(i);
            int i2 = 0;
            while (i2 < atomNode.versionList.size()) {
                ModuleInfo moduleInfo = (ModuleInfo) atomNode.versionList.get(i2);
                for (ModuleInfo moduleInfo2 : list) {
                    if (moduleInfo.dependency.packageName.equals(moduleInfo2.dependency.packageName)) {
                        break;
                    }
                }
                ModuleInfo moduleInfo22 = null;
                if (moduleInfo22 == null) {
                    break;
                }
                if (Dependency.checkVersion(moduleInfo.dependency.versionCode, moduleInfo22.dependency.versionCode) <= 0) {
                    atomNode.versionList.remove(moduleInfo);
                    deleteSo(context, moduleInfo);
                    i2--;
                }
                i2++;
            }
            if (atomNode.versionList.isEmpty()) {
                b.remove(atomNode);
                i--;
            }
            i++;
        }
        DeleteSoCallback cVar = new c(context);
        do {
            obj = 1;
            for (AtomNode atomNode2 : b) {
                Object obj2;
                if (atomNode2.needCheck()) {
                    atomNode2.getCanLoadDependency(f, list, b, null);
                    obj2 = null;
                } else {
                    obj2 = obj;
                }
                obj = obj2;
            }
        } while (obj == null);
        for (AtomNode atomNode22 : b) {
            ModuleInfo canLoadDependency = atomNode22.getCanLoadDependency(f, list, b, cVar);
            if (canLoadDependency != null) {
                hashSet.add(canLoadDependency);
            }
        }
        for (ModuleInfo canLoadDependency2 : list) {
            for (ModuleInfo moduleInfo3 : hashSet) {
                if (moduleInfo3.dependency.packageName.equals(canLoadDependency2.dependency.packageName)) {
                    obj = 1;
                    break;
                }
            }
            obj = null;
            if (obj == null) {
                hashSet.add(canLoadDependency2);
            }
        }
        return hashSet;
    }

    public static void copyFile(ZipFile zipFile, ZipEntry zipEntry, File file) {
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        Throwable th;
        Throwable th2;
        Object obj;
        FileOutputStream fileOutputStream2 = null;
        Object obj2 = 1;
        try {
            inputStream = zipFile.getInputStream(zipEntry);
            try {
                file.getParentFile().mkdirs();
                fileOutputStream = new FileOutputStream(file);
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = null;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                    }
                }
                throw th;
            }
            try {
                long size = zipEntry.getSize();
                byte[] bArr = new byte[4096];
                int i = 0;
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                    i += read;
                }
                if (((long) i) != size) {
                    fileOutputStream2 = new RuntimeException("文件未读完!");
                    obj2 = null;
                }
                if (obj2 != null) {
                    fileOutputStream.flush();
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e3) {
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                        th2 = fileOutputStream2;
                    } catch (IOException e4) {
                        obj = fileOutputStream2;
                    }
                } else {
                    obj = fileOutputStream2;
                }
            } catch (Throwable th4) {
                th = th4;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            inputStream = null;
            fileOutputStream = null;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
        if (obj2 != null) {
            file.delete();
            throw new RuntimeException(th2);
        }
    }

    private static List<String> a(Context context) {
        ZipEntry zipEntry;
        Throwable th;
        Collection hashSet = new HashSet();
        int length = "lib/armeabi/".length();
        ZipFile zipFile = null;
        ZipFile zipFile2;
        try {
            zipFile2 = new ZipFile(context.getApplicationInfo().sourceDir);
            File file;
            try {
                Enumeration entries = zipFile2.entries();
                while (entries.hasMoreElements()) {
                    zipEntry = (ZipEntry) entries.nextElement();
                    if (zipEntry.getName().startsWith("lib/armeabi/") && zipEntry.getSize() > 0 && zipEntry.getCompressedSize() > 0) {
                        String substring = zipEntry.getName().substring(length);
                        file = new File(LIB_PATH, substring);
                        if (Build.HARDWARE.toLowerCase().contains("mt6592") && (substring.startsWith("libq_lib_") || substring.startsWith("libq_atom_"))) {
                            substring = substring.substring(0, substring.lastIndexOf(".")) + ".jar";
                        } else if ((file.exists() && file.length() == zipEntry.getSize()) || !ModuleParser.checkEquals(zipFile2, zipEntry, file)) {
                            File file2 = new File(c, substring);
                            if (file2.exists()) {
                                file2.delete();
                            }
                            hashSet.add(file.getAbsolutePath());
                        }
                        file = new File(c, substring);
                        if (file.exists() && file.length() == zipEntry.getSize() && ModuleParser.checkEquals(zipFile2, zipEntry, file)) {
                            hashSet.add(file.getAbsolutePath());
                        } else {
                            copyFile(zipFile2, zipEntry, file);
                            hashSet.add(file.getAbsolutePath());
                        }
                    }
                }
                if (zipFile2 != null) {
                    try {
                        zipFile2.close();
                    } catch (IOException e) {
                    }
                }
                return new ArrayList(hashSet);
            } catch (Throwable th2) {
                th = th2;
                zipFile = zipFile2;
            }
        } catch (Throwable th3) {
            th = th3;
            zipFile2 = null;
            if (zipFile2 != null) {
                zipFile2.close();
            }
            throw th;
        }
    }

    private static List<String> c() {
        getMInfoList();
        if (!(ArrayUtils.isEmpty(a) || TextUtils.isEmpty(b))) {
            if (b.compareTo(GlobalEnv.getInstance().getVid()) >= 0) {
                List<String> arrayList = new ArrayList(a.size());
                for (MInfo mInfo : a) {
                    arrayList.add(mInfo.packageName);
                }
                return arrayList;
            }
        }
        return new ArrayList(SchemeManager.getMappingInAsset().keySet());
    }

    public static String getQlibPath() {
        return c;
    }

    private static void b(Context context) {
        int i;
        int i2;
        int size;
        ModuleInfo parseModuleDependency;
        List<String> a = a(context);
        File[] listFiles = new File(c).listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {
                if (!a.contains(file.getAbsolutePath())) {
                    file.delete();
                }
            }
        }
        for (size = a.size() - 1; size >= 0; size--) {
            String name = new File((String) a.get(size)).getName();
            if (!(name.startsWith("libq_lib_") || name.startsWith("libq_atom_"))) {
                a.remove(size);
            }
        }
        Object<ModuleInfo> arrayList = new ArrayList();
        f = ModuleParser.parseDependencyFromAsset();
        for (String name2 : a) {
            long currentTimeMillis = System.currentTimeMillis();
            ModuleInfo parseModuleDependency2 = ModuleParser.parseModuleDependency(name2);
            QLog.d("WASTE", name2 + " parseModuleDependency " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            if (parseModuleDependency2 != null) {
                parseModuleDependency2.isDataFile = false;
                arrayList.add(parseModuleDependency2);
            } else {
                throw new RuntimeException("解析lib失败,filename is : " + name2);
            }
        }
        HashSet hashSet = new HashSet();
        List<ModuleInfo> arrayList2 = new ArrayList();
        File[] listFiles2 = new File(d).listFiles();
        if (listFiles2 != null) {
            for (i2 = 0; i2 < listFiles2.length; i2++) {
                if (listFiles2[i2].isFile()) {
                    long currentTimeMillis2 = System.currentTimeMillis();
                    try {
                        parseModuleDependency = ModuleParser.parseModuleDependency(listFiles2[i2].getAbsolutePath());
                    } catch (Throwable th) {
                        QLog.crash(th, "解析data目录失败!");
                        Object obj = hashSet;
                    }
                    if (parseModuleDependency != null) {
                        parseModuleDependency.isDataFile = true;
                        arrayList2.add(parseModuleDependency);
                    }
                    QLog.d("WASTE", listFiles2[i2].getName() + " parseModuleDependency " + (System.currentTimeMillis() - currentTimeMillis2), new Object[0]);
                }
            }
        }
        List c = c();
        size = 0;
        while (size < arrayList2.size()) {
            parseModuleDependency = (ModuleInfo) arrayList2.get(size);
            if (c.contains(parseModuleDependency.name)) {
                i = size;
            } else {
                deleteSo(context, parseModuleDependency);
                arrayList2.remove(size);
                i = size - 1;
            }
            size = i + 1;
        }
        Set a2 = a(context, arrayList, arrayList2);
        if (r1.isEmpty()) {
            r1.addAll(arrayList);
        } else {
            for (ModuleInfo parseModuleDependency3 : arrayList) {
                if (!r1.contains(parseModuleDependency3)) {
                    parseModuleDependency3.freeZipFile();
                }
            }
        }
        for (ModuleInfo parseModuleDependency32 : arrayList2) {
            if (!r1.contains(parseModuleDependency32)) {
                parseModuleDependency32.freeZipFile();
            }
        }
        List arrayList3 = new ArrayList(r1.size());
        File file2 = new File(QunarApkLoader.getDexCachePath(context));
        for (ModuleInfo parseModuleDependency322 : r1) {
            if (b(parseModuleDependency322)) {
                modules.put(parseModuleDependency322);
                arrayList3.add(DexPathList.optimizedPathFor(new File(parseModuleDependency322.fileName), file2));
                if (parseModuleDependency322.dexList != null) {
                    for (DexInfo dexInfo : parseModuleDependency322.dexList) {
                        arrayList3.add(DexPathList.optimizedPathFor(dexInfo.dexOutPath, file2));
                    }
                }
            }
        }
        listFiles = file2.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File file3 : listFiles) {
                if (!arrayList3.contains(file3.getAbsolutePath())) {
                    file3.delete();
                }
            }
        }
        QunarApkLoader.onRegisterOk();
    }

    public static void loadApk(Context context) {
        int i = 0;
        List<ModuleInfo> arrayList = new ArrayList();
        for (Entry value : modules.mapCopy().entrySet()) {
            ModuleInfo moduleInfo = (ModuleInfo) value.getValue();
            if (moduleInfo.type == (byte) 1) {
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    boolean addNotRun = QunarApkLoader.addNotRun(moduleInfo);
                    QLog.d("WASTE", "PreLoad Library LOAD so filename(%s) name(%s) time(%d)", moduleInfo.fileName, moduleInfo.name, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    if (addNotRun) {
                        arrayList.add(moduleInfo);
                    } else {
                        throw new Exception("signature failed:" + moduleInfo.fileName + ",len:" + new File(moduleInfo.fileName).length() + ",md5:" + ModuleParser.getMd5(moduleInfo.fileName));
                    }
                } catch (Throwable th) {
                    Exception exception = new Exception("Fatal error:" + moduleInfo.fileName + ",len:" + new File(moduleInfo.fileName).length() + ",md5:" + ModuleParser.getMd5(moduleInfo.fileName), th);
                }
            }
        }
        Object metaData = ModuleParser.getMetaData(context, "PRELOAD_MODULE");
        if (TextUtils.isEmpty(metaData)) {
            preLoad = new String[0];
        } else {
            preLoad = metaData.split(",");
        }
        String[] strArr = preLoad;
        int length = strArr.length;
        while (i < length) {
            String str = strArr[i];
            moduleInfo = (ModuleInfo) modules.get(str);
            if (moduleInfo == null) {
                throw new RuntimeException("Fatal error,can not found:" + str);
            }
            try {
                long currentTimeMillis2 = System.currentTimeMillis();
                boolean addNotRun2 = QunarApkLoader.addNotRun(moduleInfo);
                QLog.d("WASTE", "PreLoad LOAD so filename(%s) name(%s) time(%d)", moduleInfo.fileName, moduleInfo.name, Long.valueOf(System.currentTimeMillis() - currentTimeMillis2));
                if (addNotRun2) {
                    arrayList.add(moduleInfo);
                    i++;
                } else {
                    throw new RuntimeException("signature failed:" + moduleInfo.fileName + ",len:" + new File(moduleInfo.fileName).length() + ",md5:" + ModuleParser.getMd5(moduleInfo.fileName));
                }
            } catch (Throwable th2) {
                RuntimeException runtimeException = new RuntimeException("Fatal error:" + moduleInfo.fileName + ",len:" + new File(moduleInfo.fileName).length() + ",md5:" + ModuleParser.getMd5(moduleInfo.fileName), th2);
            }
        }
        for (ModuleInfo moduleInfo2 : arrayList) {
            QunarApkLoader.runApplicationOncreate(moduleInfo2);
        }
    }

    public static ModuleInfo matchModule(String str) {
        ModuleInfo moduleInfo = null;
        Map mapCopy = modules.mapCopy();
        int i = -1;
        for (String str2 : mapCopy.keySet()) {
            ModuleInfo moduleInfo2;
            int i2;
            if (str.startsWith(str2)) {
                int length = str2.length();
                if (length > i) {
                    int i3 = length;
                    moduleInfo2 = (ModuleInfo) mapCopy.get(str2);
                    i2 = i3;
                    i = i2;
                    moduleInfo = moduleInfo2;
                }
            }
            i2 = i;
            moduleInfo2 = moduleInfo;
            i = i2;
            moduleInfo = moduleInfo2;
        }
        return moduleInfo;
    }

    public static void clearLocalModules(Context context) {
        if (d == null) {
            d = Storage.getAppFileDir(context) + "/data/";
        }
        File file = new File(d);
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (int i = 0; i < listFiles.length; i++) {
                    if (listFiles[i].isFile()) {
                        listFiles[i].delete();
                    }
                }
            }
        }
    }
}
