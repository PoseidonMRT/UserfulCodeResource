package com.mqunar.necro.agent;

import android.content.Context;
import android.text.TextUtils;
import com.mqunar.necro.agent.bean.BaseData;
import com.mqunar.necro.agent.bean.NecroParam;
import com.mqunar.necro.agent.bean.NetworkData;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import com.mqunar.necro.agent.task.IHttpSender;
import com.mqunar.necro.agent.util.AndroidUtils;
import com.mqunar.necro.agent.util.NecroFileUtils;
import com.mqunar.necro.agent.util.NecroSpStorage;
import com.mqunar.necro.agent.util.SafeUtils;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NecroUtils {
    private static final int LOG_MAX_COUNT = 20;
    private static final String NECRO_FILE_NAME = "necro";
    private static final String NECRO_NETWORK = "network";
    private static final AgentLog log = AgentLogManager.getAgentLog();
    public static Context mContext;
    private static NecroSpStorage mStorage;
    private static IHttpSender sender;
    private static ExecutorService senderSingleThreadExecutor = Executors.newSingleThreadExecutor();
    private static String tempDir = null;

    public static void init(Context context, IHttpSender iHttpSender) {
        mContext = context.getApplicationContext();
        sender = iHttpSender;
        mStorage = NecroSpStorage.newInstance(context, NECRO_FILE_NAME);
    }

    static void dealData(BaseData baseData) {
        long currentTimeMillis = System.currentTimeMillis();
        if (mStorage.getCount() < 19) {
            saveData(baseData);
        } else {
            popData2File(baseData);
            sendData();
        }
        log.info("dealData time " + (System.currentTimeMillis() - currentTimeMillis));
    }

    private static void popData2File(BaseData baseData) {
        String convertNecroParam2Json = convertNecroParam2Json(getStorageData(baseData, true));
        if (SafeUtils.canEncryption()) {
            convertNecroParam2Json = SafeUtils.ea(convertNecroParam2Json);
        }
        NecroFileUtils.str2File(convertNecroParam2Json, getUploadDir() + "/" + System.currentTimeMillis());
    }

    private static void sendData() {
        if (AndroidUtils.isNetworkConnected(mContext)) {
            senderSingleThreadExecutor.execute(new Runnable() {
                public void run() {
                    String[] list = new File(NecroUtils.getUploadDir()).list(new FilenameFilter() {
                        public boolean accept(File file, String str) {
                            return str.matches("[0-9]+");
                        }
                    });
                    if (list != null && list.length > 0) {
                        Arrays.sort(list, new Comparator<String>() {
                            public int compare(String str, String str2) {
                                return str.compareTo(str2);
                            }
                        });
                        for (String str : list) {
                            NecroUtils.log.info("send necro data : " + str);
                            NecroUtils.sender.send(NecroUtils.mContext, NecroUtils.getUploadDir() + "/" + str);
                        }
                    }
                }
            });
        }
    }

    public static void sendDataNow() {
        popData2File(null);
        sendData();
    }

    private static synchronized NecroParam getStorageData(BaseData baseData, boolean z) {
        NecroParam necroParam = null;
        synchronized (NecroUtils.class) {
            if (mStorage != null) {
                NecroParam necroParam2 = new NecroParam();
                JSONArray jSONArray = new JSONArray();
                List<String> keys = mStorage.getKeys();
                Collections.sort(keys, new Comparator<String>() {
                    public int compare(String str, String str2) {
                        return str.compareTo(str2);
                    }
                });
                try {
                    for (String str : keys) {
                        if (str.contains("network_")) {
                            jSONArray.put(new JSONObject(mStorage.getString(str, null)));
                        }
                    }
                    if (baseData != null) {
                        if (baseData instanceof NetworkData) {
                            jSONArray.put(convertNetworkData2Json((NetworkData) baseData));
                        }
                    }
                } catch (JSONException e) {
                    log.error("get Storage Data failed : " + e);
                }
                necroParam2.network = jSONArray.toString();
                necroParam2.cLogCount = mStorage.getCount();
                if (z) {
                    mStorage.clean();
                }
                necroParam = necroParam2;
            }
        }
        return necroParam;
    }

    private static synchronized void saveData(BaseData baseData) {
        synchronized (NecroUtils.class) {
            if (!(mStorage == null || baseData == null || !(baseData instanceof NetworkData))) {
                JSONObject convertNetworkData2Json = convertNetworkData2Json((NetworkData) baseData);
                log.debug("save data : " + convertNetworkData2Json);
                if (convertNetworkData2Json != null) {
                    mStorage.putString("network_" + System.currentTimeMillis(), convertNetworkData2Json.toString());
                }
            }
        }
    }

    private static String convertNecroParam2Json(NecroParam necroParam) {
        JSONObject jSONObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(necroParam.network)) {
                jSONObject.put(NECRO_NETWORK, new JSONArray(necroParam.network));
            }
            return jSONObject.toString();
        } catch (JSONException e) {
            log.error("convertNecroParam2Json failed : " + e.toString());
            return "";
        }
    }

    private static JSONObject convertNetworkData2Json(NetworkData networkData) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("reqUrl", networkData.reqUrl);
            jSONObject.put("startTime", networkData.startTime);
            jSONObject.put("endTime", networkData.endTime);
            jSONObject.put("connTime", networkData.connTime);
            jSONObject.put("reqSize", networkData.reqSize);
            jSONObject.put("resSize", networkData.resSize);
            jSONObject.put("httpCode", networkData.httpCode);
            jSONObject.put("hf", networkData.hf);
            jSONObject.put("netType", networkData.netType);
            jSONObject.put("loc", networkData.loc);
            jSONObject.put("mno", networkData.mno);
            JSONObject jSONObject2 = new JSONObject();
            if (networkData.headers != null && networkData.headers.size() > 0) {
                for (Entry entry : networkData.headers.entrySet()) {
                    jSONObject2.put((String) entry.getKey(), entry.getValue());
                }
            }
            if (jSONObject2.length() != 0) {
                jSONObject.put("header", jSONObject2);
            }
            return jSONObject;
        } catch (JSONException e) {
            log.error("convertNetworkData2Json failed : " + e.toString());
            return null;
        }
    }

    private static String getUploadDir() {
        if (tempDir == null) {
            try {
                tempDir = mContext.getFilesDir() + "/necro";
            } catch (Exception e) {
                tempDir = "/data/data/" + mContext.getPackageName() + "/files/necro";
            }
            if (!new File(tempDir).exists()) {
                new File(tempDir).mkdirs();
            }
        }
        return tempDir;
    }
}
