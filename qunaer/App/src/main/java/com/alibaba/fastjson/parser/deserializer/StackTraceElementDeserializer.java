package com.alibaba.fastjson.parser.deserializer;

public class StackTraceElementDeserializer implements ObjectDeserializer {
    public static final StackTraceElementDeserializer instance = new StackTraceElementDeserializer();

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r9, java.lang.reflect.Type r10, java.lang.Object r11) {
        /*
        r8 = this;
        r4 = r9.getLexer();
        r0 = r4.token();
        r1 = 8;
        if (r0 != r1) goto L_0x0011;
    L_0x000c:
        r4.nextToken();
        r0 = 0;
    L_0x0010:
        return r0;
    L_0x0011:
        r0 = r4.token();
        r1 = 12;
        if (r0 == r1) goto L_0x0042;
    L_0x0019:
        r0 = r4.token();
        r1 = 16;
        if (r0 == r1) goto L_0x0042;
    L_0x0021:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "syntax error: ";
        r1 = r1.append(r2);
        r2 = r4.token();
        r2 = com.alibaba.fastjson.parser.JSONToken.name(r2);
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0042:
        r3 = 0;
        r2 = 0;
        r1 = 0;
        r0 = 0;
    L_0x0046:
        r5 = r9.getSymbolTable();
        r5 = r4.scanSymbol(r5);
        if (r5 != 0) goto L_0x0074;
    L_0x0050:
        r6 = r4.token();
        r7 = 13;
        if (r6 != r7) goto L_0x0064;
    L_0x0058:
        r5 = 16;
        r4.nextToken(r5);
    L_0x005d:
        r4 = new java.lang.StackTraceElement;
        r4.<init>(r3, r2, r1, r0);
        r0 = r4;
        goto L_0x0010;
    L_0x0064:
        r6 = r4.token();
        r7 = 16;
        if (r6 != r7) goto L_0x0074;
    L_0x006c:
        r6 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;
        r6 = r4.isEnabled(r6);
        if (r6 != 0) goto L_0x0046;
    L_0x0074:
        r6 = 4;
        r4.nextTokenWithColon(r6);
        r6 = "className";
        r6 = r6.equals(r5);
        if (r6 == 0) goto L_0x00ab;
    L_0x0080:
        r3 = r4.token();
        r5 = 8;
        if (r3 != r5) goto L_0x0097;
    L_0x0088:
        r3 = 0;
    L_0x0089:
        r5 = r4.token();
        r6 = 13;
        if (r5 != r6) goto L_0x0046;
    L_0x0091:
        r5 = 16;
        r4.nextToken(r5);
        goto L_0x005d;
    L_0x0097:
        r3 = r4.token();
        r5 = 4;
        if (r3 != r5) goto L_0x00a3;
    L_0x009e:
        r3 = r4.stringVal();
        goto L_0x0089;
    L_0x00a3:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = "syntax error";
        r0.<init>(r1);
        throw r0;
    L_0x00ab:
        r6 = "methodName";
        r6 = r6.equals(r5);
        if (r6 == 0) goto L_0x00d1;
    L_0x00b3:
        r2 = r4.token();
        r5 = 8;
        if (r2 != r5) goto L_0x00bd;
    L_0x00bb:
        r2 = 0;
        goto L_0x0089;
    L_0x00bd:
        r2 = r4.token();
        r5 = 4;
        if (r2 != r5) goto L_0x00c9;
    L_0x00c4:
        r2 = r4.stringVal();
        goto L_0x0089;
    L_0x00c9:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = "syntax error";
        r0.<init>(r1);
        throw r0;
    L_0x00d1:
        r6 = "fileName";
        r6 = r6.equals(r5);
        if (r6 == 0) goto L_0x00f7;
    L_0x00d9:
        r1 = r4.token();
        r5 = 8;
        if (r1 != r5) goto L_0x00e3;
    L_0x00e1:
        r1 = 0;
        goto L_0x0089;
    L_0x00e3:
        r1 = r4.token();
        r5 = 4;
        if (r1 != r5) goto L_0x00ef;
    L_0x00ea:
        r1 = r4.stringVal();
        goto L_0x0089;
    L_0x00ef:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = "syntax error";
        r0.<init>(r1);
        throw r0;
    L_0x00f7:
        r6 = "lineNumber";
        r6 = r6.equals(r5);
        if (r6 == 0) goto L_0x011e;
    L_0x00ff:
        r0 = r4.token();
        r5 = 8;
        if (r0 != r5) goto L_0x0109;
    L_0x0107:
        r0 = 0;
        goto L_0x0089;
    L_0x0109:
        r0 = r4.token();
        r5 = 2;
        if (r0 != r5) goto L_0x0116;
    L_0x0110:
        r0 = r4.intValue();
        goto L_0x0089;
    L_0x0116:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = "syntax error";
        r0.<init>(r1);
        throw r0;
    L_0x011e:
        r6 = "nativeMethod";
        r6 = r6.equals(r5);
        if (r6 == 0) goto L_0x0159;
    L_0x0126:
        r5 = r4.token();
        r6 = 8;
        if (r5 != r6) goto L_0x0135;
    L_0x012e:
        r5 = 16;
        r4.nextToken(r5);
        goto L_0x0089;
    L_0x0135:
        r5 = r4.token();
        r6 = 6;
        if (r5 != r6) goto L_0x0143;
    L_0x013c:
        r5 = 16;
        r4.nextToken(r5);
        goto L_0x0089;
    L_0x0143:
        r5 = r4.token();
        r6 = 7;
        if (r5 != r6) goto L_0x0151;
    L_0x014a:
        r5 = 16;
        r4.nextToken(r5);
        goto L_0x0089;
    L_0x0151:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = "syntax error";
        r0.<init>(r1);
        throw r0;
    L_0x0159:
        r6 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;
        if (r5 != r6) goto L_0x0199;
    L_0x015d:
        r5 = r4.token();
        r6 = 4;
        if (r5 != r6) goto L_0x0189;
    L_0x0164:
        r5 = r4.stringVal();
        r6 = "java.lang.StackTraceElement";
        r6 = r5.equals(r6);
        if (r6 != 0) goto L_0x0089;
    L_0x0170:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "syntax error : ";
        r1 = r1.append(r2);
        r1 = r1.append(r5);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0189:
        r5 = r4.token();
        r6 = 8;
        if (r5 == r6) goto L_0x0089;
    L_0x0191:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = "syntax error";
        r0.<init>(r1);
        throw r0;
    L_0x0199:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "syntax error : ";
        r1 = r1.append(r2);
        r1 = r1.append(r5);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.StackTraceElementDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):T");
    }

    public int getFastMatchToken() {
        return 12;
    }
}
