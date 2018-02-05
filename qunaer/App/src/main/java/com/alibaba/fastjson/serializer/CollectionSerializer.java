package com.alibaba.fastjson.serializer;

import com.mqunar.necro.agent.bean.NecroParam;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class CollectionSerializer implements ObjectSerializer {
    public static final CollectionSerializer instance = new CollectionSerializer();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        int i = 0;
        SerializeWriter writer = jSONSerializer.getWriter();
        if (obj != null) {
            Type type2;
            if (jSONSerializer.isEnabled(SerializerFeature.WriteClassName) && (type instanceof ParameterizedType)) {
                type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                type2 = null;
            }
            Collection collection = (Collection) obj;
            SerialContext context = jSONSerializer.getContext();
            jSONSerializer.setContext(context, obj, obj2, 0);
            if (jSONSerializer.isEnabled(SerializerFeature.WriteClassName)) {
                if (HashSet.class == collection.getClass()) {
                    writer.append((CharSequence) "Set");
                } else if (TreeSet.class == collection.getClass()) {
                    writer.append((CharSequence) "TreeSet");
                }
            }
            try {
                writer.append('[');
                for (Object next : collection) {
                    int i2 = i + 1;
                    if (i != 0) {
                        writer.append(',');
                    }
                    if (next == null) {
                        writer.writeNull();
                        i = i2;
                    } else {
                        Class cls = next.getClass();
                        if (cls == Integer.class) {
                            writer.writeInt(((Integer) next).intValue());
                            i = i2;
                        } else if (cls == Long.class) {
                            writer.writeLong(((Long) next).longValue());
                            if (writer.isEnabled(SerializerFeature.WriteClassName)) {
                                writer.write('L');
                                i = i2;
                            } else {
                                i = i2;
                            }
                        } else {
                            jSONSerializer.getObjectWriter(cls).write(jSONSerializer, next, Integer.valueOf(i2 - 1), type2);
                            i = i2;
                        }
                    }
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
