package com.example.servletstudy.web.servlet;

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
@WebServlet(name = "memberFormServlet", urlPatterns = "/servlet/members/new-form")
public class MemberFormServlet extends HttpServlet {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("MemberFormServlet.service");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter writer = resp.getWriter();
        writer.write("""
                         <!DOCTYPE html>
                         <html>
                         <head>
                            <meta charset="UTF-8">
                            <title>Title</title>
                         </head>
                         <body>
                         <form action="/servlet/members/save" method="post">
                            username: <input type="text" name="username">
                            age: <input type="text" name="age">
                            <button type="submit">Submit</button>
                         </form>
                         </body>
                         </html>
                         """);
    }
}
