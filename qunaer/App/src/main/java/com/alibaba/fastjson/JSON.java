package com.alibaba.fastjson;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.mqunar.tools.DateTimeUtils;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class JSON implements JSONAware, JSONStreamAware {
    public static int DEFAULT_GENERATE_FEATURE = ((((SerializerFeature.QuoteFieldNames.getMask() | 0) | SerializerFeature.SkipTransientField.getMask()) | SerializerFeature.WriteEnumUsingToString.getMask()) | SerializerFeature.SortField.getMask());
    public static int DEFAULT_PARSER_FEATURE = ((((((((Feature.AutoCloseSource.getMask() | 0) | Feature.InternFieldNames.getMask()) | Feature.UseBigDecimal.getMask()) | Feature.AllowUnQuotedFieldNames.getMask()) | Feature.AllowSingleQuotes.getMask()) | Feature.AllowArbitraryCommas.getMask()) | Feature.SortFeidFastMatch.getMask()) | Feature.IgnoreNotMatch.getMask());
    public static String DEFAULT_TYPE_KEY = "@type";
    public static String DEFFAULT_DATE_FORMAT = DateTimeUtils.yyyy_MM_dd_HH_mm_ss;
    public static String DUMP_CLASS = null;
    public static final String VERSION = "1.1.45";

    public static final Object parse(String str) {
        return parse(str, DEFAULT_PARSER_FEATURE);
    }

    public static final Object parse(String str, int i) {
        if (str == null) {
            return null;
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, ParserConfig.getGlobalInstance(), i);
        Object parse = defaultJSONParser.parse();
        defaultJSONParser.handleResovleTask(parse);
        defaultJSONParser.close();
        return parse;
    }

    public static final Object parse(byte[] bArr, Feature... featureArr) {
        try {
            return parseObject(new String(bArr, "UTF-8"), featureArr);
        } catch (Throwable e) {
            throw new JSONException("parseObject error", e);
        }
    }

    public static final Object parse(String str, Feature... featureArr) {
        int i = DEFAULT_PARSER_FEATURE;
        for (Feature config : featureArr) {
            i = Feature.config(i, config, true);
        }
        return parse(str, i);
    }

    public static final JSONObject parseObject(String str, Feature... featureArr) {
        return (JSONObject) parse(str, featureArr);
    }

    public static final JSONObject parseObject(String str) {
        Object parse = parse(str);
        if (parse instanceof JSONObject) {
            return (JSONObject) parse;
        }
        return (JSONObject) toJSON(parse);
    }

    public static final <T> T parseObject(String str, TypeReference<T> typeReference, Feature... featureArr) {
        return parseObject(str, typeReference.getType(), ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static final <T> T parseObject(String str, Class<T> cls, Feature... featureArr) {
        return parseObject(str, cls, ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static final <T> T parseObject(String str, Class<T> cls, ParseProcess parseProcess, Feature... featureArr) {
        return parseObject(str, cls, ParserConfig.getGlobalInstance(), parseProcess, DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static final <T> T parseObject(String str, Type type, Feature... featureArr) {
        return parseObject(str, type, ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static final <T> T parseObject(String str, Type type, ParseProcess parseProcess, Feature... featureArr) {
        return parseObject(str, type, ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static final <T> T parseObject(String str, Type type, int i, Feature... featureArr) {
        if (str == null) {
            return null;
        }
        for (Feature config : featureArr) {
            i = Feature.config(i, config, true);
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, ParserConfig.getGlobalInstance(), i);
        T parseObject = defaultJSONParser.parseObject(type);
        defaultJSONParser.handleResovleTask(parseObject);
        defaultJSONParser.close();
        return parseObject;
    }

    public static final <T> T parseObject(String str, Type type, ParserConfig parserConfig, int i, Feature... featureArr) {
        return parseObject(str, type, parserConfig, null, i, featureArr);
    }

    public static final <T> T parseObject(String str, Type type, ParserConfig parserConfig, ParseProcess parseProcess, int i, Feature... featureArr) {
        if (str == null) {
            return null;
        }
        for (Feature config : featureArr) {
            i = Feature.config(i, config, true);
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, parserConfig, i);
        if (parseProcess instanceof ExtraTypeProvider) {
            defaultJSONParser.getExtraTypeProviders().add((ExtraTypeProvider) parseProcess);
        }
        if (parseProcess instanceof ExtraProcessor) {
            defaultJSONParser.getExtraProcessors().add((ExtraProcessor) parseProcess);
        }
        T parseObject = defaultJSONParser.parseObject(type);
        defaultJSONParser.handleResovleTask(parseObject);
        defaultJSONParser.close();
        return parseObject;
    }

    public static final <T> T parseObject(byte[] bArr, Type type, Feature... featureArr) {
        try {
            return parseObject(new String(bArr, "UTF-8"), type, featureArr);
        } catch (Throwable e) {
            throw new JSONException("parseObject error", e);
        }
    }

    public static final <T> T parseObject(char[] cArr, int i, Type type, Feature... featureArr) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        int i2 = DEFAULT_PARSER_FEATURE;
        for (Feature config : featureArr) {
            i2 = Feature.config(i2, config, true);
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(cArr, i, ParserConfig.getGlobalInstance(), i2);
        T parseObject = defaultJSONParser.parseObject(type);
        defaultJSONParser.handleResovleTask(parseObject);
        defaultJSONParser.close();
        return parseObject;
    }

    public static final <T> T parseObject(String str, Class<T> cls) {
        return parseObject(str, (Class) cls, new Feature[0]);
    }

    public static final JSONArray parseArray(String str) {
        JSONArray jSONArray = null;
        if (str != null) {
            DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, ParserConfig.getGlobalInstance());
            JSONLexer lexer = defaultJSONParser.getLexer();
            if (lexer.token() == 8) {
                lexer.nextToken();
            } else if (lexer.token() != 20) {
                Collection jSONArray2 = new JSONArray();
                defaultJSONParser.parseArray(jSONArray2);
                defaultJSONParser.handleResovleTask(jSONArray2);
            }
            defaultJSONParser.close();
        }
        return jSONArray;
    }

    public static final <T> List<T> parseArray(String str, Class<T> cls) {
        List<T> list = null;
        if (str != null) {
            DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, ParserConfig.getGlobalInstance());
            JSONLexer lexer = defaultJSONParser.getLexer();
            if (lexer.token() == 8) {
                lexer.nextToken();
            } else {
                Collection arrayList = new ArrayList();
                defaultJSONParser.parseArray((Class) cls, arrayList);
                defaultJSONParser.handleResovleTask(arrayList);
            }
            defaultJSONParser.close();
        }
        return list;
    }

    public static final List<Object> parseArray(String str, Type[] typeArr) {
        List<Object> list = null;
        if (str != null) {
            DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, ParserConfig.getGlobalInstance());
            Object[] parseArray = defaultJSONParser.parseArray(typeArr);
            if (parseArray != null) {
                list = Arrays.asList(parseArray);
            }
            defaultJSONParser.handleResovleTask(list);
            defaultJSONParser.close();
        }
        return list;
    }

    public static final String toJSONString(Object obj) {
        return toJSONString(obj, new SerializerFeature[0]);
    }

    public static final String toJSONString(Object obj, SerializerFeature... serializerFeatureArr) {
        String serializeWriter;
        SerializeWriter serializeWriter2 = new SerializeWriter();
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter2);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            jSONSerializer.write(obj);
            serializeWriter = serializeWriter2.toString();
        } catch (Exception e) {
            serializeWriter = null;
        } finally {
            serializeWriter2.close();
        }
        return serializeWriter;
    }

    public static final String toJSONStringWithDateFormat(Object obj, String str, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            jSONSerializer.config(SerializerFeature.WriteDateUseDateFormat, true);
            if (str != null) {
                jSONSerializer.setDateFormat(str);
            }
            jSONSerializer.write(obj);
            String serializeWriter2 = serializeWriter.toString();
            return serializeWriter2;
        } finally {
            serializeWriter.close();
        }
    }

    public static final String toJSONString(Object obj, SerializeFilter serializeFilter, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            jSONSerializer.config(SerializerFeature.WriteDateUseDateFormat, true);
            setFilter(jSONSerializer, serializeFilter);
            jSONSerializer.write(obj);
            String serializeWriter2 = serializeWriter.toString();
            return serializeWriter2;
        } finally {
            serializeWriter.close();
        }
    }

    public static final String toJSONString(Object obj, SerializeFilter[] serializeFilterArr, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            jSONSerializer.config(SerializerFeature.WriteDateUseDateFormat, true);
            setFilter(jSONSerializer, serializeFilterArr);
            jSONSerializer.write(obj);
            String serializeWriter2 = serializeWriter.toString();
            return serializeWriter2;
        } finally {
            serializeWriter.close();
        }
    }

    public static final byte[] toJSONBytes(Object obj, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            jSONSerializer.write(obj);
            byte[] toBytes = serializeWriter.toBytes("UTF-8");
            return toBytes;
        } finally {
            serializeWriter.close();
        }
    }

    public static final String toJSONString(Object obj, SerializeConfig serializeConfig, SerializerFeature... serializerFeatureArr) {
        return toJSONString(obj, serializeConfig, (SerializeFilter) null, serializerFeatureArr);
    }

    public static final String toJSONString(Object obj, SerializeConfig serializeConfig, SerializeFilter serializeFilter, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter, serializeConfig);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            setFilter(jSONSerializer, serializeFilter);
            jSONSerializer.write(obj);
            String serializeWriter2 = serializeWriter.toString();
            return serializeWriter2;
        } finally {
            serializeWriter.close();
        }
    }

    public static final String toJSONString(Object obj, SerializeConfig serializeConfig, SerializeFilter[] serializeFilterArr, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter, serializeConfig);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            setFilter(jSONSerializer, serializeFilterArr);
            jSONSerializer.write(obj);
            String serializeWriter2 = serializeWriter.toString();
            return serializeWriter2;
        } finally {
            serializeWriter.close();
        }
    }

    public static final String toJSONStringZ(Object obj, SerializeConfig serializeConfig, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter(serializerFeatureArr);
        try {
            new JSONSerializer(serializeWriter, serializeConfig).write(obj);
            String serializeWriter2 = serializeWriter.toString();
            return serializeWriter2;
        } finally {
            serializeWriter.close();
        }
    }

    public static final byte[] toJSONBytes(Object obj, SerializeConfig serializeConfig, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter, serializeConfig);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            jSONSerializer.write(obj);
            byte[] toBytes = serializeWriter.toBytes("UTF-8");
            return toBytes;
        } finally {
            serializeWriter.close();
        }
    }

    public static final String toJSONString(Object obj, boolean z) {
        if (!z) {
            return toJSONString(obj);
        }
        return toJSONString(obj, SerializerFeature.PrettyFormat);
    }

    public static final void writeJSONStringTo(Object obj, Writer writer, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter(writer);
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            jSONSerializer.write(obj);
        } finally {
            serializeWriter.close();
        }
    }

    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            new JSONSerializer(serializeWriter).write((Object) this);
            String serializeWriter2 = serializeWriter.toString();
            return serializeWriter2;
        } finally {
            serializeWriter.close();
        }
    }

    public void writeJSONString(Appendable appendable) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            new JSONSerializer(serializeWriter).write((Object) this);
            appendable.append(serializeWriter.toString());
            serializeWriter.close();
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        } catch (Throwable th) {
            serializeWriter.close();
        }
    }

    public static final Object toJSON(Object obj) {
        return toJSON(obj, ParserConfig.getGlobalInstance());
    }

    public static final Object toJSON(Object obj, ParserConfig parserConfig) {
        int i = 0;
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSON) {
            return (JSON) obj;
        }
        JSONObject jSONObject;
        if (obj instanceof Map) {
            Map map = (Map) obj;
            jSONObject = new JSONObject(map.size());
            for (Entry entry : map.entrySet()) {
                jSONObject.put(TypeUtils.castToString(entry.getKey()), toJSON(entry.getValue()));
            }
            return jSONObject;
        } else if (obj instanceof Collection) {
            Collection<Object> collection = (Collection) obj;
            r0 = new JSONArray(collection.size());
            for (Object toJSON : collection) {
                r0.add(toJSON(toJSON));
            }
            return r0;
        } else {
            Class cls = obj.getClass();
            if (cls.isEnum()) {
                return ((Enum) obj).name();
            }
            if (cls.isArray()) {
                int length = Array.getLength(obj);
                r0 = new JSONArray(length);
                while (i < length) {
                    r0.add(toJSON(Array.get(obj, i)));
                    i++;
                }
                return r0;
            } else if (parserConfig.isPrimitive(cls)) {
                return obj;
            } else {
                try {
                    List<FieldInfo> computeGetters = TypeUtils.computeGetters(cls, null);
                    JSONType jSONType = (JSONType) cls.getAnnotation(JSONType.class);
                    int size = computeGetters.size();
                    boolean z = (jSONType == null || jSONType.orders() == null) ? false : true;
                    jSONObject = new JSONObject(size, z);
                    for (FieldInfo fieldInfo : computeGetters) {
                        jSONObject.put(fieldInfo.getName(), toJSON(fieldInfo.get(obj)));
                    }
                    return jSONObject;
                } catch (Throwable e) {
                    throw new JSONException("toJSON error", e);
                } catch (Throwable e2) {
                    throw new JSONException("toJSON error", e2);
                }
            }
        }
    }

    public static final <T> T toJavaObject(JSON json, Class<T> cls) {
        return TypeUtils.cast((Object) json, (Class) cls, ParserConfig.getGlobalInstance());
    }

    private static void setFilter(JSONSerializer jSONSerializer, SerializeFilter... serializeFilterArr) {
        for (SerializeFilter filter : serializeFilterArr) {
            setFilter(jSONSerializer, filter);
        }
    }

    private static void setFilter(JSONSerializer jSONSerializer, SerializeFilter serializeFilter) {
        if (serializeFilter != null) {
            if (serializeFilter instanceof PropertyPreFilter) {
                jSONSerializer.getPropertyPreFilters().add((PropertyPreFilter) serializeFilter);
            }
            if (serializeFilter instanceof NameFilter) {
                jSONSerializer.getNameFilters().add((NameFilter) serializeFilter);
            }
            if (serializeFilter instanceof ValueFilter) {
                jSONSerializer.getValueFilters().add((ValueFilter) serializeFilter);
            }
            if (serializeFilter instanceof PropertyFilter) {
                jSONSerializer.getPropertyFilters().add((PropertyFilter) serializeFilter);
            }
            if (serializeFilter instanceof BeforeFilter) {
                jSONSerializer.getBeforeFilters().add((BeforeFilter) serializeFilter);
            }
            if (serializeFilter instanceof AfterFilter) {
                jSONSerializer.getAfterFilters().add((AfterFilter) serializeFilter);
            }
        }
    }
}
