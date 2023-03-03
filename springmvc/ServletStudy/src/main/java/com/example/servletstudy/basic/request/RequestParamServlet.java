package com.example.servletstudy.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("RequestParamServlet.service");

        log.info("[All parameter lookup] - START");
        req.getParameterNames().asIterator()
           .forEachRemaining(paramName -> log.info("{}: {}", paramName, req.getParameter(paramName)));
        log.info("[All parameter lookup] - END");

        log.info("[Single parameter lookup] - START");
        String username = req.getParameter("username");
        String age = req.getParameter("age");
        log.info("username: {}", username);
        log.info("age: {}", age);
        log.info("[Single parameter lookup] - END");

        log.info("[values with same key lookup] - START");
        String[] usernames = req.getParameterValues("username");
        log.info("usernames = {}", Arrays.toString(usernames));
        log.info("[values with same key lookup] - END");

        resp.getWriter().write("ok");
    }
}
