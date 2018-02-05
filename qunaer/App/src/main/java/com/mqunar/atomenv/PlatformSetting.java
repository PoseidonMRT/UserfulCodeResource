package com.mqunar.atomenv;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.mqunar.atomenv.model.Config;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.json.JsonUtils;
import com.mqunar.storage.Storage;
import com.mqunar.tools.log.QLog;

class PlatformSetting {
    public static final SharedPreferences sharedPreferences = QApplication.getContext().getSharedPreferences("QunarPreferences", 0);
    private static Storage storage_sys = Storage.newStorage(QApplication.getContext(), OwnerConstant.STORAGE_OWNER_SYS);

    PlatformSetting() {
    }

    private static void setPid(String str) {
        storage_sys.putString(AtomEnvConstants.SYS_PID, str);
    }

    private static void setVid(String str) {
        storage_sys.putString(AtomEnvConstants.SYS_VID, str);
    }

    private static void setSid(String str) {
        storage_sys.putString(AtomEnvConstants.SYS_SID, str);
    }

    private static void setGid(String str) {
        storage_sys.putString(AtomEnvConstants.SYS_GID, str);
    }

    private static void setCid(String str, String str2, String str3) {
        CharSequence cid = GlobalEnv.getInstance().getCid();
        if (!str.equalsIgnoreCase(str2)) {
            storage_sys.putString(AtomEnvConstants.SYS_CID, str);
        } else if (!TextUtils.isEmpty(cid)) {
            storage_sys.putString(AtomEnvConstants.SYS_CID, cid);
        } else if (TextUtils.isEmpty(cid)) {
            storage_sys.putString(AtomEnvConstants.SYS_CID, str3);
        }
        storage_sys.putString(AtomEnvConstants.SYS_RCID, str);
    }

    private static void setWXAppId(String str) {
        storage_sys.putString(AtomEnvConstants.SYS_WXAPPID, str);
    }

    private static void setScheme(String str) {
        storage_sys.putString(AtomEnvConstants.SYS_SCHEME, str);
    }

    private static void setSchemeWap(String str) {
        storage_sys.putString(AtomEnvConstants.SYS_SCHEMEWAP, str);
    }

    private static void setConfig(String str) {
        try {
            Object preferences = getPreferences("dic", "");
            if (!TextUtils.isEmpty(preferences)) {
                storage_sys.putString(AtomEnvConstants.SYS_CID, preferences);
                removePreferences("dic");
            }
        } catch (Throwable th) {
            removePreferences("dic");
        }
        storage_sys.putString(AtomEnvConstants.SYS_CONFIG, str);
        try {
            Config config = (Config) JsonUtils.parseObject(str, Config.class);
            setCid(config.cid, config.ucid, config.dcid);
            setVid(config.vid);
            setPid(config.pid);
            setScheme(config.scheme);
            setSchemeWap(config.schemewap);
            setWXAppId(config.wxappid);
        } catch (Throwable th2) {
            QLog.e(th2);
        }
    }

    private static void setSwitch(String str) {
        storage_sys.putString(AtomEnvConstants.SYS_SWITCH, str);
        SwitchEnv.getInstance();
    }

    public static String getPreferences(String str, String str2) {
        try {
            str2 = sharedPreferences.getString(str, str2);
        } catch (Exception e) {
        }
        return str2;
    }

    public static void removePreferences(String str) {
        if (str != null) {
            Editor edit = sharedPreferences.edit();
            edit.remove(str);
            edit.commit();
        }
    }
}
