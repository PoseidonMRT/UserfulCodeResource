package com.mqunar.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.util.List;
import java.util.Map;

class a implements IParser {
    a() {
    }

    public Object parseObject(String str, Class<?> cls) {
        return JSON.parseObject(str, (Class) cls);
    }

    public List parseArray(String str, Class<?> cls) {
        return JSON.parseArray(str, (Class) cls);
    }

    public String toJsonString(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteTabAsSpecial);
    }

    public Map<String, Object> fromJson(String str) {
        return (Map) JSON.parse(str);
    }

    public Map<String, Object> fromBean(Object obj) {
        return (Map) JSON.toJSON(obj);
    }
}
