package com.example.servletstudy.web.frontcontroller.v3.controller;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import com.example.servletstudy.web.frontcontroller.ModelView;
import com.example.servletstudy.web.frontcontroller.Params;
import com.example.servletstudy.web.frontcontroller.v3.ControllerV3;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MemberListControllerV3 implements ControllerV3 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Params params) {
        log.info("MemberListControllerV3.process");

        List<Member> members = memberRepository.findAll();

        ModelView modelView = new ModelView("memberList");
        modelView.getModel().setAttribute("members", members);
        return modelView;
    }
}
