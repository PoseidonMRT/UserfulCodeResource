package com.mqunar.cock.utils;

public final class CockConstants {
    public static final String ACTION_MESSAGE = "android.intent.action.MESSAGE_NOTIFIED";
    public static final String ACTION_MESSAGE_REPRORTED = "action.im.message.reported";
    public static final String ACTION_SESSION_INFO_CHANGE = "action.im.group.admin.change";
    public static final String EXTRA_OBJ = "message_obj";
    public static final String EXTRA_WHAT = "message_what";

    public static String getAppname() {
        return "QuTui";
    }
}
