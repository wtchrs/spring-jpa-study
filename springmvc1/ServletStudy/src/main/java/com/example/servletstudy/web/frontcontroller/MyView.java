package com.example.servletstudy.web.frontcontroller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class MyView {

    private final String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("MyView.render");
        log.info("viewPath = {}", viewPath);
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    public void render(Model model, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        log.info("MyView.render");
        log.info("viewPath = {}", viewPath);
        request.getRequestDispatcher(viewPath).forward(request, response);
    }
}
