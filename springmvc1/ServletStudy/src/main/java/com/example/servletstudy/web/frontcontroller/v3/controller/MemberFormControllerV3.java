package com.example.servletstudy.web.frontcontroller.v3.controller;

import com.example.servletstudy.web.frontcontroller.ModelView;
import com.example.servletstudy.web.frontcontroller.Params;
import com.example.servletstudy.web.frontcontroller.v3.ControllerV3;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberFormControllerV3 implements ControllerV3 {

    @Override
    public ModelView process(Params paramMap) {
        log.info("MemberFormControllerV3.process");
        return new ModelView("new-form");
    }
}
