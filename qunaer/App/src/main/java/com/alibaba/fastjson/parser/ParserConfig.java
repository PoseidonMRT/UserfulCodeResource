package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.ArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.BooleanFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.CharArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.ClassDerializer;
import com.alibaba.fastjson.parser.deserializer.CollectionDeserializer;
import com.alibaba.fastjson.parser.deserializer.DateDeserializer;
import com.alibaba.fastjson.parser.deserializer.DateFormatDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.DoubleFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FloatFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.IntegerFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JSONArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.JSONObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.LongFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;
import com.alibaba.fastjson.parser.deserializer.StackTraceElementDeserializer;
import com.alibaba.fastjson.parser.deserializer.StringFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimeDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimestampDeserializer;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.alibaba.fastjson.serializer.BigIntegerCodec;
import com.alibaba.fastjson.serializer.BooleanCodec;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.CharacterCodec;
import com.alibaba.fastjson.serializer.CharsetCodec;
import com.alibaba.fastjson.serializer.CurrencyCodec;
import com.alibaba.fastjson.serializer.FloatCodec;
import com.alibaba.fastjson.serializer.InetAddressCodec;
import com.alibaba.fastjson.serializer.InetSocketAddressCodec;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LocaleCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.PatternCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.serializer.TimeZoneCodec;
import com.alibaba.fastjson.serializer.URICodec;
import com.alibaba.fastjson.serializer.URLCodec;
import com.alibaba.fastjson.serializer.UUIDCodec;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IdentityHashMap;
import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class ParserConfig {
    private static ParserConfig global = new ParserConfig();
    private final IdentityHashMap<Type, ObjectDeserializer> derializers = new IdentityHashMap();
    private final Set<Class<?>> primitiveClasses = new HashSet();
    protected final SymbolTable symbolTable = new SymbolTable();

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    public ParserConfig() {
        this.primitiveClasses.add(Boolean.TYPE);
        this.primitiveClasses.add(Boolean.class);
        this.primitiveClasses.add(Character.TYPE);
        this.primitiveClasses.add(Character.class);
        this.primitiveClasses.add(Byte.TYPE);
        this.primitiveClasses.add(Byte.class);
        this.primitiveClasses.add(Short.TYPE);
        this.primitiveClasses.add(Short.class);
        this.primitiveClasses.add(Integer.TYPE);
        this.primitiveClasses.add(Integer.class);
        this.primitiveClasses.add(Long.TYPE);
        this.primitiveClasses.add(Long.class);
        this.primitiveClasses.add(Float.TYPE);
        this.primitiveClasses.add(Float.class);
        this.primitiveClasses.add(Double.TYPE);
        this.primitiveClasses.add(Double.class);
        this.primitiveClasses.add(BigInteger.class);
        this.primitiveClasses.add(BigDecimal.class);
        this.primitiveClasses.add(String.class);
        this.primitiveClasses.add(Date.class);
        this.primitiveClasses.add(java.sql.Date.class);
        this.primitiveClasses.add(Time.class);
        this.primitiveClasses.add(Timestamp.class);
        this.derializers.put(SimpleDateFormat.class, DateFormatDeserializer.instance);
        this.derializers.put(Timestamp.class, TimestampDeserializer.instance);
        this.derializers.put(java.sql.Date.class, SqlDateDeserializer.instance);
        this.derializers.put(Time.class, TimeDeserializer.instance);
        this.derializers.put(Date.class, DateDeserializer.instance);
        this.derializers.put(Calendar.class, CalendarCodec.instance);
        this.derializers.put(JSONObject.class, JSONObjectDeserializer.instance);
        this.derializers.put(JSONArray.class, JSONArrayDeserializer.instance);
        this.derializers.put(Map.class, MapDeserializer.instance);
        this.derializers.put(HashMap.class, MapDeserializer.instance);
        this.derializers.put(LinkedHashMap.class, MapDeserializer.instance);
        this.derializers.put(TreeMap.class, MapDeserializer.instance);
        this.derializers.put(ConcurrentMap.class, MapDeserializer.instance);
        this.derializers.put(ConcurrentHashMap.class, MapDeserializer.instance);
        this.derializers.put(Collection.class, CollectionDeserializer.instance);
        this.derializers.put(List.class, CollectionDeserializer.instance);
        this.derializers.put(ArrayList.class, CollectionDeserializer.instance);
        this.derializers.put(Object.class, JavaObjectDeserializer.instance);
        this.derializers.put(String.class, StringCodec.instance);
        this.derializers.put(Character.TYPE, CharacterCodec.instance);
        this.derializers.put(Character.class, CharacterCodec.instance);
        this.derializers.put(Byte.TYPE, NumberDeserializer.instance);
        this.derializers.put(Byte.class, NumberDeserializer.instance);
        this.derializers.put(Short.TYPE, NumberDeserializer.instance);
        this.derializers.put(Short.class, NumberDeserializer.instance);
        this.derializers.put(Integer.TYPE, IntegerCodec.instance);
        this.derializers.put(Integer.class, IntegerCodec.instance);
        this.derializers.put(Long.TYPE, LongCodec.instance);
        this.derializers.put(Long.class, LongCodec.instance);
        this.derializers.put(BigInteger.class, BigIntegerCodec.instance);
        this.derializers.put(BigDecimal.class, BigDecimalCodec.instance);
        this.derializers.put(Float.TYPE, FloatCodec.instance);
        this.derializers.put(Float.class, FloatCodec.instance);
        this.derializers.put(Double.TYPE, NumberDeserializer.instance);
        this.derializers.put(Double.class, NumberDeserializer.instance);
        this.derializers.put(Boolean.TYPE, BooleanCodec.instance);
        this.derializers.put(Boolean.class, BooleanCodec.instance);
        this.derializers.put(Class.class, ClassDerializer.instance);
        this.derializers.put(char[].class, CharArrayDeserializer.instance);
        this.derializers.put(UUID.class, UUIDCodec.instance);
        this.derializers.put(TimeZone.class, TimeZoneCodec.instance);
        this.derializers.put(Locale.class, LocaleCodec.instance);
        this.derializers.put(Currency.class, CurrencyCodec.instance);
        this.derializers.put(InetAddress.class, InetAddressCodec.instance);
        this.derializers.put(Inet4Address.class, InetAddressCodec.instance);
        this.derializers.put(Inet6Address.class, InetAddressCodec.instance);
        this.derializers.put(InetSocketAddress.class, InetSocketAddressCodec.instance);
        this.derializers.put(URI.class, URICodec.instance);
        this.derializers.put(URL.class, URLCodec.instance);
        this.derializers.put(Pattern.class, PatternCodec.instance);
        this.derializers.put(Charset.class, CharsetCodec.instance);
        this.derializers.put(Number.class, NumberDeserializer.instance);
        this.derializers.put(StackTraceElement.class, StackTraceElementDeserializer.instance);
        this.derializers.put(Serializable.class, JavaObjectDeserializer.instance);
        this.derializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        this.derializers.put(Comparable.class, JavaObjectDeserializer.instance);
        this.derializers.put(Closeable.class, JavaObjectDeserializer.instance);
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
        return this.derializers;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer objectDeserializer = (ObjectDeserializer) this.derializers.get(type);
        if (objectDeserializer != null) {
            return objectDeserializer;
        }
        if (type instanceof Class) {
            return getDeserializer((Class) type, type);
        }
        if (!(type instanceof ParameterizedType)) {
            return JavaObjectDeserializer.instance;
        }
        Type rawType = ((ParameterizedType) type).getRawType();
        if (rawType instanceof Class) {
            return getDeserializer((Class) rawType, type);
        }
        return getDeserializer(rawType);
    }

    public ObjectDeserializer getDeserializer(Class<?> cls, Type type) {
        ObjectDeserializer objectDeserializer = (ObjectDeserializer) this.derializers.get(type);
        if (objectDeserializer != null) {
            return objectDeserializer;
        }
        if (type == null) {
            type = cls;
        }
        objectDeserializer = (ObjectDeserializer) this.derializers.get(type);
        if (objectDeserializer != null) {
            return objectDeserializer;
        }
        JSONType jSONType = (JSONType) cls.getAnnotation(JSONType.class);
        if (jSONType != null) {
            Type mappingTo = jSONType.mappingTo();
            if (mappingTo != Void.class) {
                return getDeserializer(mappingTo, mappingTo);
            }
        }
        if ((type instanceof WildcardType) || (type instanceof TypeVariable) || (type instanceof ParameterizedType)) {
            objectDeserializer = (ObjectDeserializer) this.derializers.get(cls);
        }
        if (objectDeserializer != null) {
            return objectDeserializer;
        }
        objectDeserializer = (ObjectDeserializer) this.derializers.get(type);
        if (objectDeserializer != null) {
            return objectDeserializer;
        }
        if (cls.isEnum()) {
            objectDeserializer = new EnumDeserializer(cls);
        } else if (cls.isArray()) {
            objectDeserializer = ArrayDeserializer.instance;
        } else if (cls == Set.class || cls == HashSet.class || cls == Collection.class || cls == List.class || cls == ArrayList.class) {
            objectDeserializer = CollectionDeserializer.instance;
        } else if (Collection.class.isAssignableFrom(cls)) {
            objectDeserializer = CollectionDeserializer.instance;
        } else if (Map.class.isAssignableFrom(cls)) {
            objectDeserializer = MapDeserializer.instance;
        } else if (Throwable.class.isAssignableFrom(cls)) {
            objectDeserializer = new ThrowableDeserializer(this, cls);
        } else {
            objectDeserializer = createJavaBeanDeserializer(cls, type);
        }
        putDeserializer(type, objectDeserializer);
        return objectDeserializer;
    }

    public ObjectDeserializer createJavaBeanDeserializer(Class<?> cls, Type type) {
        return new JavaBeanDeserializer(this, cls, type);
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig parserConfig, Class<?> cls, FieldInfo fieldInfo) {
        Class fieldClass = fieldInfo.getFieldClass();
        if (fieldClass == Boolean.TYPE || fieldClass == Boolean.class) {
            return new BooleanFieldDeserializer(parserConfig, cls, fieldInfo);
        }
        if (fieldClass == Integer.TYPE || fieldClass == Integer.class) {
            return new IntegerFieldDeserializer(parserConfig, cls, fieldInfo);
        }
        if (fieldClass == Long.TYPE || fieldClass == Long.class) {
            return new LongFieldDeserializer(parserConfig, cls, fieldInfo);
        }
        if (fieldClass == String.class) {
            return new StringFieldDeserializer(parserConfig, cls, fieldInfo);
        }
        if (fieldClass == List.class || fieldClass == ArrayList.class) {
            return new ArrayListTypeFieldDeserializer(parserConfig, cls, fieldInfo);
        }
        if (fieldClass == Float.TYPE || fieldClass == Float.class) {
            return new FloatFieldDeserializer(parserConfig, cls, fieldInfo);
        }
        if (fieldClass == Double.TYPE || fieldClass == Double.class) {
            return new DoubleFieldDeserializer(parserConfig, cls, fieldInfo);
        }
        return new DefaultFieldDeserializer(parserConfig, cls, fieldInfo);
    }

    public void putDeserializer(Type type, ObjectDeserializer objectDeserializer) {
        this.derializers.put(type, objectDeserializer);
    }

    public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
        return getDeserializer(fieldInfo.getFieldClass(), fieldInfo.getFieldType());
    }

    public boolean isPrimitive(Class<?> cls) {
        return this.primitiveClasses.contains(cls);
    }

    public static Field getField(Class<?> cls, String str) {
        Field field0 = getField0(cls, str);
        if (field0 == null) {
            field0 = getField0(cls, "_" + str);
        }
        if (field0 == null) {
            return getField0(cls, "m_" + str);
        }
        return field0;
    }

    private static Field getField0(Class<?> cls, String str) {
        for (Field field : cls.getDeclaredFields()) {
            if (str.equals(field.getName())) {
                return field;
            }
        }
        if (cls.getSuperclass() == null || cls.getSuperclass() == Object.class) {
            return null;
        }
        return getField(cls.getSuperclass(), str);
    }

    public Map<String, FieldDeserializer> getFieldDeserializers(Class<?> cls) {
        ObjectDeserializer deserializer = getDeserializer((Type) cls);
        if (deserializer instanceof JavaBeanDeserializer) {
            return ((JavaBeanDeserializer) deserializer).getFieldDeserializerMap();
        }
        return Collections.emptyMap();
    }
}
