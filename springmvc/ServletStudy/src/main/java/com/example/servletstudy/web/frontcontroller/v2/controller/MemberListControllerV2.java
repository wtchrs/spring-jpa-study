package com.example.servletstudy.web.frontcontroller.v2.controller;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import com.example.servletstudy.web.frontcontroller.v2.ControllerV2;
import com.example.servletstudy.web.frontcontroller.MyView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MemberListControllerV2 implements ControllerV2 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) {
        log.info("MemberListControllerV2.process");

        List<Member> members = memberRepository.findAll();
        request.setAttribute("members", members);
        log.info("members = {}", members);

        return new MyView("/WEB-INF/views/memberList.jsp");
    }
}
