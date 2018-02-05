package com.mqunar.core.dependency;

import com.mqunar.module.ModuleInfo;
import java.util.ArrayList;
import java.util.List;

public class AtomNode {
    public List<ModuleInfo> versionList;

    public interface DeleteSoCallback {
        void delete(ModuleInfo moduleInfo);
    }

    public boolean needCheck() {
        for (ModuleInfo moduleInfo : this.versionList) {
            if (moduleInfo.dependency.checkFlag == 1) {
                return false;
            }
            if (moduleInfo.dependency.checkFlag == 0) {
                return true;
            }
        }
        return false;
    }

    public ModuleInfo getCanLoadDependency(Dependency dependency, List<ModuleInfo> list, List<AtomNode> list2, DeleteSoCallback deleteSoCallback) {
        ModuleInfo moduleInfo = null;
        for (ModuleInfo moduleInfo2 : this.versionList) {
            ModuleInfo moduleInfo22;
            if (moduleInfo == null) {
                moduleInfo22.check(dependency, list, list2, new ArrayList(1), new ArrayList(1));
                if (moduleInfo22.dependency.checkFlag == 1) {
                }
                moduleInfo22 = moduleInfo;
            } else {
                if (deleteSoCallback != null) {
                    deleteSoCallback.delete(moduleInfo22);
                }
                moduleInfo22 = moduleInfo;
            }
            moduleInfo = moduleInfo22;
        }
        return moduleInfo;
    }
}
