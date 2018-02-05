package org.apache.commons.codecc.language;

import org.apache.commons.codecc.EncoderException;
import org.apache.commons.codecc.StringEncoder;

public abstract class AbstractCaverphone implements StringEncoder {
    public Object encode(Object obj) {
        if (obj instanceof String) {
            return encode((String) obj);
        }
        throw new EncoderException("Parameter supplied to Caverphone encode is not of type java.lang.String");
    }

    public boolean isEncodeEqual(String str, String str2) {
        return encode(str).equals(encode(str2));
    }
}
