package com.alibaba.fastjson.serializer;

import com.mqunar.necro.agent.bean.NecroParam;
import java.lang.reflect.Type;

public class LongArraySerializer implements ObjectSerializer {
    public static LongArraySerializer instance = new LongArraySerializer();

    public final void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        SerializeWriter writer = jSONSerializer.getWriter();
        if (obj != null) {
            long[] jArr = (long[]) obj;
            writer.write('[');
            for (int i = 0; i < jArr.length; i++) {
                if (i != 0) {
                    writer.write(',');
                }
                writer.writeLong(jArr[i]);
            }
            writer.write(']');
        } else if (writer.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            writer.write(NecroParam.NECRO_EMPTY_ARRAY);
        } else {
            writer.writeNull();
        }
    }
}
