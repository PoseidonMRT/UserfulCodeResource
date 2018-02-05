package com.alibaba.fastjson.serializer;

import com.mqunar.necro.agent.bean.NecroParam;
import java.lang.reflect.Type;

public class DoubleArraySerializer implements ObjectSerializer {
    public static final DoubleArraySerializer instance = new DoubleArraySerializer();

    public final void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        SerializeWriter writer = jSONSerializer.getWriter();
        if (obj != null) {
            double[] dArr = (double[]) obj;
            int length = dArr.length - 1;
            if (length == -1) {
                writer.append(NecroParam.NECRO_EMPTY_ARRAY);
                return;
            }
            writer.append('[');
            for (int i = 0; i < length; i++) {
                double d = dArr[i];
                if (Double.isNaN(d)) {
                    writer.writeNull();
                } else {
                    writer.append(Double.toString(d));
                }
                writer.append(',');
            }
            double d2 = dArr[length];
            if (Double.isNaN(d2)) {
                writer.writeNull();
            } else {
                writer.append(Double.toString(d2));
            }
            writer.append(']');
        } else if (writer.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            writer.write(NecroParam.NECRO_EMPTY_ARRAY);
        } else {
            writer.writeNull();
        }
    }
}
