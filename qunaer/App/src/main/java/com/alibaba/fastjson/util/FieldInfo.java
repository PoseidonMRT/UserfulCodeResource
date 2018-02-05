package com.alibaba.fastjson.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class FieldInfo implements Comparable<FieldInfo> {
    private final Class<?> declaringClass;
    private final Field field;
    private final Class<?> fieldClass;
    private final Type fieldType;
    private boolean getOnly;
    private final Method method;
    private final String name;
    private int ordinal;

    public FieldInfo(String str, Class<?> cls, Class<?> cls2, Type type, Field field, int i, int i2) {
        this.ordinal = 0;
        this.getOnly = false;
        this.name = str;
        this.declaringClass = cls;
        this.fieldClass = cls2;
        this.fieldType = type;
        this.method = null;
        this.field = field;
        this.ordinal = i;
        if (field != null) {
            TypeUtils.setAccessible(field);
        }
    }

    public FieldInfo(String str, Method method, Field field) {
        this(str, method, field, null, null);
    }

    public FieldInfo(String str, Method method, Field field, int i, int i2) {
        this(str, method, field, null, null, i, i2);
    }

    public FieldInfo(String str, Method method, Field field, Class<?> cls, Type type) {
        this(str, method, field, (Class) cls, type, 0, 0);
    }

    public FieldInfo(String str, Method method, Field field, Class<?> cls, Type type, int i, int i2) {
        Type type2;
        Class cls2;
        Type type3;
        Class cls3;
        this.ordinal = 0;
        this.getOnly = false;
        this.name = str;
        this.method = method;
        this.field = field;
        this.ordinal = i;
        if (method != null) {
            TypeUtils.setAccessible(method);
        }
        if (field != null) {
            TypeUtils.setAccessible(field);
        }
        if (method != null) {
            Class cls4;
            if (method.getParameterTypes().length == 1) {
                cls4 = method.getParameterTypes()[0];
                type2 = method.getGenericParameterTypes()[0];
            } else {
                cls4 = method.getReturnType();
                type2 = method.getGenericReturnType();
                this.getOnly = true;
            }
            this.declaringClass = method.getDeclaringClass();
            cls2 = cls4;
            type3 = type2;
        } else {
            cls2 = field.getType();
            type3 = field.getGenericType();
            this.declaringClass = field.getDeclaringClass();
        }
        if (cls != null && cls2 == Object.class && (type3 instanceof TypeVariable)) {
            type2 = getInheritGenericType(cls, (TypeVariable) type3);
            if (type2 != null) {
                this.fieldClass = TypeUtils.getClass(type2);
                this.fieldType = type2;
                return;
            }
        }
        Type fieldType = getFieldType(cls, type, type3);
        if (fieldType != type3) {
            if (fieldType instanceof ParameterizedType) {
                cls3 = TypeUtils.getClass(fieldType);
            } else if (fieldType instanceof Class) {
                cls3 = TypeUtils.getClass(fieldType);
            }
            this.fieldType = fieldType;
            this.fieldClass = cls3;
        }
        cls3 = cls2;
        this.fieldType = fieldType;
        this.fieldClass = cls3;
    }

    public static Type getFieldType(Class<?> cls, Type type, Type type2) {
        if (cls == null || type == null) {
            return type2;
        }
        Type fieldType;
        if (type2 instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) type2).getGenericComponentType();
            fieldType = getFieldType(cls, type, genericComponentType);
            if (genericComponentType != fieldType) {
                return Array.newInstance(TypeUtils.getClass(fieldType), 0).getClass();
            }
            return type2;
        } else if (!TypeUtils.isGenericParamType(type)) {
            return type2;
        } else {
            ParameterizedType parameterizedType;
            TypeVariable typeVariable;
            int i;
            if (type2 instanceof TypeVariable) {
                parameterizedType = (ParameterizedType) TypeUtils.getGenericParamType(type);
                Class cls2 = TypeUtils.getClass(parameterizedType);
                typeVariable = (TypeVariable) type2;
                for (i = 0; i < cls2.getTypeParameters().length; i++) {
                    if (cls2.getTypeParameters()[i].getName().equals(typeVariable.getName())) {
                        return parameterizedType.getActualTypeArguments()[i];
                    }
                }
            }
            if (!(type2 instanceof ParameterizedType)) {
                return type2;
            }
            parameterizedType = (ParameterizedType) type2;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            int i2 = 0;
            int i3 = 0;
            while (i2 < actualTypeArguments.length) {
                fieldType = actualTypeArguments[i2];
                if (fieldType instanceof TypeVariable) {
                    typeVariable = (TypeVariable) fieldType;
                    if (type instanceof ParameterizedType) {
                        ParameterizedType parameterizedType2 = (ParameterizedType) type;
                        int i4 = i3;
                        for (i3 = 0; i3 < cls.getTypeParameters().length; i3++) {
                            if (cls.getTypeParameters()[i3].getName().equals(typeVariable.getName())) {
                                actualTypeArguments[i2] = parameterizedType2.getActualTypeArguments()[i3];
                                i4 = 1;
                            }
                        }
                        i = i4;
                        i2++;
                        i3 = i;
                    }
                }
                i = i3;
                i2++;
                i3 = i;
            }
            if (i3 != 0) {
                return new ParameterizedTypeImpl(actualTypeArguments, parameterizedType.getOwnerType(), parameterizedType.getRawType());
            }
            return type2;
        }
    }

    public static Type getInheritGenericType(Class<?> cls, TypeVariable<?> typeVariable) {
        Type genericDeclaration = typeVariable.getGenericDeclaration();
        Type genericSuperclass;
        do {
            genericSuperclass = cls.getGenericSuperclass();
            if (genericSuperclass == null) {
                return null;
            }
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                if (parameterizedType.getRawType() == genericDeclaration) {
                    TypeVariable[] typeParameters = genericDeclaration.getTypeParameters();
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    for (int i = 0; i < typeParameters.length; i++) {
                        if (typeParameters[i] == typeVariable) {
                            return actualTypeArguments[i];
                        }
                    }
                    return null;
                }
            }
            cls = TypeUtils.getClass(genericSuperclass);
        } while (genericSuperclass != null);
        return null;
    }

    public String toString() {
        return this.name;
    }

    public Class<?> getDeclaringClass() {
        return this.declaringClass;
    }

    public Class<?> getFieldClass() {
        return this.fieldClass;
    }

    public Type getFieldType() {
        return this.fieldType;
    }

    public String getName() {
        return this.name;
    }

    public String gerQualifiedName() {
        Member member = getMember();
        return member.getDeclaringClass().getName() + "." + member.getName();
    }

    public Member getMember() {
        if (this.method != null) {
            return this.method;
        }
        return this.field;
    }

    public Method getMethod() {
        return this.method;
    }

    public Field getField() {
        return this.field;
    }

    public int compareTo(FieldInfo fieldInfo) {
        if (this.ordinal < fieldInfo.ordinal) {
            return -1;
        }
        if (this.ordinal > fieldInfo.ordinal) {
            return 1;
        }
        return this.name.compareTo(fieldInfo.name);
    }

    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        T t = null;
        if (this.method != null) {
            t = this.method.getAnnotation(cls);
        }
        if (t != null || this.field == null) {
            return t;
        }
        return this.field.getAnnotation(cls);
    }

    public Object get(Object obj) {
        if (this.method != null) {
            return this.method.invoke(obj, new Object[0]);
        }
        return this.field.get(obj);
    }

    public void set(Object obj, Object obj2) {
        if (this.method != null) {
            this.method.invoke(obj, new Object[]{obj2});
            return;
        }
        this.field.set(obj, obj2);
    }

    public void setAccessible(boolean z) {
        if (this.method != null) {
            TypeUtils.setAccessible(this.method);
        } else {
            TypeUtils.setAccessible(this.field);
        }
    }

    public boolean isGetOnly() {
        return this.getOnly;
    }
}
