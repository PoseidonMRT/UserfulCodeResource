package com.mqunar.storage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IStorage {
    boolean cleanAllStorage();

    boolean contains(String str);

    Map<String, Object> getAll();

    boolean getBoolean(String str, boolean z);

    byte[] getBytes(String str, byte[] bArr);

    double getDouble(String str, double d);

    float getFloat(String str, float f);

    int getInt(String str, int i);

    List<String> getKeys();

    long getLong(String str, long j);

    <T extends Serializable> T getSerializable(String str, Class<T> cls, T t);

    short getShort(String str, short s);

    String getString(String str, String str2);

    boolean putBoolean(String str, boolean z);

    boolean putBytes(String str, byte[] bArr);

    boolean putDouble(String str, double d);

    boolean putFloat(String str, float f);

    boolean putInt(String str, int i);

    boolean putLong(String str, long j);

    boolean putSerializable(String str, Serializable serializable);

    boolean putShort(String str, short s);

    boolean putSmoothBoolean(String str, boolean z);

    boolean putSmoothBytes(String str, byte[] bArr);

    boolean putSmoothDouble(String str, double d);

    boolean putSmoothFloat(String str, float f);

    boolean putSmoothInt(String str, int i);

    boolean putSmoothLong(String str, long j);

    boolean putSmoothSerializable(String str, Serializable serializable);

    boolean putSmoothShort(String str, short s);

    boolean putSmoothString(String str, String str2);

    boolean putString(String str, String str2);

    boolean remove(String str);
}
