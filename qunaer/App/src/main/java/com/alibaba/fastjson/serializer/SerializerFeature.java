package com.alibaba.fastjson.serializer;

public enum SerializerFeature {
    QuoteFieldNames,
    UseSingleQuotes,
    WriteMapNullValue,
    WriteEnumUsingToString,
    UseISO8601DateFormat,
    WriteNullListAsEmpty,
    WriteNullStringAsEmpty,
    WriteNullNumberAsZero,
    WriteNullBooleanAsFalse,
    SkipTransientField,
    SortField,
    WriteTabAsSpecial,
    PrettyFormat,
    WriteClassName,
    DisableCircularReferenceDetect,
    WriteSlashAsSpecial,
    BrowserCompatible,
    WriteDateUseDateFormat,
    NotWriteRootClassName,
    DisableCheckSpecialChar,
    BeanToArray,
    WriteNonStringKeyAsString,
    NotWriteDefaultValue;
    
    private final int mask;

    public final int getMask() {
        return this.mask;
    }

    public static boolean isEnabled(int i, SerializerFeature serializerFeature) {
        return (serializerFeature.getMask() & i) != 0;
    }

    public static int config(int i, SerializerFeature serializerFeature, boolean z) {
        if (z) {
            return serializerFeature.getMask() | i;
        }
        return (serializerFeature.getMask() ^ -1) & i;
    }

    public static int of(SerializerFeature[] serializerFeatureArr) {
        int i = 0;
        if (serializerFeatureArr != null) {
            int i2 = 0;
            while (i2 < serializerFeatureArr.length) {
                int mask = serializerFeatureArr[i2].getMask() | i;
                i2++;
                i = mask;
            }
        }
        return i;
    }
}
