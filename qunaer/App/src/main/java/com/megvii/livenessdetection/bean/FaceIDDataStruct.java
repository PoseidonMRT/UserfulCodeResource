package com.megvii.livenessdetection.bean;

import java.util.HashMap;
import java.util.Map;

public class FaceIDDataStruct {
    public String delta;
    public Map<String, byte[]> images = new HashMap();
}
