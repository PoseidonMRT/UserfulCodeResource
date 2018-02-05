package com.alibaba.fastjson.serializer;

import com.mqunar.necro.agent.bean.NecroParam;
import java.lang.reflect.Type;

public class FloatArraySerializer implements ObjectSerializer {
    public static final FloatArraySerializer instance = new FloatArraySerializer();

    public final void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        SerializeWriter writer = jSONSerializer.getWriter();
        if (obj != null) {
            float[] fArr = (float[]) obj;
            int length = fArr.length - 1;
            if (length == -1) {
                writer.append(NecroParam.NECRO_EMPTY_ARRAY);
                return;
            }
            writer.append('[');
            for (int i = 0; i < length; i++) {
                float f = fArr[i];
                if (Float.isNaN(f)) {
                    writer.writeNull();
                } else {
                    writer.append(Float.toString(f));
                }
                writer.append(',');
            }
            float f2 = fArr[length];
            if (Float.isNaN(f2)) {
                writer.writeNull();
            } else {
                writer.append(Float.toString(f2));
            }
            writer.append(']');
        } else if (writer.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            writer.write(NecroParam.NECRO_EMPTY_ARRAY);
        } else {
            writer.writeNull();
        }
    }
}
