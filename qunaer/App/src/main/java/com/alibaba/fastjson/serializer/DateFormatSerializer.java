package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

public class DateFormatSerializer implements ObjectSerializer {
    public static final DateFormatSerializer instance = new DateFormatSerializer();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        SerializeWriter writer = jSONSerializer.getWriter();
        if (obj == null) {
            writer.writeNull();
            return;
        }
        String toPattern = ((SimpleDateFormat) obj).toPattern();
        if (!writer.isEnabled(SerializerFeature.WriteClassName) || obj.getClass() == type) {
            writer.writeString(toPattern);
            return;
        }
        writer.write('{');
        writer.writeFieldName(JSON.DEFAULT_TYPE_KEY);
        jSONSerializer.write(obj.getClass().getName());
        writer.writeFieldValue(',', "val", toPattern);
        writer.write('}');
    }
}
