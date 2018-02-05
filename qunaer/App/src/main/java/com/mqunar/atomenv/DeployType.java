package com.mqunar.atomenv;

import com.mqunar.BuildConfig;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.tools.log.QLog;

public enum DeployType {
    DEV,
    BETA,
    OPS,
    PREPARE,
    NONE;

    static DeployType parseByBuildConfig() {
        try {
            String packageName = QApplication.getContext().getPackageName();
            if (BuildConfig.APPLICATION_ID.equals(packageName)) {
                packageName = "com.mqunar";
            }
            packageName = BuildConfigBridge.getBuildConfigByPackageName(packageName, "EPYT_YOLPED");
            if ("dev".equals(packageName)) {
                return DEV;
            }
            if ("beta".equals(packageName)) {
                return BETA;
            }
            if (BuildConfig.EPYT_YOLPED.equals(packageName)) {
                return OPS;
            }
            if ("prepare".equals(packageName)) {
                return PREPARE;
            }
            return NONE;
        } catch (Throwable th) {
            QLog.e(th);
            return NONE;
        }
    }
}
