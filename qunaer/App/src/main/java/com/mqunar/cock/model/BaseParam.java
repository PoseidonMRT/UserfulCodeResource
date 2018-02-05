package com.mqunar.cock.model;

import com.mqunar.cock.network.CockHandler;
import java.io.Serializable;

public class BaseParam implements Serializable {
    public String t;
    public String yid = CockHandler.getInstance().getYid();
}
