package org.acra.collector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.view.View;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import org.acra.ACRA;
import org.acra.builder.LastActivityManager;
import org.acra.file.e;
import org.acra.sender.b;
import org.acra.util.a;
import org.acra.util.g;

class r {
    private final LastActivityManager a;

    r(LastActivityManager lastActivityManager) {
        this.a = lastActivityManager;
    }

    @NonNull
    String a(@NonNull Context context) {
        Exception e;
        Throwable th;
        Activity lastActivity = this.a.getLastActivity();
        if (lastActivity != null) {
            Closeable closeable = null;
            String str;
            try {
                View peekDecorView = lastActivity.getWindow().peekDecorView();
                if (peekDecorView == null) {
                    str = "screen shot failed :decor view has not been created";
                    g.a(null);
                    return str;
                }
                Bitmap createBitmap = Bitmap.createBitmap(peekDecorView.getWidth(), peekDecorView.getHeight(), Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                peekDecorView.layout(peekDecorView.getLeft(), peekDecorView.getTop(), peekDecorView.getRight(), peekDecorView.getBottom());
                peekDecorView.draw(canvas);
                String str2 = new e(context).e().getPath() + "/" + UUID.randomUUID();
                Closeable fileOutputStream = new FileOutputStream(str2);
                try {
                    Matrix matrix = new Matrix();
                    matrix.setScale(0.4f, 0.4f);
                    Bitmap.createBitmap(createBitmap, 0, 0, createBitmap.getWidth(), createBitmap.getHeight(), matrix, true).compress(CompressFormat.JPEG, 8, fileOutputStream);
                    File file = new File(str2);
                    if (file.exists()) {
                        File a = a.a(file, "gz", true);
                        if (a != null) {
                            str = a.getAbsolutePath();
                            g.a(fileOutputStream);
                            return str;
                        }
                    }
                    b.a(file);
                    str = "压缩截屏失败！！！";
                    g.a(fileOutputStream);
                    return str;
                } catch (Exception e2) {
                    e = e2;
                    closeable = fileOutputStream;
                    try {
                        ACRA.f.e(ACRA.e, "screen shot failed :" + e.toString());
                        str = "screen shot failed :" + e.toString();
                        g.a(closeable);
                        return str;
                    } catch (Throwable th2) {
                        th = th2;
                        g.a(closeable);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    closeable = fileOutputStream;
                    g.a(closeable);
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                ACRA.f.e(ACRA.e, "screen shot failed :" + e.toString());
                str = "screen shot failed :" + e.toString();
                g.a(closeable);
                return str;
            }
        }
        ACRA.f.e(ACRA.e, "screen shot failed :can not get last Activity");
        return "screen shot failed :can not get last Activity";
    }
}
