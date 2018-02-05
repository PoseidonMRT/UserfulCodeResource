package com.mqunar.atomenv;

import android.text.TextUtils;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.json.JsonUtils;
import com.mqunar.storage.Storage;
import java.util.Map;

public class SwitchEnv {
    public static final String CLOSE_AD = "closead";
    public static final String CLOSE_WEB_LINK = "closeweblink";
    public static final String HAS_PUSH_OPTIONS = "pushoptions";
    public static final String KILL_PROGRESS_ON_QUIT = "killonquit";
    public static final String NET_TRAFFIC_TIPS = "nettips";
    public static final String NO_SHORT_CUT = "noshortcut";
    public static final String PUSH_DEFAULT_CLOSE = "pushclose";
    private static SwitchEnv a = null;
    private Storage b = Storage.newStorage(QApplication.getContext(), OwnerConstant.STORAGE_OWNER_SW);
    private Map<String, Object> c;
    protected Storage storage_sys = Storage.newStorage(QApplication.getContext(), OwnerConstant.STORAGE_OWNER_SYS);

    private SwitchEnv() {
        String string = this.storage_sys.getString(AtomEnvConstants.SYS_SWITCH, "");
        if (!TextUtils.isEmpty(string)) {
            try {
                this.c = JsonUtils.fromJson(string);
            } catch (Throwable e) {
                throw new RuntimeException(string, e);
            }
        }
    }

    public static SwitchEnv getInstance() {
        if (a == null) {
            synchronized (SwitchEnv.class) {
                if (a == null) {
                    a = new SwitchEnv();
                }
            }
        }
        return a;
    }

    public Map<String, Object> getSwitchMap() {
        return this.c;
    }

    public boolean isShowNetTips() {
        if (this.c == null || !this.c.containsKey(NET_TRAFFIC_TIPS)) {
            return false;
        }
        return Boolean.parseBoolean((String) this.c.get(NET_TRAFFIC_TIPS));
    }

    public boolean isCloseAd() {
        if (this.c == null || !this.c.containsKey(CLOSE_AD)) {
            return false;
        }
        return Boolean.parseBoolean((String) this.c.get(CLOSE_AD));
    }

    public boolean isCloseWebLink() {
        if (this.c == null || !this.c.containsKey(CLOSE_WEB_LINK)) {
            return false;
        }
        return Boolean.parseBoolean((String) this.c.get(CLOSE_WEB_LINK));
    }

    public boolean hasPushOptions() {
        if (this.c == null || !this.c.containsKey(HAS_PUSH_OPTIONS)) {
            return false;
        }
        return Boolean.parseBoolean((String) this.c.get(HAS_PUSH_OPTIONS));
    }

    public boolean isDefaultPushClose() {
        if (this.c == null || !this.c.containsKey(PUSH_DEFAULT_CLOSE)) {
            return false;
        }
        return Boolean.parseBoolean((String) this.c.get(PUSH_DEFAULT_CLOSE));
    }

    public boolean isKillProgressOnQuit() {
        if (this.c == null || !this.c.containsKey(KILL_PROGRESS_ON_QUIT)) {
            return false;
        }
        return Boolean.parseBoolean((String) this.c.get(KILL_PROGRESS_ON_QUIT));
    }

    public boolean isNoShortCut() {
        if (this.c == null || !this.c.containsKey(NO_SHORT_CUT)) {
            return false;
        }
        return Boolean.parseBoolean((String) this.c.get(NO_SHORT_CUT));
    }

    public boolean isPushClose() {
        return hasPushOptions() && this.b.getBoolean(AtomEnvConstants.SW_SPLASH, isDefaultPushClose());
    }

    public void setPushClose(boolean z) {
        this.b.putBoolean(AtomEnvConstants.SW_SPLASH, z);
    }
}
