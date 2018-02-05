package com.mqunar.idscan.decode;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public final class d implements OnCancelListener, OnClickListener {
    private final Activity a;

    public d(Activity activity) {
        this.a = activity;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.a.finish();
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.a.finish();
    }
}
