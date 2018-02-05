package com.alibaba.fastjson.serializer;

import com.mqunar.necro.agent.bean.NecroParam;
import java.lang.reflect.Type;

public class CharArraySerializer implements ObjectSerializer {
    public static CharArraySerializer instance = new CharArraySerializer();

    public final void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        SerializeWriter writer = jSONSerializer.getWriter();
        if (obj != null) {
            writer.writeString(new String((char[]) obj));
        } else if (writer.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            writer.write(NecroParam.NECRO_EMPTY_ARRAY);
        } else {
            writer.writeNull();
        }
    }
}
