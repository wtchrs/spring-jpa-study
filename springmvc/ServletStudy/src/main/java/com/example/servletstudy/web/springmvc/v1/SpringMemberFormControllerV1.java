package com.example.servletstudy.web.springmvc.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class SpringMemberFormControllerV1 {

    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView newForm() {
        log.info("SpringMemberFormControllerV1.newForm");
        return new ModelAndView("new-form");
    }
}
