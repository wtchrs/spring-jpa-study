package com.example.servletstudy.web.frontcontroller.v4.controller;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import com.example.servletstudy.web.frontcontroller.Model;
import com.example.servletstudy.web.frontcontroller.Params;
import com.example.servletstudy.web.frontcontroller.v4.ControllerV4;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberSaveControllerV4 implements ControllerV4 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Params params, Model model) {
        log.info("MemberSaveControllerV4.process");

        String username = params.getParameter("username");
        int age = Integer.parseInt(params.getParameter("age"));

        Member savedMember = memberRepository.save(new Member(username, age));
        log.info("savedMember = {}", savedMember);

        model.setAttribute("savedMember", savedMember);
        return "save-result";
    }
}
