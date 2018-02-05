package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import com.mqunar.necro.agent.bean.NecroParam;
import java.util.Collection;

public class ObjectFieldSerializer extends FieldSerializer {
    private String format;
    private RuntimeSerializerInfo runtimeInfo;
    boolean writeEnumUsingToString = false;
    boolean writeNullBooleanAsFalse = false;
    boolean writeNullListAsEmpty = false;
    boolean writeNullStringAsEmpty = false;
    boolean writeNumberAsZero = false;

    class RuntimeSerializerInfo {
        ObjectSerializer fieldSerializer;
        Class<?> runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer objectSerializer, Class<?> cls) {
            this.fieldSerializer = objectSerializer;
            this.runtimeFieldClass = cls;
        }
    }

    public ObjectFieldSerializer(FieldInfo fieldInfo) {
        super(fieldInfo);
        JSONField jSONField = (JSONField) fieldInfo.getAnnotation(JSONField.class);
        if (jSONField != null) {
            this.format = jSONField.format();
            if (this.format.trim().length() == 0) {
                this.format = null;
            }
            for (SerializerFeature serializerFeature : jSONField.serialzeFeatures()) {
                if (serializerFeature == SerializerFeature.WriteNullNumberAsZero) {
                    this.writeNumberAsZero = true;
                } else if (serializerFeature == SerializerFeature.WriteNullStringAsEmpty) {
                    this.writeNullStringAsEmpty = true;
                } else if (serializerFeature == SerializerFeature.WriteNullBooleanAsFalse) {
                    this.writeNullBooleanAsFalse = true;
                } else if (serializerFeature == SerializerFeature.WriteNullListAsEmpty) {
                    this.writeNullListAsEmpty = true;
                } else if (serializerFeature == SerializerFeature.WriteEnumUsingToString) {
                    this.writeEnumUsingToString = true;
                }
            }
        }
    }

    public void writeProperty(JSONSerializer jSONSerializer, Object obj) {
        writePrefix(jSONSerializer);
        writeValue(jSONSerializer, obj);
    }

    public void writeValue(JSONSerializer jSONSerializer, Object obj) {
        if (this.format != null) {
            jSONSerializer.writeWithFormat(obj, this.format);
            return;
        }
        if (this.runtimeInfo == null) {
            Class fieldClass;
            if (obj == null) {
                fieldClass = this.fieldInfo.getFieldClass();
            } else {
                fieldClass = obj.getClass();
            }
            this.runtimeInfo = new RuntimeSerializerInfo(jSONSerializer.getObjectWriter(fieldClass), fieldClass);
        }
        RuntimeSerializerInfo runtimeSerializerInfo = this.runtimeInfo;
        if (obj == null) {
            if (this.writeNumberAsZero && Number.class.isAssignableFrom(runtimeSerializerInfo.runtimeFieldClass)) {
                jSONSerializer.getWriter().write('0');
            } else if (this.writeNullStringAsEmpty && String.class == runtimeSerializerInfo.runtimeFieldClass) {
                jSONSerializer.getWriter().write("\"\"");
            } else if (this.writeNullBooleanAsFalse && Boolean.class == runtimeSerializerInfo.runtimeFieldClass) {
                jSONSerializer.getWriter().write("false");
            } else if (this.writeNullListAsEmpty && Collection.class.isAssignableFrom(runtimeSerializerInfo.runtimeFieldClass)) {
                jSONSerializer.getWriter().write(NecroParam.NECRO_EMPTY_ARRAY);
            } else {
                runtimeSerializerInfo.fieldSerializer.write(jSONSerializer, null, this.fieldInfo.getName(), null);
            }
        } else if (this.writeEnumUsingToString && runtimeSerializerInfo.runtimeFieldClass.isEnum()) {
            jSONSerializer.getWriter().writeString(((Enum) obj).name());
        } else {
            Class cls = obj.getClass();
            if (cls == runtimeSerializerInfo.runtimeFieldClass) {
                runtimeSerializerInfo.fieldSerializer.write(jSONSerializer, obj, this.fieldInfo.getName(), this.fieldInfo.getFieldType());
            } else {
                jSONSerializer.getObjectWriter(cls).write(jSONSerializer, obj, this.fieldInfo.getName(), this.fieldInfo.getFieldType());
            }
        }
    }
}
