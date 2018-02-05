package com.mqunar.json;

import java.util.List;
import java.util.Map;

public class JsonUtils {
    static IParser a = new b();

    interface IParser {
        Map<String, Object> fromBean(Object obj);

        Map<String, Object> fromJson(String str);

        List parseArray(String str, Class<?> cls);

        Object parseObject(String str, Class<?> cls);

        String toJsonString(Object obj);
    }

    public static void chooseParser(String str) {
        try {
            if ("fastjson".equals(str)) {
                a = new a();
            } else if ("jackson".equals(str)) {
                a = new Jackson();
            } else {
                throw new IllegalArgumentException("no such parser found");
            }
        } catch (Exception e) {
            throw new JsonwireException(str + "not found in library");
        }
    }

    public static <T> T parseObject(String str, Class<T> cls) {
        return a.parseObject(str, cls);
    }

    public static <T> List<T> parseArray(String str, Class<T> cls) {
        return a.parseArray(str, cls);
    }

    public static String toJsonString(Object obj) {
        return a.toJsonString(obj);
    }

    public static Map<String, Object> fromJson(String str) {
        return a.fromJson(str);
    }

    public static Map<String, Object> fromBean(Object obj) {
        return a.fromBean(obj);
    }

    public static String getParserName() {
        return a.getClass().getSimpleName().toLowerCase();
    }
}
