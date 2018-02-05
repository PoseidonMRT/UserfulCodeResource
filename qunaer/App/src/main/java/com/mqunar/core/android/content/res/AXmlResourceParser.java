package com.mqunar.core.android.content.res;

import android.content.res.XmlResourceParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.xmlpull.v1.XmlPullParserException;

public class AXmlResourceParser implements XmlResourceParser {
    private c a;
    private boolean b = false;
    private d c;
    private int[] d;
    private a e = new a();
    private boolean f;
    private int g;
    private int h;
    private int i;
    private int j;
    private int[] k;
    private int l;
    private int m;
    private int n;

    public AXmlResourceParser() {
        a();
    }

    public void open(InputStream inputStream) {
        close();
        if (inputStream != null) {
            this.a = new c(inputStream, false);
        }
    }

    public void close() {
        if (this.b) {
            this.b = false;
            this.a.a();
            this.a = null;
            this.c = null;
            this.d = null;
            this.e.a();
            a();
        }
    }

    public int next() {
        if (this.a == null) {
            throw new XmlPullParserException("Parser is not opened.", this, null);
        }
        try {
            b();
            return this.g;
        } catch (IOException e) {
            close();
            throw e;
        }
    }

    public int nextToken() {
        return next();
    }

    public int nextTag() {
        int next = next();
        if (next == 4 && isWhitespace()) {
            next = next();
        }
        if (next == 2 || next == 3) {
            return next;
        }
        throw new XmlPullParserException("Expected start or end tag.", this, null);
    }

    public String nextText() {
        if (getEventType() != 2) {
            throw new XmlPullParserException("Parser must be on START_TAG to read next text.", this, null);
        }
        int next = next();
        if (next == 4) {
            String text = getText();
            if (next() == 3) {
                return text;
            }
            throw new XmlPullParserException("Event TEXT must be immediately followed by END_TAG.", this, null);
        } else if (next == 3) {
            return "";
        } else {
            throw new XmlPullParserException("Parser must be on START_TAG or TEXT to read text.", this, null);
        }
    }

    public void require(int i, String str, String str2) {
        if (i != getEventType() || ((str != null && !str.equals(getNamespace())) || (str2 != null && !str2.equals(getName())))) {
            throw new XmlPullParserException(TYPES[i] + " is expected.", this, null);
        }
    }

    public int getDepth() {
        return this.e.d() - 1;
    }

    public int getEventType() {
        return this.g;
    }

    public int getLineNumber() {
        return this.h;
    }

    public String getName() {
        if (this.i == -1 || (this.g != 2 && this.g != 3)) {
            return null;
        }
        return this.c.a(this.i);
    }

    public String getText() {
        if (this.i == -1 || this.g != 4) {
            return null;
        }
        return this.c.a(this.i);
    }

    public char[] getTextCharacters(int[] iArr) {
        String text = getText();
        if (text == null) {
            return null;
        }
        iArr[0] = 0;
        iArr[1] = text.length();
        char[] cArr = new char[text.length()];
        text.getChars(0, text.length(), cArr, 0);
        return cArr;
    }

    public String getNamespace() {
        return this.c.a(this.j);
    }

    public String getPrefix() {
        return this.c.a(this.e.d(this.j));
    }

    public String getPositionDescription() {
        return "XML line #" + getLineNumber();
    }

    public int getNamespaceCount(int i) {
        return this.e.a(i);
    }

    public String getNamespacePrefix(int i) {
        return this.c.a(this.e.b(i));
    }

    public String getNamespaceUri(int i) {
        return this.c.a(this.e.c(i));
    }

    public String getClassAttribute() {
        if (this.m == -1) {
            return null;
        }
        return this.c.a(this.k[a(this.m) + 2]);
    }

    public String getIdAttribute() {
        if (this.l == -1) {
            return null;
        }
        return this.c.a(this.k[a(this.l) + 2]);
    }

    public int getIdAttributeResourceValue(int i) {
        if (this.l == -1) {
            return i;
        }
        int a = a(this.l);
        return this.k[a + 3] == 1 ? this.k[a + 4] : i;
    }

