package com.example.servletstudy.web.servletmvc;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet(name = "mvcMemberSaveServlet", urlPatterns = "/servlet-mvc/members/save")
public class MvcMemberSaveServlet extends HttpServlet {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("MvcMemberSaveServlet.service");

        String username = req.getParameter("username");
        int age = Integer.parseInt(req.getParameter("age"));

        Member savedMember = memberRepository.save(new Member(username, age));
        log.info("savedMember = {}", savedMember);
        req.setAttribute("savedMember", savedMember);

        String viewPath = "/WEB-INF/views/save-result.jsp";
        log.info("viewPath = {}", viewPath);
        RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
        dispatcher.forward(req, resp);
    }
}
