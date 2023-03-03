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
import java.util.List;

@Slf4j
@WebServlet(name = "memberListServlet", urlPatterns = "/servlet/members")
public class MemberListServlet extends HttpServlet {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("MemberListServlet.service");

        List<Member> memberList = memberRepository.findAll();

        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n" +
                       "<html>\n" +
                       "<head>\n" +
                       "<meta charset=\"UTF-8\">\n" +
                       "</head>\n" +
                       "<body>\n" +
                       "<a href=\"/index.html\">Home</a>\n" +
                       "Member List\n" +
                       "<table>\n" +
                       "<thead>\n" +
                       "<th>id</th>\n" +
                       "<th>username</th>\n" +
                       "<th>age</th>\n" +
                       "</thead>\n" +
                       "<tbody>\n");

        for (Member member : memberList) {
            builder.append("<tr>\n" +
                           "<td>\n")
                   .append(member.getId())
                   .append("</td>\n" +
                           "<td>\n")
                   .append(member.getUsername())
                   .append("</td>\n" +
                           "<td>\n")
                   .append(member.getAge())
                   .append("</td>\n" +
                           "</tr>\n");
        }

        builder.append("</tbody>\n" +
                       "</table>\n" +
                       "</body>\n" +
                       "</html>");

        resp.getWriter().write(builder.toString());
    }
}
