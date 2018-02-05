package com.alibaba.fastjson.serializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatSerializer implements ObjectSerializer {
    private final String pattern;

    public SimpleDateFormatSerializer(String str) {
        this.pattern = str;
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        if (obj == null) {
            jSONSerializer.getWriter().writeNull();
            return;
        }
        jSONSerializer.write(new SimpleDateFormat(this.pattern).format((Date) obj));
    }
}
