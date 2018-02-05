package com.mqunar.dispatcher;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.mqunar.core.basectx.SchemeDispatcher;
import java.util.HashMap;

final class e implements Runnable {
    final /* synthetic */ Intent a;
    final /* synthetic */ Object b;

    e(Intent intent, Object obj) {
        this.a = intent;
        this.b = obj;
    }

    public void run() {
        HashMap splitParams = DispatcherLogic.splitParams(this.a.getData());
        if ("true".equals(splitParams.get("isTransferHome")) || "0".equals(splitParams.get("isNotClosePre"))) {
            if (this.b != null) {
                if (this.b instanceof Fragment) {
                    SchemeDispatcher.sendSchemeAndClearStack((Fragment) this.b, SchemeDispatcher.getHomeScheme(DispatcherLogic.c(this.b)), this.a.getDataString(), this.a.getExtras());
                } else {
                    SchemeDispatcher.sendSchemeAndClearStack((Context) this.b, SchemeDispatcher.getHomeScheme(DispatcherLogic.c(this.b)), this.a.getDataString(), this.a.getExtras());
                }
            }
            DispatcherLogic.d(this.b);
            return;
        }
        DispatcherLogic.processIntentInner(this.b, this.a);
    }
}
