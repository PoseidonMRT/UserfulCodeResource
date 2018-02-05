package com.huawei.hwid.openapi.quicklogin.d;

import android.util.Xml;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class e {
    public static void a(XmlSerializer xmlSerializer, String str, String str2) {
        if (str2 != null && xmlSerializer != null && str != null) {
            xmlSerializer.startTag(null, str).text(str2).endTag(null, str);
        }
    }

    public static XmlSerializer a(OutputStream outputStream) {
        XmlSerializer newSerializer = Xml.newSerializer();
        newSerializer.setOutput(outputStream, "UTF-8");
        return newSerializer;
    }

    public static XmlPullParser a(byte[] bArr) {
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new ByteArrayInputStream(bArr), "UTF-8");
        return newPullParser;
    }
}
