package com.mqunar.tools.log;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.mqunar.atomenv.OwnerConstant;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.storage.Storage;
import java.lang.reflect.Method;
import java.util.Calendar;

@Deprecated
public class UELog {
    private static Storage a;
    private static Storage b;
    private static final Object c = new Object();
    private static final Calendar e = Calendar.getInstance();
    private Context d;

    static {
        e.set(2012, 0, 0, 0, 0, 0);
    }

    private static Storage a(Context context) {
        if (a == null) {
            synchronized ("qunar_ue") {
                if (a == null) {
                    a = Storage.newStorage(context, "qunar_ue");
                }
            }
        }
        return a;
    }

    private static Storage b(Context context) {
        if (b == null) {
            synchronized (OwnerConstant.STORAGE_OWNER_BETA) {
                if (b == null) {
                    b = Storage.newStorage(context, OwnerConstant.STORAGE_OWNER_BETA);
                }
            }
        }
        return b;
    }

    public UELog(Context context) {
        this.d = context;
    }

    public static long getSecond(Calendar calendar) {
        return (System.currentTimeMillis() - calendar.getTimeInMillis()) / 1000;
    }

    public static long getSecond() {
        return getSecond(e);
    }

    public void setUELogtoTag(View view, String str) {
        try {
            if (a(this.d).getBoolean("crazy", true)) {
                Class[] clsArr = new Class[]{View.class, String.class};
                Object[] objArr = new Object[]{view, str};
                a(Class.forName("com.mqunar.qav.uelog.QAVLog").getDeclaredMethod("getInstance", new Class[]{Context.class}).invoke(null, new Object[]{this.d}), "setUELogtoTag", clsArr, objArr);
            }
        } catch (Exception e) {
            QLog.w(e.getMessage(), new Object[0]);
        }
    }

    public void setUELogtoTag(View view, int i) {
        try {
            if (a(this.d).getBoolean("crazy", true)) {
                Class[] clsArr = new Class[]{View.class, Integer.class};
                Object[] objArr = new Object[]{view, Integer.valueOf(i)};
                a(Class.forName("com.mqunar.qav.uelog.QAVLog").getDeclaredMethod("getInstance", new Class[]{Context.class}).invoke(null, new Object[]{this.d}), "setUELogtoTag", clsArr, objArr);
            }
        } catch (Exception e) {
            QLog.w(e.getMessage(), new Object[0]);
        }
    }

    public void log(String str, String str2, View view) {
    }

    Object a(Object obj, String str, Class[] clsArr, Object... objArr) {
        Object obj2 = null;
        Class cls = obj.getClass();
        Throwable th = null;
        while (!cls.equals(Object.class)) {
            try {
                Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
                declaredMethod.setAccessible(true);
                obj2 = declaredMethod.invoke(obj, objArr);
                break;
            } catch (NoSuchMethodException e) {
                th = e;
                cls = cls.getSuperclass();
            } catch (Throwable th2) {
                th2.printStackTrace();
                throw new RuntimeException(th2);
            } catch (Throwable th22) {
                th22.printStackTrace();
                throw new RuntimeException(th22);
            }
        }
        if (th22 != null) {
            throw new RuntimeException(th22);
        }
        return obj2;
    }

    public void log(long j, String str, String str2, View view) {
    }

    public void log(String str, String str2) {
        a(str, str2);
        if (a(this.d).getBoolean("crazy", true)) {
            try {
                CharSequence stringBuilder = new StringBuilder();
                stringBuilder.append(getSecond());
                stringBuilder.append(Contact.OTHER);
                stringBuilder.append(str);
                stringBuilder.append(":set");
                stringBuilder.append(Contact.OTHER);
                stringBuilder.append(str2);
                a(stringBuilder, false);
            } catch (Exception e) {
                QLog.w(e.getMessage(), new Object[0]);
            }
        }
    }

    public void logIntent(String str, String str2) {
    }

    public void appendLog(CharSequence charSequence) {
        a(charSequence, true);
    }

    private void a(CharSequence charSequence, boolean z) {
        if (z) {
            a("UELogUtils", charSequence.toString());
        }
        synchronized (c) {
            if (b(this.d).getBoolean("beta_key_show_ue_log", false)) {
                Log.v("UELogUtils", charSequence.toString());
            }
            if (a(this.d).getBoolean("crazy", true)) {
                a(this.d).putInt("cc", a(this.d).getInt("cc", 0) + 1);
                a(this.d).append("ue", "|" + charSequence.toString());
            }
        }
    }

    public int getCount() {
        return a(this.d).getInt("cc", 0);
    }

    public String pop() {
        String string;
        synchronized (c) {
            string = a(this.d).getString("ue", "");
            a(this.d).clean();
        }
        return string;
    }

    private void a(String str, String str2) {
        try {
            if (a(this.d).getBoolean("crazy", true)) {
                Class[] clsArr = new Class[]{String.class, String.class};
                Object[] objArr = new Object[]{str, str2};
                a(Class.forName("com.mqunar.qav.uelog.QAVLog").getDeclaredMethod("getInstance", new Class[]{Context.class}).invoke(null, new Object[]{this.d}), "log", clsArr, objArr);
            }
        } catch (Exception e) {
            QLog.w(e.getMessage(), new Object[0]);
        }
    }
}
