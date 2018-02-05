package org.acra.file;

import android.support.annotation.NonNull;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map.Entry;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.util.g;
import org.apache.commons.io.IOUtils;

public final class c {
    @NonNull
    public CrashReportData a(@NonNull File file) {
        Closeable bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 8192);
        try {
            CrashReportData a = a(new InputStreamReader(bufferedInputStream, "ISO8859-1"));
            return a;
        } finally {
            g.a(bufferedInputStream);
        }
    }

    public void a(@NonNull CrashReportData crashReportData, @NonNull File file) {
        Closeable outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "ISO8859_1");
        try {
            StringBuilder stringBuilder = new StringBuilder(200);
            for (Entry entry : crashReportData.entrySet()) {
                a(stringBuilder, ((ReportField) entry.getKey()).toString(), true);
                stringBuilder.append('=');
                a(stringBuilder, (String) entry.getValue(), false);
                stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
                outputStreamWriter.write(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
            outputStreamWriter.flush();
        } finally {
            g.a(outputStreamWriter);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.support.annotation.NonNull
    private synchronized org.acra.collector.CrashReportData a(@android.support.annotation.NonNull java.io.Reader r15) {
        /*
        r14 = this;
        monitor-enter(r14);
        r3 = 0;
        r5 = 0;
        r4 = 0;
        r0 = 40;
        r7 = new char[r0];	 Catch:{ all -> 0x0036 }
        r1 = 0;
        r2 = -1;
        r0 = 1;
        r9 = new org.acra.collector.CrashReportData;	 Catch:{ all -> 0x0036 }
        r9.<init>();	 Catch:{ all -> 0x0036 }
        r10 = new java.io.BufferedReader;	 Catch:{ all -> 0x0036 }
        r6 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r10.<init>(r15, r6);	 Catch:{ all -> 0x0036 }
        r8 = r0;
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
    L_0x001c:
        r0 = r10.read();	 Catch:{ all -> 0x0031 }
        r6 = -1;
        if (r0 != r6) goto L_0x0039;
    L_0x0023:
        r0 = 2;
        if (r5 != r0) goto L_0x015d;
    L_0x0026:
        r0 = 4;
        if (r3 > r0) goto L_0x015d;
    L_0x0029:
        r0 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0031 }
        r1 = "luni.08";
        r0.<init>(r1);	 Catch:{ all -> 0x0031 }
        throw r0;	 Catch:{ all -> 0x0031 }
    L_0x0031:
        r0 = move-exception;
        org.acra.util.g.a(r10);	 Catch:{ all -> 0x0036 }
        throw r0;	 Catch:{ all -> 0x0036 }
    L_0x0036:
        r0 = move-exception;
        monitor-exit(r14);
        throw r0;
    L_0x0039:
        r0 = (char) r0;
        r6 = r7.length;	 Catch:{ all -> 0x0031 }
        if (r1 != r6) goto L_0x0048;
    L_0x003d:
        r6 = r7.length;	 Catch:{ all -> 0x0031 }
        r6 = r6 * 2;
        r6 = new char[r6];	 Catch:{ all -> 0x0031 }
        r11 = 0;
        r12 = 0;
        java.lang.System.arraycopy(r7, r11, r6, r12, r1);	 Catch:{ all -> 0x0031 }
        r7 = r6;
    L_0x0048:
        r6 = 2;
        if (r5 != r6) goto L_0x01ad;
    L_0x004b:
        r6 = 16;
        r6 = java.lang.Character.digit(r0, r6);	 Catch:{ all -> 0x0031 }
        if (r6 < 0) goto L_0x005e;
    L_0x0053:
        r4 = r4 << 4;
        r6 = r6 + r4;
        r4 = r3 + 1;
        r3 = 4;
        if (r4 >= r3) goto L_0x01aa;
    L_0x005b:
        r3 = r4;
        r4 = r6;
        goto L_0x001c;
    L_0x005e:
        r5 = 4;
        if (r3 > r5) goto L_0x0069;
    L_0x0061:
        r0 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0031 }
        r1 = "luni.09";
        r0.<init>(r1);	 Catch:{ all -> 0x0031 }
        throw r0;	 Catch:{ all -> 0x0031 }
    L_0x0069:
        r5 = r4;
        r4 = r3;
    L_0x006b:
        r6 = 0;
        r3 = r1 + 1;
        r11 = (char) r5;	 Catch:{ all -> 0x0031 }
        r7[r1] = r11;	 Catch:{ all -> 0x0031 }
        r1 = 10;
        if (r0 == r1) goto L_0x007e;
    L_0x0075:
        r1 = 133; // 0x85 float:1.86E-43 double:6.57E-322;
        if (r0 == r1) goto L_0x007e;
    L_0x0079:
        r1 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x001c;
    L_0x007e:
        r1 = r3;
        r3 = r6;
    L_0x0080:
        r6 = 1;
        if (r3 != r6) goto L_0x00bd;
    L_0x0083:
        r3 = 0;
        switch(r0) {
            case 10: goto L_0x00a1;
            case 13: goto L_0x009a;
            case 98: goto L_0x00a8;
            case 102: goto L_0x00ab;
            case 110: goto L_0x00ae;
            case 114: goto L_0x00b1;
            case 116: goto L_0x00b4;
            case 117: goto L_0x00b7;
            case 133: goto L_0x00a1;
            default: goto L_0x0087;
        };	 Catch:{ all -> 0x0031 }
    L_0x0087:
        r13 = r0;
        r0 = r3;
        r3 = r13;
    L_0x008a:
        r6 = 0;
        r8 = 4;
        if (r0 != r8) goto L_0x0090;
    L_0x008e:
        r0 = 0;
        r2 = r1;
    L_0x0090:
        r8 = r1 + 1;
        r7[r1] = r3;	 Catch:{ all -> 0x0031 }
        r1 = r8;
        r3 = r4;
        r8 = r6;
        r4 = r5;
        r5 = r0;
        goto L_0x001c;
    L_0x009a:
        r3 = 3;
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x00a1:
        r3 = 5;
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x00a8:
        r0 = 8;
        goto L_0x0087;
    L_0x00ab:
        r0 = 12;
        goto L_0x0087;
    L_0x00ae:
        r0 = 10;
        goto L_0x0087;
    L_0x00b1:
        r0 = 13;
        goto L_0x0087;
    L_0x00b4:
        r0 = 9;
        goto L_0x0087;
    L_0x00b7:
        r3 = 2;
        r4 = 0;
        r5 = r3;
        r3 = r4;
        goto L_0x001c;
    L_0x00bd:
        switch(r0) {
            case 10: goto L_0x00f9;
            case 13: goto L_0x0103;
            case 33: goto L_0x00d7;
            case 35: goto L_0x00d7;
            case 58: goto L_0x013c;
            case 61: goto L_0x013c;
            case 92: goto L_0x0130;
            case 133: goto L_0x0103;
            default: goto L_0x00c0;
        };	 Catch:{ all -> 0x0031 }
    L_0x00c0:
        r6 = java.lang.Character.isWhitespace(r0);	 Catch:{ all -> 0x0031 }
        if (r6 == 0) goto L_0x0151;
    L_0x00c6:
        r6 = 3;
        if (r3 != r6) goto L_0x00ca;
    L_0x00c9:
        r3 = 5;
    L_0x00ca:
        if (r1 == 0) goto L_0x01a2;
    L_0x00cc:
        if (r1 == r2) goto L_0x01a2;
    L_0x00ce:
        r6 = 5;
        if (r3 != r6) goto L_0x0147;
    L_0x00d1:
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x00d7:
        if (r8 == 0) goto L_0x00c0;
    L_0x00d9:
        r0 = r10.read();	 Catch:{ all -> 0x0031 }
        r6 = -1;
        if (r0 != r6) goto L_0x00e6;
    L_0x00e0:
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x00e6:
        r0 = (char) r0;	 Catch:{ all -> 0x0031 }
        r6 = 13;
        if (r0 == r6) goto L_0x01a2;
    L_0x00eb:
        r6 = 10;
        if (r0 == r6) goto L_0x01a2;
    L_0x00ef:
        r6 = 133; // 0x85 float:1.86E-43 double:6.57E-322;
        if (r0 != r6) goto L_0x00d9;
    L_0x00f3:
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x00f9:
        r0 = 3;
        if (r3 != r0) goto L_0x0103;
    L_0x00fc:
        r3 = 5;
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x0103:
        r3 = 0;
        r0 = 1;
        if (r1 > 0) goto L_0x010b;
    L_0x0107:
        if (r1 != 0) goto L_0x0127;
    L_0x0109:
        if (r2 != 0) goto L_0x0127;
    L_0x010b:
        r6 = -1;
        if (r2 != r6) goto L_0x010f;
    L_0x010e:
        r2 = r1;
    L_0x010f:
        r6 = new java.lang.String;	 Catch:{ all -> 0x0031 }
        r8 = 0;
        r6.<init>(r7, r8, r1);	 Catch:{ all -> 0x0031 }
        r1 = org.acra.ReportField.class;
        r8 = 0;
        r8 = r6.substring(r8, r2);	 Catch:{ all -> 0x0031 }
        r1 = java.lang.Enum.valueOf(r1, r8);	 Catch:{ all -> 0x0031 }
        r2 = r6.substring(r2);	 Catch:{ all -> 0x0031 }
        r9.put(r1, r2);	 Catch:{ all -> 0x0031 }
    L_0x0127:
        r2 = -1;
        r1 = 0;
        r8 = r0;
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x0130:
        r0 = 4;
        if (r3 != r0) goto L_0x01a8;
    L_0x0133:
        r0 = r1;
    L_0x0134:
        r3 = 1;
        r2 = r0;
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x013c:
        r6 = -1;
        if (r2 != r6) goto L_0x00c0;
    L_0x013f:
        r3 = 0;
        r2 = r1;
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x0147:
        r6 = -1;
        if (r2 != r6) goto L_0x0151;
    L_0x014a:
        r3 = 4;
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x0151:
        r6 = 5;
        if (r3 == r6) goto L_0x0157;
    L_0x0154:
        r6 = 3;
        if (r3 != r6) goto L_0x019d;
    L_0x0157:
        r3 = 0;
        r13 = r0;
        r0 = r3;
        r3 = r13;
        goto L_0x008a;
    L_0x015d:
        r0 = -1;
        if (r2 != r0) goto L_0x0163;
    L_0x0160:
        if (r1 <= 0) goto L_0x0163;
    L_0x0162:
        r2 = r1;
    L_0x0163:
        if (r2 < 0) goto L_0x0195;
    L_0x0165:
        r3 = new java.lang.String;	 Catch:{ all -> 0x0031 }
        r0 = 0;
        r3.<init>(r7, r0, r1);	 Catch:{ all -> 0x0031 }
        r0 = org.acra.ReportField.class;
        r1 = 0;
        r1 = r3.substring(r1, r2);	 Catch:{ all -> 0x0031 }
        r0 = java.lang.Enum.valueOf(r0, r1);	 Catch:{ all -> 0x0031 }
        r0 = (org.acra.ReportField) r0;	 Catch:{ all -> 0x0031 }
        r1 = r3.substring(r2);	 Catch:{ all -> 0x0031 }
        r2 = 1;
        if (r5 != r2) goto L_0x0192;
    L_0x017f:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0031 }
        r2.<init>();	 Catch:{ all -> 0x0031 }
        r1 = r2.append(r1);	 Catch:{ all -> 0x0031 }
        r2 = "\u0000";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0031 }
        r1 = r1.toString();	 Catch:{ all -> 0x0031 }
    L_0x0192:
        r9.put(r0, r1);	 Catch:{ all -> 0x0031 }
    L_0x0195:
        org.acra.util.g.a(r15);	 Catch:{ all -> 0x0031 }
        org.acra.util.g.a(r10);	 Catch:{ all -> 0x0036 }
        monitor-exit(r14);
        return r9;
    L_0x019d:
        r13 = r0;
        r0 = r3;
        r3 = r13;
        goto L_0x008a;
    L_0x01a2:
        r13 = r4;
        r4 = r5;
        r5 = r3;
        r3 = r13;
        goto L_0x001c;
    L_0x01a8:
        r0 = r2;
        goto L_0x0134;
    L_0x01aa:
        r5 = r6;
        goto L_0x006b;
    L_0x01ad:
        r13 = r3;
        r3 = r5;
        r5 = r4;
        r4 = r13;
        goto L_0x0080;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.acra.file.c.a(java.io.Reader):org.acra.collector.CrashReportData");
    }

    private void a(@NonNull StringBuilder stringBuilder, @NonNull String str, boolean z) {
        int i;
        if (z || 0 >= str.length() || str.charAt(0) != ' ') {
            i = 0;
        } else {
            stringBuilder.append("\\ ");
            i = 1;
        }
        while (i < str.length()) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case '\t':
                    stringBuilder.append("\\t");
                    break;
                case '\n':
                    stringBuilder.append("\\n");
                    break;
                case '\f':
                    stringBuilder.append("\\f");
                    break;
                case '\r':
                    stringBuilder.append("\\r");
                    break;
                default:
                    if ("\\#!=:".indexOf(charAt) >= 0 || (z && charAt == ' ')) {
                        stringBuilder.append(IOUtils.DIR_SEPARATOR_WINDOWS);
                    }
                    if (charAt >= ' ' && charAt <= '~') {
                        stringBuilder.append(charAt);
                        break;
                    }
                    String toHexString = Integer.toHexString(charAt);
                    stringBuilder.append("\\u");
                    for (int i2 = 0; i2 < 4 - toHexString.length(); i2++) {
                        stringBuilder.append('0');
                    }
                    stringBuilder.append(toHexString);
                    break;
                    break;
            }
            i++;
        }
    }
}
