package org.acra.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.android.internal.util.Predicate;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import org.acra.collections.a;
import org.apache.commons.io.IOUtils;

public final class g {
    private static final Predicate<String> a = new Predicate<String>() {
        public /* synthetic */ boolean apply(Object obj) {
            return a((String) obj);
        }

        public boolean a(String str) {
            return true;
        }
    };

    public static void a(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    @NonNull
    public static String a(@NonNull InputStream inputStream) {
        return a(inputStream, a, -1);
    }

    @NonNull
    public static String a(@NonNull InputStream inputStream, Predicate<String> predicate) {
        return a(inputStream, predicate, -1);
    }

    @NonNull
    public static String a(@NonNull InputStream inputStream, int i) {
        return a(inputStream, a, i);
    }

    @NonNull
    public static String a(@NonNull InputStream inputStream, Predicate<String> predicate, int i) {
        Iterable linkedList;
        Closeable bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8192);
        if (i == -1) {
            try {
                linkedList = new LinkedList();
            } catch (Throwable th) {
                a(bufferedReader);
            }
        } else {
            linkedList = new a(i);
        }
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                String join = TextUtils.join(IOUtils.LINE_SEPARATOR_UNIX, linkedList);
                a(bufferedReader);
                return join;
            } else if (predicate.apply(readLine)) {
                linkedList.add(readLine);
            }
        }
    }
}
