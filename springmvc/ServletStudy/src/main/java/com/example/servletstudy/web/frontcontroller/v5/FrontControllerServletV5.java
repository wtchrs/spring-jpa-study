package com.example.servletstudy.web.frontcontroller.v5;

import com.example.servletstudy.web.frontcontroller.ModelView;
import com.example.servletstudy.web.frontcontroller.MyView;
import com.example.servletstudy.web.frontcontroller.ViewResolver;
import com.example.servletstudy.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.example.servletstudy.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.example.servletstudy.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import com.example.servletstudy.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.example.servletstudy.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.example.servletstudy.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import com.example.servletstudy.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import com.example.servletstudy.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

@Slf4j
@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final ViewResolver resolver = ViewResolver.getDefaultResolver();

    private final Map<String, Object> handlerMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());

        handlerMap.put("/front-controller/v5/v3-adapter/members/new-form", new MemberFormControllerV3());
        handlerMap.put("/front-controller/v5/v3-adapter/members/save", new MemberSaveControllerV3());
        handlerMap.put("/front-controller/v5/v3-adapter/members", new MemberListControllerV3());

        handlerMap.put("/front-controller/v5/v4-adapter/members/new-form", new MemberFormControllerV4());
        handlerMap.put("/front-controller/v5/v4-adapter/members/save", new MemberSaveControllerV4());
        handlerMap.put("/front-controller/v5/v4-adapter/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("FrontControllerServletV5.service");

        String requestURI = req.getRequestURI();
        Object handler = handlerMap.get(requestURI);

        if (handler == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = handlerAdapters
                .stream().filter(handlerAdapter -> handlerAdapter.supports(handler)).findFirst()
                .orElseThrow(() -> new IllegalStateException("No matched adapter for handler '" + handler + "'"));

        ModelView modelView = adapter.handle(handler, req, resp);
        MyView view = resolver.resolve(modelView);
        view.render(modelView.getModel(), req, resp);
    }
}
