package com.mqunar.hy.res.model;

import com.alibaba.fastjson.JSONArray;
import java.io.Serializable;
import java.util.List;

public class HybridParam implements Serializable {
    private static final long serialVersionUID = 1;
    public HybridInfo current;
    public List<HybridInfo> hlist;

    public HybridParam(List<HybridInfo> list) {
        this.hlist = list;
    }

    public static String toJsonString(List<HybridInfo> list) {
        if (list == null) {
            return null;
        }
        JSONArray jSONArray = new JSONArray();
        for (HybridInfo toJsonObj : list) {
            jSONArray.add(toJsonObj.toJsonObj());
        }
        return jSONArray.toJSONString();
    }

    public boolean isSingleRequest() {
        return this.current != null;
    }
}
