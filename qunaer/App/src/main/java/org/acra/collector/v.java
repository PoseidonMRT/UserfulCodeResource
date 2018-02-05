package org.acra.collector;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.acra.ACRA;
import org.acra.anr.ANRException;
import org.acra.util.g;
import org.apache.commons.io.IOUtils;

final class v {
    @NonNull
    static String a(@NonNull Context context, @NonNull ANRException aNRException) {
        Exception e;
        Throwable th;
        File file = new File(aNRException.tracesFilePath);
        Pattern compile = Pattern.compile("-{5}\\spid\\s\\d+\\sat\\s\\d+-\\d+-\\d+\\s\\d{2}:\\d{2}:\\d{2}\\s-{5}");
        Pattern compile2 = Pattern.compile("-{5}\\send\\s\\d+\\s-{5}");
        Pattern compile3 = Pattern.compile("Cmd\\sline:\\s(\\S+)");
        Pattern compile4 = Pattern.compile("\"main\"\\sprio=\\d+\\stid=\\d+\\s.*");
        Pattern compile5 = Pattern.compile("\".+\"\\s(daemon\\s)?prio=\\d+\\stid=\\d+\\s.*");
        if (file.exists()) {
            Closeable bufferedReader;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                Object obj = null;
                Object obj2 = null;
                Object obj3 = null;
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    StringBuilder stringBuilder2 = new StringBuilder();
                    while (true) {
                        CharSequence readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        if (obj == null && compile.matcher(readLine).matches()) {
                            stringBuilder.append(readLine).append(IOUtils.LINE_SEPARATOR_UNIX);
                            readLine = bufferedReader.readLine();
                            Matcher matcher = compile3.matcher(readLine);
                            if (!matcher.matches() || !context.getPackageName().equals(matcher.group(1))) {
                                break;
                            }
                            obj = 1;
                        }
                        if (obj != null) {
                            stringBuilder.append(readLine).append(IOUtils.LINE_SEPARATOR_UNIX);
                            if (obj3 == null) {
                                if (obj2 == null && compile4.matcher(readLine).matches()) {
                                    obj2 = 1;
                                    stringBuilder2.append("ANRException").append(IOUtils.LINE_SEPARATOR_UNIX);
                                }
                                if (obj2 != null) {
                                    if (!compile5.matcher(readLine).matches() || compile4.matcher(readLine).matches()) {
                                        stringBuilder2.append(readLine).append(IOUtils.LINE_SEPARATOR_UNIX);
                                    } else {
                                        aNRException.mainThreadInfo = stringBuilder2.toString();
                                        obj3 = 1;
                                    }
                                }
                            }
                            if (compile2.matcher(readLine).matches()) {
                                break;
                            }
                        }
                    }
                    String stringBuilder3 = stringBuilder.toString();
                    g.a(bufferedReader);
                    return stringBuilder3;
                } catch (Exception e2) {
                    e = e2;
                }
            } catch (Exception e3) {
                e = e3;
                bufferedReader = null;
                try {
                    ACRA.f.e(ACRA.e, "get traces file failed " + e.toString());
                    g.a(bufferedReader);
                    return "";
                } catch (Throwable th2) {
                    th = th2;
                    g.a(bufferedReader);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = null;
                g.a(bufferedReader);
                throw th;
            }
        }
        return "";
    }
}
