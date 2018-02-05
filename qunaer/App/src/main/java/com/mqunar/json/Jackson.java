package com.mqunar.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Jackson implements IParser {
    private ObjectMapper a = new ObjectMapper();

    public Jackson() {
        this.a.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.a.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    }

    public Object parseObject(String str, Class<?> cls) {
        try {
            return this.a.readValue(str, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List parseArray(String str, Class<?> cls) {
        throw new UnsupportedOperationException("do it later");
    }

    public String toJsonString(Object obj) {
        try {
            return this.a.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> fromJson(String str) {
        try {
            return (Map) this.a.readValue(str, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> fromBean(Object obj) {
        return fromJson(toJsonString(obj));
    }
}
