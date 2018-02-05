package okhttp3.internal.http2;

import java.util.Arrays;

public final class Settings {
    static final int COUNT = 10;
    static final int DEFAULT_INITIAL_WINDOW_SIZE = 65535;
    static final int ENABLE_PUSH = 2;
    static final int HEADER_TABLE_SIZE = 1;
    static final int INITIAL_WINDOW_SIZE = 7;
    static final int MAX_CONCURRENT_STREAMS = 4;
    static final int MAX_FRAME_SIZE = 5;
    static final int MAX_HEADER_LIST_SIZE = 6;
    private int set;
    private final int[] values = new int[10];

    void clear() {
        this.set = 0;
        Arrays.fill(this.values, 0);
    }

    Settings set(int i, int i2) {
        if (i < this.values.length) {
            this.set = (1 << i) | this.set;
            this.values[i] = i2;
        }
        return this;
    }

    boolean isSet(int i) {
        if (((1 << i) & this.set) != 0) {
            return true;
        }
        return false;
    }

    int get(int i) {
        return this.values[i];
    }

    int size() {
        return Integer.bitCount(this.set);
    }

    int getHeaderTableSize() {
        return (2 & this.set) != 0 ? this.values[1] : -1;
    }

    boolean getEnablePush(boolean z) {
        int i;
        if ((4 & this.set) != 0) {
            i = this.values[2];
        } else if (z) {
            boolean z2 = true;
        } else {
            i = 0;
        }
        if (i == 1) {
            return true;
        }
        return false;
    }

    int getMaxConcurrentStreams(int i) {
        return (16 & this.set) != 0 ? this.values[4] : i;
    }

    int getMaxFrameSize(int i) {
        return (32 & this.set) != 0 ? this.values[5] : i;
    }

    int getMaxHeaderListSize(int i) {
        return (64 & this.set) != 0 ? this.values[6] : i;
    }

    int getInitialWindowSize() {
        return (128 & this.set) != 0 ? this.values[7] : 65535;
    }

    void merge(Settings settings) {
        for (int i = 0; i < 10; i++) {
            if (settings.isSet(i)) {
                set(i, settings.get(i));
            }
        }
    }
}
