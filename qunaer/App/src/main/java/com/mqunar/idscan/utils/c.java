package com.mqunar.idscan.utils;

import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.TaskCallback;
import java.io.File;

final class c implements TaskCallback {
    private String a;

    public c(String str) {
        this.a = str;
    }

    public final void onMsgCacheHit(AbsConductor absConductor, boolean z) {
    }

    public final void onMsgCancel(AbsConductor absConductor, boolean z) {
    }

    public final void onMsgEnd(AbsConductor absConductor, boolean z) {
        File file = new File(this.a);
        if (file.exists()) {
            file.delete();
        }
    }

    public final void onMsgError(AbsConductor absConductor, boolean z) {
    }

    public final void onMsgProgress(AbsConductor absConductor, boolean z) {
    }

    public final void onMsgRequest(AbsConductor absConductor, boolean z) {
    }

    public final void onMsgResult(AbsConductor absConductor, boolean z) {
    }

    public final void onMsgStart(AbsConductor absConductor, boolean z) {
    }
}