    public int getStyleAttribute() {
        if (this.n == -1) {
            return 0;
        }
        return this.k[a(this.n) + 4];
    }

    public int getAttributeCount() {
        if (this.g != 2) {
            return -1;
        }
        return this.k.length / 5;
    }

    public String getAttributeNamespace(int i) {
        int i2 = this.k[a(i) + 0];
        if (i2 == -1) {
            return "";
        }
        return this.c.a(i2);
    }

    public String getAttributePrefix(int i) {
        int d = this.e.d(this.k[a(i) + 0]);
        if (d == -1) {
            return "";
        }
        return this.c.a(d);
    }

    public String getAttributeName(int i) {
        int i2 = this.k[a(i) + 1];
        if (i2 == -1) {
            return "";
        }
        return this.c.a(i2);
    }

    public int getAttributeNameResource(int i) {
        int i2 = this.k[a(i) + 1];
        if (this.d == null || i2 < 0 || i2 >= this.d.length) {
            return 0;
        }
        return this.d[i2];
    }

    public int getAttributeValueType(int i) {
        return this.k[a(i) + 3];
    }

    public int getAttributeValueData(int i) {
        return this.k[a(i) + 4];
    }

    public String getAttributeValue(int i) {
        int a = a(i);
        if (this.k[a + 3] == 3) {
            return this.c.a(this.k[a + 2]);
        }
        a = this.k[a + 4];
        return "";
    }

    public boolean getAttributeBooleanValue(int i, boolean z) {
        return getAttributeIntValue(i, z ? 1 : 0) != 0;
    }

    public float getAttributeFloatValue(int i, float f) {
        int a = a(i);
        if (this.k[a + 3] == 4) {
            return Float.intBitsToFloat(this.k[a + 4]);
        }
        return f;
    }

    public int getAttributeIntValue(int i, int i2) {
        int a = a(i);
        int i3 = this.k[a + 3];
        if (i3 < 16 || i3 > 31) {
            return i2;
        }
        return this.k[a + 4];
    }

    public int getAttributeUnsignedIntValue(int i, int i2) {
        return getAttributeIntValue(i, i2);
    }

    public int getAttributeResourceValue(int i, int i2) {
        int a = a(i);
        if (this.k[a + 3] == 1) {
            return this.k[a + 4];
        }
        return i2;
    }

    public String getAttributeValue(String str, String str2) {
        int a = a(str, str2);
        if (a == -1) {
            return null;
        }
        return getAttributeValue(a);
    }

    public boolean getAttributeBooleanValue(String str, String str2, boolean z) {
        int a = a(str, str2);
        return a == -1 ? z : getAttributeBooleanValue(a, z);
    }

    public float getAttributeFloatValue(String str, String str2, float f) {
        int a = a(str, str2);
        return a == -1 ? f : getAttributeFloatValue(a, f);
    }

    public int getAttributeIntValue(String str, String str2, int i) {
        int a = a(str, str2);
        return a == -1 ? i : getAttributeIntValue(a, i);
    }

    public int getAttributeUnsignedIntValue(String str, String str2, int i) {
        int a = a(str, str2);
        return a == -1 ? i : getAttributeUnsignedIntValue(a, i);
    }

    public int getAttributeResourceValue(String str, String str2, int i) {
        int a = a(str, str2);
        return a == -1 ? i : getAttributeResourceValue(a, i);
    }

    public int getAttributeListValue(int i, String[] strArr, int i2) {
        return 0;
    }

    public int getAttributeListValue(String str, String str2, String[] strArr, int i) {
        return 0;
    }

    public String getAttributeType(int i) {
        return "CDATA";
    }

    public boolean isAttributeDefault(int i) {
        return false;
    }

    public void setInput(InputStream inputStream, String str) {
        throw new XmlPullParserException("Method is not supported.");
    }

    public void setInput(Reader reader) {
        throw new XmlPullParserException("Method is not supported.");
    }

    public String getInputEncoding() {
        return null;
    }

    public int getColumnNumber() {
        return -1;
    }

    public boolean isEmptyElementTag() {
        return false;
    }

    public boolean isWhitespace() {
        return false;
    }

