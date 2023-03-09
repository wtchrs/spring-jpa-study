package com.example.servletstudy.web.frontcontroller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Model {

    private final Map<String, Object> modelMap = new HashMap<>();

    public void setAttribute(String attributeName, Object value) {
        modelMap.put(attributeName, value);
    }

    public Object getAttribute(String attributeName) {
        return modelMap.get(attributeName);
    }

    public Iterator<String> getAttributeNames() {
        return modelMap.keySet().iterator();
    }

    public void removeAttribute(String attributeName) {
        modelMap.remove(attributeName);
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return modelMap.entrySet();
    }
}
