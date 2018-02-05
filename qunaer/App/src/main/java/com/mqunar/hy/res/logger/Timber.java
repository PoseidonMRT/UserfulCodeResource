package com.mqunar.hy.res.logger;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

public final class Timber {
    private static final List<Tree> FOREST = new ArrayList();
    private static final Tree[] TREE_ARRAY_EMPTY = new Tree[0];
    private static final Tree TREE_OF_SOULS = new Tree() {
        public void v(String str, Object... objArr) {
            for (Tree v : Timber.forestAsArray) {
                v.v(str, objArr);
            }
        }

        public void v(Throwable th, String str, Object... objArr) {
            for (Tree v : Timber.forestAsArray) {
                v.v(th, str, objArr);
            }
        }

        public void d(String str, Object... objArr) {
            for (Tree d : Timber.forestAsArray) {
                d.d(str, objArr);
            }
        }

        public void d(Throwable th, String str, Object... objArr) {
            for (Tree d : Timber.forestAsArray) {
                d.d(th, str, objArr);
            }
        }

        public void i(String str, Object... objArr) {
            for (Tree i : Timber.forestAsArray) {
                i.i(str, objArr);
            }
        }

        public void i(Throwable th, String str, Object... objArr) {
            for (Tree i : Timber.forestAsArray) {
                i.i(th, str, objArr);
            }
        }

        public void w(String str, Object... objArr) {
            for (Tree w : Timber.forestAsArray) {
                w.w(str, objArr);
            }
        }

        public void w(Throwable th, String str, Object... objArr) {
            for (Tree w : Timber.forestAsArray) {
                w.w(th, str, objArr);
            }
        }

        public void e(String str, Object... objArr) {
            for (Tree e : Timber.forestAsArray) {
                e.e(str, objArr);
            }
        }

        public void e(Throwable th, String str, Object... objArr) {
            for (Tree e : Timber.forestAsArray) {
                e.e(th, str, objArr);
            }
        }

        public void wtf(String str, Object... objArr) {
            for (Tree wtf : Timber.forestAsArray) {
                wtf.wtf(str, objArr);
            }
        }

        public void wtf(Throwable th, String str, Object... objArr) {
            for (Tree wtf : Timber.forestAsArray) {
                wtf.wtf(th, str, objArr);
            }
        }

        public void log(int i, String str, Object... objArr) {
            for (Tree log : Timber.forestAsArray) {
                log.log(i, str, objArr);
            }
        }

        public void log(int i, Throwable th, String str, Object... objArr) {
            for (Tree log : Timber.forestAsArray) {
                log.log(i, th, str, objArr);
            }
        }

        protected void log(int i, String str, String str2, Throwable th) {
            throw new AssertionError("Missing override for log method.");
        }
    };
    static volatile Tree[] forestAsArray = TREE_ARRAY_EMPTY;

    public abstract class Tree {
        final ThreadLocal<String> explicitTag = new ThreadLocal();

        protected abstract void log(int i, String str, String str2, Throwable th);

        String getTag() {
            String str = (String) this.explicitTag.get();
            if (str != null) {
                this.explicitTag.remove();
            }
            return str;
        }

        public void v(String str, Object... objArr) {
            prepareLog(2, null, str, objArr);
        }

        public void v(Throwable th, String str, Object... objArr) {
            prepareLog(2, th, str, objArr);
        }

        public void d(String str, Object... objArr) {
            prepareLog(3, null, str, objArr);
        }

        public void d(Throwable th, String str, Object... objArr) {
            prepareLog(3, th, str, objArr);
        }

        public void i(String str, Object... objArr) {
            prepareLog(4, null, str, objArr);
        }

        public void i(Throwable th, String str, Object... objArr) {
            prepareLog(4, th, str, objArr);
        }

        public void w(String str, Object... objArr) {
            prepareLog(5, null, str, objArr);
        }

        public void w(Throwable th, String str, Object... objArr) {
            prepareLog(5, th, str, objArr);
        }

        public void e(String str, Object... objArr) {
            prepareLog(6, null, str, objArr);
        }

        public void e(Throwable th, String str, Object... objArr) {
            prepareLog(6, th, str, objArr);
        }

        public void wtf(String str, Object... objArr) {
            prepareLog(7, null, str, objArr);
        }

        public void wtf(Throwable th, String str, Object... objArr) {
            prepareLog(7, th, str, objArr);
        }

        public void log(int i, String str, Object... objArr) {
            prepareLog(i, null, str, objArr);
        }

        public void log(int i, Throwable th, String str, Object... objArr) {
            prepareLog(i, th, str, objArr);
        }

        protected boolean isLoggable(int i) {
            return true;
        }

        private void prepareLog(int i, Throwable th, String str, Object... objArr) {
            if (isLoggable(i)) {
                String str2;
                if (str == null || str.length() != 0) {
                    str2 = str;
                } else {
                    str2 = null;
                }
                if (str2 != null) {
                    if (objArr.length > 0) {
                        str2 = String.format(str2, objArr);
                    }
                    if (th != null) {
                        str2 = str2 + IOUtils.LINE_SEPARATOR_UNIX + getStackTraceString(th);
                    }
                } else if (th != null) {
                    str2 = getStackTraceString(th);
                } else {
                    return;
                }
                log(i, getTag(), str2, th);
            }
        }

