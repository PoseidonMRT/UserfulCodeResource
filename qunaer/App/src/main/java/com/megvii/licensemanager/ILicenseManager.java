package com.megvii.licensemanager;

public interface ILicenseManager {
    long checkCachedLicense();

    String getContext(String str);

    String getVersion();

    long setLicense(String str);
}
