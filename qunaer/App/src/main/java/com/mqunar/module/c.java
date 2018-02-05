package com.mqunar.module;

import android.content.Context;
import com.mqunar.core.dependency.AtomNode.DeleteSoCallback;

final class c implements DeleteSoCallback {
    final /* synthetic */ Context a;

    c(Context context) {
        this.a = context;
    }

    public void delete(ModuleInfo moduleInfo) {
        ModuleInfoController.deleteSo(this.a, moduleInfo);
    }
}
