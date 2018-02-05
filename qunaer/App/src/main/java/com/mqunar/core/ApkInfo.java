package com.mqunar.core;

import android.content.IntentFilter;
import java.util.ArrayList;
import java.util.List;

public class ApkInfo {
    public String applicationClassName;
    public List<Component> components;
    public String fileName;
    public String launcherClassName;
    public List<IntentFilter> logicIntentFilters;
    public String packageName;
    public int versionCode;

    public void addComponents(String str, List<IntentFilter> list) {
        if (this.components == null) {
            this.components = new ArrayList();
        }
        Component component = new Component();
        component.name = str;
        component.intentFilters = list;
        this.components.add(component);
    }
}
