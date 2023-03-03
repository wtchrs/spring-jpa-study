package com.example.servletstudy.web.frontcontroller.v1.controller;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import com.example.servletstudy.web.frontcontroller.v1.ControllerV1;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class MemberListControllerV1 implements ControllerV1 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("MemberListControllerV1.process");

        List<Member> members = memberRepository.findAll();
        request.setAttribute("members", members);
        log.info("members = {}", members);

        String viewPath = "/WEB-INF/views/memberList.jsp";
        log.info("viewPath = {}", viewPath);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
