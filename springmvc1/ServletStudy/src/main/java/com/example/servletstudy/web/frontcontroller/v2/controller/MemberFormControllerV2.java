package com.example.servletstudy.web.frontcontroller.v2.controller;

import com.example.servletstudy.web.frontcontroller.v2.ControllerV2;
import com.example.servletstudy.web.frontcontroller.MyView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberFormControllerV2 implements ControllerV2 {

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) {
        log.info("MemberFormControllerV2.process");
        return new MyView("/WEB-INF/views/new-form.jsp");
    }
}
