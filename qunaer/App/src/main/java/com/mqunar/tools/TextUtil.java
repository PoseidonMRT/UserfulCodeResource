package com.mqunar.tools;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    public static CharSequence toBold(String str, String str2, String str3) {
        int i = 0;
        if (CheckUtils.isEmpty(str2)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int length = CheckUtils.isEmpty(str) ? 0 : stringBuilder.append(str).length();
        if (!CheckUtils.isEmpty(str2)) {
            i = stringBuilder.append(str2).length();
        }
        if (!CheckUtils.isEmpty(str3)) {
            stringBuilder.append(str3).length();
        }
        CharSequence spannableString = new SpannableString(stringBuilder.toString());
        spannableString.setSpan(new StyleSpan(1), length, i, 33);
        return spannableString;
    }

    public static CharSequence toColor(String str, String str2, String str3, int i) {
        int i2 = 0;
        if (CheckUtils.isEmpty(str2)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int length = CheckUtils.isEmpty(str) ? 0 : stringBuilder.append(str).length();
        if (!CheckUtils.isEmpty(str2)) {
            i2 = stringBuilder.append(str2).length();
        }
        if (!CheckUtils.isEmpty(str3)) {
            stringBuilder.append(str3).length();
        }
        CharSequence spannableString = new SpannableString(stringBuilder.toString());
        spannableString.setSpan(new ForegroundColorSpan(i), length, i2, 33);
        return spannableString;
    }

    public static CharSequence colorDigitsInString(String str, int i) {
        if (CheckUtils.isEmpty(str)) {
            return null;
        }
        Matcher matcher = Pattern.compile("\\d+").matcher(str);
        Map hashMap = new HashMap();
        int i2 = 0;
        while (matcher.find(i2)) {
            int indexOf = str.indexOf(matcher.group(0), i2);
            i2 = matcher.group(0).length() + indexOf;
            hashMap.put(Integer.valueOf(indexOf), Integer.valueOf(i2));
        }
        CharSequence spannableString = new SpannableString(str.toString());
        for (Integer num : hashMap.keySet()) {
            spannableString.setSpan(new ForegroundColorSpan(i), num.intValue(), ((Integer) hashMap.get(num)).intValue(), 33);
        }
        return spannableString;
    }

    public static CharSequence toBold(String str) {
        return toBold(null, str, null);
    }

    public static CharSequence toSize(String str, int i) {
        return toSize(null, str, null, i);
    }

    public static CharSequence toSize(String str, String str2, String str3, int i, boolean z) {
        int i2 = 0;
        if (CheckUtils.isEmpty(str2)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int length = CheckUtils.isEmpty(str) ? 0 : stringBuilder.append(str).length();
        if (!CheckUtils.isEmpty(str2)) {
            i2 = stringBuilder.append(str2).length();
        }
        if (!CheckUtils.isEmpty(str3)) {
            stringBuilder.append(str3).length();
        }
        CharSequence spannableString = new SpannableString(stringBuilder.toString());
        spannableString.setSpan(new AbsoluteSizeSpan(i, z), length, i2, 33);
        return spannableString;
    }

    public static CharSequence toSize(String str, String str2, String str3, int i) {
        return toSize(str, str2, str3, i, true);
    }

    public static CharSequence joinSpan(CharSequence... charSequenceArr) {
        if (CheckUtils.isEmpty(charSequenceArr)) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (CharSequence append : charSequenceArr) {
            spannableStringBuilder.append(append);
        }
        return spannableStringBuilder.subSequence(0, spannableStringBuilder.length());
    }

    public static CharSequence joinAllowedNull(Object... objArr) {
        if (CheckUtils.isEmpty(objArr)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object obj : objArr) {
            if (!CheckUtils.isEmpty(obj)) {
                stringBuilder.append(obj.toString());
            }
        }
        if (CheckUtils.isEmpty(stringBuilder)) {
            return null;
        }
        return stringBuilder.toString();
    }

    public static CharSequence joinNotAllowedNull(Object... objArr) {
        if (CheckUtils.isContainsEmpty(objArr)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object obj : objArr) {
            stringBuilder.append(obj.toString());
        }
        return stringBuilder.toString();
    }
}
