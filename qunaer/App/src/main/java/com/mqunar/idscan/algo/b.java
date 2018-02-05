package com.mqunar.idscan.algo;

import com.alibaba.fastjson.JSON;
import com.mqunar.idscan.R;
import com.mqunar.idscan.a;
import com.mqunar.tools.log.QLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;

public final class b {
    private static HashSet a = new HashSet(249);

    public b() {
        a();
    }

    private static void a() {
        BufferedReader bufferedReader;
        Throwable e;
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(a.a().getResources().openRawResource(R.raw.idscan_country_code), Charset.forName("utf-8")));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        a.add(readLine);
                    } else {
                        QLog.d("CheckCountryCode", JSON.toJSONString(a), new Object[0]);
                        try {
                            bufferedReader.close();
                            return;
                        } catch (Throwable e2) {
                            QLog.e(e2);
                            return;
                        }
                    }
                } catch (IOException e3) {
                    e2 = e3;
                }
            }
        } catch (IOException e4) {
            e2 = e4;
            bufferedReader = null;
            try {
                QLog.e(e2);
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable e22) {
                        QLog.e(e22);
                    }
                }
            } catch (Throwable th) {
                e22 = th;
                bufferedReader2 = bufferedReader;
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (Throwable e5) {
                        QLog.e(e5);
                    }
                }
                throw e22;
            }
        } catch (Throwable th2) {
            e22 = th2;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            throw e22;
        }
    }

    public static boolean a(String str) {
        String substring = str.substring(2, 5);
        if (!a.contains(substring)) {
            return false;
        }
        String substring2 = str.substring(54, 57);
        return a.contains(substring2) ? (substring != "CHN" || substring2 == "CHN") ? substring == "CHN" || substring2 != "CHN" : false : false;
    }
}
