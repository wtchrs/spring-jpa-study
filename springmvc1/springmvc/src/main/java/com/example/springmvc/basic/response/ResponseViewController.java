package com.example.springmvc.basic.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        log.info("ResponseViewController.responseViewV1");
        return new ModelAndView("response/hello").addObject("data", "hello");
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        log.info("ResponseViewController.responseViewV2");
        model.addAttribute("data", "hello");
        return "response/hello";
    }

    /**
     * Not recommended.
     */
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        log.info("ResponseViewController.responseViewV3");
        model.addAttribute("data", "hello");
    }
}
