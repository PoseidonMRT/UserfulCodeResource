package com.mqunar.dispatcher;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mqunar.core.Component;
import com.mqunar.core.QunarApkLoader;
import com.mqunar.libtask.ProgressType;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.cookie.ClientCookie;

public class SchemeManager {
    private static Map<String, QScheme> a = new HashMap();
    private static Map<String, List<IntentFilter>> b;

    public static void addScheme(String str, int i, List<Component> list) {
        synchronized (a) {
            QScheme qScheme = (QScheme) a.get(str);
            if (qScheme == null || qScheme.a <= i) {
                qScheme = new QScheme();
                qScheme.a = i;
                qScheme.b = list;
                a.put(str, qScheme);
            }
        }
    }

    static List<String> a(Context context, Intent intent, int i) {
        List<String> arrayList = new ArrayList(2);
        synchronized (a) {
            for (QScheme qScheme : a.values()) {
                if (!(qScheme == null || qScheme.b == null || qScheme.b.isEmpty())) {
                    for (Component component : qScheme.b) {
                        for (IntentFilter matchData : component.intentFilters) {
                            if (matchData.matchData(intent.resolveType(context), intent.getScheme(), intent.getData()) >= 0) {
                                arrayList.add(component.name);
                                break;
                            }
                        }
                        if (arrayList.size() > i) {
                            break;
                        }
                    }
                    if (arrayList.size() > i) {
                        break;
                    }
                }
            }
        }
        return arrayList;
    }

    public static String getMappingComponent(Context context, Intent intent) {
        List<String> a = a(context, intent, ProgressType.PRO_END);
        if (a.isEmpty()) {
            return null;
        }
        if (a.size() == 1) {
            return (String) a.get(0);
        }
        StringBuffer stringBuffer = new StringBuffer("[");
        for (String append : a) {
            stringBuffer.append(append).append(",");
        }
        stringBuffer.append("]");
        throw new RuntimeException("scheme : " + intent.getDataString() + " 匹配到了多个组件! " + stringBuffer.toString());
    }

    public static Map<String, List<IntentFilter>> getMappingInAsset() {
        if (b == null) {
            b = new HashMap();
            try {
                InputStream open = QunarApkLoader.getAppContext().getAssets().open("host.json");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = open.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                JSONObject parseObject = JSON.parseObject(new String(byteArrayOutputStream.toByteArray(), "UTF-8"));
                for (String str : parseObject.keySet()) {
                    JSONArray jSONArray = parseObject.getJSONArray(str);
                    List arrayList = new ArrayList(jSONArray.size());
                    for (int i = 0; i < jSONArray.size(); i++) {
                        IntentFilter intentFilter = new IntentFilter();
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        if (jSONObject.containsKey("host")) {
                            intentFilter.addAction(jSONObject.getString("host"));
                        }
                        if (jSONObject.containsKey("scheme")) {
                            intentFilter.addDataScheme(jSONObject.getString("scheme"));
                            if (jSONObject.containsKey("mimeType")) {
                                intentFilter.addDataType(jSONObject.getString("mimeType"));
                            }
                            if (jSONObject.containsKey("host")) {
                                intentFilter.addDataAuthority(jSONObject.getString("host"), jSONObject.getString(ClientCookie.PORT_ATTR));
                            }
                            if (jSONObject.containsKey("path")) {
                                intentFilter.addDataPath(jSONObject.getString("path"), 0);
                            }
                            if (jSONObject.containsKey("pathPrefix")) {
                                intentFilter.addDataPath(jSONObject.getString("pathPrefix"), 1);
                            }
                            if (jSONObject.containsKey("pathPattern")) {
                                intentFilter.addDataPath(jSONObject.getString("pathPattern"), 2);
                            }
                        }
                        arrayList.add(intentFilter);
                    }
                    b.put(str, arrayList);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return b;
    }

    public static String mapping(Context context, Intent intent) {
        getMappingInAsset();
        Set hashSet = new HashSet();
        for (String str : b.keySet()) {
            for (IntentFilter matchData : (List) b.get(str)) {
                if (matchData.matchData(intent.resolveType(context), intent.getScheme(), intent.getData()) >= 0) {
                    hashSet.add(str);
                    if (hashSet.size() > 1) {
                        throw new RuntimeException("scheme : " + intent.getDataString() + " 匹配到了多个组件! " + hashSet);
                    }
                }
            }
        }
        if (hashSet.isEmpty()) {
            return null;
        }
        return (String) hashSet.iterator().next();
    }
}
