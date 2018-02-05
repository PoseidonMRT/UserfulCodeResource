package com.mqunar.hy.res.utils;

import android.content.res.AssetManager;
import com.mqunar.hy.res.logger.Timber;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AssetUtils {
    public static String getAssetFileToStr(AssetManager assetManager, String str) {
        BufferedReader bufferedReader;
        String readLine;
        Throwable e;
        Throwable th;
        String str2 = "";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(str), "UTF-8"));
            while (true) {
                try {
                    readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    str2 = str2 + readLine;
                } catch (Throwable e2) {
                    Throwable th2 = e2;
                    readLine = str2;
                    th = th2;
                }
            }
            readLine = str2.trim();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e3) {
                }
            }
        } catch (Throwable e22) {
            bufferedReader = null;
            String str3 = str2;
            th = e22;
            readLine = str3;
            try {
                Timber.e(th, "打开asset出错", new Object[0]);
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e4) {
                    }
                }
                return readLine;
            } catch (Throwable th3) {
                e22 = th3;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e5) {
                    }
                }
                throw e22;
            }
        } catch (Throwable th4) {
            e22 = th4;
            bufferedReader = null;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw e22;
        }
        return readLine;
    }
}
