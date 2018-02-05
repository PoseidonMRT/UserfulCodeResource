package com.megvii.licensemanager;

import android.content.Context;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.HttpPost;

@Instrumented
public class Manager {
    private static final String AUTHURL = "http://api.faceid.com/faceid/v1/sdk/authm";
    private static List<ILicenseManager> managers = new ArrayList();
    private Context mContext;

    public Manager(Context context) {
        if (context == null) {
            throw new InvalidParameterException("context can not be null");
        }
        this.mContext = context.getApplicationContext();
    }

    public String getContext(String str) {
        if (this.mContext == null || managers.size() == 0) {
            return null;
        }
        this.mContext = this.mContext.getApplicationContext();
        StringBuilder stringBuilder = new StringBuilder();
        for (ILicenseManager context : managers) {
            stringBuilder.append(context.getContext(str));
            stringBuilder.append('$');
        }
        String stringBuilder2 = stringBuilder.toString();
        return stringBuilder2.substring(0, stringBuilder2.length() - 1);
    }

    public Map<String, Long> setLicense(String str) {
        if (str == null || this.mContext == null) {
            return null;
        }
        String[] split = str.split("\\$");
        if (split.length != managers.size()) {
            return null;
        }
        this.mContext = this.mContext.getApplicationContext();
        HashMap hashMap = new HashMap(split.length);
        for (int i = 0; i < managers.size(); i++) {
            hashMap.put(((ILicenseManager) managers.get(i)).getVersion(), Long.valueOf(((ILicenseManager) managers.get(i)).setLicense(split[i])));
        }
        return hashMap;
    }

    public HashMap<String, Long> checkCachedLicense() {
        HashMap<String, Long> hashMap = new HashMap();
        for (ILicenseManager iLicenseManager : managers) {
            hashMap.put(iLicenseManager.getVersion(), Long.valueOf(iLicenseManager.checkCachedLicense()));
        }
        return hashMap;
    }

    private void findAllManager() {
    }

    public synchronized boolean registerLicenseManager(ILicenseManager iLicenseManager) {
        boolean z = true;
        synchronized (this) {
            boolean z2 = false;
            for (ILicenseManager version : managers) {
                boolean z3;
                if (version.getVersion().equals(iLicenseManager.getVersion())) {
                    z3 = true;
                } else {
                    z3 = z2;
                }
                z2 = z3;
            }
            if (!z2) {
                managers.add(iLicenseManager);
            }
            if (z2) {
                z = false;
            }
        }
        return z;
    }

    public synchronized Map<String, Long> takeLicenseFromNetwork(String str) {
        return setLicense(doPost(getContext(str)));
    }

    private static String doPost(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) HttpInstrumentation.openConnection(new URL(AUTHURL).openConnection());
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
            httpURLConnection.setRequestProperty("Content-type", "text/plain");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
            bufferedOutputStream.write(str.getBytes());
            bufferedOutputStream.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    stringBuilder.append(readLine);
                } else {
                    bufferedReader.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
