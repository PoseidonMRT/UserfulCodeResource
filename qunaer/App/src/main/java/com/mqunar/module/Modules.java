package com.mqunar.module;

import com.mqunar.core.BaseLockMap;

public class Modules extends BaseLockMap<String, ModuleInfo> {
    public void put(ModuleInfo moduleInfo) {
        super.put(moduleInfo.name, moduleInfo);
    }

    public boolean hasModuleInfo(ModuleInfo moduleInfo) {
        boolean z;
        readLock();
        for (ModuleInfo moduleInfo2 : this.map.values()) {
            if (moduleInfo2.fileName.equals(moduleInfo.fileName)) {
                z = true;
                break;
            }
        }
        z = false;
        readUnlock();
        return z;
    }
}
