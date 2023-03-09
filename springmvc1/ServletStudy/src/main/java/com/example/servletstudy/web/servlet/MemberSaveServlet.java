package com.example.servletstudy.web.servlet;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@WebServlet(name = "memberSaveServlet", urlPatterns = "/servlet/members/save")
public class MemberSaveServlet extends HttpServlet {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("MemberSaveServlet.service");

        String username = req.getParameter("username");
        int age = Integer.parseInt(req.getParameter("age"));
        Member savedMember = memberRepository.save(new Member(username, age));
        log.info("savedMember = {}", savedMember);

        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter writer = resp.getWriter();
        writer.write("<!DOCTYPE html>\n" +
                     "<html>\n" +
                     "<head>\n" +
                     "   <meta charset=\"UTF-8\">\n" +
                     "</head>\n" +
                     "<body>\n" +
                     "Success\n" +
                     "<ul>\n" +
                     "   <li>id=" + savedMember.getId() + "</li>\n" +
                     "   <li>username=" + savedMember.getUsername() + "</li>\n" +
                     "   <li>age=" + savedMember.getAge() + "</li>\n" +
                     "</ul>\n" +
                     "<a href=\"/index.html\">Home</a>" +
                     "</body>\n" +
                     "</html>");
    }
}
