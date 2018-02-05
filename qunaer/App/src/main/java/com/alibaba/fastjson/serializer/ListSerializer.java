package com.alibaba.fastjson.serializer;

public final class ListSerializer implements ObjectSerializer {
    public static final ListSerializer instance = new ListSerializer();

    public final void write(com.alibaba.fastjson.serializer.JSONSerializer r10, java.lang.Object r11, java.lang.Object r12, java.lang.reflect.Type r13) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r9 = this;
        r1 = 0;
        r0 = com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName;
        r3 = r10.isEnabled(r0);
        r4 = r10.getWriter();
        r0 = 0;
        if (r3 == 0) goto L_0x012b;
    L_0x000e:
        r2 = r13 instanceof java.lang.reflect.ParameterizedType;
        if (r2 == 0) goto L_0x012b;
    L_0x0012:
        r13 = (java.lang.reflect.ParameterizedType) r13;
        r0 = r13.getActualTypeArguments();
        r0 = r0[r1];
        r2 = r0;
    L_0x001b:
        if (r11 != 0) goto L_0x002f;
    L_0x001d:
        r0 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
        r0 = r4.isEnabled(r0);
        if (r0 == 0) goto L_0x002b;
    L_0x0025:
        r0 = "[]";
        r4.write(r0);
    L_0x002a:
        return;
    L_0x002b:
        r4.writeNull();
        goto L_0x002a;
    L_0x002f:
        r0 = r11;
        r0 = (java.util.List) r0;
        r5 = r0.size();
        if (r5 != 0) goto L_0x003e;
    L_0x0038:
        r0 = "[]";
        r4.append(r0);
        goto L_0x002a;
    L_0x003e:
        r5 = r10.getContext();
        r10.setContext(r5, r11, r12, r1);
        r6 = com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;	 Catch:{ all -> 0x0094 }
        r6 = r4.isEnabled(r6);	 Catch:{ all -> 0x0094 }
        if (r6 == 0) goto L_0x00b1;	 Catch:{ all -> 0x0094 }
    L_0x004d:
        r3 = 91;	 Catch:{ all -> 0x0094 }
        r4.append(r3);	 Catch:{ all -> 0x0094 }
        r10.incrementIndent();	 Catch:{ all -> 0x0094 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0094 }
    L_0x0059:
        r3 = r0.hasNext();	 Catch:{ all -> 0x0094 }
        if (r3 == 0) goto L_0x00a1;	 Catch:{ all -> 0x0094 }
    L_0x005f:
        r3 = r0.next();	 Catch:{ all -> 0x0094 }
        if (r1 == 0) goto L_0x006a;	 Catch:{ all -> 0x0094 }
    L_0x0065:
        r6 = 44;	 Catch:{ all -> 0x0094 }
        r4.append(r6);	 Catch:{ all -> 0x0094 }
    L_0x006a:
        r10.println();	 Catch:{ all -> 0x0094 }
        if (r3 == 0) goto L_0x0099;	 Catch:{ all -> 0x0094 }
    L_0x006f:
        r6 = r10.containsReference(r3);	 Catch:{ all -> 0x0094 }
        if (r6 == 0) goto L_0x007b;	 Catch:{ all -> 0x0094 }
    L_0x0075:
        r10.writeReference(r3);	 Catch:{ all -> 0x0094 }
    L_0x0078:
        r1 = r1 + 1;	 Catch:{ all -> 0x0094 }
        goto L_0x0059;	 Catch:{ all -> 0x0094 }
    L_0x007b:
        r6 = r3.getClass();	 Catch:{ all -> 0x0094 }
        r6 = r10.getObjectWriter(r6);	 Catch:{ all -> 0x0094 }
        r7 = new com.alibaba.fastjson.serializer.SerialContext;	 Catch:{ all -> 0x0094 }
        r8 = 0;	 Catch:{ all -> 0x0094 }
        r7.<init>(r5, r11, r12, r8);	 Catch:{ all -> 0x0094 }
        r10.setContext(r7);	 Catch:{ all -> 0x0094 }
        r7 = java.lang.Integer.valueOf(r1);	 Catch:{ all -> 0x0094 }
        r6.write(r10, r3, r7, r2);	 Catch:{ all -> 0x0094 }
        goto L_0x0078;
    L_0x0094:
        r0 = move-exception;
        r10.setContext(r5);
        throw r0;
    L_0x0099:
        r3 = r10.getWriter();	 Catch:{ all -> 0x0094 }
        r3.writeNull();	 Catch:{ all -> 0x0094 }
        goto L_0x0078;	 Catch:{ all -> 0x0094 }
    L_0x00a1:
        r10.decrementIdent();	 Catch:{ all -> 0x0094 }
        r10.println();	 Catch:{ all -> 0x0094 }
        r0 = 93;	 Catch:{ all -> 0x0094 }
        r4.append(r0);	 Catch:{ all -> 0x0094 }
        r10.setContext(r5);
        goto L_0x002a;
    L_0x00b1:
        r6 = 91;
        r4.append(r6);	 Catch:{ all -> 0x0094 }
        r6 = r0.iterator();	 Catch:{ all -> 0x0094 }
    L_0x00ba:
        r0 = r6.hasNext();	 Catch:{ all -> 0x0094 }
        if (r0 == 0) goto L_0x0121;	 Catch:{ all -> 0x0094 }
    L_0x00c0:
        r0 = r6.next();	 Catch:{ all -> 0x0094 }
        if (r1 == 0) goto L_0x00cb;	 Catch:{ all -> 0x0094 }
    L_0x00c6:
        r7 = 44;	 Catch:{ all -> 0x0094 }
        r4.append(r7);	 Catch:{ all -> 0x0094 }
    L_0x00cb:
        if (r0 != 0) goto L_0x00d6;	 Catch:{ all -> 0x0094 }
    L_0x00cd:
        r0 = "null";	 Catch:{ all -> 0x0094 }
        r4.append(r0);	 Catch:{ all -> 0x0094 }
    L_0x00d2:
        r0 = r1 + 1;	 Catch:{ all -> 0x0094 }
        r1 = r0;	 Catch:{ all -> 0x0094 }
        goto L_0x00ba;	 Catch:{ all -> 0x0094 }
    L_0x00d6:
        r7 = r0.getClass();	 Catch:{ all -> 0x0094 }
        r8 = java.lang.Integer.class;	 Catch:{ all -> 0x0094 }
        if (r7 != r8) goto L_0x00e8;	 Catch:{ all -> 0x0094 }
    L_0x00de:
        r0 = (java.lang.Integer) r0;	 Catch:{ all -> 0x0094 }
        r0 = r0.intValue();	 Catch:{ all -> 0x0094 }
        r4.writeInt(r0);	 Catch:{ all -> 0x0094 }
        goto L_0x00d2;	 Catch:{ all -> 0x0094 }
    L_0x00e8:
        r8 = java.lang.Long.class;	 Catch:{ all -> 0x0094 }
        if (r7 != r8) goto L_0x00fe;	 Catch:{ all -> 0x0094 }
    L_0x00ec:
        r0 = (java.lang.Long) r0;	 Catch:{ all -> 0x0094 }
        r7 = r0.longValue();	 Catch:{ all -> 0x0094 }
        if (r3 == 0) goto L_0x00fa;	 Catch:{ all -> 0x0094 }
    L_0x00f4:
        r0 = 76;	 Catch:{ all -> 0x0094 }
        r4.writeLongAndChar(r7, r0);	 Catch:{ all -> 0x0094 }
        goto L_0x00d2;	 Catch:{ all -> 0x0094 }
    L_0x00fa:
        r4.writeLong(r7);	 Catch:{ all -> 0x0094 }
        goto L_0x00d2;	 Catch:{ all -> 0x0094 }
    L_0x00fe:
        r7 = new com.alibaba.fastjson.serializer.SerialContext;	 Catch:{ all -> 0x0094 }
        r8 = 0;	 Catch:{ all -> 0x0094 }
        r7.<init>(r5, r11, r12, r8);	 Catch:{ all -> 0x0094 }
        r10.setContext(r7);	 Catch:{ all -> 0x0094 }
        r7 = r10.containsReference(r0);	 Catch:{ all -> 0x0094 }
        if (r7 == 0) goto L_0x0111;	 Catch:{ all -> 0x0094 }
    L_0x010d:
        r10.writeReference(r0);	 Catch:{ all -> 0x0094 }
        goto L_0x00d2;	 Catch:{ all -> 0x0094 }
    L_0x0111:
        r7 = r0.getClass();	 Catch:{ all -> 0x0094 }
        r7 = r10.getObjectWriter(r7);	 Catch:{ all -> 0x0094 }
        r8 = java.lang.Integer.valueOf(r1);	 Catch:{ all -> 0x0094 }
        r7.write(r10, r0, r8, r2);	 Catch:{ all -> 0x0094 }
        goto L_0x00d2;	 Catch:{ all -> 0x0094 }
    L_0x0121:
        r0 = 93;	 Catch:{ all -> 0x0094 }
        r4.append(r0);	 Catch:{ all -> 0x0094 }
        r10.setContext(r5);
        goto L_0x002a;
    L_0x012b:
        r2 = r0;
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.ListSerializer.write(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type):void");
    }
}
