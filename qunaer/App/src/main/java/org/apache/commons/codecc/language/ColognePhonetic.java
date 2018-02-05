package org.apache.commons.codecc.language;

import java.util.Locale;
import org.apache.commons.codecc.EncoderException;
import org.apache.commons.codecc.StringEncoder;

public class ColognePhonetic implements StringEncoder {
    private static final char[][] PREPROCESS_MAP = new char[][]{new char[]{'Ä', 'A'}, new char[]{'Ü', 'U'}, new char[]{'Ö', 'O'}, new char[]{'ß', 'S'}};

    abstract class CologneBuffer {
        protected final char[] data;
        protected int length = 0;

        protected abstract char[] copyData(int i, int i2);

        public CologneBuffer(char[] cArr) {
            this.data = cArr;
            this.length = cArr.length;
        }

        public CologneBuffer(int i) {
            this.data = new char[i];
            this.length = 0;
        }

        public int length() {
            return this.length;
        }

        public String toString() {
            return new String(copyData(0, this.length));
        }
    }

    class CologneInputBuffer extends CologneBuffer {
        public CologneInputBuffer(char[] cArr) {
            super(cArr);
        }

        public void addLeft(char c) {
            this.length++;
            this.data[getNextPos()] = c;
        }

        protected char[] copyData(int i, int i2) {
            Object obj = new char[i2];
            System.arraycopy(this.data, (this.data.length - this.length) + i, obj, 0, i2);
            return obj;
        }

        public char getNextChar() {
            return this.data[getNextPos()];
        }

        protected int getNextPos() {
            return this.data.length - this.length;
        }

        public char removeNext() {
            char nextChar = getNextChar();
            this.length--;
            return nextChar;
        }
    }

    class CologneOutputBuffer extends CologneBuffer {
        public CologneOutputBuffer(int i) {
            super(i);
        }

        public void addRight(char c) {
            this.data[this.length] = c;
            this.length++;
        }

        protected char[] copyData(int i, int i2) {
            Object obj = new char[i2];
            System.arraycopy(this.data, i, obj, 0, i2);
            return obj;
        }
    }

    private static boolean arrayContains(char[] cArr, char c) {
        for (char c2 : cArr) {
            if (c2 == c) {
                return true;
            }
        }
        return false;
    }

    public String colognePhonetic(String str) {
        if (str == null) {
            return null;
        }
        String preprocess = preprocess(str);
        CologneOutputBuffer cologneOutputBuffer = new CologneOutputBuffer(preprocess.length() * 2);
        CologneInputBuffer cologneInputBuffer = new CologneInputBuffer(preprocess.toCharArray());
        int length = cologneInputBuffer.length();
        char c = '/';
        char c2 = '-';
        while (length > 0) {
            char nextChar;
            char removeNext = cologneInputBuffer.removeNext();
            length = cologneInputBuffer.length();
            if (length > 0) {
                nextChar = cologneInputBuffer.getNextChar();
            } else {
                nextChar = '-';
            }
            if (arrayContains(new char[]{'A', 'E', 'I', 'J', 'O', 'U', 'Y'}, removeNext)) {
                nextChar = '0';
            } else if (removeNext == 'H' || removeNext < 'A' || removeNext > 'Z') {
                if (c != '/') {
                    nextChar = '-';
                }
            } else if (removeNext == 'B' || (removeNext == 'P' && nextChar != 'H')) {
                nextChar = '1';
            } else if ((removeNext == 'D' || removeNext == 'T') && !arrayContains(new char[]{'S', 'C', 'Z'}, nextChar)) {
                nextChar = '2';
            } else if (arrayContains(new char[]{'W', 'F', 'P', 'V'}, removeNext)) {
                nextChar = '3';
            } else if (arrayContains(new char[]{'G', 'K', 'Q'}, removeNext)) {
                nextChar = '4';
            } else if (removeNext == 'X' && !arrayContains(new char[]{'C', 'K', 'Q'}, c2)) {
                cologneInputBuffer.addLeft('S');
                length++;
                nextChar = '4';
            } else if (removeNext == 'S' || removeNext == 'Z') {
                nextChar = '8';
            } else if (removeNext == 'C') {
                if (c == '/') {
                    if (arrayContains(new char[]{'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X'}, nextChar)) {
                        nextChar = '4';
                    } else {
                        nextChar = '8';
                    }
                } else if (arrayContains(new char[]{'S', 'Z'}, c2) || !arrayContains(new char[]{'A', 'H', 'O', 'U', 'K', 'Q', 'X'}, nextChar)) {
                    nextChar = '8';
                } else {
                    nextChar = '4';
                }
            } else if (arrayContains(new char[]{'T', 'D', 'X'}, removeNext)) {
                nextChar = '8';
            } else if (removeNext == 'R') {
                nextChar = '7';
            } else if (removeNext == 'L') {
                nextChar = '5';
            } else if (removeNext == 'M' || removeNext == 'N') {
                nextChar = '6';
            } else {
                nextChar = removeNext;
            }
            if (nextChar != '-' && ((c != nextChar && (nextChar != '0' || c == '/')) || nextChar < '0' || nextChar > '8')) {
                cologneOutputBuffer.addRight(nextChar);
            }
            c = nextChar;
            c2 = removeNext;
        }
        return cologneOutputBuffer.toString();
    }

    public Object encode(Object obj) {
        if (obj instanceof String) {
            return encode((String) obj);
        }
        throw new EncoderException("This method’s parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + obj.getClass().getName() + ".");
    }

    public String encode(String str) {
        return colognePhonetic(str);
    }

    public boolean isEncodeEqual(String str, String str2) {
        return colognePhonetic(str).equals(colognePhonetic(str2));
    }

    private String preprocess(String str) {
        char[] toCharArray = str.toUpperCase(Locale.GERMAN).toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] > 'Z') {
                for (int i2 = 0; i2 < PREPROCESS_MAP.length; i2++) {
                    if (toCharArray[i] == PREPROCESS_MAP[i2][0]) {
                        toCharArray[i] = PREPROCESS_MAP[i2][1];
                        break;
                    }
                }
            }
        }
        return new String(toCharArray);
    }
}
