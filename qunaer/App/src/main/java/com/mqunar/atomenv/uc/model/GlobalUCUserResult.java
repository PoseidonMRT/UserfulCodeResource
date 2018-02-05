package com.mqunar.atomenv.uc.model;

import java.io.Serializable;

public class GlobalUCUserResult implements Serializable {
    public static final String TAG = "GlobalUCUserResult";
    private static final long serialVersionUID = 1;
    private GlobalUserData data;

    public class GlobalUserData implements Serializable {
        private static final long serialVersionUID = 1;
        private GlobalUCUserInfo user = null;

        public GlobalUCUserInfo getUinfo() {
            return this.user;
        }

        public void setUinfo(GlobalUCUserInfo globalUCUserInfo) {
            this.user = globalUCUserInfo;
        }

        public void setUser(GlobalUCUserInfo globalUCUserInfo) {
            this.user = globalUCUserInfo;
        }
    }

    public GlobalUserData getData() {
        return this.data;
    }

    public void setData(GlobalUserData globalUserData) {
        this.data = globalUserData;
    }
}
