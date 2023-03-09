package com.example.servletstudy.web.frontcontroller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelView {

    private String viewName;
//    private final Map<String, Object> model = new HashMap<>();
    private final Model model;

    public ModelView(String viewName) {
        this.viewName = viewName;
        this.model = new Model();
    }

    public ModelView(String viewName, Model model) {
        this.viewName = viewName;
        this.model = model;
    }
}
