package com.mqunar.tools;

import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.http.HttpStatus;

public class DateTimeUtils {
    public static final int DATETIME_FIELD_REFERSH = 10;
    public static final String HH_mm = "HH:mm";
    public static final String HH_mm_ss = "HH:mm:ss";
    public static final String KEY_TSLGAPM = "chaos.liu.tslgapm";
    public static final String MM_Yue_dd_Ri = "MM月dd日";
    public static final String MM_dd = "MM-dd";
    public static final String MM_yy = "MM/yy";
    public static final String M_Yue_d_Ri = "M月d日";
    public static final long ONE_DAY = 86400000;
    public static final long ONE_HOUR = 3600000;
    public static final long ONE_MINUTE = 60000;
    public static final long ONE_SECOND = 1000;
    private static final String[] a = new String[]{yyyy_MM_dd_HH_mm_ss, yyyy_MM_dd_HH_mm, yyyy_MM_dd, yyyyMMdd};
    private static String[] b = new String[]{"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private static String[] c = new String[]{"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    public static final String d_Ri = "d日";
    public static final String dd_MM = "dd/MM";
    public static boolean hasServerTime = false;
    public static long tslgapm = 0;
    public static String tss = null;
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyy_MM = "yyyy-MM";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_Nian_MM_Yue = "yyyy年MM月";
    public static final String yyyy_Nian_MM_Yue_dd_Ri = "yyyy年MM月dd日";

    public static void cleanCalendarTime(Calendar calendar) {
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
    }

    public static <T> Calendar getCalendar(T t, Calendar calendar) {
        if (t != null) {
            try {
                return getCalendar(t);
            } catch (Exception e) {
            }
        }
        return (Calendar) calendar.clone();
    }

    public static <T> Calendar getCalendar(T t) {
        Calendar instance = Calendar.getInstance();
        instance.setLenient(false);
        if (t == null) {
            return null;
        }
        if (t instanceof Calendar) {
            instance.setTimeInMillis(((Calendar) t).getTimeInMillis());
        } else if (t instanceof Date) {
            instance.setTime((Date) t);
        } else if (t instanceof Long) {
            instance.setTimeInMillis(((Long) t).longValue());
        } else if (t instanceof String) {
            String str = (String) t;
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                if (Pattern.compile("\\d{4}年\\d{1,2}月\\d{1,2}日").matcher(str).find()) {
                    return getCalendarByPattern(a(str), yyyy_MM_dd);
                }
                return getCalendarByPatterns(str, a);
            } catch (Exception e) {
                try {
                    instance.setTimeInMillis(Long.valueOf(str).longValue());
                } catch (Throwable e2) {
                    throw new IllegalArgumentException(e2);
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
        return instance;
    }

    private static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String[] split = str.split("[年月日]");
        if (split.length == 1) {
            split = str.split("-");
        }
        for (int i = 0; i < 3; i++) {
            if (split[i].length() == 1) {
                split[i] = "0" + split[i];
            }
        }
        return split[0] + "-" + split[1] + "-" + split[2];
    }

    public static Calendar getCalendarByPattern(String str, String str2) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str2, Locale.US);
            simpleDateFormat.setLenient(false);
            Date parse = simpleDateFormat.parse(str);
            Calendar instance = Calendar.getInstance();
            instance.setLenient(false);
            instance.setTimeInMillis(parse.getTime());
            return instance;
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Calendar getCalendarByPatterns(String str, String[] strArr) {
        int i = 0;
        while (i < strArr.length) {
            try {
                return getCalendarByPattern(str, strArr[i]);
            } catch (Exception e) {
                i++;
            }
        }
        throw new IllegalArgumentException();
    }

    public static Calendar getCurrentDateTime() {
        Calendar instance = Calendar.getInstance();
        instance.setLenient(false);
        if (hasServerTime) {
            instance.setTimeInMillis(instance.getTimeInMillis() + tslgapm);
        }
        return instance;
    }

    public static Calendar getLoginServerDate() {
        return getCalendar(tss);
    }

    public static Calendar getDateAdd(Calendar calendar, int i) {
        if (calendar == null) {
            return null;
        }
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.add(5, i);
        return calendar2;
    }

    public static long getIntervalTimes(Calendar calendar, Calendar calendar2, long j) {
        if (calendar == null || calendar2 == null) {
            return 0;
        }
        return Math.abs(calendar.getTimeInMillis() - calendar2.getTimeInMillis()) / j;
    }

    public static int getIntervalDays(String str, String str2, String str3) {
        if (str == null || str2 == null) {
            return 0;
        }
        return getIntervalDays(getCalendarByPattern(str, str3), getCalendarByPattern(str2, str3));
    }

    public static <T> int getIntervalDays(T t, T t2) {
        Calendar calendar = getCalendar(t);
        Calendar calendar2 = getCalendar(t2);
        cleanCalendarTime(calendar);
        cleanCalendarTime(calendar2);
        return (int) getIntervalTimes(calendar, calendar2, ONE_DAY);
    }

    public static String getWeekDayFromCalendar(Calendar calendar) {
        if (calendar != null) {
            return b[calendar.get(7)];
        }
        throw new IllegalArgumentException();
    }

    public static String getWeekDayFromCalendar1(Calendar calendar) {
        if (calendar != null) {
            return c[calendar.get(7)];
        }
        throw new IllegalArgumentException();
    }

    public static boolean isLeapyear(String str) {
        Calendar calendar = getCalendar(str);
        if (calendar == null) {
            return false;
        }
        int i = calendar.get(1);
        if (i % 4 != 0 || (i % 100 == 0 && i % HttpStatus.SC_BAD_REQUEST != 0)) {
            return false;
        }
        return true;
    }

    public static boolean isRefersh(long j) {
        return isRefersh(600000, j);
    }

    public static boolean isRefersh(long j, long j2) {
        return new Date().getTime() - j2 >= j;
    }

    public static String printCalendarByPattern(Calendar calendar, String str) {
        if (calendar == null || str == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, Locale.US);
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static int compareCalendarIgnoreTime(Calendar calendar, Calendar calendar2) {
        if (calendar.get(1) > calendar2.get(1)) {
            return 1;
        }
        if (calendar.get(1) < calendar2.get(1)) {
            return -1;
        }
        if (calendar.get(2) > calendar2.get(2)) {
            return 1;
        }
        if (calendar.get(2) < calendar2.get(2)) {
            return -1;
        }
        if (calendar.get(5) > calendar2.get(5)) {
            return 1;
        }
        if (calendar.get(5) < calendar2.get(5)) {
            return -1;
        }
        return 0;
    }

    public static void setTimeWithHHmm(Calendar calendar, String str) {
        if (!TextUtils.isEmpty(str) && calendar != null) {
            String[] split = str.split(":");
            if (split.length == 2) {
                try {
                    cleanCalendarTime(calendar);
                    calendar.set(11, Integer.valueOf(split[0]).intValue());
                    calendar.set(12, Integer.valueOf(split[1]).intValue());
                } catch (NumberFormatException e) {
                }
            }
        }
    }
}
