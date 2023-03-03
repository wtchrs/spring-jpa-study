package com.example.servletstudy.web.frontcontroller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ViewResolver {

    @Getter
    private static final ViewResolver defaultResolver = new ViewResolver("/WEB-INF/views/", ".jsp");

    private final String viewNamePrefix;
    private final String viewNameSuffix;

    public ViewResolver(String viewNamePrefix, String viewNameSuffix) {
        this.viewNamePrefix = viewNamePrefix;
        this.viewNameSuffix = viewNameSuffix;
    }

    public MyView resolve(ModelView mv) {
        log.info("ViewResolver.resolve");
        return new MyView(viewNamePrefix + mv.getViewName() + viewNameSuffix);
    }

    public MyView resolve(String viewName) {
        log.info("ViewResolver.resolve");
        return new MyView(viewNamePrefix + viewName + viewNameSuffix);
    }
}
