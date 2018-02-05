package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONStreamAware;
import java.lang.reflect.Type;

public class JSONStreamAwareSerializer implements ObjectSerializer {
    public static JSONStreamAwareSerializer instance = new JSONStreamAwareSerializer();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        ((JSONStreamAware) obj).writeJSONString(jSONSerializer.getWriter());
    }
}
