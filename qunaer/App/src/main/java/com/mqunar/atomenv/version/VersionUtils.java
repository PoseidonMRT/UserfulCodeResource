package com.mqunar.atomenv.version;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.mqunar.atomenv.annotation.Since;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.storage.Storage;
import com.mqunar.tools.log.QLog;
import java.io.File;
import java.io.Serializable;

public class VersionUtils {
    public static final int ATOM_VERSION_NOT_FOUND = -1;
    public static final int ATOM_VERSION_PARSE_ERROR = -2;

    public enum Atom {
        Attemper("com.mqunar.atom.attemper"),
        Browser("com.mqunar.atom.browser"),
        Bus("com.mqunar.atom.bus"),
        Car("com.mqunar.atom.car"),
        Carpool("com.mqunar.atom.carpool"),
        Collab("com.mqunar.atom.collab"),
        Dangdi("com.mqunar.atom.localman"),
        Debugsetting("com.mqunar.atom.debugsetting"),
        Flight("com.mqunar.atom.flight"),
        Framework("com.mqunar.framework"),
        Gonglue("com.mqunar.atom.travelplan"),
        Gongyu("com.mqunar.atom.gongyu"),
        Groupbuy("com.mqunar.atom.gb"),
        Hepburn("com.mqunar.hepburn"),
        Homealex("com.mqunar.atom.alexhome"),
        Hotel("com.mqunar.atom.hotel"),
        Hy("com.mqunar.hy"),
        IM("com.mqunar.atom.im"),
        ImageCache("com.mqunar.imagecache"),
        IMSdk("com.mqunar.imsdk"),
        InterCar("com.mqunar.atom.intercar"),
        LuoTuoShu("com.mqunar.atom.luotuoshu"),
        Movie("com.mqunar.atom.mv"),
        Open("com.mqunar.atom.open"),
        Order("com.mqunar.atom.order"),
        Patch("com.mqunar.patch"),
        Pay("com.mqunar.pay"),
        Push("com.mqunar.atom.push"),
        Share("com.mqunar.atom.share"),
        Sight("com.mqunar.atom.sight"),
        Train("com.mqunar.atom.train"),
        UserCenter("com.mqunar.atom.uc"),
        Vacation("om.mqunar.atom.vacation"),
        Voice("com.mqunar.atom.voice"),
        Wagon("com.mqunar.wagon"),
        WaiMai("com.mqunar.atom.waimai");
        
        private String packageName;

        private Atom(String str) {
            this.packageName = str;
        }

        public String getPackageName() {
            return this.packageName;
        }
    }

    public class AtomInfo implements Serializable {
        public byte downloadFlag = (byte) 0;
        public String fileName;
        public String md5;
        public String packageName;
        public String patchUrl;
        public byte restart;
        public String touch;
        public byte type = (byte) 1;
        public String url;
        public int version;
    }

    public static int getAtomVersionCode(Atom atom) {
        return getAtomVersionCode(atom.getPackageName());
    }

    @Since(compile = "1.1.5")
    public static AtomInfo getAtomVersionInfoFromUpdate(Atom atom) {
        return getAtomVersionInfoFromUpdate(atom.getPackageName());
    }

    @Since(compile = "1.1.5")
    public static AtomInfo getAtomVersionInfoFromUpdate(String str) {
        try {
            for (AtomInfo atomInfo : JSON.parseArray(Storage.newStorage(QApplication.getContext(), "spider_minfo").getString("minfo_json", ""), AtomInfo.class)) {
                if (str.equals(atomInfo.packageName)) {
                    return atomInfo;
                }
            }
            return null;
        } catch (Throwable th) {
            QLog.e(th, "get atom version error", new Object[0]);
            return null;
        }
    }

    @Since(compile = "1.1.5")
    public static File getAtomFolder(Context context) {
        return new File(Storage.getAppFileDir(context) + "/data/");
    }

    public static File getAtomFileWithUpdate(Context context, String str) {
        try {
            AtomInfo atomVersionInfoFromUpdate = getAtomVersionInfoFromUpdate(str);
            if (atomVersionInfoFromUpdate == null) {
                return null;
            }
            String[] split = atomVersionInfoFromUpdate.url.split("/");
            return new File(getAtomFolder(context), split[split.length - 1]);
        } catch (Throwable th) {
            QLog.e(th, "get atom file error", new Object[0]);
            return null;
        }
    }

    public static int getAtomVersionCode(String str) {
        try {
            for (AtomInfo atomInfo : JSON.parseArray(QApplication.getVersionInfo(), AtomInfo.class)) {
                if (str.equals(atomInfo.packageName)) {
                    return atomInfo.version;
                }
            }
            return 0;
        } catch (Throwable th) {
            QLog.e(th);
            return -1;
        }
    }
}
