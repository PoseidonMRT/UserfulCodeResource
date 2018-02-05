package com.alibaba.fastjson.serializer;

import com.mqunar.necro.agent.bean.NecroParam;
import java.lang.reflect.Type;

public class ArraySerializer implements ObjectSerializer {
    private final ObjectSerializer compObjectSerializer;
    private final Class<?> componentType;

    public ArraySerializer(Class<?> cls, ObjectSerializer objectSerializer) {
        this.componentType = cls;
        this.compObjectSerializer = objectSerializer;
    }

    public final void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        int i = 0;
        SerializeWriter writer = jSONSerializer.getWriter();
        if (obj != null) {
            Object[] objArr = (Object[]) obj;
            int length = objArr.length;
            SerialContext context = jSONSerializer.getContext();
            jSONSerializer.setContext(context, obj, obj2, 0);
            try {
                writer.append('[');
                while (i < length) {
                    if (i != 0) {
                        writer.append(',');
                    }
                    Object obj3 = objArr[i];
                    if (obj3 == null) {
                        writer.append((CharSequence) "null");
                    } else if (obj3.getClass() == this.componentType) {
                        this.compObjectSerializer.write(jSONSerializer, obj3, Integer.valueOf(i), null);
                    } else {
                        jSONSerializer.getObjectWriter(obj3.getClass()).write(jSONSerializer, obj3, Integer.valueOf(i), null);
                    }
                    i++;
                }
                writer.append(']');
            } finally {
                jSONSerializer.setContext(context);
            }
        } else if (writer.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            writer.write(NecroParam.NECRO_EMPTY_ARRAY);
        } else {
            writer.writeNull();
        }
    }
}
