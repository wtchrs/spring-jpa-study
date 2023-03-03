package com.example.servletstudy.web.frontcontroller.v4.controller;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import com.example.servletstudy.web.frontcontroller.Model;
import com.example.servletstudy.web.frontcontroller.Params;
import com.example.servletstudy.web.frontcontroller.v4.ControllerV4;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MemberListControllerV4 implements ControllerV4 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Params params, Model model) {
        List<Member> members = memberRepository.findAll();
        model.setAttribute("members", members);
        return "memberList";
    }
}
