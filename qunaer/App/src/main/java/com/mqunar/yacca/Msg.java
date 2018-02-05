package com.mqunar.yacca;

import com.mqunar.necro.agent.bean.NecroParam;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Msg implements Serializable {
    public final Map<String, Object> data;
    public final int protocol;

    public interface OnMsg {
        void onConnected();

        void onMessage(byte[] bArr, int i, byte[] bArr2);
    }

    static Msg readMsg(ByteBuffer byteBuffer) {
        byteBuffer.mark();
        if (byteBuffer.remaining() == 0) {
            byteBuffer.reset();
            return null;
        }
        byte b = byteBuffer.get();
        Map hashMap = new HashMap();
        switch (b) {
            case (byte) 0:
                break;
            case (byte) 15:
                if (byteBuffer.remaining() >= 4) {
                    int i = byteBuffer.getInt();
                    if (byteBuffer.remaining() >= i) {
                        byte b2 = byteBuffer.get();
                        Object obj = new byte[b2];
                        byteBuffer.get(obj);
                        hashMap.put("appName", obj);
                        byte b3 = byteBuffer.get();
                        Object obj2 = new byte[((i - b2) - 2)];
                        byteBuffer.get(obj2);
                        hashMap.put("agentId", Integer.valueOf(b3));
                        hashMap.put("msg", obj2);
                        break;
                    }
                    byteBuffer.reset();
                    return null;
                }
                byteBuffer.reset();
                return null;
            default:
                throw new RuntimeException("unknown protocol");
        }
        return new Msg(b, hashMap);
    }

    private Msg(int i, Map<String, Object> map) {
        this.protocol = i;
        this.data = map;
    }

    public String toString() {
        Iterator it = this.data.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        if (it.hasNext()) {
            while (true) {
                Entry entry = (Entry) it.next();
                String str = (String) entry.getKey();
                Map value = entry.getValue();
                stringBuilder.append(str);
                stringBuilder.append('=');
                if (value == this.data) {
                    stringBuilder.append("(this Map)");
                } else if (value == null) {
                    stringBuilder.append("null");
                } else if (value.getClass().isArray()) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    int length = Array.getLength(value) - 1;
                    if (length == -1) {
                        stringBuilder2.append(NecroParam.NECRO_EMPTY_ARRAY);
                    } else {
                        stringBuilder2.append('[');
                        int i = 0;
                        while (true) {
                            stringBuilder2.append(String.valueOf(Array.get(value, i)));
                            if (i == length) {
                                break;
                            }
                            stringBuilder2.append(", ");
                            i++;
                        }
                        stringBuilder2.append(']');
                    }
                    stringBuilder.append(stringBuilder2);
                } else {
                    stringBuilder.append(value);
                }
                if (!it.hasNext()) {
                    break;
                }
                stringBuilder.append(',').append(' ');
            }
            stringBuilder.append('}');
        } else {
            stringBuilder.append('}');
        }
        return "Msg { protocol=" + this.protocol + ", data=" + stringBuilder.toString() + " } ";
    }
}
