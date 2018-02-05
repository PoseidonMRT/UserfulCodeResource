package com.mqunar.hy.res.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import com.mqunar.hy.res.logger.Timber;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class QunarUtils {
    private static int canWriteFlag = 0;

    public static File getAppFileDir(Context context) {
        Object externalStorageState;
        Throwable e;
        FileOutputStream fileOutputStream;
        File file = null;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (Throwable e2) {
            Timber.e(e2, new Object[0]);
            externalStorageState = null;
        }
        if ("mounted".equals(externalStorageState)) {
            File externalFilesDir = getExternalFilesDir(context);
            if (canWriteFlag == 0) {
                FileOutputStream fileOutputStream2;
                try {
                    String uuid = UUID.randomUUID().toString();
                    File file2 = new File(externalFilesDir, uuid);
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    File file3 = new File(file2, uuid);
                    try {
                        fileOutputStream2 = new FileOutputStream(file3);
                        try {
                            fileOutputStream2.write(0);
                            fileOutputStream2.flush();
                            canWriteFlag = 1;
                            if (fileOutputStream2 != null) {
                                try {
                                    fileOutputStream2.close();
                                } catch (Throwable e3) {
                                    Timber.e(e3, new Object[0]);
                                }
                                file3.delete();
                                file3.getParentFile().delete();
                            }
                        } catch (Throwable th) {
                            e2 = th;
                            file = file3;
                            if (fileOutputStream2 != null) {
                                fileOutputStream2.close();
                                file.delete();
                                file.getParentFile().delete();
                            }
                            throw e2;
                        }
                    } catch (Throwable th2) {
                        e2 = th2;
                        fileOutputStream2 = null;
                        file = file3;
                        if (fileOutputStream2 != null) {
                            fileOutputStream2.close();
                            file.delete();
                            file.getParentFile().delete();
                        }
                        throw e2;
                    }
                } catch (Throwable th3) {
                    e2 = th3;
                    fileOutputStream2 = null;
                    if (fileOutputStream2 != null) {
                        fileOutputStream2.close();
                        file.delete();
                        file.getParentFile().delete();
                    }
                    throw e2;
                }
            }
            if (canWriteFlag == 1) {
                return externalFilesDir;
            }
        }
        return context.getFilesDir();
    }

    @TargetApi(8)
    private static File getExternalFilesDir(Context context) {
        if (VERSION.SDK_INT >= 8) {
            try {
                File externalFilesDir = context.getExternalFilesDir(null);
                if (externalFilesDir != null) {
                    return externalFilesDir;
                }
            } catch (Throwable e) {
                Timber.e(e, new Object[0]);
            }
        }
        return new File(Environment.getExternalStorageDirectory(), "/Android/data/" + context.getPackageName() + "/files");
    }

    public static File getAppDir(Context context) {
        return getAppFileDir(context).getParentFile();
    }

    public static String getQueryParameter(Uri uri, String str) {
        if (uri.isOpaque()) {
            throw new UnsupportedOperationException("This isn't a hierarchical URI.");
        } else if (str == null) {
            throw new NullPointerException("key");
        } else {
            String encodedQuery = uri.getEncodedQuery();
            if (encodedQuery == null) {
                return null;
            }
            int i;
            int indexOf;
            String encode = Uri.encode(str, null);
            int length = encodedQuery.length();
            int i2 = 0;
            while (true) {
                int indexOf2 = encodedQuery.indexOf(38, i2);
                i = indexOf2 != -1 ? indexOf2 : length;
                indexOf = encodedQuery.indexOf(61, i2);
                if (indexOf > i || indexOf == -1) {
                    indexOf = i;
                }
                if (indexOf - i2 == encode.length() && encodedQuery.regionMatches(i2, encode, 0, encode.length())) {
                    break;
                } else if (indexOf2 == -1) {
                    return null;
                } else {
                    i2 = indexOf2 + 1;
                }
            }
            if (indexOf == i) {
                return "";
            }
            return UriCodec.decode(encodedQuery.substring(indexOf + 1, i), true, UriCodec.UTF_8, false);
        }
    }
}
