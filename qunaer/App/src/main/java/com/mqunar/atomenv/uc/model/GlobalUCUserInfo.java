package com.mqunar.atomenv.uc.model;

import java.io.Serializable;

public class GlobalUCUserInfo implements Serializable {
    public static final long serialVersionUID = 1;
    public GlobalUCookie UCookie;
    public int avOrOrderdetailFlag = 0;
    public String displayName;
    public String email = "";
    public GlobalUCDisplayAndValue emailObj;
    public String exptime = "";
    public String imageurl;
    public int isObtainVerifyCode = 0;
    public int isactive = 0;
    public int loginStatus = -1;
    public int loginT = 0;
    public String modifyPhone = "";
    public String paramData = "";
    public String phone = "";
    public GlobalUCDisplayAndValue phoneObj;
    public String prenum = "";
    public String pwd = "";
    public int status = -1;
    public String uname = "";
    public String userid = "";
    public String uuid = "";
    public String verifyOrCheckCode = "";
    public int verifyStatus = -1;

    public String getUname() {
        return this.uname;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getPrenum() {
        return this.prenum;
    }

    public String getPhone() {
        return this.phone;
    }

    public GlobalUCDisplayAndValue getPhoneObj() {
        return this.phoneObj;
    }

    public String getEmail() {
        return this.email;
    }

    public GlobalUCDisplayAndValue getEmailObj() {
        return this.emailObj;
    }

    public int getIsactive() {
        return this.isactive;
    }

    public String getPwd() {
        return this.pwd;
    }

    public int getStatus() {
        return this.status;
    }

    public int getVerifyStatus() {
        return this.verifyStatus;
    }

    public int getIsObtainVerifyCode() {
        return this.isObtainVerifyCode;
    }

    public String getModifyPhone() {
        return this.modifyPhone;
    }

    public String getExptime() {
        return this.exptime;
    }

    public int getLoginStatus() {
        return this.loginStatus;
    }

    public int getAvOrOrderdetailFlag() {
        return this.avOrOrderdetailFlag;
    }

    public int getLoginT() {
        return this.loginT;
    }

    public String getParamData() {
        return this.paramData;
    }

    public String getVerifyOrCheckCode() {
        return this.verifyOrCheckCode;
    }

    public String getUserid() {
        return this.userid;
    }

    public GlobalUCookie getUCookie() {
        return this.UCookie;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getImageurl() {
        return this.imageurl;
    }
}
