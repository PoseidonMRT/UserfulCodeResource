package com.mqunar.cock.utils;

public interface IJsonProcessor {
    <T> T deserialize(byte[] bArr, Class<T> cls);

    byte[] serialize(Object obj);
}
