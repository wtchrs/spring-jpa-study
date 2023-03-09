package com.example.servletstudy.web.frontcontroller.v3.controller;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import com.example.servletstudy.web.frontcontroller.ModelView;
import com.example.servletstudy.web.frontcontroller.Params;
import com.example.servletstudy.web.frontcontroller.v3.ControllerV3;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberSaveControllerV3 implements ControllerV3 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Params params) {
        log.info("MemberSaveControllerV3.process");

        String username = params.getParameter("username");
        int age = Integer.parseInt(params.getParameter("age"));

        Member savedMember = memberRepository.save(new Member(username, age));
        log.info("savedMember = {}", savedMember);

        ModelView modelView = new ModelView("save-result");
        modelView.getModel().setAttribute("savedMember", savedMember);

        return modelView;
    }
}
