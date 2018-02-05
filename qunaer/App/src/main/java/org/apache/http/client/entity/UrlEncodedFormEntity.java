package org.apache.http.client.entity;

import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;

@Deprecated
public class UrlEncodedFormEntity extends StringEntity {
    public UrlEncodedFormEntity(List<? extends NameValuePair> list, String str) {
        super((String) null);
        throw new RuntimeException("Stub!");
    }

    public UrlEncodedFormEntity(List<? extends NameValuePair> list) {
        super((String) null);
        throw new RuntimeException("Stub!");
    }
}
