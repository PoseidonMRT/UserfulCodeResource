package com.mqunar.hy.res.utils;

import android.os.FileObserver;
import com.mqunar.hy.res.model.HybridInfo;
import java.util.HashMap;
import java.util.Map;

public class FileObserverManager {
    private static Map<String, HybridInfo> hybridInfoMap = new HashMap();
    private static Map<String, FileObserver> map = new HashMap();

    class MyFileObserver extends FileObserver {
        private String pa = "";

        public MyFileObserver(String str) {
            super(str, 4095);
        }

        public void onEvent(int i, String str) {
            if (i == 2 || i == 512 || i == 128 || i == 64 || i == 1024 || i == 2048) {
                FileObserverManager.removeObserver(this.pa);
            }
        }
    }

    public static void addHybridInfoObserver(HybridInfo hybridInfo) {
        FileObserver fileObserver = (FileObserver) map.get(hybridInfo.path);
        if (fileObserver == null) {
            fileObserver = new MyFileObserver(hybridInfo.path);
            map.put(hybridInfo.path, fileObserver);
        } else {
            fileObserver.stopWatching();
        }
        fileObserver.startWatching();
        hybridInfoMap.put(hybridInfo.path, hybridInfo);
    }

    public static void removeObserver(String str) {
        FileObserver fileObserver = (FileObserver) map.get(str);
        if (fileObserver != null) {
            fileObserver.stopWatching();
        }
        map.remove(str);
        HybridInfo hybridInfo = (HybridInfo) hybridInfoMap.get(str);
        if (hybridInfo != null) {
            hybridInfo.checked = false;
        }
        hybridInfoMap.remove(str);
    }
}
