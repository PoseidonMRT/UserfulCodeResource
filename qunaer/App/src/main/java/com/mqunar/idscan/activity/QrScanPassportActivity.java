package com.mqunar.idscan.activity;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.mqunar.idscan.R;
import com.mqunar.idscan.a;
import com.mqunar.idscan.a.f;
import com.mqunar.idscan.model.ScanPassportParam;
import com.mqunar.idscan.model.ScanPassportResultData;
import com.mqunar.idscan.utils.Constants;
import com.mqunar.idscan.utils.UploadUtils;
import com.mqunar.idscan.view.ViewfinderView;
import com.mqunar.qav.uelog.QAVLog;
import com.mqunar.qav.uelog.QAVOpenApi;
import com.mqunar.tools.log.QLog;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class QrScanPassportActivity extends a implements OnClickListener {
    private String d = "QrScanPassportActivity";
    private LinearLayout e;
    private ImageView f;
    private Bundle g;
    private String h;
    private boolean i = false;

    private static Bitmap a(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f = 700.0f / ((float) width);
        float f2 = 131.0f / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(f, f2);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return createBitmap;
    }

    private String a(String str, int i) {
        FileOutputStream fileOutputStream;
        Throwable e;
        Throwable th;
        Object obj;
        String str2 = null;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        InputStream inputStream;
        try {
            File dir = getBaseContext().getDir("passport", 0);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, str);
            if (file.exists() || !file.createNewFile()) {
                fileOutputStream = str2;
                inputStream = str2;
            } else {
                inputStream = getResources().openRawResource(i);
                try {
                    fileOutputStream = new FileOutputStream(file);
                    try {
                        byte[] bArr = new byte[8192];
                        while (true) {
                            int read = inputStream.read(bArr);
                            if (read > 0) {
                                fileOutputStream.write(bArr, 0, read);
                            }
                        }
                        QLog.d("passport :", file.getAbsoluteFile());
                        str2 = file.getAbsolutePath();
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Throwable e2) {
                                QLog.e(e2);
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Throwable e22) {
                                QLog.e(e22);
                            }
                        }
                    } catch (IOException e3) {
                        e22 = e3;
                        try {
                            QLog.e(e22);
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable e222) {
                                    QLog.e(e222);
                                }
                            }
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (Throwable e2222) {
                                    QLog.e(e2222);
                                }
                            }
                            QLog.e("passport", (SystemClock.elapsedRealtime() - elapsedRealtime) + "ms", new Object[0]);
                            return str2;
                        } catch (Throwable th2) {
                            th = th2;
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable e22222) {
                                    QLog.e(e22222);
                                }
                            }
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (Throwable e222222) {
                                    QLog.e(e222222);
                                }
                            }
                            throw th;
                        }
                    }
                } catch (IOException e4) {
                    e222222 = e4;
                    obj = str2;
                    QLog.e(e222222);
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    QLog.e("passport", (SystemClock.elapsedRealtime() - elapsedRealtime) + "ms", new Object[0]);
                    return str2;
                } catch (Throwable e2222222) {
                    obj = str2;
                    th = e2222222;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    throw th;
                }
                return str2;
            }
            QLog.d("passport :", file.getAbsoluteFile());
            str2 = file.getAbsolutePath();
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (IOException e5) {
            e2222222 = e5;
            obj = str2;
            Object obj2 = str2;
            QLog.e(e2222222);
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            QLog.e("passport", (SystemClock.elapsedRealtime() - elapsedRealtime) + "ms", new Object[0]);
            return str2;
        } catch (Throwable e22222222) {
            fileOutputStream = str2;
            inputStream = str2;
            th = e22222222;
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
        return str2;
    }

    private void f() {
        if (isInMultiWindowMode() && !this.i) {
            this.i = true;
            new Builder(this).setMessage("分屏模式下无法使用该功能！").setPositiveButton(R.string.idscan_sure, new d(this)).setCancelable(false).show();
        }
    }

    public final void b(ScanPassportResultData scanPassportResultData) {
        YuvImage yuvImage;
        OutputStream outputStream;
        Point a = d().h().a();
        int i = a.x;
        int i2 = a.y;
        YuvImage yuvImage2;
        OutputStream byteArrayOutputStream;
        if (f.c == 0) {
            yuvImage2 = new YuvImage(f.b, 17, i, i2, null);
            byteArrayOutputStream = new ByteArrayOutputStream(f.b.length);
            yuvImage = yuvImage2;
            outputStream = byteArrayOutputStream;
        } else if (f.c == 1) {
            yuvImage2 = new YuvImage(f.a, 17, i, i2, null);
            byteArrayOutputStream = new ByteArrayOutputStream(f.a.length);
            yuvImage = yuvImage2;
            outputStream = byteArrayOutputStream;
        } else {
            outputStream = null;
            yuvImage = null;
        }
        if (yuvImage.compressToJpeg(new Rect(0, 0, i, i2), 100, outputStream)) {
            byte[] toByteArray = outputStream.toByteArray();
            try {
                Bitmap decodeByteArray = BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length);
                Rect g = d().g();
                if (g != null) {
                    Bitmap createBitmap = Bitmap.createBitmap(decodeByteArray, g.left, g.top, g.right - g.left, g.bottom - g.top);
                    decodeByteArray.recycle();
                    createBitmap = a(createBitmap);
                    File file = new File(getExternalFilesDir(null), "successedOcrImage.jpg");
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                        createBitmap.compress(CompressFormat.JPEG, 100, bufferedOutputStream);
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                    } catch (Throwable e) {
                        QLog.e(e);
                    } finally {
                        createBitmap.recycle();
                        f.a = null;
                        f.b = null;
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.TAG_SCAN_RESULT, scanPassportResultData);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(-1, intent);
                finish();
            } catch (Exception e2) {
                QLog.e("isUsable", "图片不可用", new Object[0]);
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        QAVLog.getInstance(getApplicationContext()).log(this.d + "-" + this.h, "scan passport failed");
        Point a = d().h().a();
        if (a != null) {
            YuvImage yuvImage;
            OutputStream outputStream;
            int i = a.x;
            int i2 = a.y;
            YuvImage yuvImage2;
            OutputStream byteArrayOutputStream;
            if (f.c == 0) {
                yuvImage2 = new YuvImage(f.a, 17, i, i2, null);
                byteArrayOutputStream = new ByteArrayOutputStream(f.a.length);
                yuvImage = yuvImage2;
                outputStream = byteArrayOutputStream;
            } else if (f.c == 1) {
                yuvImage2 = new YuvImage(f.b, 17, i, i2, null);
                byteArrayOutputStream = new ByteArrayOutputStream(f.b.length);
                yuvImage = yuvImage2;
                outputStream = byteArrayOutputStream;
            } else {
                outputStream = null;
                yuvImage = null;
            }
            if (yuvImage != null && yuvImage.compressToJpeg(new Rect(0, 0, i, i2), 100, outputStream)) {
                byte[] toByteArray = outputStream.toByteArray();
                try {
                    Bitmap decodeByteArray = BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length);
                    Rect g = d().g();
                    if (g != null) {
                        Bitmap createBitmap = Bitmap.createBitmap(decodeByteArray, g.left, g.top, g.right - g.left, g.bottom - g.top);
                        decodeByteArray.recycle();
                        createBitmap = a(createBitmap);
                        File file = new File(getExternalFilesDir(null), "failedOcrImage.jpg");
                        try {
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            outputStream = new BufferedOutputStream(new FileOutputStream(file));
                            createBitmap.compress(CompressFormat.JPEG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        } catch (Throwable e) {
                            QLog.e(e);
                        } finally {
                            createBitmap.recycle();
                            f.a = null;
                            f.b = null;
                        }
                        if (file.exists()) {
                            ScanPassportParam scanPassportParam = new ScanPassportParam();
                            scanPassportParam.airCode = d().h().b;
                            scanPassportParam.cat = this.h;
                            UploadUtils.a(scanPassportParam, Uri.fromFile(file).getPath());
                        }
                    }
                } catch (Exception e2) {
                    QLog.e("isUsable", "图片不可用", e2);
                }
            }
        }
    }

    public void onClick(View view) {
        if (view != null && view == this.f) {
            onBackPressed();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        a.a(getApplicationContext());
        com.mqunar.idscan.algo.f.a = a("idscan_passport.weights", R.raw.idscan_passport);
        if (bundle == null) {
            bundle = getIntent().getExtras();
        }
        this.g = bundle;
        if (this.g == null) {
            this.g = new Bundle();
        }
        setContentView(R.layout.idscan_layout_activity_passport_scan);
        this.h = this.g.getString(Constants.CAT_KEY);
        QAVOpenApi.setPageName(this, this.h);
        this.f = (ImageView) findViewById(R.id.idscan_iv_scan_back);
        this.e = (LinearLayout) findViewById(R.id.idscan_ll_qrcode_progress);
        this.a = (ViewfinderView) findViewById(R.id.idscan_viewfinder_view);
        this.b = (SurfaceView) findViewById(R.id.idscan_preview_view);
        this.f.setOnClickListener(this);
        if (this.c) {
            this.e.setVisibility(0);
            new Handler().postDelayed(new c(this), 1000);
        } else {
            this.e.setVisibility(8);
        }
        if (VERSION.SDK_INT >= 24) {
            QLog.d("------", "oncreate_show dialog", new Object[0]);
            f();
        }
    }

    public void onMultiWindowModeChanged(boolean z) {
        super.onMultiWindowModeChanged(z);
        QLog.d("------", "onMultiWindowModeChanged show dialog:" + z, new Object[0]);
        if (z && !this.i) {
            f();
        }
    }

    protected void onResume() {
        super.onResume();
        e();
    }

    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putAll(this.g);
        super.onSaveInstanceState(bundle);
    }
}
