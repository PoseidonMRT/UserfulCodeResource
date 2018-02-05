package com.mqunar.tools.log;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.mqunar.contacts.basis.model.Contact;
import java.lang.reflect.Field;
import java.util.Calendar;
import org.apache.commons.io.IOUtils;

public class NewUELog {
    public NewUELog(Context context) {
    }

    public void setUELogtoTag(View view, String str) {
    }

    public void setUELogtoTag(View view, int i) {
    }

    public void log(String str, String str2, View view) {
    }

    public static String getRequestId() {
        return "";
    }

    public void log(long j, String str, String str2, View view) {
    }

    public static String genViewKey(View view) {
        return null;
    }

    public static String genAction(String str, View view) {
        return replace(str) + ":" + replace(view.getClass().getSimpleName());
    }

    public static String getPageName(View view) {
        return getPageName(view.getContext());
    }

    public static String getAtomName(Class cls) {
        try {
            return ((String) Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("getPackageName", new Class[]{String.class}).invoke(null, new Object[]{cls.getName()})).replace("com.mqunar.", "").replace("atom.", "");
        } catch (Exception e) {
            return null;
        }
    }

    public static String getPageName(Context context) {
        String str;
        String simpleName = context.getClass().getSimpleName();
        try {
            Class cls = Class.forName("com.mqunar.core.basectx.launcherfragment.TransparentFragmentActivityBase");
            Class cls2 = Class.forName("com.mqunar.core.basectx.launcherfragment.LauncherFragmentActivityBase");
            Field declaredField;
            if (cls.isAssignableFrom(context.getClass())) {
                declaredField = cls.getDeclaredField("_fragmentName");
                declaredField.setAccessible(true);
                str = (String) declaredField.get(context);
            } else {
                if (cls2.isAssignableFrom(context.getClass())) {
                    declaredField = cls2.getDeclaredField("_fragmentName");
                    declaredField.setAccessible(true);
                    str = (String) declaredField.get(context);
                }
                str = simpleName;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        Object atomName = getAtomName(context.getClass());
        if (TextUtils.isEmpty(atomName)) {
            return str;
        }
        return atomName + "." + str;
    }

    public static String replace(String str) {
        return TextUtils.isEmpty(str) ? str : str.replace("|", "｜").replace(Contact.OTHER, "＊").replace(":", "：").replace("&", "＆").replace(IOUtils.LINE_SEPARATOR_UNIX, "、Ｎ").trim();
    }

    public static long getSecond(Calendar calendar) {
        return System.currentTimeMillis();
    }

    public static long getSecond() {
        return System.currentTimeMillis();
    }

    public void log(String str, String str2) {
    }

    public void logIntent(String str, String str2) {
        logIntent(str, str2, null, null);
    }

    public void logIntent(String str, String str2, String str3, String str4) {
    }

    public void appendLog(CharSequence charSequence) {
    }

    public int getCount() {
        return 0;
    }

    public String pop() {
        return null;
    }
}
