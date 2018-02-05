package com.mqunar.necro.agent.util;

import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import java.lang.reflect.Method;

class ReflectUtils {
    private static final AgentLog log = AgentLogManager.getAgentLog();

    ReflectUtils() {
    }

    private static Method getMethod(Class<?> cls, String str, Class<?>[] clsArr) {
        Method method = null;
        while (cls != null) {
            try {
                method = cls.getDeclaredMethod(str, clsArr);
            } catch (Exception e) {
            }
            if (method != null) {
                method.setAccessible(true);
                break;
            }
            cls = cls.getSuperclass();
        }
        return method;
    }

    static Object invokeStaticMethod(String str, String str2, Class<?>[] clsArr, Object[] objArr) {
        Object obj = null;
        try {
            obj = getMethod(Class.forName(str), str2, clsArr).invoke(null, objArr);
        } catch (Exception e) {
            log.error("reflect failed :" + e);
        }
        return obj;
    }

    static boolean isMethodExit(String str, String str2, Class<?>[] clsArr) {
        try {
            Class.forName(str).getDeclaredMethod(str2, clsArr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
