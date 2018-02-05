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

public final class e {
    public final String a = "CheckSurname";
    private HashSet b = new HashSet(361);
    private c c;

    public e(c cVar) {
        this.c = cVar;
        a();
    }

    private void a() {
        BufferedReader bufferedReader;
        Throwable e;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(a.a().getResources().openRawResource(R.raw.idscan_surname), Charset.forName("utf-8")));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        char[] toCharArray = readLine.toCharArray();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (char c : toCharArray) {
                            Object obj = (c < '\u0000' || c > 'ÿ') ? null : 1;
                            if (obj != null) {
                                stringBuilder.append(c);
                            }
                        }
                        this.b.add(stringBuilder.toString());
                    } else {
                        QLog.d("CheckSurname", JSON.toJSONString(this.b), new Object[0]);
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
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable e5) {
                        QLog.e(e5);
                    }
                }
                throw e22;
            }
        } catch (Throwable th2) {
            e22 = th2;
            bufferedReader = null;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw e22;
        }
    }

    public final boolean a(String str) {
        if (str.charAt(0) == 'P' && str.charAt(2) == 'C' && str.charAt(3) == 'H' && str.charAt(4) == 'N') {
            String substring = str.substring(5, str.indexOf("<<", 5));
            if (substring.contains("AAA")) {
                return false;
            }
            if (!this.b.contains(substring)) {
                return substring.length() > 5 ? this.c.b(substring) : false;
            }
        }
        return true;
    }
}
