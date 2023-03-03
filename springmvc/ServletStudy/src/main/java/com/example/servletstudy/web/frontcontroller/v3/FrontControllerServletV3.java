package com.example.servletstudy.web.frontcontroller.v3;

import com.example.servletstudy.web.frontcontroller.ModelView;
import com.example.servletstudy.web.frontcontroller.MyView;
import com.example.servletstudy.web.frontcontroller.Params;
import com.example.servletstudy.web.frontcontroller.ViewResolver;
import com.example.servletstudy.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.example.servletstudy.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.example.servletstudy.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private final Map<String, ControllerV3> controllerMap = new HashMap<>();
    private final ViewResolver viewResolver = ViewResolver.getDefaultResolver();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("FrontControllerServletV3.service");

        String requestURI = req.getRequestURI();
        log.info("requestURI = {}", requestURI);

        ControllerV3 controller = controllerMap.get(requestURI);
        log.info("controller = {}", controller);
        if (controller == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ModelView modelView = controller.process(Params.from(req));
        MyView view = viewResolver.resolve(modelView);
        view.render(modelView.getModel(), req, resp);
    }
}
