package com.mqunar.necro.agent.instrumentation;

import com.mqunar.contacts.basis.model.Contact;
import java.util.HashMap;
import java.util.Map;

public enum MetricCategory {
    NONE("None"),
    VIEW_LOADING("View Loading"),
    VIEW_LAYOUT("Layout"),
    DATABASE("Database"),
    IMAGE("Images"),
    JSON("JSON"),
    NETWORK("Network");
    
    private static final Map<String, MetricCategory> methodMap = null;
    private String categoryName;

    static {
        methodMap = new HashMap() {
            {
                put("onCreate", MetricCategory.VIEW_LOADING);
            }
        };
    }

    private MetricCategory(String str) {
        this.categoryName = str;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public static MetricCategory categoryForMethod(String str) {
        if (str == null) {
            return NONE;
        }
        Object obj = null;
        int indexOf = str.indexOf(Contact.NUMBER);
        if (indexOf >= 0) {
            obj = str.substring(indexOf + 1);
        }
        MetricCategory metricCategory = (MetricCategory) methodMap.get(obj);
        if (metricCategory == null) {
            return NONE;
        }
        return metricCategory;
    }
}
