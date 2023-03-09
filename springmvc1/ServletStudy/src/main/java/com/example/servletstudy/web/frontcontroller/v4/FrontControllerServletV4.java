package com.example.servletstudy.web.frontcontroller.v4;

import com.example.servletstudy.web.frontcontroller.*;
import com.example.servletstudy.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.example.servletstudy.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.example.servletstudy.web.frontcontroller.v4.controller.MemberSaveControllerV4;
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
@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private final Map<String, ControllerV4> controllerMap = new HashMap<>();
    private final ViewResolver viewResolver = ViewResolver.getDefaultResolver();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("FrontControllerServletV4.service");

        String requestURI = req.getRequestURI();
        log.info("requestURI = {}", requestURI);

        ControllerV4 controller = controllerMap.get(requestURI);
        log.info("controller = {}", controller);

        if (controller == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Params params = Params.from(req);
        Model model = new Model();

        String viewName = controller.process(params, model);
        MyView view = viewResolver.resolve(viewName);
        view.render(model, req, resp);
    }
}
