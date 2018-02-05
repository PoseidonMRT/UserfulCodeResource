package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.math.BigDecimal;

public class IntegerCodec implements ObjectDeserializer, ObjectSerializer {
    public static IntegerCodec instance = new IntegerCodec();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        SerializeWriter writer = jSONSerializer.getWriter();
        Number number = (Number) obj;
        if (number != null) {
            writer.writeInt(number.intValue());
            if (jSONSerializer.isEnabled(SerializerFeature.WriteClassName)) {
                Class cls = number.getClass();
                if (cls == Byte.class) {
                    writer.write('B');
                } else if (cls == Short.class) {
                    writer.write('S');
                }
            }
        } else if (writer.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
            writer.write('0');
        } else {
            writer.writeNull();
        }
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        JSONLexer lexer = defaultJSONParser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        } else if (lexer.token() == 2) {
            int intValue = lexer.intValue();
            lexer.nextToken(16);
            return Integer.valueOf(intValue);
        } else if (lexer.token() != 3) {
            return TypeUtils.castToInt(defaultJSONParser.parse());
        } else {
            BigDecimal decimalValue = lexer.decimalValue();
            lexer.nextToken(16);
            return Integer.valueOf(decimalValue.intValue());
        }
    }

    public int getFastMatchToken() {
        return 2;
    }
}
