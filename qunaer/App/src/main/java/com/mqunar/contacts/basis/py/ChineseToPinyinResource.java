package com.mqunar.contacts.basis.py;

import android.content.Context;
import android.text.TextUtils;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.basis.utils.CrashUtils;
import com.mqunar.tools.log.QLog;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

public class ChineseToPinyinResource {
    private static ChineseToPinyinResource theInstance;
    private Properties unicodeToHanyuPinyinTable = null;

    class Field {
        static final String COMMA = ",";
        static final String LEFT_BRACKET = "(";
        static final String RIGHT_BRACKET = ")";

        Field() {
        }
    }

    private void setUnicodeToHanyuPinyinTable(Properties properties) {
        this.unicodeToHanyuPinyinTable = properties;
    }

    private Properties getUnicodeToHanyuPinyinTable() {
        return this.unicodeToHanyuPinyinTable;
    }

    private ChineseToPinyinResource(Context context) {
        initializeResource(context);
    }

    private void initializeResource(Context context) {
        try {
            InputStream open = context.getAssets().open("unicode_to_hanyu_pinyin");
            setUnicodeToHanyuPinyinTable(new Properties());
            getUnicodeToHanyuPinyinTable().load(open);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static String toPrefix(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            QLog.d("Contact", " NULL TO PREFIX", new Object[0]);
            return Contact.OTHER;
        } else if (isChineseChar(str)) {
            try {
                return getInstance(context).getHanyuPinyinStringArray(str.charAt(0))[0].substring(0, 1).toUpperCase();
            } catch (Throwable e) {
                QLog.d("Contact", CrashUtils.getStackTraceString(e), new Object[0]);
                return Contact.OTHER;
            }
        } else if (isAZ(str)) {
            if (isLetterAZ(str)) {
                return str.substring(0, 1).toUpperCase();
            }
            return str.substring(0, 1);
        } else if (isNumber(str)) {
            return Contact.NUMBER;
        } else {
            return Contact.OTHER;
        }
    }

    public static boolean isAZ(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.substring(0, 1).matches("[a-zA-Z]");
    }

    public static boolean isLetterAZ(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.substring(0, 1).matches("[a-z]");
    }

    public static boolean isNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.substring(0, 1).matches("[0-9]");
    }

    public static boolean isChineseChar(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.compile("[一-龥]").matcher(str.substring(0, 1)).find();
    }

    public String[] getHanyuPinyinStringArray(char c) {
        String hanyuPinyinRecordFromChar = getHanyuPinyinRecordFromChar(c);
        if (hanyuPinyinRecordFromChar == null) {
            return null;
        }
        int indexOf = hanyuPinyinRecordFromChar.indexOf("(");
        return hanyuPinyinRecordFromChar.substring(indexOf + "(".length(), hanyuPinyinRecordFromChar.lastIndexOf(")")).split(",");
    }

    private boolean isValidRecord(String str) {
        String str2 = "(none0)";
        if (str == null || str.equals("(none0)") || !str.startsWith("(") || !str.endsWith(")")) {
            return false;
        }
        return true;
    }

    private String getHanyuPinyinRecordFromChar(char c) {
        String property = getUnicodeToHanyuPinyinTable().getProperty(Integer.toHexString(c).toUpperCase());
        return isValidRecord(property) ? property : null;
    }

    public static ChineseToPinyinResource getInstance(Context context) {
        if (theInstance == null) {
            theInstance = new ChineseToPinyinResource(context);
        }
        return theInstance;
    }
}
