package com.mqunar.necro.agent.util;

import android.annotation.TargetApi;
import android.util.Base64;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;

public class SafeUtils {
    private static final AgentLog log = AgentLogManager.getAgentLog();

    private static byte[] ea(byte[] bArr) {
        try {
            return (byte[]) ReflectUtils.invokeStaticMethod("qunar.lego.utils.Goblin", "ea", new Class[]{byte[].class}, new Object[]{bArr});
        } catch (Throwable th) {
            log.error("reflect failed :" + th);
            return bArr;
        }
    }

    private static byte[] da(byte[] bArr) {
        try {
            return (byte[]) ReflectUtils.invokeStaticMethod("qunar.lego.utils.Goblin", "da", new Class[]{byte[].class}, new Object[]{bArr});
        } catch (Throwable th) {
            log.error("reflect failed :" + th);
            return bArr;
        }
    }

    @TargetApi(8)
    public static String ea(String str) {
        try {
            Object bytes = str.getBytes("UTF-8");
            byte[] bArr = new byte[(bytes.length + 1)];
            bArr[0] = (byte) 7;
            System.arraycopy(bytes, 0, bArr, 1, bytes.length);
            str = Base64.encodeToString(ea(bArr), 2);
        } catch (Throwable th) {
            log.error("ea str failed : " + th);
        }
        return str;
    }

    @TargetApi(8)
    public static String da(String str) {
        try {
            Object da = da(Base64.decode(str, 2));
            Object obj = new byte[(da.length - 1)];
            System.arraycopy(da, 1, obj, 0, obj.length);
            return new String(obj, "UTF-8");
        } catch (Throwable th) {
            log.error("ea str failed : " + th);
            return str;
        }
    }

    public static boolean canEncryption() {
        if (ReflectUtils.isMethodExit("qunar.lego.utils.Goblin", "ea", new Class[]{byte[].class})) {
            if (ReflectUtils.isMethodExit("qunar.lego.utils.Goblin", "da", new Class[]{byte[].class})) {
                return true;
            }
        }
        return false;
    }
}
