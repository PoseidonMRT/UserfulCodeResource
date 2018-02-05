package com.mqunar.core.basectx.activity;

import com.mqunar.core.basectx.application.QApplication;

class SpiderR {
    SpiderR() {
    }

    public static int getResId(String str, String str2) {
        return QApplication.getContext().getResources().getIdentifier(str2, str, QApplication.getContext().getPackageName());
    }
}