    public void defineEntityReplacementText(String str, String str2) {
        throw new XmlPullParserException("Method is not supported.");
    }

    public String getNamespace(String str) {
        throw new RuntimeException("Method is not supported.");
    }

    public Object getProperty(String str) {
        return null;
    }

    public void setProperty(String str, Object obj) {
        throw new XmlPullParserException("Method is not supported.");
    }

    public boolean getFeature(String str) {
        return false;
    }

    public void setFeature(String str, boolean z) {
        throw new XmlPullParserException("Method is not supported.");
    }

    private final int a(int i) {
        if (this.g != 2) {
            throw new IndexOutOfBoundsException("Current event is not START_TAG.");
        }
        int i2 = i * 5;
        if (i2 < this.k.length) {
            return i2;
        }
        throw new IndexOutOfBoundsException("Invalid attribute index (" + i + ").");
    }

    private final int a(String str, String str2) {
        if (this.c == null || str2 == null) {
            return -1;
        }
        int a = this.c.a(str2);
        if (a == -1) {
            return -1;
        }
        int a2 = str != null ? this.c.a(str) : -1;
        int i = 0;
        while (i != this.k.length) {
            if (a == this.k[i + 1] && (a2 == -1 || a2 == this.k[i + 0])) {
                return i / 5;
            }
            i += 5;
        }
        return -1;
    }

