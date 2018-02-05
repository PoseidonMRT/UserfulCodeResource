package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONAware;
import java.lang.reflect.Type;

public class JSONAwareSerializer implements ObjectSerializer {
    public static JSONAwareSerializer instance = new JSONAwareSerializer();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        jSONSerializer.getWriter().write(((JSONAware) obj).toJSONString());
    }
}
