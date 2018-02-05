package com.mapquest.android.maps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

class FSTileCache implements ITileCache {
    static final String EXTERNAL_CACHE_DIRECTORY = "mapquest/tiles";
    private static final int EXTERNAL_CACHE_SIZE = 104857600;
    static final String INTERNAL_CACHE_DIRECTORY = "tiles";
    private static final int INTERNAL_CACHE_SIZE = 10485760;
    private static final String LOG_TAG = FSTileCache.class.getName();
    private static final long MILISECONDS_IN_A_DAY = 86400000;
    private static int VERSION = 1;
    CacheHandler cacheHandler;
    private Context context;
    private File file;
    HandlerThread handlerThread;
    boolean mExternalStorageAvailable = false;
    private BroadcastReceiver mExternalStorageReceiver;
    boolean mExternalStorageWriteable = false;

    interface IFileCallback {
        void process(File file);
    }

    class CacheHandler extends Handler {
        static final int EMPTY_CACHE = 0;
        static final int ENSURE_CACHE = 1;
        static final int ENSURE_CACHE_BASED_ON_EXPIRY = 3;
        static final int ENSURE_CACHE_BASED_ON_SIZE = 2;

        public CacheHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    FSTileCache.this.iterateDirectory(Util.getAppFileDir(FSTileCache.this.context, "mapquestCache"), 0, FSTileCache.this.getCallback(FileCallbackType.EMPTY_CACHE));
                    removeMessages(0);
                    break;
                case 1:
                    sendEmptyMessage(3);
                    sendEmptyMessage(2);
                    break;
                case 2:
                    try {
                        FSTileCache.this.iterateDirectory(Util.getAppFileDir(FSTileCache.this.context, "mapquestCache"), -1, FSTileCache.this.getCallback(FileCallbackType.PURGE_EXTERNAL_CACHE_BASED_ON_SIZE));
                        removeMessages(2);
                        break;
                    } catch (Exception e) {
                        sendEmptyMessageDelayed(2, 5000);
                        break;
                    }
                case 3:
                    FSTileCache.this.iterateDirectory(Util.getAppFileDir(FSTileCache.this.context, "mapquestCache"), 0, FSTileCache.this.getCallback(FileCallbackType.PURGE_CACHE_BASED_ON_EXPIRY));
                    removeMessages(3);
                    break;
            }
            super.handleMessage(message);
        }
    }

    enum FileCallbackType {
        EMPTY_CACHE,
        PURGE_EXTERNAL_CACHE_BASED_ON_SIZE,
        PURGE_INTERNAL_CACHE_BASED_ON_SIZE,
        PURGE_CACHE_BASED_ON_EXPIRY
    }

    public FSTileCache(Context context, boolean z) {
        this.context = context;
        startWatchingExternalStorage();
        if (z) {
            this.handlerThread = new HandlerThread("cache", 1);
            this.handlerThread.start();
            this.cacheHandler = new CacheHandler(this.handlerThread.getLooper());
        }
    }

    private File getTileDirectory(Tile tile) {
        File file = new File(this.file, tile.getProvider() + "_" + VERSION);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public void addTile(Tile tile) {
        Throwable th;
        FileOutputStream fileOutputStream = null;
        if (tile.isValid() && tile.getBytes() != null && tile.getBytes() != null) {
            this.cacheHandler.removeMessages(1);
            FileOutputStream fileOutputStream2;
            try {
                fileOutputStream2 = new FileOutputStream(new File(getTileDirectory(tile), tile.buildCacheKey()));
                try {
                    fileOutputStream2.write(tile.getBytes());
                    fileOutputStream2.flush();
                    fileOutputStream2.close();
                    fileOutputStream2 = null;
                    this.cacheHandler.sendEmptyMessageDelayed(1, 500);
                    if (null != null) {
                        try {
                            fileOutputStream2.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (FileNotFoundException e2) {
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                } catch (IOException e4) {
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e5) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream2 != null) {
                        try {
                            fileOutputStream2.close();
                        } catch (IOException e6) {
                        }
                    }
                    throw th;
                }
            } catch (FileNotFoundException e7) {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e8) {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                fileOutputStream2 = null;
                th = th4;
                if (fileOutputStream2 != null) {
                    fileOutputStream2.close();
                }
                throw th;
            }
        }
    }

    public Tile getTile(Tile tile) {
        FileInputStream fileInputStream;
        Tile tile2;
        Throwable th;
        File file = new File(getTileDirectory(tile), tile.buildCacheKey());
        if (file.exists()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    tile.setBytes(byteArrayOutputStream.toByteArray());
                    fileInputStream.close();
                    fileInputStream = null;
                    if (null == null) {
                        return tile;
                    }
                    try {
                        fileInputStream.close();
                        return tile;
                    } catch (IOException e) {
                        return tile;
                    }
                } catch (FileNotFoundException e2) {
                    try {
                        tile2 = (Tile) null;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e3) {
                            }
                        }
                        return tile2;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e4) {
                            }
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e6) {
                        }
                    }
                    return (Tile) null;
                }
            } catch (FileNotFoundException e7) {
                fileInputStream = null;
                tile2 = (Tile) null;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return tile2;
            } catch (IOException e8) {
                fileInputStream = null;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return (Tile) null;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                fileInputStream = null;
                th = th4;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                throw th;
            }
        }
        tile.setId(-1);
        return tile;
    }

    public void removeTile(Tile tile) {
        new File(this.file, tile.buildCacheKey()).delete();
    }

    public void clear() {
        this.cacheHandler.sendEmptyMessage(0);
    }

    public boolean contains(Tile tile) {
        return new File(this.file, tile.buildCacheKey()).exists();
    }

    public void destroy() {
        stopWatchingExternalStorage();
        if (this.cacheHandler != null) {
            this.cacheHandler.removeMessages(0);
            this.cacheHandler.removeMessages(1);
            this.cacheHandler.removeMessages(3);
            this.cacheHandler.removeMessages(2);
        }
        if (this.handlerThread != null) {
            Looper looper = this.handlerThread.getLooper();
            if (looper != null) {
                looper.quit();
            }
        }
        this.file = null;
    }

    public int size() {
        return size(Util.getAppFileDir(this.context, "mapquestCache"));
    }

    private int size(File file) {
        int i = 0;
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if (file2.isDirectory()) {
                    i += size(file2);
                } else {
                    i = (int) (((long) i) + file2.length());
                }
            }
        }
        return i;
    }

    public IFileCallback getCallback(FileCallbackType fileCallbackType) {
        switch (fileCallbackType) {
            case EMPTY_CACHE:
                return new IFileCallback() {
                    public void process(File file) {
                        file.delete();
                    }
                };
            case PURGE_EXTERNAL_CACHE_BASED_ON_SIZE:
                return new IFileCallback() {
                    int days = 30;

                    public void process(File file) {
                        if (System.currentTimeMillis() - file.lastModified() > 86400000 * ((long) this.days)) {
                            file.delete();
                        }
                    }
                };
            case PURGE_INTERNAL_CACHE_BASED_ON_SIZE:
                return createPurgeCallback(EXTERNAL_CACHE_SIZE);
            case PURGE_CACHE_BASED_ON_EXPIRY:
                return createPurgeCallback(INTERNAL_CACHE_SIZE);
            default:
                return null;
        }
    }

    private IFileCallback createPurgeCallback(final int i) {
        return new IFileCallback() {
            int max_size = i;
            int total_size;

            public void process(File file) {
                if (((long) this.total_size) + file.length() > ((long) this.max_size)) {
                    file.delete();
                } else {
                    this.total_size = (int) (((long) this.total_size) + file.length());
                }
            }
        };
    }

    public void iterateDirectory(File file, final int i, IFileCallback iFileCallback) {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (i != 0) {
                Arrays.sort(listFiles, new Comparator() {
                    public int compare(Object obj, Object obj2) {
                        if ((obj instanceof File) && (obj2 instanceof File)) {
                            return Long.valueOf(((File) obj).lastModified()).compareTo(Long.valueOf(((File) obj2).lastModified())) * i;
                        }
                        return 0;
                    }
                });
            }
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    if (file2.isDirectory()) {
                        iterateDirectory(file2, i, iFileCallback);
                    }
                    iFileCallback.process(file2);
                }
            }
        }
    }

    void updateExternalStorageState(Context context) {
        String externalStorageState = Environment.getExternalStorageState();
        if ("mounted".equals(externalStorageState)) {
            this.mExternalStorageWriteable = true;
            this.mExternalStorageAvailable = true;
        } else if ("mounted_ro".equals(externalStorageState)) {
            this.mExternalStorageAvailable = true;
            this.mExternalStorageWriteable = false;
        } else {
            this.mExternalStorageWriteable = false;
            this.mExternalStorageAvailable = false;
        }
        handleExternalStorageState(context, this.mExternalStorageAvailable, this.mExternalStorageWriteable);
    }

    private void handleExternalStorageState(Context context, boolean z, boolean z2) {
        this.file = Util.getAppFileDir(context, "mapquestCache");
    }

    void startWatchingExternalStorage() {
        this.mExternalStorageReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                FSTileCache.this.updateExternalStorageState(context);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_REMOVED");
        this.context.registerReceiver(this.mExternalStorageReceiver, intentFilter);
        updateExternalStorageState(this.context);
    }

    void stopWatchingExternalStorage() {
        try {
            this.context.unregisterReceiver(this.mExternalStorageReceiver);
        } catch (Exception e) {
        }
    }
}