    private final void a() {
        this.g = -1;
        this.h = -1;
        this.i = -1;
        this.j = -1;
        this.k = null;
        this.l = -1;
        this.m = -1;
        this.n = -1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void b() {
        /*
        r9 = this;
        r8 = 0;
        r2 = 1048834; // 0x100102 float:1.46973E-39 double:5.18193E-318;
        r7 = 1048832; // 0x100100 float:1.469727E-39 double:5.18192E-318;
        r0 = 3;
        r6 = 1;
        r1 = r9.c;
        if (r1 != 0) goto L_0x0029;
    L_0x000d:
        r1 = r9.a;
        r3 = 524291; // 0x80003 float:7.34688E-40 double:2.59034E-318;
        com.mqunar.core.android.content.res.b.a(r1, r3);
        r1 = r9.a;
        r1.c();
        r1 = r9.a;
        r1 = com.mqunar.core.android.content.res.d.a(r1);
        r9.c = r1;
        r1 = r9.e;
        r1.e();
        r9.b = r6;
    L_0x0029:
        r1 = r9.g;
        if (r1 != r6) goto L_0x002e;
    L_0x002d:
        return;
    L_0x002e:
        r3 = r9.g;
        r9.a();
    L_0x0033:
        r1 = r9.f;
        if (r1 == 0) goto L_0x003e;
    L_0x0037:
        r9.f = r8;
        r1 = r9.e;
        r1.f();
    L_0x003e:
        if (r3 != r0) goto L_0x0053;
    L_0x0040:
        r1 = r9.e;
        r1 = r1.d();
        if (r1 != r6) goto L_0x0053;
    L_0x0048:
        r1 = r9.e;
        r1 = r1.b();
        if (r1 != 0) goto L_0x0053;
    L_0x0050:
        r9.g = r6;
        goto L_0x002d;
    L_0x0053:
        if (r3 != 0) goto L_0x0088;
    L_0x0055:
        r1 = r2;
    L_0x0056:
        r4 = 524672; // 0x80180 float:7.35222E-40 double:2.592224E-318;
        if (r1 != r4) goto L_0x009c;
    L_0x005b:
        r1 = r9.a;
        r1 = r1.b();
        r4 = 8;
        if (r1 < r4) goto L_0x0069;
    L_0x0065:
        r4 = r1 % 4;
        if (r4 == 0) goto L_0x008f;
    L_0x0069:
        r0 = new java.io.IOException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Invalid resource ids size (";
        r2 = r2.append(r3);
        r1 = r2.append(r1);
        r2 = ").";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0088:
        r1 = r9.a;
        r1 = r1.b();
        goto L_0x0056;
    L_0x008f:
        r4 = r9.a;
        r1 = r1 / 4;
        r1 = r1 + -2;
        r1 = r4.b(r1);
        r9.d = r1;
        goto L_0x0033;
    L_0x009c:
        if (r1 < r7) goto L_0x00a3;
    L_0x009e:
        r4 = 1048836; // 0x100104 float:1.469732E-39 double:5.18194E-318;
        if (r1 <= r4) goto L_0x00c2;
    L_0x00a3:
        r0 = new java.io.IOException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Invalid chunk type (";
        r2 = r2.append(r3);
        r1 = r2.append(r1);
        r2 = ").";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x00c2:
        if (r1 != r2) goto L_0x00cb;
    L_0x00c4:
        r4 = -1;
        if (r3 != r4) goto L_0x00cb;
    L_0x00c7:
        r9.g = r8;
        goto L_0x002d;
    L_0x00cb:
        r4 = r9.a;
        r4.c();
        r4 = r9.a;
        r4 = r4.b();
        r5 = r9.a;
        r5.c();
        if (r1 == r7) goto L_0x00e2;
    L_0x00dd:
        r5 = 1048833; // 0x100101 float:1.469728E-39 double:5.181924E-318;
        if (r1 != r5) goto L_0x0108;
    L_0x00e2:
        if (r1 != r7) goto L_0x00f7;
    L_0x00e4:
        r1 = r9.a;
        r1 = r1.b();
        r4 = r9.a;
        r4 = r4.b();
        r5 = r9.e;
        r5.a(r1, r4);
        goto L_0x0033;
    L_0x00f7:
        r1 = r9.a;
        r1.c();
        r1 = r9.a;
        r1.c();
        r1 = r9.e;
        r1.c();
        goto L_0x0033;
    L_0x0108:
        r9.h = r4;
        if (r1 != r2) goto L_0x0171;
    L_0x010c:
        r1 = r9.a;
        r1 = r1.b();
        r9.j = r1;
        r1 = r9.a;
        r1 = r1.b();
        r9.i = r1;
        r1 = r9.a;
        r1.c();
        r1 = r9.a;
        r1 = r1.b();
        r2 = r1 >>> 16;
        r2 = r2 + -1;
        r9.l = r2;
        r2 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r1 = r1 & r2;
        r2 = r9.a;
        r2 = r2.b();
        r9.m = r2;
        r2 = r9.m;
        r2 = r2 >>> 16;
        r2 = r2 + -1;
        r9.n = r2;
        r2 = r9.m;
        r3 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r2 = r2 & r3;
        r2 = r2 + -1;
        r9.m = r2;
        r2 = r9.a;
        r1 = r1 * 5;
        r1 = r2.b(r1);
        r9.k = r1;
    L_0x0155:
        r1 = r9.k;
        r1 = r1.length;
        if (r0 >= r1) goto L_0x0167;
    L_0x015a:
        r1 = r9.k;
        r2 = r9.k;
        r2 = r2[r0];
        r2 = r2 >>> 24;
        r1[r0] = r2;
        r0 = r0 + 5;
        goto L_0x0155;
    L_0x0167:
        r0 = r9.e;
        r0.e();
        r0 = 2;
        r9.g = r0;
        goto L_0x002d;
    L_0x0171:
        r4 = 1048835; // 0x100103 float:1.469731E-39 double:5.181933E-318;
        if (r1 != r4) goto L_0x018c;
    L_0x0176:
        r1 = r9.a;
        r1 = r1.b();
        r9.j = r1;
        r1 = r9.a;
        r1 = r1.b();
        r9.i = r1;
        r9.g = r0;
        r9.f = r6;
        goto L_0x002d;
    L_0x018c:
        r4 = 1048836; // 0x100104 float:1.469732E-39 double:5.18194E-318;
        if (r1 != r4) goto L_0x0033;
    L_0x0191:
        r0 = r9.a;
        r0 = r0.b();
        r9.i = r0;
        r0 = r9.a;
        r0.c();
        r0 = r9.a;
        r0.c();
        r0 = 4;
        r9.g = r0;
        goto L_0x002d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mqunar.core.android.content.res.AXmlResourceParser.b():void");
    }
}
