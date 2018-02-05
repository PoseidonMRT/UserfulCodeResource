package com.mqunar.idscan.decode;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class b implements OnClickListener {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        this.a.c.finish();
    }
}
