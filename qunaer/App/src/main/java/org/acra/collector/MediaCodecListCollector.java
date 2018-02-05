package org.acra.collector;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecList;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

final class MediaCodecListCollector {
    private static final String[] a = new String[]{"mp4", "mpeg4", "MP4", "MPEG4"};
    private static final String[] b = new String[]{"avc", "h264", "AVC", "H264"};
    private static final String[] c = new String[]{"h263", "H263"};
    private static final String[] d = new String[]{"aac", "AAC"};
    private static final SparseArray<String> e = new SparseArray();
    private static final SparseArray<String> f = new SparseArray();
    private static final SparseArray<String> g = new SparseArray();
    private static final SparseArray<String> h = new SparseArray();
    private static final SparseArray<String> i = new SparseArray();
    private static final SparseArray<String> j = new SparseArray();
    private static final SparseArray<String> k = new SparseArray();
    private static final SparseArray<String> l = new SparseArray();

    enum CodecType {
        AVC,
        H263,
        MPEG4,
        AAC
    }

    static {
        int i = 0;
        try {
            for (Field field : Class.forName("android.media.MediaCodecInfo$CodecCapabilities").getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getName().startsWith("COLOR_")) {
                    e.put(field.getInt(null), field.getName());
                }
            }
            Field[] fields = Class.forName("android.media.MediaCodecInfo$CodecProfileLevel").getFields();
            int length = fields.length;
            while (i < length) {
                Field field2 = fields[i];
                if (Modifier.isStatic(field2.getModifiers()) && Modifier.isFinal(field2.getModifiers())) {
                    if (field2.getName().startsWith("AVCLevel")) {
                        f.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("AVCProfile")) {
                        g.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("H263Level")) {
                        h.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("H263Profile")) {
                        i.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("MPEG4Level")) {
                        j.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("MPEG4Profile")) {
                        k.put(field2.getInt(null), field2.getName());
                    } else if (field2.getName().startsWith("AAC")) {
                        l.put(field2.getInt(null), field2.getName());
                    }
                }
                i++;
            }
        } catch (ClassNotFoundException e) {
        } catch (SecurityException e2) {
        } catch (IllegalAccessException e3) {
        } catch (IllegalArgumentException e4) {
        }
    }

    @NonNull
    public static String a() {
        if (VERSION.SDK_INT < 16) {
            return "";
        }
        MediaCodecInfo[] mediaCodecInfoArr;
        int i;
        if (VERSION.SDK_INT < 21) {
            int codecCount;
            codecCount = MediaCodecList.getCodecCount();
            mediaCodecInfoArr = new MediaCodecInfo[codecCount];
            for (i = 0; i < codecCount; i++) {
                mediaCodecInfoArr[i] = MediaCodecList.getCodecInfoAt(i);
            }
        } else {
            mediaCodecInfoArr = new MediaCodecList(1).getCodecInfos();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (i = 0; i < mediaCodecInfoArr.length; i++) {
            MediaCodecInfo mediaCodecInfo = mediaCodecInfoArr[i];
            stringBuilder.append('\n').append(i).append(": ").append(mediaCodecInfo.getName()).append('\n').append("isEncoder: ").append(mediaCodecInfo.isEncoder()).append('\n');
            String[] supportedTypes = mediaCodecInfo.getSupportedTypes();
            stringBuilder.append("Supported types: ").append(Arrays.toString(supportedTypes)).append('\n');
            for (String a : supportedTypes) {
                stringBuilder.append(a(mediaCodecInfo, a));
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    @TargetApi(16)
    @NonNull
    private static String a(@NonNull MediaCodecInfo mediaCodecInfo, @NonNull String str) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
        int[] iArr = capabilitiesForType.colorFormats;
        if (iArr.length > 0) {
            stringBuilder.append(str).append(" color formats:");
            for (int i2 = 0; i2 < iArr.length; i2++) {
                stringBuilder.append((String) e.get(iArr[i2]));
                if (i2 < iArr.length - 1) {
                    stringBuilder.append(',');
                }
            }
            stringBuilder.append('\n');
        }
        CodecType a = a(mediaCodecInfo);
        CodecProfileLevel[] codecProfileLevelArr = capabilitiesForType.profileLevels;
        if (codecProfileLevelArr.length > 0) {
            stringBuilder.append(str).append(" profile levels:");
            while (i < codecProfileLevelArr.length) {
                int i3 = codecProfileLevelArr[i].profile;
                int i4 = codecProfileLevelArr[i].level;
                if (a == null) {
                    stringBuilder.append(i3).append('-').append(i4);
                    stringBuilder.append('\n');
                } else {
                    switch (a) {
                        case AVC:
                            stringBuilder.append(i3).append((String) g.get(i3)).append('-').append((String) f.get(i4));
                            break;
                        case H263:
                            stringBuilder.append((String) i.get(i3)).append('-').append((String) h.get(i4));
                            break;
                        case MPEG4:
                            stringBuilder.append((String) k.get(i3)).append('-').append((String) j.get(i4));
                            break;
                        case AAC:
                            stringBuilder.append((String) l.get(i3));
                            break;
                    }
                    if (i < codecProfileLevelArr.length - 1) {
                        stringBuilder.append(',');
                    }
                    i++;
                }
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.append('\n').toString();
    }

    @Nullable
    @TargetApi(16)
    private static CodecType a(@NonNull MediaCodecInfo mediaCodecInfo) {
        int i = 0;
        String name = mediaCodecInfo.getName();
        for (CharSequence contains : b) {
            if (name.contains(contains)) {
                return CodecType.AVC;
            }
        }
        for (CharSequence contains2 : c) {
            if (name.contains(contains2)) {
                return CodecType.H263;
            }
        }
        for (CharSequence contains22 : a) {
            if (name.contains(contains22)) {
                return CodecType.MPEG4;
            }
        }
        String[] strArr = d;
        int length = strArr.length;
        while (i < length) {
            if (name.contains(strArr[i])) {
                return CodecType.AAC;
            }
            i++;
        }
        return null;
    }
}