        private String getStackTraceString(Throwable th) {
            Writer stringWriter = new StringWriter(256);
            PrintWriter printWriter = new PrintWriter(stringWriter, false);
            th.printStackTrace(printWriter);
            printWriter.flush();
            return stringWriter.toString();
        }
    }

    public class DebugTree extends Tree {
        private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");
        private static final int CALL_STACK_INDEX = 5;
        private static final int MAX_LOG_LENGTH = 4000;

        protected String createStackElementTag(StackTraceElement stackTraceElement) {
            String className = stackTraceElement.getClassName();
            Matcher matcher = ANONYMOUS_CLASS.matcher(className);
            if (matcher.find()) {
                className = matcher.replaceAll("");
            }
            return className.substring(className.lastIndexOf(46) + 1);
        }

        final String getTag() {
            String tag = super.getTag();
            if (tag != null) {
                return tag;
            }
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            if (stackTrace.length > 5) {
                return createStackElementTag(stackTrace[5]);
            }
            throw new IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }

        protected void log(int i, String str, String str2, Throwable th) {
            if (str2.length() >= 4000) {
                int i2 = 0;
                int length = str2.length();
                while (i2 < length) {
                    int min;
                    int indexOf = str2.indexOf(10, i2);
                    if (indexOf == -1) {
                        indexOf = length;
                    }
                    while (true) {
                        min = Math.min(indexOf, i2 + 4000);
                        String substring = str2.substring(i2, min);
                        if (i == 7) {
                            Log.wtf(str, substring);
                        } else {
                            Log.println(i, str, substring);
                        }
                        if (min >= indexOf) {
                            break;
                        }
                        i2 = min;
                    }
                    i2 = min + 1;
                }
            } else if (i == 7) {
                Log.wtf(str, str2);
            } else {
                Log.println(i, str, str2);
            }
        }
    }

    public static void v(@NonNls String str, Object... objArr) {
        TREE_OF_SOULS.v(str, objArr);
    }

    public static void v(Throwable th, @NonNls String str, Object... objArr) {
        TREE_OF_SOULS.v(th, str, objArr);
    }

    public static void d(@NonNls String str, Object... objArr) {
        TREE_OF_SOULS.d(str, objArr);
    }

    public static void d(Throwable th, @NonNls String str, Object... objArr) {
        TREE_OF_SOULS.d(th, str, objArr);
    }

    public static void i(@NonNls String str, Object... objArr) {
        TREE_OF_SOULS.i(str, objArr);
    }

    public static void i(Throwable th, @NonNls String str, Object... objArr) {
        TREE_OF_SOULS.i(th, str, objArr);
    }

    public static void w(@NonNls String str, Object... objArr) {
        TREE_OF_SOULS.w(str, objArr);
    }

    public static void w(Throwable th, @NonNls String str, Object... objArr) {
        TREE_OF_SOULS.w(th, str, objArr);
    }

    public static void e(@NonNls String str, Object... objArr) {
        TREE_OF_SOULS.e(str, objArr);
    }

    public static void e(Throwable th, Object... objArr) {
        TREE_OF_SOULS.e(th, "", objArr);
    }

    public static void e(Throwable th, @NonNls String str, Object... objArr) {
        TREE_OF_SOULS.e(th, str, objArr);
    }

    public static void wtf(@NonNls String str, Object... objArr) {
        TREE_OF_SOULS.wtf(str, objArr);
    }

    public static void wtf(Throwable th, @NonNls String str, Object... objArr) {
        TREE_OF_SOULS.wtf(th, str, objArr);
    }

    public static void log(int i, @NonNls String str, Object... objArr) {
        TREE_OF_SOULS.log(i, str, objArr);
    }

    public static void log(int i, Throwable th, @NonNls String str, Object... objArr) {
        TREE_OF_SOULS.log(i, th, str, objArr);
    }

    public static Tree asTree() {
        return TREE_OF_SOULS;
    }

    public static Tree tag(String str) {
        for (Tree tree : forestAsArray) {
            tree.explicitTag.set(str);
        }
        return TREE_OF_SOULS;
    }

    public static void plant(Tree tree) {
        if (tree == null) {
            throw new NullPointerException("tree == null");
        } else if (tree.equals(TREE_OF_SOULS)) {
            throw new IllegalArgumentException("Cannot plant Timber into itself.");
        } else {
            synchronized (FOREST) {
                FOREST.add(tree);
                forestAsArray = (Tree[]) FOREST.toArray(new Tree[FOREST.size()]);
            }
        }
    }

    public static void uproot(Tree tree) {
        synchronized (FOREST) {
            if (FOREST.remove(tree)) {
                forestAsArray = (Tree[]) FOREST.toArray(new Tree[FOREST.size()]);
            } else {
                throw new IllegalArgumentException("Cannot uproot tree which is not planted: " + tree);
            }
        }
    }

    public static void uprootAll() {
        synchronized (FOREST) {
            FOREST.clear();
            forestAsArray = TREE_ARRAY_EMPTY;
        }
    }

    public static List<Tree> forest() {
        List<Tree> unmodifiableList;
        synchronized (FOREST) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(FOREST));
        }
        return unmodifiableList;
    }

    public static int treeCount() {
        int size;
        synchronized (FOREST) {
            size = FOREST.size();
        }
        return size;
    }

    private Timber() {
        throw new AssertionError("No instances.");
    }
}
