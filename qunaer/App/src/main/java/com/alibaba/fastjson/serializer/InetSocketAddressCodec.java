package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.apache.http.cookie.ClientCookie;

public class InetSocketAddressCodec implements ObjectDeserializer, ObjectSerializer {
    public static InetSocketAddressCodec instance = new InetSocketAddressCodec();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        if (obj == null) {
            jSONSerializer.writeNull();
            return;
        }
        SerializeWriter writer = jSONSerializer.getWriter();
        InetSocketAddress inetSocketAddress = (InetSocketAddress) obj;
        Object address = inetSocketAddress.getAddress();
        writer.write('{');
        if (address != null) {
            writer.writeFieldName("address");
            jSONSerializer.write(address);
            writer.write(',');
        }
        writer.writeFieldName(ClientCookie.PORT_ATTR);
        writer.writeInt(inetSocketAddress.getPort());
        writer.write('}');
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        T t = null;
        JSONLexer lexer = defaultJSONParser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken();
            return null;
        }
        defaultJSONParser.accept(12);
        int i = 0;
        while (true) {
            T t2;
            int i2;
            String stringVal = lexer.stringVal();
            lexer.nextToken(17);
            if (stringVal.equals("address")) {
                defaultJSONParser.accept(17);
                int i3 = i;
                t2 = (InetAddress) defaultJSONParser.parseObject(InetAddress.class);
                i2 = i3;
            } else if (stringVal.equals(ClientCookie.PORT_ATTR)) {
                defaultJSONParser.accept(17);
                if (lexer.token() != 2) {
                    throw new JSONException("port is not int");
                }
                i2 = lexer.intValue();
                lexer.nextToken();
                t2 = t;
            } else {
                defaultJSONParser.accept(17);
                defaultJSONParser.parse();
                i2 = i;
                t2 = t;
            }
            if (lexer.token() == 16) {
                lexer.nextToken();
                t = t2;
                i = i2;
            } else {
                defaultJSONParser.accept(13);
                return new InetSocketAddress(t2, i2);
            }
        }
    }

    public int getFastMatchToken() {
        return 12;
    }
}
