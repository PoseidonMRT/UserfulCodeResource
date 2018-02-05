package com.mqunar.cock.utils;

import com.mqunar.cock.model.PubsyncGet;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.storage.Storage;

public class CockStore {
    private static Storage a = Storage.newStorage(QApplication.getContext());

    public static void savePubSync(PubsyncGet pubsyncGet) {
        if (pubsyncGet != null) {
            a.putSerializable("pubsync", pubsyncGet);
        }
    }

    public static PubsyncGet getPubSync() {
        return (PubsyncGet) a.getSerializable("pubsync");
    }
}
