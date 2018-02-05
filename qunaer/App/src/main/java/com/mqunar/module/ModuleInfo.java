package com.mqunar.module;

import android.app.Application;
import com.mqunar.core.BaseApkLoader;
import com.mqunar.core.ModuleParser;
import com.mqunar.core.QunarApkLoader;
import com.mqunar.core.dependency.Atom;
import com.mqunar.core.dependency.AtomNode;
import com.mqunar.core.dependency.Circular;
import com.mqunar.core.dependency.ComponentInfo;
import com.mqunar.core.dependency.Dependency;
import com.mqunar.tools.log.QLog;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ModuleInfo {
    public static final String MULTI_CLASS_DIR = "multi_classes";
    public Application application;
    public String applicationClassName;
    public Dependency dependency;
    public String description = "";
    public List<DexInfo> dexList;
    public String fileName;
    public boolean isDataFile;
    public boolean isLoad;
    public String launcherClassName;
    public BaseApkLoader loader;
    public int localVersion = -1;
    public String name;
    public byte onlineType = (byte) 1;
    public byte type = (byte) 0;
    public byte updateType = (byte) 0;
    public ZipFile zipFile;

    public class DexInfo {
        public File dexOutPath;
        public String entryName;
        public int indexInElement;
        public boolean isLoad;
    }

    public void initDexList() {
        if (this.dexList != null && this.dexList.isEmpty()) {
            File file = new File(QunarApkLoader.getAppContext().getFilesDir(), MULTI_CLASS_DIR);
            int i = 2;
            while (true) {
                StringBuilder append = new StringBuilder().append("classes");
                int i2 = i + 1;
                String stringBuilder = append.append(i).append(".dex").toString();
                if (this.zipFile.getEntry(stringBuilder) != null) {
                    DexInfo dexInfo = new DexInfo();
                    dexInfo.entryName = stringBuilder;
                    dexInfo.dexOutPath = new File(file, this.name + "." + stringBuilder + ".zip");
                    dexInfo.isLoad = false;
                    this.dexList.add(dexInfo);
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    public boolean hasUnloadMultiDex() {
        if (this.dexList != null) {
            synchronized (this.dexList) {
                for (DexInfo dexInfo : this.dexList) {
                    if (!dexInfo.isLoad) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Class loadFromMultiDex(String str) {
        if (this.dexList != null) {
            synchronized (this.dexList) {
                for (DexInfo dexInfo : this.dexList) {
                    if (!dexInfo.isLoad) {
                        synchronized (dexInfo) {
                            if (!dexInfo.isLoad) {
                                ZipEntry entry = this.zipFile.getEntry(dexInfo.entryName);
                                boolean z = false;
                                if (dexInfo.dexOutPath.exists() && dexInfo.dexOutPath.length() > 0 && dexInfo.dexOutPath.length() == entry.getSize()) {
                                    z = ModuleParser.checkEquals(this.zipFile, entry, dexInfo.dexOutPath);
                                }
                                if (!z) {
                                    dexInfo.dexOutPath.delete();
                                    a(this.zipFile, entry, dexInfo.dexOutPath);
                                }
                                this.loader.loadMultiDex(dexInfo.indexInElement);
                                dexInfo.isLoad = true;
                            }
                        }
                    }
                    Class findClass = this.loader.getMultiDexElement(dexInfo.indexInElement).findClass(str);
                    if (findClass != null) {
                        return findClass;
                    }
                }
            }
        }
        return null;
    }

    private static void a(ZipFile zipFile, ZipEntry zipEntry, File file) {
        ZipOutputStream zipOutputStream;
        Throwable th;
        Object obj;
        Throwable th2;
        ZipOutputStream zipOutputStream2 = null;
        Object obj2 = 1;
        InputStream inputStream;
        try {
            inputStream = zipFile.getInputStream(zipEntry);
            try {
                file.getParentFile().mkdirs();
                zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                try {
                    ZipEntry zipEntry2 = new ZipEntry("classes.dex");
                    zipEntry2.setTime(zipEntry.getTime());
                    zipOutputStream.putNextEntry(zipEntry2);
                    long size = zipEntry.getSize();
                    byte[] bArr = new byte[4096];
                    int i = 0;
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        zipOutputStream.write(bArr, 0, read);
                        i += read;
                    }
                    if (((long) i) != size) {
                        zipOutputStream2 = new RuntimeException("文件未读完!");
                        obj2 = null;
                    }
                    if (obj2 != null) {
                        zipOutputStream.flush();
                    }
                    if (zipOutputStream != null) {
                        try {
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            zipOutputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                            th = zipOutputStream2;
                        } catch (IOException e3) {
                            obj = zipOutputStream2;
                        }
                    } else {
                        obj = zipOutputStream2;
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    if (zipOutputStream != null) {
                        try {
                            zipOutputStream.closeEntry();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                        try {
                            zipOutputStream.close();
                        } catch (IOException e5) {
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e6) {
                        }
                    }
                    throw th2;
                }
            } catch (Throwable th4) {
                th2 = th4;
                zipOutputStream = null;
                if (zipOutputStream != null) {
                    zipOutputStream.closeEntry();
                    zipOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th2;
            }
        } catch (Throwable th5) {
            th2 = th5;
            inputStream = null;
            zipOutputStream = null;
            if (zipOutputStream != null) {
                zipOutputStream.closeEntry();
                zipOutputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            throw th2;
        }
        if (obj2 != null) {
            file.delete();
            throw new RuntimeException(th);
        }
    }

    public int checkLibOnSpider(List<ModuleInfo> list, Atom atom) {
        for (ModuleInfo moduleInfo : list) {
            if (moduleInfo.dependency.packageName.equals(atom.packageName)) {
                if (Dependency.checkVersion(moduleInfo.dependency.versionCode, atom.versionCode) >= 0) {
                    return 1;
                }
                a(this.fileName + "不能加载，原因：" + atom.packageName + "不满足版本要求,需要的版本号:" + atom.versionCode + ",平台能够提供的版本号:" + moduleInfo.dependency.versionCode + ",错误码:-21");
                return -21;
            }
        }
        a(this.fileName + "不能加载，原因：需要" + atom.packageName + ",但是平台没有,错误码:-20");
        return -20;
    }

    private static void a(String str) {
        QLog.i("Dependency", str, new Object[0]);
    }

    public static int checkCanOnSpider(ModuleInfo moduleInfo, Dependency dependency) {
        if (moduleInfo.dependency.libDependenciesList == null || moduleInfo.dependency.libDependenciesList.isEmpty()) {
            return 1;
        }
        for (ComponentInfo componentInfo : moduleInfo.dependency.libDependenciesList) {
            Object obj;
            for (ComponentInfo componentInfo2 : dependency.libDependenciesList) {
                if (componentInfo.packageName.equals(componentInfo2.packageName)) {
                    if (Dependency.checkVersion(componentInfo.versionCode, componentInfo2.versionCode) > 0) {
                        a(moduleInfo.fileName + "不能加载，原因：" + componentInfo.packageName + "不满足版本要求,需要的版本号:" + componentInfo.versionCode + ",平台能够提供的版本号:" + componentInfo2.versionCode + ",错误码:-11");
                        return -11;
                    }
                    obj = 1;
                    continue;
                    if (obj == null) {
                        a(moduleInfo.fileName + "不能加载，原因：需要" + componentInfo.packageName + ",但是平台没有,错误码:-10");
                        return -10;
                    }
                }
            }
            obj = null;
            continue;
            if (obj == null) {
                a(moduleInfo.fileName + "不能加载，原因：需要" + componentInfo.packageName + ",但是平台没有,错误码:-10");
                return -10;
            }
        }
        return 1;
    }

    public void check(Dependency dependency, List<ModuleInfo> list, List<AtomNode> list2, List<Dependency> list3, List<Circular> list4) {
        if (this.dependency.checkFlag != 1 && this.dependency.checkFlag != -10 && this.dependency.checkFlag != -11 && this.dependency.checkFlag != -20 && this.dependency.checkFlag != -21 && this.dependency.checkFlag != -22) {
            int checkCanOnSpider = checkCanOnSpider(this, dependency);
            if (checkCanOnSpider != 1) {
                this.dependency.checkFlag = checkCanOnSpider;
            } else if (this.dependency.atomDependenciesList == null || this.dependency.atomDependenciesList.isEmpty()) {
                this.dependency.checkFlag = 1;
            } else {
                list3.add(this.dependency);
                int i = 0;
                for (Atom atom : this.dependency.atomDependenciesList) {
                    Object obj;
                    AtomNode atomNode = atom.getAtomNode(list2);
                    if (atomNode != null) {
                        for (ModuleInfo moduleInfo : atomNode.versionList) {
                            if (Dependency.checkVersion(moduleInfo.dependency.versionCode, atom.versionCode) >= 0) {
                                if (list3.contains(moduleInfo.dependency)) {
                                    obj = 1;
                                    a((List) list4, this.dependency, moduleInfo.dependency);
                                    checkCanOnSpider = 2;
                                    break;
                                }
                                moduleInfo.check(dependency, list, list2, list3, list4);
                                if (moduleInfo.dependency.checkFlag == 1) {
                                    obj = 1;
                                    checkCanOnSpider = i;
                                    break;
                                } else if (moduleInfo.dependency.checkFlag == 2) {
                                    obj = 1;
                                    a((List) list4, this.dependency, moduleInfo.dependency);
                                    checkCanOnSpider = 2;
                                    break;
                                }
                            }
                        }
                    }
                    obj = null;
                    checkCanOnSpider = i;
                    if (obj != null || checkLibOnSpider(list, atom) == 1) {
                        i = checkCanOnSpider;
                    } else {
                        if (atomNode == null) {
                            checkCanOnSpider = -20;
                        } else {
                            checkCanOnSpider = -21;
                        }
                        if (checkCanOnSpider == 0) {
                            checkCanOnSpider = 1;
                        }
                        if ((checkCanOnSpider == 1 || checkCanOnSpider == 2) && a(list, list2)) {
                            checkCanOnSpider = -22;
                        }
                        this.dependency.checkFlag = checkCanOnSpider;
                        for (Circular circular : list4) {
                            if (circular.nodeList.contains(this.dependency)) {
                                circular.update(this.dependency);
                                break;
                            }
                        }
                        list3.remove(this.dependency);
                    }
                }
                checkCanOnSpider = i;
                if (checkCanOnSpider == 0) {
                    checkCanOnSpider = 1;
                }
                checkCanOnSpider = -22;
                this.dependency.checkFlag = checkCanOnSpider;
                for (Circular circular2 : list4) {
                    if (circular2.nodeList.contains(this.dependency)) {
                        circular2.update(this.dependency);
                        break;
                    }
                }
                list3.remove(this.dependency);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean a(java.util.List<com.mqunar.module.ModuleInfo> r7, java.util.List<com.mqunar.core.dependency.AtomNode> r8) {
        /*
        r6 = this;
        r1 = 1;
        r0 = r6.fileName;
        r2 = r0.intern();
        monitor-enter(r2);
        r3 = r7.iterator();	 Catch:{ all -> 0x005d }
    L_0x000c:
        r0 = r3.hasNext();	 Catch:{ all -> 0x005d }
        if (r0 == 0) goto L_0x002d;
    L_0x0012:
        r0 = r3.next();	 Catch:{ all -> 0x005d }
        r0 = (com.mqunar.module.ModuleInfo) r0;	 Catch:{ all -> 0x005d }
        r4 = r0.dependency;	 Catch:{ all -> 0x005d }
        r4 = r4.packageName;	 Catch:{ all -> 0x005d }
        r5 = r6.dependency;	 Catch:{ all -> 0x005d }
        r5 = r5.packageName;	 Catch:{ all -> 0x005d }
        r4 = r4.equals(r5);	 Catch:{ all -> 0x005d }
        if (r4 == 0) goto L_0x000c;
    L_0x0026:
        r0 = r0.isLoad;	 Catch:{ all -> 0x005d }
        if (r0 == 0) goto L_0x002d;
    L_0x002a:
        monitor-exit(r2);	 Catch:{ all -> 0x005d }
        r0 = r1;
    L_0x002c:
        return r0;
    L_0x002d:
        r0 = r6.dependency;	 Catch:{ all -> 0x005d }
        r0 = r0.getAtomNode(r8);	 Catch:{ all -> 0x005d }
        r0 = r0.versionList;	 Catch:{ all -> 0x005d }
        r3 = r0.iterator();	 Catch:{ all -> 0x005d }
    L_0x0039:
        r0 = r3.hasNext();	 Catch:{ all -> 0x005d }
        if (r0 == 0) goto L_0x005a;
    L_0x003f:
        r0 = r3.next();	 Catch:{ all -> 0x005d }
        r0 = (com.mqunar.module.ModuleInfo) r0;	 Catch:{ all -> 0x005d }
        r4 = r0.isLoad;	 Catch:{ all -> 0x005d }
        if (r4 == 0) goto L_0x0039;
    L_0x0049:
        r0 = r0.dependency;	 Catch:{ all -> 0x005d }
        r0 = r0.versionCode;	 Catch:{ all -> 0x005d }
        r3 = r6.dependency;	 Catch:{ all -> 0x005d }
        r3 = r3.versionCode;	 Catch:{ all -> 0x005d }
        r0 = com.mqunar.core.dependency.Dependency.checkVersion(r0, r3);	 Catch:{ all -> 0x005d }
        if (r0 >= 0) goto L_0x005a;
    L_0x0057:
        monitor-exit(r2);	 Catch:{ all -> 0x005d }
        r0 = r1;
        goto L_0x002c;
    L_0x005a:
        monitor-exit(r2);	 Catch:{ all -> 0x005d }
        r0 = 0;
        goto L_0x002c;
    L_0x005d:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x005d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mqunar.module.ModuleInfo.a(java.util.List, java.util.List):boolean");
    }

    private void a(List<Circular> list, Dependency dependency, Dependency dependency2) {
        Circular circular;
        List<Circular> arrayList = new ArrayList(2);
        for (Circular circular2 : list) {
            if (circular2.nodeList.contains(dependency) || circular2.nodeList.contains(dependency2)) {
                arrayList.add(circular2);
            }
        }
        if (arrayList.size() == 1) {
            circular2 = (Circular) arrayList.get(0);
        } else {
            Circular circular3 = new Circular();
            if (arrayList.isEmpty()) {
                list.add(circular3);
                circular2 = circular3;
            } else {
                for (Circular circular22 : arrayList) {
                    circular3.mergeCircular(circular22);
                    list.remove(circular22);
                }
                list.add(circular3);
                circular22 = circular3;
            }
        }
        circular22.addNode(dependency);
        circular22.addNode(dependency2);
    }

    public String toString() {
        return "ModuleInfo{fileName='" + this.fileName + '\'' + ", name='" + this.name + '\'' + ", applicationClassName='" + this.applicationClassName + '\'' + '}';
    }

    public void freeZipFile() {
        if (this.zipFile != null) {
            try {
                this.zipFile.close();
            } catch (Throwable th) {
            }
            this.zipFile = null;
        }
    }

    protected void finalize() {
        freeZipFile();
        super.finalize();
    }
}
