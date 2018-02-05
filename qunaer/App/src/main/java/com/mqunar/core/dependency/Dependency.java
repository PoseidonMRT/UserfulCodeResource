package com.mqunar.core.dependency;

import com.mqunar.module.ModuleInfo;
import com.mqunar.tools.log.QLog;
import java.util.ArrayList;
import java.util.List;

public class Dependency extends ComponentInfo {
    public List<Atom> atomDependenciesList;
    public List<String> atomPackages;
    public int checkFlag;
    public List<ComponentInfo> libDependenciesList;
    public int packageId = -1;

    public void setPackageId(String str) {
        try {
            this.packageId = Integer.parseInt(str, 16);
        } catch (Throwable e) {
            QLog.e(e);
        }
    }

    public List<ComponentInfo> getFailAtomList(List<ModuleInfo> list) {
        return getFailAtomList(new ArrayList(), list);
    }

    public List<ComponentInfo> getFailAtomList(List<ComponentInfo> list, List<ModuleInfo> list2) {
        List<ComponentInfo> arrayList = new ArrayList();
        if (!(this.atomDependenciesList == null || this.atomDependenciesList.isEmpty())) {
            list.add(this);
            for (Atom atom : this.atomDependenciesList) {
                ComponentInfo componentInfo;
                for (ModuleInfo moduleInfo : list2) {
                    if (atom.packageName.equals(moduleInfo.dependency.packageName)) {
                        componentInfo = moduleInfo.dependency;
                        break;
                    }
                }
                componentInfo = null;
                if (componentInfo == null) {
                    a(arrayList, atom);
                } else if (!list.contains(componentInfo) && (componentInfo.checkFlag == -20 || componentInfo.checkFlag == -21)) {
                    a(arrayList, componentInfo);
                    for (ComponentInfo a : componentInfo.getFailAtomList(list, list2)) {
                        a(arrayList, a);
                    }
                }
            }
            list.remove(this);
        }
        return arrayList;
    }

    private static void a(List<ComponentInfo> list, ComponentInfo componentInfo) {
        for (ComponentInfo componentInfo2 : list) {
            if (componentInfo2.packageName.equals(componentInfo.packageName)) {
                if (checkVersion(componentInfo.versionCode, componentInfo2.versionCode) > 0) {
                    list.remove(componentInfo2);
                    list.add(componentInfo);
                    return;
                }
                return;
            }
        }
        list.add(componentInfo);
    }

    public String toString() {
        return "Dependency [packageName=" + this.packageName + ", packageId=" + this.packageId + ", versionCode=" + this.versionCode + "]";
    }

    public static int checkVersion(String str, String str2) {
        int i = 0;
        if (str.endsWith("-SNAPSHOT")) {
            str = str.substring(i, str.length() - "-SNAPSHOT".length());
        }
        if (str2.endsWith("-SNAPSHOT")) {
            str2 = str2.substring(i, str2.length() - "-SNAPSHOT".length());
        }
        try {
            return Integer.parseInt(str) - Integer.parseInt(str2);
        } catch (NumberFormatException e) {
            String[] split = str.split("\\.");
            String[] split2 = str2.split("\\.");
            for (int i2 = i; i2 < split.length; i2++) {
                if (!split[i2].equals(split2[i2])) {
                    return Integer.parseInt(split[i2]) - Integer.parseInt(split2[i2]);
                }
            }
            return i;
        }
    }
}
