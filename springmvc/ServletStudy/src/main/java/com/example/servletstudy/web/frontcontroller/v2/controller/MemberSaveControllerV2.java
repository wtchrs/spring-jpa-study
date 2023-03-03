package com.example.servletstudy.web.frontcontroller.v2.controller;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import com.example.servletstudy.web.frontcontroller.v2.ControllerV2;
import com.example.servletstudy.web.frontcontroller.MyView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberSaveControllerV2 implements ControllerV2 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) {
        log.info("MemberSaveControllerV2.process");

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member savedMember = memberRepository.save(new Member(username, age));
        log.info("savedMember = {}", savedMember);
        request.setAttribute("savedMember", savedMember);

        return new MyView("/WEB-INF/views/save-result.jsp");
    }
}
