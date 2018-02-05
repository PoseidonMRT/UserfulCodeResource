package org.acra.collector;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.acra.util.b;
import org.apache.commons.io.IOUtils;

final class k {
    @NonNull
    static String a(@Nullable String str, @NonNull Throwable th) {
        List arrayList = new ArrayList();
        arrayList.add("FileObserver");
        arrayList.add("WifiManager");
        arrayList.add("ANR-WatchDog");
        if (b.a() >= 2) {
            return a(arrayList);
        }
        return a(arrayList, str, th);
    }

    @NonNull
    private static String a(@NonNull List<String> list, @Nullable String str, @Nullable Throwable th) {
        String str2 = c.a(str, th) + IOUtils.LINE_SEPARATOR_UNIX;
        return str2 + b(list);
    }

    @NonNull
    private static String a(@NonNull List<String> list) {
        return b(list);
    }

    @NonNull
    private static String b(@NonNull List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = "";
        for (Entry key : Thread.getAllStackTraces().entrySet()) {
            String a;
            Thread thread = (Thread) key.getKey();
            String name = thread.getName();
            if (!(!thread.getThreadGroup().getName().equals("main") || thread.getId() == Thread.currentThread().getId() || list.contains(name) || name.startsWith("Binder"))) {
                if (thread.getName().equals("main")) {
                    a = a(thread);
                    str = a;
                } else {
                    stringBuilder.append(a(thread));
                }
            }
            a = str;
            str = a;
        }
        return str + stringBuilder.toString();
    }

    @NonNull
    private static String a(@NonNull Thread thread) {
        String str = "";
        StackTraceElement[] stackTrace = thread.getStackTrace();
        if (stackTrace.length == 0) {
            return str;
        }
        str = str + "thread name: " + thread.getName() + IOUtils.LINE_SEPARATOR_UNIX;
        if (!thread.getName().equals("main") && (stackTrace[0].toString().contains("java.lang.Object.wait") || stackTrace[0].toString().contains("android.os.MessageQueue.nativePollOnce") || stackTrace[0].getMethodName().equals("accept"))) {
            return "";
        }
        String str2 = str;
        for (StackTraceElement stackTraceElement : stackTrace) {
            str2 = str2 + stackTraceElement.toString() + IOUtils.LINE_SEPARATOR_UNIX;
        }
        return str2 + IOUtils.LINE_SEPARATOR_UNIX;
    }
}
