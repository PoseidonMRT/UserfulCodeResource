package com.mqunar.idscan.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mqunar.idscan.R;
import com.mqunar.idscan.a;
import com.mqunar.tools.log.QLog;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class b {
    private static b a;
    private JSONArray b;

    private b() {
        c();
    }

    public static b a() {
        if (a == null) {
            a = new b();
        }
        return a;
    }

    private static JSONArray c() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(a.a().getResources().openRawResource(R.raw.idscan_nation), "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    stringBuffer.append(readLine);
                } else {
                    bufferedReader.close();
                    return JSON.parseArray(stringBuffer.toString());
                }
            }
        } catch (Throwable e) {
            QLog.e(e);
            return null;
        }
    }

    public final JSONArray b() {
        return this.b == null ? c() : this.b;
    }
}
