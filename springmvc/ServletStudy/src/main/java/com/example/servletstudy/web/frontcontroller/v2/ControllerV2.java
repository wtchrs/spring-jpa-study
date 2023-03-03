package com.example.servletstudy.web.frontcontroller.v2;

import com.example.servletstudy.web.frontcontroller.MyView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ControllerV2 {

    MyView process(HttpServletRequest request, HttpServletResponse response);
}
