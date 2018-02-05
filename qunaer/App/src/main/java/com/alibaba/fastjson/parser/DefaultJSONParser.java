package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.CollectionResolveFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ListResolveFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.MapResolveFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class DefaultJSONParser extends AbstractJSONParser implements Closeable {
    public static final int NONE = 0;
    public static final int NeedToResolve = 1;
    public static final int TypeNameRedirect = 2;
    private static final Set<Class<?>> primitiveClasses = new HashSet();
    protected ParserConfig config;
    protected ParseContext context;
    private ParseContext[] contextArray;
    private int contextArrayIndex;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private List<ExtraProcessor> extraProcessors;
    private List<ExtraTypeProvider> extraTypeProviders;
    protected final Object input;
    protected final JSONLexer lexer;
    private int resolveStatus;
    private List<ResolveTask> resolveTaskList;
    protected final SymbolTable symbolTable;

    public class ResolveTask {
        private final ParseContext context;
        private FieldDeserializer fieldDeserializer;
        private ParseContext ownerContext;
        private final String referenceValue;

        public ResolveTask(ParseContext parseContext, String str) {
            this.context = parseContext;
            this.referenceValue = str;
        }

        public ParseContext getContext() {
            return this.context;
        }

        public String getReferenceValue() {
            return this.referenceValue;
        }

        public FieldDeserializer getFieldDeserializer() {
            return this.fieldDeserializer;
        }

        public void setFieldDeserializer(FieldDeserializer fieldDeserializer) {
            this.fieldDeserializer = fieldDeserializer;
        }

        public ParseContext getOwnerContext() {
            return this.ownerContext;
        }

        public void setOwnerContext(ParseContext parseContext) {
            this.ownerContext = parseContext;
        }
    }

    static {
        primitiveClasses.add(Boolean.TYPE);
        primitiveClasses.add(Byte.TYPE);
        primitiveClasses.add(Short.TYPE);
        primitiveClasses.add(Integer.TYPE);
        primitiveClasses.add(Long.TYPE);
        primitiveClasses.add(Float.TYPE);
        primitiveClasses.add(Double.TYPE);
        primitiveClasses.add(Boolean.class);
        primitiveClasses.add(Byte.class);
        primitiveClasses.add(Short.class);
        primitiveClasses.add(Integer.class);
        primitiveClasses.add(Long.class);
        primitiveClasses.add(Float.class);
        primitiveClasses.add(Double.class);
        primitiveClasses.add(BigInteger.class);
        primitiveClasses.add(BigDecimal.class);
        primitiveClasses.add(String.class);
    }

    public String getDateFomartPattern() {
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat(this.dateFormatPattern);
        }
        return this.dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormatPattern = str;
        this.dateFormat = null;
    }

    public void setDateFomrat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DefaultJSONParser(String str) {
        this(str, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig) {
        this((Object) str, new JSONScanner(str, JSON.DEFAULT_PARSER_FEATURE), parserConfig);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig, int i) {
        this((Object) str, new JSONScanner(str, i), parserConfig);
    }

    public DefaultJSONParser(char[] cArr, int i, ParserConfig parserConfig, int i2) {
        this((Object) cArr, new JSONScanner(cArr, i, i2), parserConfig);
    }

    public DefaultJSONParser(JSONLexer jSONLexer) {
        this(jSONLexer, ParserConfig.getGlobalInstance());
    }

    public DefaultJSONParser(JSONLexer jSONLexer, ParserConfig parserConfig) {
        this(null, jSONLexer, parserConfig);
    }

    public DefaultJSONParser(Object obj, JSONLexer jSONLexer, ParserConfig parserConfig) {
        this.dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
        this.contextArray = new ParseContext[8];
        this.contextArrayIndex = 0;
        this.resolveStatus = 0;
        this.extraTypeProviders = null;
        this.extraProcessors = null;
        this.lexer = jSONLexer;
        this.input = obj;
        this.config = parserConfig;
        this.symbolTable = parserConfig.getSymbolTable();
        jSONLexer.nextToken(12);
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public String getInput() {
        if (this.input instanceof char[]) {
            return new String((char[]) this.input);
        }
        return this.input.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object parseObject(java.util.Map r14, java.lang.Object r15) {
        /*
        r13 = this;
        r12 = 58;
        r11 = 34;
        r10 = 13;
        r9 = 16;
        r3 = 1;
        r6 = r13.lexer;
        r0 = r6.token();
        r1 = 8;
        if (r0 != r1) goto L_0x0018;
    L_0x0013:
        r6.next();
        r14 = 0;
    L_0x0017:
        return r14;
    L_0x0018:
        r0 = r6.token();
        r1 = 12;
        if (r0 == r1) goto L_0x0043;
    L_0x0020:
        r0 = r6.token();
        if (r0 == r9) goto L_0x0043;
    L_0x0026:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "syntax error, expect {, actual ";
        r1 = r1.append(r2);
        r2 = r6.tokenName();
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0043:
        r5 = r13.getContext();
        r4 = 0;
        r1 = r4;
    L_0x0049:
        r6.skipWhitespace();	 Catch:{ all -> 0x00a2 }
        r0 = r6.getCurrent();	 Catch:{ all -> 0x00a2 }
        r2 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;	 Catch:{ all -> 0x00a2 }
        r2 = r13.isEnabled(r2);	 Catch:{ all -> 0x00a2 }
        if (r2 == 0) goto L_0x0067;
    L_0x0058:
        r2 = 44;
        if (r0 != r2) goto L_0x0067;
    L_0x005c:
        r6.next();	 Catch:{ all -> 0x00a2 }
        r6.skipWhitespace();	 Catch:{ all -> 0x00a2 }
        r0 = r6.getCurrent();	 Catch:{ all -> 0x00a2 }
        goto L_0x0058;
    L_0x0067:
        r2 = 0;
        if (r0 != r11) goto L_0x00a7;
    L_0x006a:
        r0 = r13.symbolTable;	 Catch:{ all -> 0x00a2 }
        r4 = 34;
        r0 = r6.scanSymbol(r0, r4);	 Catch:{ all -> 0x00a2 }
        r6.skipWhitespace();	 Catch:{ all -> 0x00a2 }
        r4 = r6.getCurrent();	 Catch:{ all -> 0x00a2 }
        if (r4 == r12) goto L_0x0173;
    L_0x007b:
        r1 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
        r2.<init>();	 Catch:{ all -> 0x00a2 }
        r3 = "expect ':' at ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r3 = r6.pos();	 Catch:{ all -> 0x00a2 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r3 = ", name ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r0 = r2.append(r0);	 Catch:{ all -> 0x00a2 }
        r0 = r0.toString();	 Catch:{ all -> 0x00a2 }
        r1.<init>(r0);	 Catch:{ all -> 0x00a2 }
        throw r1;	 Catch:{ all -> 0x00a2 }
    L_0x00a2:
        r0 = move-exception;
        r13.setContext(r5);
        throw r0;
    L_0x00a7:
        r4 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r0 != r4) goto L_0x00b9;
    L_0x00ab:
        r6.next();	 Catch:{ all -> 0x00a2 }
        r6.resetStringPosition();	 Catch:{ all -> 0x00a2 }
        r6.nextToken();	 Catch:{ all -> 0x00a2 }
        r13.setContext(r5);
        goto L_0x0017;
    L_0x00b9:
        r4 = 39;
        if (r0 != r4) goto L_0x00fb;
    L_0x00bd:
        r0 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes;	 Catch:{ all -> 0x00a2 }
        r0 = r13.isEnabled(r0);	 Catch:{ all -> 0x00a2 }
        if (r0 != 0) goto L_0x00cd;
    L_0x00c5:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r1 = "syntax error";
        r0.<init>(r1);	 Catch:{ all -> 0x00a2 }
        throw r0;	 Catch:{ all -> 0x00a2 }
    L_0x00cd:
        r0 = r13.symbolTable;	 Catch:{ all -> 0x00a2 }
        r4 = 39;
        r0 = r6.scanSymbol(r0, r4);	 Catch:{ all -> 0x00a2 }
        r6.skipWhitespace();	 Catch:{ all -> 0x00a2 }
        r4 = r6.getCurrent();	 Catch:{ all -> 0x00a2 }
        if (r4 == r12) goto L_0x0173;
    L_0x00de:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
        r1.<init>();	 Catch:{ all -> 0x00a2 }
        r2 = "expect ':' at ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a2 }
        r2 = r6.pos();	 Catch:{ all -> 0x00a2 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a2 }
        r1 = r1.toString();	 Catch:{ all -> 0x00a2 }
        r0.<init>(r1);	 Catch:{ all -> 0x00a2 }
        throw r0;	 Catch:{ all -> 0x00a2 }
    L_0x00fb:
        r4 = 26;
        if (r0 != r4) goto L_0x0107;
    L_0x00ff:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r1 = "syntax error";
        r0.<init>(r1);	 Catch:{ all -> 0x00a2 }
        throw r0;	 Catch:{ all -> 0x00a2 }
    L_0x0107:
        r4 = 44;
        if (r0 != r4) goto L_0x0113;
    L_0x010b:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r1 = "syntax error";
        r0.<init>(r1);	 Catch:{ all -> 0x00a2 }
        throw r0;	 Catch:{ all -> 0x00a2 }
    L_0x0113:
        r4 = 48;
        if (r0 < r4) goto L_0x011b;
    L_0x0117:
        r4 = 57;
        if (r0 <= r4) goto L_0x011f;
    L_0x011b:
        r4 = 45;
        if (r0 != r4) goto L_0x0163;
    L_0x011f:
        r6.resetStringPosition();	 Catch:{ all -> 0x00a2 }
        r6.scanNumber();	 Catch:{ all -> 0x00a2 }
        r0 = r6.token();	 Catch:{ all -> 0x00a2 }
        r4 = 2;
        if (r0 != r4) goto L_0x015d;
    L_0x012c:
        r0 = r6.integerValue();	 Catch:{ all -> 0x00a2 }
    L_0x0130:
        r4 = r6.getCurrent();	 Catch:{ all -> 0x00a2 }
        if (r4 == r12) goto L_0x0173;
    L_0x0136:
        r1 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
        r2.<init>();	 Catch:{ all -> 0x00a2 }
        r3 = "expect ':' at ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r3 = r6.pos();	 Catch:{ all -> 0x00a2 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r3 = ", name ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r0 = r2.append(r0);	 Catch:{ all -> 0x00a2 }
        r0 = r0.toString();	 Catch:{ all -> 0x00a2 }
        r1.<init>(r0);	 Catch:{ all -> 0x00a2 }
        throw r1;	 Catch:{ all -> 0x00a2 }
    L_0x015d:
        r0 = 1;
        r0 = r6.decimalValue(r0);	 Catch:{ all -> 0x00a2 }
        goto L_0x0130;
    L_0x0163:
        r4 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        if (r0 == r4) goto L_0x016b;
    L_0x0167:
        r4 = 91;
        if (r0 != r4) goto L_0x019b;
    L_0x016b:
        r6.nextToken();	 Catch:{ all -> 0x00a2 }
        r0 = r13.parse();	 Catch:{ all -> 0x00a2 }
        r2 = r3;
    L_0x0173:
        if (r2 != 0) goto L_0x017b;
    L_0x0175:
        r6.next();	 Catch:{ all -> 0x00a2 }
        r6.skipWhitespace();	 Catch:{ all -> 0x00a2 }
    L_0x017b:
        r2 = r6.getCurrent();	 Catch:{ all -> 0x00a2 }
        r6.resetStringPosition();	 Catch:{ all -> 0x00a2 }
        r4 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;	 Catch:{ all -> 0x00a2 }
        if (r0 != r4) goto L_0x023f;
    L_0x0186:
        r0 = r13.symbolTable;	 Catch:{ all -> 0x00a2 }
        r2 = 34;
        r0 = r6.scanSymbol(r0, r2);	 Catch:{ all -> 0x00a2 }
        r2 = com.alibaba.fastjson.util.TypeUtils.loadClass(r0);	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x01e1;
    L_0x0194:
        r2 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;	 Catch:{ all -> 0x00a2 }
        r14.put(r2, r0);	 Catch:{ all -> 0x00a2 }
        goto L_0x0049;
    L_0x019b:
        r0 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames;	 Catch:{ all -> 0x00a2 }
        r0 = r13.isEnabled(r0);	 Catch:{ all -> 0x00a2 }
        if (r0 != 0) goto L_0x01ab;
    L_0x01a3:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r1 = "syntax error";
        r0.<init>(r1);	 Catch:{ all -> 0x00a2 }
        throw r0;	 Catch:{ all -> 0x00a2 }
    L_0x01ab:
        r0 = r13.symbolTable;	 Catch:{ all -> 0x00a2 }
        r0 = r6.scanSymbolUnQuoted(r0);	 Catch:{ all -> 0x00a2 }
        r6.skipWhitespace();	 Catch:{ all -> 0x00a2 }
        r4 = r6.getCurrent();	 Catch:{ all -> 0x00a2 }
        if (r4 == r12) goto L_0x0173;
    L_0x01ba:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
        r1.<init>();	 Catch:{ all -> 0x00a2 }
        r2 = "expect ':' at ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a2 }
        r2 = r6.pos();	 Catch:{ all -> 0x00a2 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a2 }
        r2 = ", actual ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a2 }
        r1 = r1.append(r4);	 Catch:{ all -> 0x00a2 }
        r1 = r1.toString();	 Catch:{ all -> 0x00a2 }
        r0.<init>(r1);	 Catch:{ all -> 0x00a2 }
        throw r0;	 Catch:{ all -> 0x00a2 }
    L_0x01e1:
        r0 = 16;
        r6.nextToken(r0);	 Catch:{ all -> 0x00a2 }
        r0 = r6.token();	 Catch:{ all -> 0x00a2 }
        if (r0 != r10) goto L_0x0221;
    L_0x01ec:
        r0 = 16;
        r6.nextToken(r0);	 Catch:{ all -> 0x00a2 }
        r1 = 0;
        r0 = r13.config;	 Catch:{ Exception -> 0x0218 }
        r0 = r0.getDeserializer(r2);	 Catch:{ Exception -> 0x0218 }
        r3 = r0 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;	 Catch:{ Exception -> 0x0218 }
        if (r3 == 0) goto L_0x04e1;
    L_0x01fc:
        r0 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r0;	 Catch:{ Exception -> 0x0218 }
        r0 = r0.createInstance(r13, r2);	 Catch:{ Exception -> 0x0218 }
    L_0x0202:
        if (r0 != 0) goto L_0x020d;
    L_0x0204:
        r0 = java.lang.Cloneable.class;
        if (r2 != r0) goto L_0x0213;
    L_0x0208:
        r0 = new java.util.HashMap;	 Catch:{ Exception -> 0x0218 }
        r0.<init>();	 Catch:{ Exception -> 0x0218 }
    L_0x020d:
        r13.setContext(r5);
        r14 = r0;
        goto L_0x0017;
    L_0x0213:
        r0 = r2.newInstance();	 Catch:{ Exception -> 0x0218 }
        goto L_0x020d;
    L_0x0218:
        r0 = move-exception;
        r1 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r2 = "create instance error";
        r1.<init>(r2, r0);	 Catch:{ all -> 0x00a2 }
        throw r1;	 Catch:{ all -> 0x00a2 }
    L_0x0221:
        r0 = 2;
        r13.setResolveStatus(r0);	 Catch:{ all -> 0x00a2 }
        r0 = r13.context;	 Catch:{ all -> 0x00a2 }
        if (r0 == 0) goto L_0x0230;
    L_0x0229:
        r0 = r15 instanceof java.lang.Integer;	 Catch:{ all -> 0x00a2 }
        if (r0 != 0) goto L_0x0230;
    L_0x022d:
        r13.popContext();	 Catch:{ all -> 0x00a2 }
    L_0x0230:
        r0 = r13.config;	 Catch:{ all -> 0x00a2 }
        r0 = r0.getDeserializer(r2);	 Catch:{ all -> 0x00a2 }
        r14 = r0.deserialze(r13, r2, r15);	 Catch:{ all -> 0x00a2 }
        r13.setContext(r5);
        goto L_0x0017;
    L_0x023f:
        r4 = "$ref";
        if (r0 != r4) goto L_0x0320;
    L_0x0243:
        r0 = 4;
        r6.nextToken(r0);	 Catch:{ all -> 0x00a2 }
        r0 = r6.token();	 Catch:{ all -> 0x00a2 }
        r1 = 4;
        if (r0 != r1) goto L_0x02ff;
    L_0x024e:
        r2 = r6.stringVal();	 Catch:{ all -> 0x00a2 }
        r0 = 13;
        r6.nextToken(r0);	 Catch:{ all -> 0x00a2 }
        r0 = 0;
        r1 = "@";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x00a2 }
        if (r1 == 0) goto L_0x0295;
    L_0x0260:
        r1 = r13.getContext();	 Catch:{ all -> 0x00a2 }
        if (r1 == 0) goto L_0x02f3;
    L_0x0266:
        r2 = r13.getContext();	 Catch:{ all -> 0x00a2 }
        r1 = r2.getObject();	 Catch:{ all -> 0x00a2 }
        r3 = r1 instanceof java.lang.Object[];	 Catch:{ all -> 0x00a2 }
        if (r3 != 0) goto L_0x0276;
    L_0x0272:
        r3 = r1 instanceof java.util.Collection;	 Catch:{ all -> 0x00a2 }
        if (r3 == 0) goto L_0x0286;
    L_0x0276:
        r0 = r1;
    L_0x0277:
        r14 = r0;
    L_0x0278:
        r0 = r6.token();	 Catch:{ all -> 0x00a2 }
        if (r0 == r10) goto L_0x02f5;
    L_0x027e:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r1 = "syntax error";
        r0.<init>(r1);	 Catch:{ all -> 0x00a2 }
        throw r0;	 Catch:{ all -> 0x00a2 }
    L_0x0286:
        r1 = r2.getParentContext();	 Catch:{ all -> 0x00a2 }
        if (r1 == 0) goto L_0x0277;
    L_0x028c:
        r0 = r2.getParentContext();	 Catch:{ all -> 0x00a2 }
        r0 = r0.getObject();	 Catch:{ all -> 0x00a2 }
        goto L_0x0277;
    L_0x0295:
        r1 = "..";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x00a2 }
        if (r1 == 0) goto L_0x02ba;
    L_0x029d:
        r1 = r5.getParentContext();	 Catch:{ all -> 0x00a2 }
        r3 = r1.getObject();	 Catch:{ all -> 0x00a2 }
        if (r3 == 0) goto L_0x02ad;
    L_0x02a7:
        r0 = r1.getObject();	 Catch:{ all -> 0x00a2 }
    L_0x02ab:
        r14 = r0;
        goto L_0x0278;
    L_0x02ad:
        r3 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x00a2 }
        r3.<init>(r1, r2);	 Catch:{ all -> 0x00a2 }
        r13.addResolveTask(r3);	 Catch:{ all -> 0x00a2 }
        r1 = 1;
        r13.setResolveStatus(r1);	 Catch:{ all -> 0x00a2 }
        goto L_0x02ab;
    L_0x02ba:
        r1 = "$";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x00a2 }
        if (r1 == 0) goto L_0x02e7;
    L_0x02c2:
        r1 = r5;
    L_0x02c3:
        r3 = r1.getParentContext();	 Catch:{ all -> 0x00a2 }
        if (r3 == 0) goto L_0x02ce;
    L_0x02c9:
        r1 = r1.getParentContext();	 Catch:{ all -> 0x00a2 }
        goto L_0x02c3;
    L_0x02ce:
        r3 = r1.getObject();	 Catch:{ all -> 0x00a2 }
        if (r3 == 0) goto L_0x02da;
    L_0x02d4:
        r0 = r1.getObject();	 Catch:{ all -> 0x00a2 }
    L_0x02d8:
        r14 = r0;
        goto L_0x0278;
    L_0x02da:
        r3 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x00a2 }
        r3.<init>(r1, r2);	 Catch:{ all -> 0x00a2 }
        r13.addResolveTask(r3);	 Catch:{ all -> 0x00a2 }
        r1 = 1;
        r13.setResolveStatus(r1);	 Catch:{ all -> 0x00a2 }
        goto L_0x02d8;
    L_0x02e7:
        r1 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x00a2 }
        r1.<init>(r5, r2);	 Catch:{ all -> 0x00a2 }
        r13.addResolveTask(r1);	 Catch:{ all -> 0x00a2 }
        r1 = 1;
        r13.setResolveStatus(r1);	 Catch:{ all -> 0x00a2 }
    L_0x02f3:
        r14 = r0;
        goto L_0x0278;
    L_0x02f5:
        r0 = 16;
        r6.nextToken(r0);	 Catch:{ all -> 0x00a2 }
        r13.setContext(r5);
        goto L_0x0017;
    L_0x02ff:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
        r1.<init>();	 Catch:{ all -> 0x00a2 }
        r2 = "illegal ref, ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a2 }
        r2 = r6.token();	 Catch:{ all -> 0x00a2 }
        r2 = com.alibaba.fastjson.parser.JSONToken.name(r2);	 Catch:{ all -> 0x00a2 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a2 }
        r1 = r1.toString();	 Catch:{ all -> 0x00a2 }
        r0.<init>(r1);	 Catch:{ all -> 0x00a2 }
        throw r0;	 Catch:{ all -> 0x00a2 }
    L_0x0320:
        if (r1 != 0) goto L_0x04de;
    L_0x0322:
        r13.setContext(r14, r15);	 Catch:{ all -> 0x00a2 }
        r4 = r3;
    L_0x0326:
        r1 = r14.getClass();	 Catch:{ all -> 0x00a2 }
        r7 = com.alibaba.fastjson.JSONObject.class;
        if (r1 != r7) goto L_0x0332;
    L_0x032e:
        if (r0 != 0) goto L_0x036d;
    L_0x0330:
        r0 = "null";
    L_0x0332:
        if (r2 != r11) goto L_0x0372;
    L_0x0334:
        r6.scanString();	 Catch:{ all -> 0x00a2 }
        r1 = r6.stringVal();	 Catch:{ all -> 0x00a2 }
        r2 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat;	 Catch:{ all -> 0x00a2 }
        r2 = r6.isEnabled(r2);	 Catch:{ all -> 0x00a2 }
        if (r2 == 0) goto L_0x0359;
    L_0x0343:
        r2 = new com.alibaba.fastjson.parser.JSONScanner;	 Catch:{ all -> 0x00a2 }
        r2.<init>(r1);	 Catch:{ all -> 0x00a2 }
        r7 = r2.scanISO8601DateIfMatch();	 Catch:{ all -> 0x00a2 }
        if (r7 == 0) goto L_0x0356;
    L_0x034e:
        r1 = r2.getCalendar();	 Catch:{ all -> 0x00a2 }
        r1 = r1.getTime();	 Catch:{ all -> 0x00a2 }
    L_0x0356:
        r2.close();	 Catch:{ all -> 0x00a2 }
    L_0x0359:
        r14.put(r0, r1);	 Catch:{ all -> 0x00a2 }
    L_0x035c:
        r6.skipWhitespace();	 Catch:{ all -> 0x00a2 }
        r1 = r6.getCurrent();	 Catch:{ all -> 0x00a2 }
        r2 = 44;
        if (r1 != r2) goto L_0x04a2;
    L_0x0367:
        r6.next();	 Catch:{ all -> 0x00a2 }
        r1 = r4;
        goto L_0x0049;
    L_0x036d:
        r0 = r0.toString();	 Catch:{ all -> 0x00a2 }
        goto L_0x0332;
    L_0x0372:
        r1 = 48;
        if (r2 < r1) goto L_0x037a;
    L_0x0376:
        r1 = 57;
        if (r2 <= r1) goto L_0x037e;
    L_0x037a:
        r1 = 45;
        if (r2 != r1) goto L_0x039b;
    L_0x037e:
        r6.scanNumber();	 Catch:{ all -> 0x00a2 }
        r1 = r6.token();	 Catch:{ all -> 0x00a2 }
        r2 = 2;
        if (r1 != r2) goto L_0x0390;
    L_0x0388:
        r1 = r6.integerValue();	 Catch:{ all -> 0x00a2 }
    L_0x038c:
        r14.put(r0, r1);	 Catch:{ all -> 0x00a2 }
        goto L_0x035c;
    L_0x0390:
        r1 = com.alibaba.fastjson.parser.Feature.UseBigDecimal;	 Catch:{ all -> 0x00a2 }
        r1 = r13.isEnabled(r1);	 Catch:{ all -> 0x00a2 }
        r1 = r6.decimalValue(r1);	 Catch:{ all -> 0x00a2 }
        goto L_0x038c;
    L_0x039b:
        r1 = 91;
        if (r2 != r1) goto L_0x03cc;
    L_0x039f:
        r6.nextToken();	 Catch:{ all -> 0x00a2 }
        r1 = new com.alibaba.fastjson.JSONArray;	 Catch:{ all -> 0x00a2 }
        r1.<init>();	 Catch:{ all -> 0x00a2 }
        r13.parseArray(r1, r0);	 Catch:{ all -> 0x00a2 }
        r14.put(r0, r1);	 Catch:{ all -> 0x00a2 }
        r0 = r6.token();	 Catch:{ all -> 0x00a2 }
        if (r0 != r10) goto L_0x03bb;
    L_0x03b3:
        r6.nextToken();	 Catch:{ all -> 0x00a2 }
        r13.setContext(r5);
        goto L_0x0017;
    L_0x03bb:
        r0 = r6.token();	 Catch:{ all -> 0x00a2 }
        if (r0 != r9) goto L_0x03c4;
    L_0x03c1:
        r1 = r4;
        goto L_0x0049;
    L_0x03c4:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r1 = "syntax error";
        r0.<init>(r1);	 Catch:{ all -> 0x00a2 }
        throw r0;	 Catch:{ all -> 0x00a2 }
    L_0x03cc:
        r1 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        if (r2 != r1) goto L_0x044e;
    L_0x03d0:
        r6.nextToken();	 Catch:{ all -> 0x00a2 }
        if (r15 == 0) goto L_0x0421;
    L_0x03d5:
        r1 = r15.getClass();	 Catch:{ all -> 0x00a2 }
        r2 = java.lang.Integer.class;
        if (r1 != r2) goto L_0x0421;
    L_0x03dd:
        r2 = r3;
    L_0x03de:
        r7 = new com.alibaba.fastjson.JSONObject;	 Catch:{ all -> 0x00a2 }
        r7.<init>();	 Catch:{ all -> 0x00a2 }
        r1 = 0;
        if (r2 != 0) goto L_0x03ea;
    L_0x03e6:
        r1 = r13.setContext(r5, r7, r0);	 Catch:{ all -> 0x00a2 }
    L_0x03ea:
        r8 = r13.parseObject(r7, r0);	 Catch:{ all -> 0x00a2 }
        if (r1 == 0) goto L_0x03f5;
    L_0x03f0:
        if (r7 == r8) goto L_0x03f5;
    L_0x03f2:
        r1.setObject(r14);	 Catch:{ all -> 0x00a2 }
    L_0x03f5:
        r1 = r0.toString();	 Catch:{ all -> 0x00a2 }
        r13.checkMapResolve(r14, r1);	 Catch:{ all -> 0x00a2 }
        r1 = r14.getClass();	 Catch:{ all -> 0x00a2 }
        r7 = com.alibaba.fastjson.JSONObject.class;
        if (r1 != r7) goto L_0x0424;
    L_0x0404:
        r1 = r0.toString();	 Catch:{ all -> 0x00a2 }
        r14.put(r1, r8);	 Catch:{ all -> 0x00a2 }
    L_0x040b:
        if (r2 == 0) goto L_0x0410;
    L_0x040d:
        r13.setContext(r5, r8, r0);	 Catch:{ all -> 0x00a2 }
    L_0x0410:
        r0 = r6.token();	 Catch:{ all -> 0x00a2 }
        if (r0 != r10) goto L_0x0428;
    L_0x0416:
        r6.nextToken();	 Catch:{ all -> 0x00a2 }
        r13.setContext(r5);	 Catch:{ all -> 0x00a2 }
        r13.setContext(r5);
        goto L_0x0017;
    L_0x0421:
        r1 = 0;
        r2 = r1;
        goto L_0x03de;
    L_0x0424:
        r14.put(r0, r8);	 Catch:{ all -> 0x00a2 }
        goto L_0x040b;
    L_0x0428:
        r0 = r6.token();	 Catch:{ all -> 0x00a2 }
        if (r0 != r9) goto L_0x0431;
    L_0x042e:
        r1 = r4;
        goto L_0x0049;
    L_0x0431:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
        r1.<init>();	 Catch:{ all -> 0x00a2 }
        r2 = "syntax error, ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a2 }
        r2 = r6.tokenName();	 Catch:{ all -> 0x00a2 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x00a2 }
        r1 = r1.toString();	 Catch:{ all -> 0x00a2 }
        r0.<init>(r1);	 Catch:{ all -> 0x00a2 }
        throw r0;	 Catch:{ all -> 0x00a2 }
    L_0x044e:
        r6.nextToken();	 Catch:{ all -> 0x00a2 }
        r1 = r13.parse();	 Catch:{ all -> 0x00a2 }
        r2 = r14.getClass();	 Catch:{ all -> 0x00a2 }
        r7 = com.alibaba.fastjson.JSONObject.class;
        if (r2 != r7) goto L_0x0461;
    L_0x045d:
        r0 = r0.toString();	 Catch:{ all -> 0x00a2 }
    L_0x0461:
        r14.put(r0, r1);	 Catch:{ all -> 0x00a2 }
        r1 = r6.token();	 Catch:{ all -> 0x00a2 }
        if (r1 != r10) goto L_0x0472;
    L_0x046a:
        r6.nextToken();	 Catch:{ all -> 0x00a2 }
        r13.setContext(r5);
        goto L_0x0017;
    L_0x0472:
        r1 = r6.token();	 Catch:{ all -> 0x00a2 }
        if (r1 != r9) goto L_0x047b;
    L_0x0478:
        r1 = r4;
        goto L_0x0049;
    L_0x047b:
        r1 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
        r2.<init>();	 Catch:{ all -> 0x00a2 }
        r3 = "syntax error, position at ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r3 = r6.pos();	 Catch:{ all -> 0x00a2 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r3 = ", name ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r0 = r2.append(r0);	 Catch:{ all -> 0x00a2 }
        r0 = r0.toString();	 Catch:{ all -> 0x00a2 }
        r1.<init>(r0);	 Catch:{ all -> 0x00a2 }
        throw r1;	 Catch:{ all -> 0x00a2 }
    L_0x04a2:
        r2 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r1 != r2) goto L_0x04b7;
    L_0x04a6:
        r6.next();	 Catch:{ all -> 0x00a2 }
        r6.resetStringPosition();	 Catch:{ all -> 0x00a2 }
        r6.nextToken();	 Catch:{ all -> 0x00a2 }
        r13.setContext(r14, r15);	 Catch:{ all -> 0x00a2 }
        r13.setContext(r5);
        goto L_0x0017;
    L_0x04b7:
        r1 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00a2 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
        r2.<init>();	 Catch:{ all -> 0x00a2 }
        r3 = "syntax error, position at ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r3 = r6.pos();	 Catch:{ all -> 0x00a2 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r3 = ", name ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x00a2 }
        r0 = r2.append(r0);	 Catch:{ all -> 0x00a2 }
        r0 = r0.toString();	 Catch:{ all -> 0x00a2 }
        r1.<init>(r0);	 Catch:{ all -> 0x00a2 }
        throw r1;	 Catch:{ all -> 0x00a2 }
    L_0x04de:
        r4 = r1;
        goto L_0x0326;
    L_0x04e1:
        r0 = r1;
        goto L_0x0202;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(java.util.Map, java.lang.Object):java.lang.Object");
    }

    public ParserConfig getConfig() {
        return this.config;
    }

    public void setConfig(ParserConfig parserConfig) {
        this.config = parserConfig;
    }

    public <T> T parseObject(Class<T> cls) {
        return parseObject((Type) cls);
    }

    public <T> T parseObject(Type type) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        if (this.lexer.token() == 4) {
            type = TypeUtils.unwrap(type);
            if (type == byte[].class) {
                T bytesValue = this.lexer.bytesValue();
                this.lexer.nextToken();
                return bytesValue;
            } else if (type == char[].class) {
                String stringVal = this.lexer.stringVal();
                this.lexer.nextToken();
                return stringVal.toCharArray();
            }
        }
        try {
            return this.config.getDeserializer(type).deserialze(this, type, null);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable th) {
            JSONException jSONException = new JSONException(th.getMessage(), th);
        }
    }

    public <T> List<T> parseArray(Class<T> cls) {
        Collection arrayList = new ArrayList();
        parseArray((Class) cls, arrayList);
        return arrayList;
    }

    public void parseArray(Class<?> cls, Collection collection) {
        parseArray((Type) cls, collection);
    }

    public void parseArray(Type type, Collection collection) {
        parseArray(type, collection, null);
    }

    public void parseArray(Type type, Collection collection, Object obj) {
        if (this.lexer.token() == 21 || this.lexer.token() == 22) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() != 14) {
            throw new JSONException("exepct '[', but " + JSONToken.name(this.lexer.token()));
        }
        ObjectDeserializer objectDeserializer;
        if (Integer.TYPE == type) {
            objectDeserializer = IntegerCodec.instance;
            this.lexer.nextToken(2);
        } else if (String.class == type) {
            objectDeserializer = StringCodec.instance;
            this.lexer.nextToken(4);
        } else {
            objectDeserializer = this.config.getDeserializer(type);
            this.lexer.nextToken(objectDeserializer.getFastMatchToken());
        }
        ParseContext context = getContext();
        setContext(collection, obj);
        int i = 0;
        while (true) {
            if (isEnabled(Feature.AllowArbitraryCommas)) {
                while (this.lexer.token() == 16) {
                    this.lexer.nextToken();
                }
            }
            try {
                if (this.lexer.token() == 15) {
                    break;
                }
                if (Integer.TYPE == type) {
                    collection.add(IntegerCodec.instance.deserialze(this, null, null));
                } else if (String.class == type) {
                    if (this.lexer.token() == 4) {
                        r1 = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        r1 = parse();
                        if (r1 == null) {
                            r1 = null;
                        } else {
                            r1 = r1.toString();
                        }
                    }
                    collection.add(r1);
                } else {
                    if (this.lexer.token() == 8) {
                        this.lexer.nextToken();
                        r1 = null;
                    } else {
                        r1 = objectDeserializer.deserialze(this, type, Integer.valueOf(i));
                    }
                    collection.add(r1);
                    checkListResolve(collection);
                }
                if (this.lexer.token() == 16) {
                    this.lexer.nextToken(objectDeserializer.getFastMatchToken());
                }
                i++;
            } finally {
                setContext(context);
            }
        }
        this.lexer.nextToken(16);
    }

    public Object[] parseArray(Type[] typeArr) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken(16);
            return null;
        } else if (this.lexer.token() != 14) {
            throw new JSONException("syntax error : " + this.lexer.tokenName());
        } else {
            Object[] objArr = new Object[typeArr.length];
            if (typeArr.length == 0) {
                this.lexer.nextToken(15);
                if (this.lexer.token() != 15) {
                    throw new JSONException("syntax error");
                }
                this.lexer.nextToken(16);
                return new Object[0];
            }
            this.lexer.nextToken(2);
            int i = 0;
            while (i < typeArr.length) {
                String str;
                if (this.lexer.token() == 8) {
                    this.lexer.nextToken(16);
                    str = null;
                } else {
                    Type type = typeArr[i];
                    if (type == Integer.TYPE || type == Integer.class) {
                        if (this.lexer.token() == 2) {
                            str = Integer.valueOf(this.lexer.intValue());
                            this.lexer.nextToken(16);
                        } else {
                            str = TypeUtils.cast(parse(), type, this.config);
                        }
                    } else if (type != String.class) {
                        boolean isArray;
                        Type componentType;
                        if (i == typeArr.length - 1 && (type instanceof Class)) {
                            Class cls = (Class) type;
                            isArray = cls.isArray();
                            componentType = cls.getComponentType();
                        } else {
                            componentType = null;
                            isArray = false;
                        }
                        if (!isArray || this.lexer.token() == 14) {
                            str = this.config.getDeserializer(type).deserialze(this, type, null);
                        } else {
                            Object arrayList = new ArrayList();
                            ObjectDeserializer deserializer = this.config.getDeserializer(componentType);
                            int fastMatchToken = deserializer.getFastMatchToken();
                            if (this.lexer.token() != 15) {
                                while (true) {
                                    arrayList.add(deserializer.deserialze(this, type, null));
                                    if (this.lexer.token() != 16) {
                                        break;
                                    }
                                    this.lexer.nextToken(fastMatchToken);
                                }
                                if (this.lexer.token() != 15) {
                                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                                }
                            }
                            str = TypeUtils.cast(arrayList, type, this.config);
                        }
                    } else if (this.lexer.token() == 4) {
                        str = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        str = TypeUtils.cast(parse(), type, this.config);
                    }
                }
                objArr[i] = str;
                if (this.lexer.token() == 15) {
                    break;
                } else if (this.lexer.token() != 16) {
                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                } else {
                    if (i == typeArr.length - 1) {
                        this.lexer.nextToken(15);
                    } else {
                        this.lexer.nextToken(2);
                    }
                    i++;
                }
            }
            if (this.lexer.token() != 15) {
                throw new JSONException("syntax error");
            }
            this.lexer.nextToken(16);
            return objArr;
        }
    }

    public void parseObject(Object obj) {
        Class cls = obj.getClass();
        Map fieldDeserializers = this.config.getFieldDeserializers(cls);
        if (this.lexer.token() == 12 || this.lexer.token() == 16) {
            while (true) {
                FieldDeserializer fieldDeserializer;
                String scanSymbol = this.lexer.scanSymbol(this.symbolTable);
                if (scanSymbol == null) {
                    if (this.lexer.token() == 13) {
                        this.lexer.nextToken(16);
                        return;
                    } else if (this.lexer.token() == 16 && isEnabled(Feature.AllowArbitraryCommas)) {
                    }
                }
                FieldDeserializer fieldDeserializer2 = (FieldDeserializer) fieldDeserializers.get(scanSymbol);
                if (fieldDeserializer2 == null && scanSymbol != null) {
                    for (Entry entry : fieldDeserializers.entrySet()) {
                        if (scanSymbol.equalsIgnoreCase((String) entry.getKey())) {
                            fieldDeserializer = (FieldDeserializer) entry.getValue();
                            break;
                        }
                    }
                }
                fieldDeserializer = fieldDeserializer2;
                if (fieldDeserializer != null) {
                    Object deserialze;
                    Class fieldClass = fieldDeserializer.getFieldClass();
                    Type fieldType = fieldDeserializer.getFieldType();
                    if (fieldClass == Integer.TYPE) {
                        this.lexer.nextTokenWithColon(2);
                        deserialze = IntegerCodec.instance.deserialze(this, fieldType, null);
                    } else if (fieldClass == String.class) {
                        this.lexer.nextTokenWithColon(4);
                        deserialze = StringCodec.deserialze(this);
                    } else if (fieldClass == Long.TYPE) {
                        this.lexer.nextTokenWithColon(2);
                        deserialze = LongCodec.instance.deserialze(this, fieldType, null);
                    } else {
                        ObjectDeserializer deserializer = this.config.getDeserializer(fieldClass, fieldType);
                        this.lexer.nextTokenWithColon(deserializer.getFastMatchToken());
                        deserialze = deserializer.deserialze(this, fieldType, null);
                    }
                    fieldDeserializer.setValue(obj, deserialze);
                    if (this.lexer.token() != 16 && this.lexer.token() == 13) {
                        this.lexer.nextToken(16);
                        return;
                    }
                } else if (isEnabled(Feature.IgnoreNotMatch)) {
                    this.lexer.nextTokenWithColon();
                    parse();
                    if (this.lexer.token() == 13) {
                        this.lexer.nextToken();
                        return;
                    }
                } else {
                    throw new JSONException("setter not found, class " + cls.getName() + ", property " + scanSymbol);
                }
            }
        }
        throw new JSONException("syntax error, expect {, actual " + this.lexer.tokenName());
    }

    public Object parseArrayWithType(Type type) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        if (actualTypeArguments.length != 1) {
            throw new JSONException("not support type " + type);
        }
        Type type2 = actualTypeArguments[0];
        Collection arrayList;
        if (type2 instanceof Class) {
            arrayList = new ArrayList();
            parseArray((Class) type2, arrayList);
            return arrayList;
        } else if (type2 instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type2;
            Object obj = wildcardType.getUpperBounds()[0];
            if (!Object.class.equals(obj)) {
                r2 = new ArrayList();
                parseArray((Class) obj, r2);
                return r2;
            } else if (wildcardType.getLowerBounds().length == 0) {
                return parse();
            } else {
                throw new JSONException("not support type : " + type);
            }
        } else {
            if (type2 instanceof TypeVariable) {
                TypeVariable typeVariable = (TypeVariable) type2;
                Type[] bounds = typeVariable.getBounds();
                if (bounds.length != 1) {
                    throw new JSONException("not support : " + typeVariable);
                }
                Type type3 = bounds[0];
                if (type3 instanceof Class) {
                    r2 = new ArrayList();
                    parseArray((Class) type3, r2);
                    return r2;
                }
            }
            if (type2 instanceof ParameterizedType) {
                type2 = (ParameterizedType) type2;
                arrayList = new ArrayList();
                parseArray(type2, arrayList);
                return arrayList;
            }
            throw new JSONException("TODO : " + type);
        }
    }

    public int getResolveStatus() {
        return this.resolveStatus;
    }

    public void setResolveStatus(int i) {
        this.resolveStatus = i;
    }

    public Object getObject(String str) {
        for (int i = 0; i < this.contextArrayIndex; i++) {
            if (str.equals(this.contextArray[i].getPath())) {
                return this.contextArray[i].getObject();
            }
        }
        return null;
    }

    public void checkListResolve(Collection collection) {
        if (this.resolveStatus != 1) {
            return;
        }
        if (collection instanceof List) {
            int size = collection.size() - 1;
            List list = (List) collection;
            ResolveTask lastResolveTask = getLastResolveTask();
            lastResolveTask.setFieldDeserializer(new ListResolveFieldDeserializer(this, list, size));
            lastResolveTask.setOwnerContext(this.context);
            setResolveStatus(0);
            return;
        }
        ResolveTask lastResolveTask2 = getLastResolveTask();
        lastResolveTask2.setFieldDeserializer(new CollectionResolveFieldDeserializer(this, collection));
        lastResolveTask2.setOwnerContext(this.context);
        setResolveStatus(0);
    }

    public void checkMapResolve(Map map, String str) {
        if (this.resolveStatus == 1) {
            FieldDeserializer mapResolveFieldDeserializer = new MapResolveFieldDeserializer(map, str);
            ResolveTask lastResolveTask = getLastResolveTask();
            lastResolveTask.setFieldDeserializer(mapResolveFieldDeserializer);
            lastResolveTask.setOwnerContext(this.context);
            setResolveStatus(0);
        }
    }

    public Object parseObject(Map map) {
        return parseObject(map, null);
    }

    public JSONObject parseObject() {
        Map jSONObject = new JSONObject();
        parseObject(jSONObject);
        return jSONObject;
    }

    public final void parseArray(Collection collection) {
        parseArray(collection, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void parseArray(java.util.Collection r9, java.lang.Object r10) {
        /*
        r8 = this;
        r1 = 0;
        r0 = 0;
        r5 = 4;
        r7 = 16;
        r3 = r8.getLexer();
        r2 = r3.token();
        r4 = 21;
        if (r2 == r4) goto L_0x0019;
    L_0x0011:
        r2 = r3.token();
        r4 = 22;
        if (r2 != r4) goto L_0x001c;
    L_0x0019:
        r3.nextToken();
    L_0x001c:
        r2 = r3.token();
        r4 = 14;
        if (r2 == r4) goto L_0x0053;
    L_0x0024:
        r0 = new com.alibaba.fastjson.JSONException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "syntax error, expect [, actual ";
        r1 = r1.append(r2);
        r2 = r3.token();
        r2 = com.alibaba.fastjson.parser.JSONToken.name(r2);
        r1 = r1.append(r2);
        r2 = ", pos ";
        r1 = r1.append(r2);
        r2 = r3.pos();
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0053:
        r3.nextToken(r5);
        r4 = r8.getContext();
        r8.setContext(r9, r10);
        r2 = r0;
    L_0x005e:
        r0 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;	 Catch:{ all -> 0x0070 }
        r0 = r8.isEnabled(r0);	 Catch:{ all -> 0x0070 }
        if (r0 == 0) goto L_0x0075;
    L_0x0066:
        r0 = r3.token();	 Catch:{ all -> 0x0070 }
        if (r0 != r7) goto L_0x0075;
    L_0x006c:
        r3.nextToken();	 Catch:{ all -> 0x0070 }
        goto L_0x0066;
    L_0x0070:
        r0 = move-exception;
        r8.setContext(r4);
        throw r0;
    L_0x0075:
        r0 = r3.token();	 Catch:{ all -> 0x0070 }
        switch(r0) {
            case 2: goto L_0x0094;
            case 3: goto L_0x009e;
            case 4: goto L_0x00b7;
            case 5: goto L_0x007c;
            case 6: goto L_0x00df;
            case 7: goto L_0x00e7;
            case 8: goto L_0x010b;
            case 9: goto L_0x007c;
            case 10: goto L_0x007c;
            case 11: goto L_0x007c;
            case 12: goto L_0x00ef;
            case 13: goto L_0x007c;
            case 14: goto L_0x00fd;
            case 15: goto L_0x0119;
            case 16: goto L_0x007c;
            case 17: goto L_0x007c;
            case 18: goto L_0x007c;
            case 19: goto L_0x007c;
            case 20: goto L_0x0122;
            case 21: goto L_0x007c;
            case 22: goto L_0x007c;
            case 23: goto L_0x0112;
            default: goto L_0x007c;
        };	 Catch:{ all -> 0x0070 }
    L_0x007c:
        r0 = r8.parse();	 Catch:{ all -> 0x0070 }
    L_0x0080:
        r9.add(r0);	 Catch:{ all -> 0x0070 }
        r8.checkListResolve(r9);	 Catch:{ all -> 0x0070 }
        r0 = r3.token();	 Catch:{ all -> 0x0070 }
        if (r0 != r7) goto L_0x0090;
    L_0x008c:
        r0 = 4;
        r3.nextToken(r0);	 Catch:{ all -> 0x0070 }
    L_0x0090:
        r0 = r2 + 1;
        r2 = r0;
        goto L_0x005e;
    L_0x0094:
        r0 = r3.integerValue();	 Catch:{ all -> 0x0070 }
        r5 = 16;
        r3.nextToken(r5);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x009e:
        r0 = com.alibaba.fastjson.parser.Feature.UseBigDecimal;	 Catch:{ all -> 0x0070 }
        r0 = r3.isEnabled(r0);	 Catch:{ all -> 0x0070 }
        if (r0 == 0) goto L_0x00b1;
    L_0x00a6:
        r0 = 1;
        r0 = r3.decimalValue(r0);	 Catch:{ all -> 0x0070 }
    L_0x00ab:
        r5 = 16;
        r3.nextToken(r5);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x00b1:
        r0 = 0;
        r0 = r3.decimalValue(r0);	 Catch:{ all -> 0x0070 }
        goto L_0x00ab;
    L_0x00b7:
        r0 = r3.stringVal();	 Catch:{ all -> 0x0070 }
        r5 = 16;
        r3.nextToken(r5);	 Catch:{ all -> 0x0070 }
        r5 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat;	 Catch:{ all -> 0x0070 }
        r5 = r3.isEnabled(r5);	 Catch:{ all -> 0x0070 }
        if (r5 == 0) goto L_0x0080;
    L_0x00c8:
        r5 = new com.alibaba.fastjson.parser.JSONScanner;	 Catch:{ all -> 0x0070 }
        r5.<init>(r0);	 Catch:{ all -> 0x0070 }
        r6 = r5.scanISO8601DateIfMatch();	 Catch:{ all -> 0x0070 }
        if (r6 == 0) goto L_0x00db;
    L_0x00d3:
        r0 = r5.getCalendar();	 Catch:{ all -> 0x0070 }
        r0 = r0.getTime();	 Catch:{ all -> 0x0070 }
    L_0x00db:
        r5.close();	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x00df:
        r0 = java.lang.Boolean.TRUE;	 Catch:{ all -> 0x0070 }
        r5 = 16;
        r3.nextToken(r5);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x00e7:
        r0 = java.lang.Boolean.FALSE;	 Catch:{ all -> 0x0070 }
        r5 = 16;
        r3.nextToken(r5);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x00ef:
        r0 = new com.alibaba.fastjson.JSONObject;	 Catch:{ all -> 0x0070 }
        r0.<init>();	 Catch:{ all -> 0x0070 }
        r5 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x0070 }
        r0 = r8.parseObject(r0, r5);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x00fd:
        r0 = new com.alibaba.fastjson.JSONArray;	 Catch:{ all -> 0x0070 }
        r0.<init>();	 Catch:{ all -> 0x0070 }
        r5 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x0070 }
        r8.parseArray(r0, r5);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x010b:
        r0 = 4;
        r3.nextToken(r0);	 Catch:{ all -> 0x0070 }
        r0 = r1;
        goto L_0x0080;
    L_0x0112:
        r0 = 4;
        r3.nextToken(r0);	 Catch:{ all -> 0x0070 }
        r0 = r1;
        goto L_0x0080;
    L_0x0119:
        r0 = 16;
        r3.nextToken(r0);	 Catch:{ all -> 0x0070 }
        r8.setContext(r4);
        return;
    L_0x0122:
        r0 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0070 }
        r1 = "unclosed jsonArray";
        r0.<init>(r1);	 Catch:{ all -> 0x0070 }
        throw r0;	 Catch:{ all -> 0x0070 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseArray(java.util.Collection, java.lang.Object):void");
    }

    public ParseContext getContext() {
        return this.context;
    }

    public void addResolveTask(ResolveTask resolveTask) {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        this.resolveTaskList.add(resolveTask);
    }

    public ResolveTask getLastResolveTask() {
        return (ResolveTask) this.resolveTaskList.get(this.resolveTaskList.size() - 1);
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (this.extraProcessors == null) {
            this.extraProcessors = new ArrayList(2);
        }
        return this.extraProcessors;
    }

    public List<ExtraProcessor> getExtraProcessorsDirect() {
        return this.extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (this.extraTypeProviders == null) {
            this.extraTypeProviders = new ArrayList(2);
        }
        return this.extraTypeProviders;
    }

    public List<ExtraTypeProvider> getExtraTypeProvidersDirect() {
        return this.extraTypeProviders;
    }

    public void setContext(ParseContext parseContext) {
        if (!isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = parseContext;
        }
    }

    public void popContext() {
        if (!isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = this.context.getParentContext();
            this.contextArray[this.contextArrayIndex - 1] = null;
            this.contextArrayIndex--;
        }
    }

    public ParseContext setContext(Object obj, Object obj2) {
        if (isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        return setContext(this.context, obj, obj2);
    }

    public ParseContext setContext(ParseContext parseContext, Object obj, Object obj2) {
        if (isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        this.context = new ParseContext(parseContext, obj, obj2);
        addContext(this.context);
        return this.context;
    }

    private void addContext(ParseContext parseContext) {
        int i = this.contextArrayIndex;
        this.contextArrayIndex = i + 1;
        if (i >= this.contextArray.length) {
            Object obj = new ParseContext[((this.contextArray.length * 3) / 2)];
            System.arraycopy(this.contextArray, 0, obj, 0, this.contextArray.length);
            this.contextArray = obj;
        }
        this.contextArray[i] = parseContext;
    }

    public Object parse() {
        return parse(null);
    }

    public Object parseKey() {
        if (this.lexer.token() != 18) {
            return parse(null);
        }
        String stringVal = this.lexer.stringVal();
        this.lexer.nextToken(16);
        return stringVal;
    }

    public Object parse(Object obj) {
        JSONLexer lexer = getLexer();
        Object integerValue;
        Collection jSONArray;
        switch (lexer.token()) {
            case 2:
                integerValue = lexer.integerValue();
                lexer.nextToken();
                return integerValue;
            case 3:
                integerValue = lexer.decimalValue(isEnabled(Feature.UseBigDecimal));
                lexer.nextToken();
                return integerValue;
            case 4:
                integerValue = lexer.stringVal();
                lexer.nextToken(16);
                if (!lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    return integerValue;
                }
                JSONScanner jSONScanner = new JSONScanner(integerValue);
                try {
                    if (jSONScanner.scanISO8601DateIfMatch()) {
                        integerValue = jSONScanner.getCalendar().getTime();
                        return integerValue;
                    }
                    jSONScanner.close();
                    return integerValue;
                } finally {
                    jSONScanner.close();
                }
            case 6:
                lexer.nextToken();
                return Boolean.TRUE;
            case 7:
                lexer.nextToken();
                return Boolean.FALSE;
            case 8:
                lexer.nextToken();
                return null;
            case 9:
                lexer.nextToken(18);
                if (lexer.token() != 18) {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken(10);
                accept(10);
                long longValue = lexer.integerValue().longValue();
                accept(2);
                accept(11);
                return new Date(longValue);
            case 12:
                return parseObject(new JSONObject(), obj);
            case 14:
                jSONArray = new JSONArray();
                parseArray(jSONArray, obj);
                return jSONArray;
            case 20:
                if (lexer.isBlankInput()) {
                    return null;
                }
                throw new JSONException("unterminated json string, pos " + lexer.getBufferPosition());
            case 21:
                lexer.nextToken();
                jSONArray = new HashSet();
                parseArray(jSONArray, obj);
                return jSONArray;
            case 22:
                lexer.nextToken();
                jSONArray = new TreeSet();
                parseArray(jSONArray, obj);
                return jSONArray;
            case 23:
                lexer.nextToken();
                return null;
            default:
                throw new JSONException("syntax error, pos " + lexer.getBufferPosition());
        }
    }

    public void config(Feature feature, boolean z) {
        getLexer().config(feature, z);
    }

    public boolean isEnabled(Feature feature) {
        return getLexer().isEnabled(feature);
    }

    public JSONLexer getLexer() {
        return this.lexer;
    }

    public final void accept(int i) {
        JSONLexer lexer = getLexer();
        if (lexer.token() == i) {
            lexer.nextToken();
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(lexer.token()));
    }

    public final void accept(int i, int i2) {
        JSONLexer lexer = getLexer();
        if (lexer.token() == i) {
            lexer.nextToken(i2);
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(lexer.token()));
    }

    public void close() {
        JSONLexer lexer = getLexer();
        try {
            if (!isEnabled(Feature.AutoCloseSource) || lexer.token() == 20) {
                lexer.close();
                return;
            }
            throw new JSONException("not close json text, token : " + JSONToken.name(lexer.token()));
        } catch (Throwable th) {
            lexer.close();
        }
    }

    public void handleResovleTask(Object obj) {
        if (this.resolveTaskList != null) {
            int size = this.resolveTaskList.size();
            for (int i = 0; i < size; i++) {
                ResolveTask resolveTask = (ResolveTask) this.resolveTaskList.get(i);
                FieldDeserializer fieldDeserializer = resolveTask.getFieldDeserializer();
                if (fieldDeserializer != null) {
                    Object object;
                    Object obj2 = null;
                    if (resolveTask.getOwnerContext() != null) {
                        obj2 = resolveTask.getOwnerContext().getObject();
                    }
                    String referenceValue = resolveTask.getReferenceValue();
                    if (referenceValue.startsWith("$")) {
                        object = getObject(referenceValue);
                    } else {
                        object = resolveTask.getContext().getObject();
                    }
                    fieldDeserializer.setValue(obj2, object);
                }
            }
        }
    }
}
