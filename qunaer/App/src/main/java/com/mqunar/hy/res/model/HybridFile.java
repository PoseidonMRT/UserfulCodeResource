package com.mqunar.hy.res.model;

import com.mqunar.hy.res.model.HybridManifest.ResItem;
import com.mqunar.hy.res.utils.QHepburnMimeTypeUtils;
import java.io.Serializable;

public class HybridFile implements Serializable {
    private static final long serialVersionUID = 1;
    public int length;
    public String mimeType;
    public int start;

    public HybridFile(ResItem resItem, int i) {
        String[] split = resItem.getSl().split(",");
        this.start = Integer.parseInt(split[0]) + i;
        this.length = Integer.parseInt(split[1]);
        this.mimeType = QHepburnMimeTypeUtils.obtainMimeType(resItem.getUrl());
    }
}
