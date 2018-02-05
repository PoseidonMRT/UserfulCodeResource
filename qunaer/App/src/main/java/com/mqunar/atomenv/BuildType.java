package com.mqunar.atomenv;

import com.mqunar.BuildConfig;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.tools.log.QLog;

public enum BuildType {
    DEV,
    BETA,
    RELEASE;

    static BuildType parseByBuildConfig() {
        try {
            String packageName = QApplication.getContext().getPackageName();
            if (BuildConfig.APPLICATION_ID.equals(packageName)) {
                packageName = "com.mqunar";
            }
            packageName = BuildConfigBridge.getBuildConfigByPackageName(packageName, "BUILD_TYPE");
            if ("debug".equals(packageName)) {
                return DEV;
            }
            if ("beta".equals(packageName)) {
                return BETA;
            }
            return RELEASE;
        } catch (Throwable th) {
            QLog.e(th);
            return RELEASE;
        }
    }
}
