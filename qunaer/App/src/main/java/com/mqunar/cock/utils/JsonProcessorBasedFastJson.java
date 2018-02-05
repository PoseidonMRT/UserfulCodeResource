package com.mqunar.cock.utils;

import com.alibaba.fastjson.JSON;
import com.mqunar.tools.log.QLog;
import java.io.UnsupportedEncodingException;

public class JsonProcessorBasedFastJson implements IJsonProcessor {
    public byte[] serialize(Object obj) {
        String toJSONString = JSON.toJSONString(obj);
        QLog.v("fRequest:", "" + toJSONString, new Object[0]);
        try {
            return toJSONString.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unsupported encoding...utf-8");
        }
    }

    public <T> T deserialize(byte[] bArr, Class<T> cls) {
        if (bArr == null) {
            try {
                return cls.newInstance();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("unsupported encoding...utf-8");
            } catch (Throwable e2) {
                throw new RuntimeException(e2);
            } catch (Throwable e22) {
                throw new RuntimeException(e22);
            }
        }
        String str = new String(bArr, "utf-8");
        QLog.v("fResponse", "" + str, new Object[0]);
        return JSON.parseObject(str, (Class) cls);
    }

    public static <T> T deserializeStatic(byte[] bArr, Class<T> cls) {
        if (bArr == null) {
            try {
                return cls.newInstance();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("unsupported encoding...utf-8");
            } catch (Throwable e2) {
                throw new RuntimeException(e2);
            } catch (Throwable e22) {
                throw new RuntimeException(e22);
            }
        }
        String str = new String(bArr, "utf-8");
        QLog.v("fResponse", "" + str, new Object[0]);
        return JSON.parseObject(str, (Class) cls);
    }
}
