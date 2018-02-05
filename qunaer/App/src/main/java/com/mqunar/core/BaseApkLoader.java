package com.mqunar.core;

import android.text.TextUtils;
import com.mqunar.core.DexPathList.Element;
import com.mqunar.module.ModuleInfo;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;

public class BaseApkLoader extends ClassLoader {
    private final DexPathList a;
    private ModuleInfo b;
    private File c;

    public BaseApkLoader(ModuleInfo moduleInfo, String str, String str2, ClassLoader classLoader) {
        super(classLoader);
        this.b = moduleInfo;
        File file = new File(str);
        this.c = file;
        this.a = new DexPathList(this, moduleInfo, str2, file);
    }

    public void loadMultiDex(int i) {
        this.a.loadMultiDex(i);
    }

    public Element getMultiDexElement(int i) {
        return this.a.getMultiDexElement(i);
    }

    public Class<?> findClass(String str) {
        return this.a.findClass(str);
    }

    public URL findResource(String str) {
        return this.a.findResource(str);
    }

    public Enumeration<URL> findResources(String str) {
        return this.a.findResources(str);
    }

    public String findLibrary(String str) {
        return this.a.findLibrary(str);
    }

    public synchronized Package getPackage(String str) {
        Package packageR = null;
        synchronized (this) {
            if (!TextUtils.isEmpty(str)) {
                packageR = super.getPackage(str);
                if (packageR == null) {
                    packageR = definePackage(str, "Unknown", "0.0", "Unknown", "Unknown", "0.0", "Unknown", null);
                }
            }
        }
        return packageR;
    }

    public String toString() {
        return getClass().getName() + "[" + this.b.fileName + "]";
    }

    public Class<?> loadClass(String str) {
        return QunarApkLoader.loadFromDexs(this, str);
    }

    public Class<?> loadClassFromCurrent(String str) {
        Class<?> findLoadedClass = findLoadedClass(str);
        if (findLoadedClass == null) {
            return findClass(str);
        }
        return findLoadedClass;
    }

    public Class<?> loadClassFromCurrentCache(String str) {
        return findLoadedClass(str);
    }

    public Class<?> loadClassFromSuper(String str) {
        return super.loadClass(str);
    }

    public ModuleInfo getModuleInfo() {
        return this.b;
    }

    public File getDebugOutputPath() {
        return this.c;
    }
}
