package com.mqunar.hy.res.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.cookie.ClientCookie;

public class HybridManifest {
    public static final String FILES_KEY = "files";
    private JSONObject extra;
    private List<ResItem> files;
    private String hybridid;
    private JSONObject manifestJson = null;
    private int version;

    public class ResItem {
        private String md5;
        private String sl;
        private String url;

        public ResItem(JSONObject jSONObject) {
            if (jSONObject.containsKey("url")) {
                this.url = jSONObject.getString("url");
            }
            if (jSONObject.containsKey("sl")) {
                this.sl = jSONObject.getString("sl");
            }
            if (jSONObject.containsKey("md5")) {
                this.md5 = jSONObject.getString("md5");
            }
        }

        public String getUrl() {
            return this.url;
        }

        public String getSl() {
            return this.sl;
        }

        public String getMd5() {
            return this.md5;
        }
    }

    public HybridManifest(String str) {
        JSONObject parseObject = JSON.parseObject(str);
        this.manifestJson = parseObject;
        if (parseObject.containsKey("hybridid")) {
            this.hybridid = parseObject.getString("hybridid");
        }
        if (parseObject.containsKey(ClientCookie.VERSION_ATTR)) {
            this.version = parseObject.getIntValue(ClientCookie.VERSION_ATTR);
        }
        if (parseObject.containsKey("extra")) {
            this.extra = parseObject.getJSONObject("extra");
        }
        if (parseObject.containsKey(FILES_KEY)) {
            this.files = new ArrayList();
            JSONArray jSONArray = parseObject.getJSONArray(FILES_KEY);
            for (int i = 0; i < jSONArray.size(); i++) {
                this.files.add(new ResItem(jSONArray.getJSONObject(i)));
            }
        }
    }

    public List<ResItem> getFiles() {
        return this.files;
    }

    public String getHybridid() {
        return this.hybridid;
    }

    public int getVersion() {
        return this.version;
    }

    public JSONObject getExtra() {
        return this.extra;
    }

    public JSONObject getManifestJson() {
        return this.manifestJson;
    }
}
