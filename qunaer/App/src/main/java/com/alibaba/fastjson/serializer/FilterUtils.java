package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import java.lang.reflect.Type;
import java.util.List;

public class FilterUtils {
    public static Type getExtratype(DefaultJSONParser defaultJSONParser, Object obj, String str) {
        Type type = null;
        List<ExtraTypeProvider> extraTypeProvidersDirect = defaultJSONParser.getExtraTypeProvidersDirect();
        if (extraTypeProvidersDirect != null) {
            for (ExtraTypeProvider extraType : extraTypeProvidersDirect) {
                type = extraType.getExtraType(obj, str);
            }
        }
        return type;
    }

    public static void processExtra(DefaultJSONParser defaultJSONParser, Object obj, String str, Object obj2) {
        List<ExtraProcessor> extraProcessorsDirect = defaultJSONParser.getExtraProcessorsDirect();
        if (extraProcessorsDirect != null) {
            for (ExtraProcessor processExtra : extraProcessorsDirect) {
                processExtra.processExtra(obj, str, obj2);
            }
        }
    }

    public static char writeBefore(JSONSerializer jSONSerializer, Object obj, char c) {
        List<BeforeFilter> beforeFiltersDirect = jSONSerializer.getBeforeFiltersDirect();
        if (beforeFiltersDirect != null) {
            for (BeforeFilter writeBefore : beforeFiltersDirect) {
                c = writeBefore.writeBefore(jSONSerializer, obj, c);
            }
        }
        return c;
    }

    public static char writeAfter(JSONSerializer jSONSerializer, Object obj, char c) {
        List<AfterFilter> afterFiltersDirect = jSONSerializer.getAfterFiltersDirect();
        if (afterFiltersDirect != null) {
            for (AfterFilter writeAfter : afterFiltersDirect) {
                c = writeAfter.writeAfter(jSONSerializer, obj, c);
            }
        }
        return c;
    }

    public static Object processValue(JSONSerializer jSONSerializer, Object obj, String str, Object obj2) {
        List<ValueFilter> valueFiltersDirect = jSONSerializer.getValueFiltersDirect();
        if (valueFiltersDirect != null) {
            for (ValueFilter process : valueFiltersDirect) {
                obj2 = process.process(obj, str, obj2);
            }
        }
        return obj2;
    }

    public static String processKey(JSONSerializer jSONSerializer, Object obj, String str, Object obj2) {
        List<NameFilter> nameFiltersDirect = jSONSerializer.getNameFiltersDirect();
        if (nameFiltersDirect != null) {
            for (NameFilter process : nameFiltersDirect) {
                str = process.process(obj, str, obj2);
            }
        }
        return str;
    }

    public static boolean applyName(JSONSerializer jSONSerializer, Object obj, String str) {
        List<PropertyPreFilter> propertyPreFiltersDirect = jSONSerializer.getPropertyPreFiltersDirect();
        if (propertyPreFiltersDirect == null) {
            return true;
        }
        for (PropertyPreFilter apply : propertyPreFiltersDirect) {
            if (!apply.apply(jSONSerializer, obj, str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean apply(JSONSerializer jSONSerializer, Object obj, String str, Object obj2) {
        List<PropertyFilter> propertyFiltersDirect = jSONSerializer.getPropertyFiltersDirect();
        if (propertyFiltersDirect == null) {
            return true;
        }
        for (PropertyFilter apply : propertyFiltersDirect) {
            if (!apply.apply(obj, str, obj2)) {
                return false;
            }
        }
        return true;
    }
}
