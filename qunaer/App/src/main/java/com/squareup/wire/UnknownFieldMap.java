package com.squareup.wire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

final class UnknownFieldMap {
    Map<Integer, List<FieldValue>> fieldMap;

    abstract class FieldValue {
        private final int tag;
        private final WireType wireType;

        public abstract int getSerializedSize();

        public abstract void write(int i, WireOutput wireOutput);

        public FieldValue(int i, WireType wireType) {
            this.tag = i;
            this.wireType = wireType;
        }

        public static VarintFieldValue varint(int i, Long l) {
            return new VarintFieldValue(i, l);
        }

        public static Fixed32FieldValue fixed32(int i, Integer num) {
            return new Fixed32FieldValue(i, num);
        }

        public static Fixed64FieldValue fixed64(int i, Long l) {
            return new Fixed64FieldValue(i, l);
        }

        public static LengthDelimitedFieldValue lengthDelimited(int i, ByteString byteString) {
            return new LengthDelimitedFieldValue(i, byteString);
        }

        public int getTag() {
            return this.tag;
        }

        public WireType getWireType() {
            return this.wireType;
        }

        public Integer getAsInteger() {
            throw new IllegalStateException();
        }

        public Long getAsLong() {
            throw new IllegalStateException();
        }

        public ByteString getAsBytes() {
            throw new IllegalStateException();
        }
    }

    final class Fixed32FieldValue extends FieldValue {
        private final Integer value;

        public Fixed32FieldValue(int i, Integer num) {
            super(i, WireType.FIXED32);
            this.value = num;
        }

        public int getSerializedSize() {
            return 4;
        }

        public void write(int i, WireOutput wireOutput) {
            wireOutput.writeTag(i, WireType.FIXED32);
            wireOutput.writeFixed32(this.value.intValue());
        }

        public Integer getAsInteger() {
            return this.value;
        }
    }

    final class Fixed64FieldValue extends FieldValue {
        private final Long value;

        public Fixed64FieldValue(int i, Long l) {
            super(i, WireType.FIXED64);
            this.value = l;
        }

        public int getSerializedSize() {
            return 8;
        }

        public void write(int i, WireOutput wireOutput) {
            wireOutput.writeTag(i, WireType.FIXED64);
            wireOutput.writeFixed64(this.value.longValue());
        }

        public Long getAsLong() {
            return this.value;
        }
    }

    final class LengthDelimitedFieldValue extends FieldValue {
        private final ByteString value;

        public LengthDelimitedFieldValue(int i, ByteString byteString) {
            super(i, WireType.LENGTH_DELIMITED);
            this.value = byteString;
        }

        public int getSerializedSize() {
            return WireOutput.varint32Size(this.value.size()) + this.value.size();
        }

        public void write(int i, WireOutput wireOutput) {
            wireOutput.writeTag(i, WireType.LENGTH_DELIMITED);
            wireOutput.writeVarint32(this.value.size());
            wireOutput.writeRawBytes(this.value.toByteArray());
        }

        public ByteString getAsBytes() {
            return this.value;
        }
    }

    enum UnknownFieldType {
        VARINT,
        FIXED32,
        FIXED64,
        LENGTH_DELIMITED;

        public static UnknownFieldType of(String str) {
            if ("varint".equals(str)) {
                return VARINT;
            }
            if ("fixed32".equals(str)) {
                return FIXED32;
            }
            if ("fixed64".equals(str)) {
                return FIXED64;
            }
            if ("length-delimited".equals(str)) {
                return LENGTH_DELIMITED;
            }
            throw new IllegalArgumentException("Unknown type " + str);
        }
    }

    final class VarintFieldValue extends FieldValue {
        private final Long value;

        public VarintFieldValue(int i, Long l) {
            super(i, WireType.VARINT);
            this.value = l;
        }

        public int getSerializedSize() {
            return WireOutput.varint64Size(this.value.longValue());
        }

        public void write(int i, WireOutput wireOutput) {
            wireOutput.writeTag(i, WireType.VARINT);
            wireOutput.writeVarint64(this.value.longValue());
        }

        public Long getAsLong() {
            return this.value;
        }
    }

    UnknownFieldMap() {
    }

    UnknownFieldMap(UnknownFieldMap unknownFieldMap) {
        if (unknownFieldMap.fieldMap != null) {
            ensureFieldMap().putAll(unknownFieldMap.fieldMap);
        }
    }

    void addVarint(int i, Long l) {
        addElement(ensureFieldMap(), i, l, WireType.VARINT);
    }

    void addFixed32(int i, Integer num) {
        addElement(ensureFieldMap(), i, num, WireType.FIXED32);
    }

    void addFixed64(int i, Long l) {
        addElement(ensureFieldMap(), i, l, WireType.FIXED64);
    }

    void addLengthDelimited(int i, ByteString byteString) {
        addElement(ensureFieldMap(), i, byteString, WireType.LENGTH_DELIMITED);
    }

    private Map<Integer, List<FieldValue>> ensureFieldMap() {
        if (this.fieldMap == null) {
            this.fieldMap = new TreeMap();
        }
        return this.fieldMap;
    }

    private <T> void addElement(Map<Integer, List<FieldValue>> map, int i, T t, WireType wireType) {
        List list;
        FieldValue varint;
        List list2 = (List) map.get(Integer.valueOf(i));
        if (list2 == null) {
            ArrayList arrayList = new ArrayList();
            map.put(Integer.valueOf(i), arrayList);
            list = arrayList;
        } else {
            list = list2;
        }
        switch (wireType) {
            case VARINT:
                varint = FieldValue.varint(i, (Long) t);
                break;
            case FIXED32:
                varint = FieldValue.fixed32(i, (Integer) t);
                break;
            case FIXED64:
                varint = FieldValue.fixed64(i, (Long) t);
                break;
            case LENGTH_DELIMITED:
                varint = FieldValue.lengthDelimited(i, (ByteString) t);
                break;
            default:
                throw new IllegalArgumentException("Unsupported wireType = " + wireType);
        }
        if (list.size() <= 0 || ((FieldValue) list.get(0)).getWireType() == varint.getWireType()) {
            list.add(varint);
            return;
        }
        throw new IllegalStateException("Wire type differs from previous type for tag");
    }

    int getSerializedSize() {
        if (this.fieldMap == null) {
            return 0;
        }
        int i = 0;
        for (Entry entry : this.fieldMap.entrySet()) {
            int varintTagSize = WireOutput.varintTagSize(((Integer) entry.getKey()).intValue()) + i;
            for (FieldValue serializedSize : (List) entry.getValue()) {
                varintTagSize += serializedSize.getSerializedSize();
            }
            i = varintTagSize;
        }
        return i;
    }

    void write(WireOutput wireOutput) {
        if (this.fieldMap != null) {
            for (Entry entry : this.fieldMap.entrySet()) {
                int intValue = ((Integer) entry.getKey()).intValue();
                for (FieldValue write : (List) entry.getValue()) {
                    write.write(intValue, wireOutput);
                }
            }
        }
    }
}
