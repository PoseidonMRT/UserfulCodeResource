package com.mqunar.cock.model;

import java.io.Serializable;

public class BaseResult implements Serializable {
    public Bstatus bstatus = new Bstatus();

    public class Bstatus implements Serializable {
        public int code = Integer.MIN_VALUE;
        public String des;
    }
}
