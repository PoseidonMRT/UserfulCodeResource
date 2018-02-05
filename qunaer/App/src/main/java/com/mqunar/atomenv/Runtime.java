package com.mqunar.atomenv;

import com.mqunar.BuildConfig;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.tools.log.QLog;

public enum Runtime {
    JENKINS_QCI,
    JENKINS_QDR,
    NATIVE,
    UNKNOWN,
    ILLEGAL;

    static Runtime parseByBuildConfig() {
        try {
            String packageName = QApplication.getContext().getPackageName();
            if (BuildConfig.APPLICATION_ID.equals(packageName)) {
                packageName = "com.mqunar";
            }
            packageName = BuildConfigBridge.getBuildConfigByPackageName(packageName, "EMITNUR");
            if (BuildConfig.EMITNUR.equals(packageName)) {
                return JENKINS_QDR;
            }
            if ("qci".equals(packageName)) {
                return JENKINS_QCI;
            }
            return NATIVE;
        } catch (Throwable th) {
            QLog.e(th);
            return UNKNOWN;
        }
    }
}
