package com.mqunar.necro.agent.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Util {
    public static final String ACTIVITY_BACKGROUND_METRIC_PREFIX = "Mobile/Activity/Background/Name/";
    public static final String ACTIVITY_DISPLAY_NAME_PREFIX = "Display ";
    public static final String ACTIVITY_METRIC_PREFIX = "Mobile/Activity/Name/";
    private static final Random random = new Random();

    public static String slurp(InputStream inputStream) {
        char[] cArr = new char[8192];
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            int read = bufferedReader.read(cArr);
            if (read < 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append(cArr, 0, read);
        }
    }

    public static String sanitizeUrl(String str) {
        if (str == null) {
            return null;
        }
        return str;
    }

    public static Random getRandom() {
        return random;
    }

    public static String formatActivityMetricName(String str) {
        return ACTIVITY_METRIC_PREFIX + str;
    }

    public static String formatActivityBackgroundMetricName(String str) {
        return ACTIVITY_BACKGROUND_METRIC_PREFIX + str;
    }

    public static String formatActivityDisplayName(String str) {
        return ACTIVITY_DISPLAY_NAME_PREFIX + str;
    }

    public static String null2String(String str) {
        return str == null ? "" : str;
    }
}
