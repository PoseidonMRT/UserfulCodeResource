package com.mqunar.dispatcher;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

class d implements OnKeyListener {
    final /* synthetic */ DispatcherActivity a;

    d(DispatcherActivity dispatcherActivity) {
        this.a = dispatcherActivity;
    }

    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        switch (i) {
            case 4:
                dialogInterface.dismiss();
                this.a.finish();
                return false;
            default:
                return true;
        }
    }
}
