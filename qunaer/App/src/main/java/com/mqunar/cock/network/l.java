package com.mqunar.cock.network;

import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.TaskCallback;

class l extends TaskCallbackWrapper {
    final /* synthetic */ RemoteSvcProxy a;

    l(RemoteSvcProxy remoteSvcProxy, TaskCallback taskCallback) {
        this.a = remoteSvcProxy;
        super(taskCallback);
    }

    public void onMsgStart(AbsConductor absConductor, boolean z) {
        super.onMsgStart(absConductor, z);
        this.a.d.add(absConductor);
    }

    public void onMsgEnd(AbsConductor absConductor, boolean z) {
        super.onMsgEnd(absConductor, z);
        this.a.d.remove(absConductor);
    }
}
