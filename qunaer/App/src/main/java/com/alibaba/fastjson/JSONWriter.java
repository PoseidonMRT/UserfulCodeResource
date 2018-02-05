package com.alibaba.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.hwid.openapi.out.OutReturn.Ret_code;
import java.io.Closeable;
import java.io.Flushable;
import java.io.Writer;

public class JSONWriter implements Closeable, Flushable {
    private JSONStreamContext context;
    private JSONSerializer serializer = new JSONSerializer(this.writer);
    private SerializeWriter writer;

    public JSONWriter(Writer writer) {
        this.writer = new SerializeWriter(writer);
    }

    public void config(SerializerFeature serializerFeature, boolean z) {
        this.writer.config(serializerFeature, z);
    }

    public void startObject() {
        if (this.context != null) {
            beginStructure();
        }
        this.context = new JSONStreamContext(this.context, 1001);
        this.writer.write('{');
    }

    public void endObject() {
        this.writer.write('}');
        endStructure();
    }

    public void writeKey(String str) {
        writeObject(str);
    }

    public void writeValue(Object obj) {
        writeObject(obj);
    }

    public void writeObject(String str) {
        beforeWrite();
        this.serializer.write(str);
        afterWriter();
    }

    public void writeObject(Object obj) {
        beforeWrite();
        this.serializer.write(obj);
        afterWriter();
    }

    public void startArray() {
        if (this.context != null) {
            beginStructure();
        }
        this.context = new JSONStreamContext(this.context, Ret_code.ERR_UP_400_FAILED);
        this.writer.write('[');
    }

    private void beginStructure() {
        int state = this.context.getState();
        switch (state) {
            case 1001:
            case Ret_code.ERR_UP_400_FAILED /*1004*/:
                return;
            case 1002:
                this.writer.write(':');
                return;
            case 1005:
                this.writer.write(',');
                return;
            default:
                throw new JSONException("illegal state : " + state);
        }
    }

    public void endArray() {
        this.writer.write(']');
        endStructure();
    }

    private void endStructure() {
        this.context = this.context.getParent();
        if (this.context != null) {
            int i;
            switch (this.context.getState()) {
                case 1001:
                    i = 1002;
                    break;
                case 1002:
                    i = 1003;
                    break;
                case Ret_code.ERR_UP_400_FAILED /*1004*/:
                    i = 1005;
                    break;
                case 1005:
                    i = -1;
                    break;
                default:
                    i = -1;
                    break;
            }
            if (i != -1) {
                this.context.setState(i);
            }
        }
    }

    private void beforeWrite() {
        if (this.context != null) {
            switch (this.context.getState()) {
                case 1001:
                case Ret_code.ERR_UP_400_FAILED /*1004*/:
                    return;
                case 1002:
                    this.writer.write(':');
                    return;
                case 1003:
                    this.writer.write(',');
                    return;
                case 1005:
                    this.writer.write(',');
                    return;
                default:
                    return;
            }
        }
    }

    private void afterWriter() {
        if (this.context != null) {
            int i;
            switch (this.context.getState()) {
                case 1001:
                case 1003:
                    i = 1002;
                    break;
                case 1002:
                    i = 1003;
                    break;
                case Ret_code.ERR_UP_400_FAILED /*1004*/:
                    i = 1005;
                    break;
                case 1005:
                    i = -1;
                    break;
                default:
                    i = -1;
                    break;
            }
            if (i != -1) {
                this.context.setState(i);
            }
        }
    }

    public void flush() {
        this.writer.flush();
    }

    public void close() {
        this.writer.close();
    }
}
