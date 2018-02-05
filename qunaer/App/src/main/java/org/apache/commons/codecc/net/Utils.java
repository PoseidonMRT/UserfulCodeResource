package org.apache.commons.codecc.net;

import org.apache.commons.codecc.DecoderException;

class Utils {
    Utils() {
    }

    static int digit16(byte b) {
        int digit = Character.digit((char) b, 16);
        if (digit != -1) {
            return digit;
        }
        throw new DecoderException("Invalid URL encoding: not a valid digit (radix 16): " + b);
    }
}
