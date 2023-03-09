package com.example.springmvc.basic.request;

import com.example.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("RequestParamController.requestParamV1");
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username = {}, age = {}", username, age);

        response.getWriter().write("ok");
    }

    @RequestMapping("/request-param-v2")
    @ResponseBody
    public String requestParamV2(@RequestParam("username") String username, @RequestParam("age") int age) {
        log.info("RequestParamController.requestParamV2");
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    @RequestMapping("/request-param-v3")
    @ResponseBody
    public String requestParamV3(@RequestParam String username, @RequestParam int age) {
        log.info("RequestParamController.requestParamV3");
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    @RequestMapping("/request-param-v4")
    @ResponseBody
    public String requestParamV4(String username, int age) {
        log.info("RequestParamController.requestParamV4");
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    @RequestMapping("/request-param-required")
    @ResponseBody
    public String requestParamRequired(@RequestParam String username,
                                       // (default)required = true, but it can be called with an empty string.
                                       @RequestParam(required = false) Integer age) {
        log.info("RequestParamController.requestParamRequired");
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    @RequestMapping("/request-param-default")
    @ResponseBody
    public String requestParamDefaultValue(@RequestParam(defaultValue = "guest") String username,
                                           // if set defaultValue, empty string -> "guest"
                                           // and required option do nothing.
                                           @RequestParam(required = false, defaultValue = "-1") Integer age) {
        log.info("RequestParamController.requestParamDefaultValue");
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    @RequestMapping("/request-param-map")
    @ResponseBody
    public String requestParamMap(@RequestParam Map<String, String> paramMap) {
        log.info("RequestParamController.requestParamMap");
        log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    @RequestMapping("/model-attribute-v1")
    @ResponseBody
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("RequestParamController.modelAttributeV1");
        log.info("helloData = {}", helloData);

        return "ok";
    }

    @RequestMapping("/model-attribute-v2")
    @ResponseBody
    public String modelAttributeV2(HelloData helloData) {
        log.info("RequestParamController.modelAttributeV2");
        log.info("helloData = {}", helloData);

        return "ok";
    }
}
