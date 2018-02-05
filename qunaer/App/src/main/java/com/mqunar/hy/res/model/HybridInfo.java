package com.mqunar.hy.res.model;

import com.alibaba.fastjson.JSONObject;
import com.mqunar.hy.res.utils.HybridInfoParser;
import com.mqunar.hy.res.utils.RsaDecodeUtil;
import com.mqunar.hy.res.utils.UriCodec;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.cookie.ClientCookie;

public class HybridInfo implements Serializable {
    private static final long serialVersionUID = 1;
    public int QpRequestType;
    public boolean checked = false;
    public boolean errorChanged = false;
    public JSONObject extra;
    public boolean force = false;
    public String hybridId;
    public long length;
    private JSONObject manifestJson = null;
    private String md5;
    public String patchUrl;
    public String path;
    private Map<String, HybridFile> resource = new HashMap();
    public String url;
    public int version;

    public JSONObject getExtra() {
        return this.extra;
    }

    public Map<String, HybridFile> getResource() {
        return null;
    }

    public void setResource(Map<String, HybridFile> map) {
        if (map != null) {
            this.resource = map;
        }
    }

    public Map<String, HybridFile> getActualResource() {
        return this.resource;
    }

    public HybridFile getHybridFileByUrl(String str) {
        if (this.resource != null) {
            HybridFile hybridFile = (HybridFile) this.resource.get(UriCodec.getUrlWithOutQueryAndHash(str));
            if (hybridFile != null && HybridInfoParser.checkQPFile(this)) {
                return hybridFile;
            }
        }
        return null;
    }

    public String getMd5() {
        return this.md5;
    }

    public String getEncodedMd5() {
        return this.md5;
    }

    public String getDecodeMd5() {
        return RsaDecodeUtil.decode(this.md5);
    }

    public void setMd5(String str) {
        this.md5 = str;
    }

    public boolean isForce() {
        return this.force;
    }

    public String toJsonString() {
        return toJsonObj().toString();
    }

    public JSONObject toJsonObj() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("hybridId", this.hybridId);
        jSONObject.put(ClientCookie.VERSION_ATTR, Integer.valueOf(this.version));
        jSONObject.put("md5", getDecodeMd5());
        jSONObject.put("length", Long.valueOf(this.length));
        jSONObject.put("force", Boolean.valueOf(this.force));
        return jSONObject;
    }

    public JSONObject getManifestJsonExclusiveFiles() {
        if (this.manifestJson == null) {
            return new JSONObject();
        }
        JSONObject jSONObject = (JSONObject) this.manifestJson.clone();
        jSONObject.remove(HybridManifest.FILES_KEY);
        return jSONObject;
    }

    public JSONObject getManifestJson() {
        return this.manifestJson;
    }

    public void setManifestJson(JSONObject jSONObject) {
        this.manifestJson = jSONObject;
    }
}
