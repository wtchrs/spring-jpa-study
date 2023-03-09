package com.example.servletstudy.web.frontcontroller;

import jakarta.servlet.ServletRequest;

import java.util.Iterator;
import java.util.Map;

public class Params {
    private final Map<String, String[]> parameterMap;

    public static Params from(ServletRequest request) {
        return new Params(request.getParameterMap());
    }

    public Params(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getParameter(String paramName) {
        return parameterMap.get(paramName)[0];
    }

    public String[] getParameterValues(String paramName) {
        return parameterMap.get(paramName);
    }

    public Iterator<String> getParameterNames() {
        return parameterMap.keySet().iterator();
    }
}
