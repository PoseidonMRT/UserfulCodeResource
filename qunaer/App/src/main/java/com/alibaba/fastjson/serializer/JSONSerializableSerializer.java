package com.alibaba.fastjson.serializer;

import java.lang.reflect.Type;

public class JSONSerializableSerializer implements ObjectSerializer {
    public static JSONSerializableSerializer instance = new JSONSerializableSerializer();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        ((JSONSerializable) obj).write(jSONSerializer, obj2, type);
    }
}
