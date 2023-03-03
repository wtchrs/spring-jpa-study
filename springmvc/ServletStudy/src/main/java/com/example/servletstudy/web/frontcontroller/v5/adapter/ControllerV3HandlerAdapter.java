package com.example.servletstudy.web.frontcontroller.v5.adapter;

import com.example.servletstudy.web.frontcontroller.ModelView;
import com.example.servletstudy.web.frontcontroller.Params;
import com.example.servletstudy.web.frontcontroller.v3.ControllerV3;
import com.example.servletstudy.web.frontcontroller.v5.MyHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ControllerV3HandlerAdapter implements MyHandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof ControllerV3;
    }

    @Override
    public ModelView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("ControllerV3HandlerAdapter.handle");
        return ((ControllerV3) handler).process(Params.from(request));
    }
}
