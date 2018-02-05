package com.mqunar.core;

import com.mqunar.module.ModuleInfo;

public class Loaders extends BaseLockMap<ModuleInfo, BaseApkLoader> {
    public void put(ModuleInfo moduleInfo, BaseApkLoader baseApkLoader) {
        writeLock();
        this.map.put(moduleInfo, baseApkLoader);
        moduleInfo.isLoad = true;
        moduleInfo.loader = baseApkLoader;
        writeUnlock();
    }
}
