package com.mqunar.yacca;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Cmd {
    private Map<String, ?> args;
    private byte protocol;

    public class Builder {
        public static Cmd protocal_version(byte b, byte b2) {
            Map hashMap = new HashMap();
            hashMap.put("mainVer", Byte.valueOf(b));
            hashMap.put("subVer", Byte.valueOf(b2));
            return new Cmd((byte) 66, hashMap);
        }

        public static Cmd set_id(byte[] bArr) {
            Map hashMap = new HashMap();
            hashMap.put("clientId", bArr);
            return new Cmd((byte) 1, hashMap);
        }

        public static Cmd heartbeat() {
            return new Cmd((byte) 0, null);
        }

        public static Cmd send(String str, byte b, byte[] bArr) {
            Map hashMap = new HashMap();
            hashMap.put("sizeAppName", Byte.valueOf((byte) str.length()));
            hashMap.put("appName", str);
            hashMap.put("agentId", Byte.valueOf(b));
            hashMap.put("msg", bArr);
            hashMap.put("sizeBin", Integer.valueOf((str.length() + 2) + bArr.length));
            return new Cmd((byte) 2, hashMap);
        }
    }

    public Cmd(byte b, Map<String, Object> map) {
        this.protocol = b;
        this.args = map;
    }

    public void getByteBuffer2(ByteBuffer byteBuffer) {
        switch (this.protocol) {
            case (byte) 0:
                byteBuffer.put(this.protocol);
                return;
            case (byte) 1:
                byte[] bArr = (byte[]) this.args.get("clientId");
                byteBuffer.put(this.protocol);
                byteBuffer.put(bArr);
                return;
            case (byte) 2:
                int intValue = ((Integer) this.args.get("sizeBin")).intValue();
                byte byteValue = ((Byte) this.args.get("sizeAppName")).byteValue();
                String str = (String) this.args.get("appName");
                byte byteValue2 = ((Byte) this.args.get("agentId")).byteValue();
                byte[] bArr2 = (byte[]) this.args.get("msg");
                byteBuffer.put(this.protocol);
                byteBuffer.putInt(intValue);
                byteBuffer.put(byteValue);
                byteBuffer.put(str.getBytes());
                byteBuffer.put(byteValue2);
                byteBuffer.put(bArr2);
                return;
            case (byte) 66:
                byte byteValue3 = ((Byte) this.args.get("mainVer")).byteValue();
                byte byteValue4 = ((Byte) this.args.get("subVer")).byteValue();
                byteBuffer.put(this.protocol);
                byteBuffer.put(byteValue3);
                byteBuffer.put(byteValue4);
                return;
            default:
                throw new IOException();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Cmd { ");
        stringBuilder.append("protocol=").append(this.protocol);
        if (this.args != null) {
            stringBuilder.append(", args=").append(this.args);
        }
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}
