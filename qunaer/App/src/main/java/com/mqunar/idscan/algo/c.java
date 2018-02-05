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
import java.util.Set;
import org.apache.http.HttpStatus;

public final class c {
    private Set a = new HashSet(HttpStatus.SC_REQUEST_URI_TOO_LONG);

    public c() {
        a();
    }

    private void a() {
        BufferedReader bufferedReader;
        Throwable e;
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(a.a().getResources().openRawResource(R.raw.idscan_pinyin), Charset.forName("utf-8")));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        this.a.add(readLine);
                    } else {
                        QLog.d("CheckName", JSON.toJSONString(this.a), new Object[0]);
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

    public final boolean a(String str) {
        if (str.charAt(0) != 'P' || str.charAt(2) != 'C' || str.charAt(3) != 'H' || str.charAt(4) != 'N') {
            return true;
        }
        int indexOf = str.indexOf("<<", 5);
        String substring = str.substring(indexOf + 2, str.indexOf("<<", indexOf + 2));
        return substring.isEmpty() ? false : b(substring);
    }

    public final boolean b(String str) {
        Set set = this.a;
        boolean[] zArr = new boolean[(str.length() + 1)];
        zArr[0] = true;
        int i = 1;
        while (i < str.length() + 1) {
            int i2 = i - 1;
            while (i2 >= 0) {
                if (zArr[i2] && set.contains(str.substring(i2, i))) {
                    zArr[i] = true;
                    break;
                }
                i2--;
            }
            i++;
        }
        return zArr[str.length()];
    }
}
