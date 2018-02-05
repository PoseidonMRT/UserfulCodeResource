package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;

public class BooleanCodec implements ObjectDeserializer, ObjectSerializer {
    public static final BooleanCodec instance = new BooleanCodec();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        SerializeWriter writer = jSONSerializer.getWriter();
        Boolean bool = (Boolean) obj;
        if (bool == null) {
            if (writer.isEnabled(SerializerFeature.WriteNullBooleanAsFalse)) {
                writer.write("false");
            } else {
                writer.writeNull();
            }
        } else if (bool.booleanValue()) {
            writer.write("true");
        } else {
            writer.write("false");
        }
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        JSONLexer lexer = defaultJSONParser.getLexer();
        if (lexer.token() == 6) {
            lexer.nextToken(16);
            return Boolean.TRUE;
        } else if (lexer.token() == 7) {
            lexer.nextToken(16);
            return Boolean.FALSE;
        } else if (lexer.token() == 2) {
            int intValue = lexer.intValue();
            lexer.nextToken(16);
            if (intValue == 1) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } else {
            Object parse = defaultJSONParser.parse();
            if (parse == null) {
                return null;
            }
            return TypeUtils.castToBoolean(parse);
        }
    }

    public int getFastMatchToken() {
        return 6;
    }
}
