package com.example.servletstudy.web.frontcontroller.v4.controller;

import com.example.servletstudy.web.frontcontroller.Model;
import com.example.servletstudy.web.frontcontroller.Params;
import com.example.servletstudy.web.frontcontroller.v4.ControllerV4;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberFormControllerV4 implements ControllerV4 {

    @Override
    public String process(Params params, Model model) {
        return "new-form";
    }
}
