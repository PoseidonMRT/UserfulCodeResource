package com.mqunar.json;

import java.util.List;
import java.util.Map;

class b implements IParser {
    IParser a;

    b() {
        try {
            Class.forName("com.alibaba.fastjson.JSON");
            this.a = new a();
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
                this.a = new Jackson();
            } catch (ClassNotFoundException e2) {
                if (this.a == null) {
                    throw new NullPointerException("No json found");
                }
            }
        }
    }

    public String toJsonString(Object obj) {
        return this.a.toJsonString(obj);
    }

    public Object parseObject(String str, Class<?> cls) {
        return this.a.parseObject(str, cls);
    }

    public List parseArray(String str, Class<?> cls) {
        return this.a.parseArray(str, cls);
    }

    public Map<String, Object> fromJson(String str) {
        return this.a.fromJson(str);
    }

    public Map<String, Object> fromBean(Object obj) {
        return this.a.fromBean(obj);
    }
}
