package com.mqunar.necro.agent.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.HandlerThread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NecroSpStorage {
    private static Handler mStorageHandler;
    private static HandlerThread mStorageHandlerThread;
    private Map<String, Object> mMemoryCacheData = new HashMap();
    private SharedPreferences sp;

    private NecroSpStorage(Context context, String str) {
        this.sp = context.getSharedPreferences(str, 0);
    }

    public static NecroSpStorage newInstance(Context context, String str) {
        return new NecroSpStorage(context, str);
    }

    public void clean() {
        synchronized (this) {
            this.mMemoryCacheData.clear();
            getStorageHandler().removeCallbacksAndMessages(null);
            this.sp.edit().clear().apply();
        }
    }

    public void putString(String str, String str2) {
        synchronized (this) {
            putData2MemoryCache(str, str2);
            try {
                if (SafeUtils.canEncryption()) {
                    str2 = SafeUtils.ea(str2);
                }
                submitEditor(this.sp.edit().putString(str, str2), str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getString(java.lang.String r3, java.lang.String r4) {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = r2.getDataFromMemoryCache(r3);	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x000b;
    L_0x0007:
        r0 = (java.lang.String) r0;	 Catch:{ all -> 0x001d }
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
    L_0x000a:
        return r0;
    L_0x000b:
        r0 = r2.sp;	 Catch:{ all -> 0x001d }
        r0 = r0.getString(r3, r4);	 Catch:{ all -> 0x001d }
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        r1 = com.mqunar.necro.agent.util.SafeUtils.canEncryption();
        if (r1 == 0) goto L_0x000a;
    L_0x0018:
        r0 = com.mqunar.necro.agent.util.SafeUtils.da(r0);
        goto L_0x000a;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mqunar.necro.agent.util.NecroSpStorage.getString(java.lang.String, java.lang.String):java.lang.String");
    }

    public int getCount() {
        Map all;
        synchronized (this) {
            all = this.sp.getAll();
            if (!(this.mMemoryCacheData == null || this.mMemoryCacheData.isEmpty())) {
                all.putAll(this.mMemoryCacheData);
            }
        }
        return all.size();
    }

    public List<String> getKeys() {
        Map all;
        synchronized (this) {
            all = this.sp.getAll();
            if (!(this.mMemoryCacheData == null || this.mMemoryCacheData.isEmpty())) {
                all.putAll(this.mMemoryCacheData);
            }
        }
        return new ArrayList(all.keySet());
    }

    private void submitEditor(Editor editor, String str) {
        apply(editor, str);
    }

    private void apply(final Editor editor, final String str) {
        getStorageHandler().post(new Runnable() {
            public void run() {
                editor.commit();
                synchronized (NecroSpStorage.this) {
                    NecroSpStorage.this.mMemoryCacheData.remove(str);
                }
            }
        });
    }

    private boolean putData2MemoryCache(String str, Object obj) {
        if (this.mMemoryCacheData == null) {
            return false;
        }
        this.mMemoryCacheData.put(str, obj);
        return true;
    }

    private Object getDataFromMemoryCache(String str) {
        if (this.mMemoryCacheData == null || !this.mMemoryCacheData.containsKey(str)) {
            return null;
        }
        return this.mMemoryCacheData.get(str);
    }

    private HandlerThread getStorageHandlerThread() {
        if (mStorageHandlerThread == null) {
            mStorageHandlerThread = new HandlerThread("NecroStorage");
            mStorageHandlerThread.start();
        }
        return mStorageHandlerThread;
    }

    private Handler getStorageHandler() {
        if (mStorageHandler == null) {
            synchronized (NecroSpStorage.class) {
                if (mStorageHandler == null) {
                    mStorageHandler = new Handler(getStorageHandlerThread().getLooper());
                }
            }
        }
        return mStorageHandler;
    }
}
