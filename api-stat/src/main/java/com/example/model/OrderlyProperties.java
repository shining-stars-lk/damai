package com.example.model;

import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Orderly Properties
 * create an orderly Properties by extending Properties
 */
public class OrderlyProperties extends Properties {
    private final LinkedHashSet<String> propertyNames = new LinkedHashSet<String>();

    @Override
    public Set<String> stringPropertyNames() {
        return propertyNames;
    }



    @Override
    public synchronized Object put(Object key, Object value) {
        propertyNames.add(key+"");
        return super.put(key, value);
    }
}
