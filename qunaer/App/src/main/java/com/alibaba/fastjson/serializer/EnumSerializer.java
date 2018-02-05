package com.alibaba.fastjson.serializer;

import java.lang.reflect.Type;

public class EnumSerializer implements ObjectSerializer {
    public static final EnumSerializer instance = new EnumSerializer();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        SerializeWriter writer = jSONSerializer.getWriter();
        if (obj == null) {
            jSONSerializer.getWriter().writeNull();
        } else if (jSONSerializer.isEnabled(SerializerFeature.WriteEnumUsingToString)) {
            jSONSerializer.write(((Enum) obj).toString());
        } else {
            writer.writeInt(((Enum) obj).ordinal());
        }
    }
}
