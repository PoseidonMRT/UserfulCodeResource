package org.acra.collector;

import android.os.Build;
import android.support.annotation.NonNull;
import com.mqunar.necro.agent.bean.NecroParam;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.acra.ACRA;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

final class o {
    private static final String a = Build.BRAND;

    @NonNull
    static String a() {
        BufferedReader bufferedReader;
        StringBuilder a;
        Throwable e;
        BufferedReader bufferedReader2 = null;
        String str = "getprop";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop").getInputStream()), 8192);
            try {
                a = a(bufferedReader);
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (IOException e3) {
                e = e3;
                try {
                    ACRA.f.c(ACRA.e, "PropertyCollector.collectProperty could not retrieve data.", e);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e4) {
                        }
                    }
                    if (a != null) {
                        return a.toString();
                    }
                    return "";
                } catch (Throwable th) {
                    e = th;
                    bufferedReader2 = bufferedReader;
                    if (bufferedReader2 != null) {
                        try {
                            bufferedReader2.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw e;
                }
            }
        } catch (IOException e6) {
            e = e6;
            bufferedReader = bufferedReader2;
            ACRA.f.c(ACRA.e, "PropertyCollector.collectProperty could not retrieve data.", e);
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (a != null) {
                return "";
            }
            return a.toString();
        } catch (Throwable th2) {
            e = th2;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            throw e;
        }
        if (a != null) {
            return a.toString();
        }
        return "";
    }

    @NonNull
    private static StringBuilder a(@NonNull BufferedReader bufferedReader) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] b = b();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return stringBuilder;
            }
            String[] split = readLine.split(":");
            if (split.length >= 2) {
                if (b.length > 0) {
                    a(b, split);
                }
                a(stringBuilder, split);
            }
        }
    }

    private static void a(@NonNull StringBuilder stringBuilder, String[] strArr) {
        if (strArr[1] != null && !strArr[1].trim().equals(NecroParam.NECRO_EMPTY_ARRAY)) {
            strArr[0] = strArr[0].replace(FilenameUtils.EXTENSION_SEPARATOR, '_');
            stringBuilder.append(strArr[0]).append("=").append(a(strArr[1])).append(IOUtils.LINE_SEPARATOR_UNIX);
        }
    }

    private static void a(@NonNull String[] strArr, @NonNull String[] strArr2) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals(strArr2[0].trim())) {
                if (strArr.length > 1) {
                    ACRA.getErrorReporter().putCustomData("rom version" + (i + 1), a(strArr2[1]) + "  (" + a + ")");
                } else {
                    ACRA.getErrorReporter().putCustomData("rom version", a(strArr2[1]) + "  (" + a + ")");
                }
            }
        }
    }

    @NonNull
    private static String a(@NonNull String str) {
        return str.replace("[", "").replace("]", "");
    }

    private static String[] b() {
        Map c = c();
        for (String str : c.keySet()) {
            if (str != null && str.equalsIgnoreCase(a)) {
                return (String[]) c.get(str);
            }
        }
        return new String[0];
    }

    @NonNull
    private static Map<String, String[]> c() {
        Map<String, String[]> hashMap = new HashMap();
        hashMap.put("OPPO", new String[]{"[ro.build.version.opporom]"});
        hashMap.put("ONEPLUS", new String[]{"[ro.rom.version]"});
        hashMap.put("Xiaomi", new String[]{"[ro.miui.ui.version.name]", "[ro.build.version.incremental]"});
        hashMap.put("Huawei", new String[]{"[ro.build.version.emui]"});
        hashMap.put("Honor", new String[]{"[ro.build.version.emui]"});
        hashMap.put("QiKU", new String[]{"[ro.build.uiversion]"});
        hashMap.put("SMARTISAN", new String[]{"[ro.smartisan.version]"});
        hashMap.put("vivo", new String[]{"[ro.vivo.rom.version]"});
        hashMap.put("Letv", new String[]{"[ro.letv.release.version]"});
        hashMap.put("htc", new String[]{"[ro.build.sense.version]"});
        hashMap.put("Meizu", new String[]{"[ro.build.display.id]"});
        return hashMap;
    }
}
