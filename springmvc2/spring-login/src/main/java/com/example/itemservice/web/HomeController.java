package com.example.itemservice.web;

import com.example.itemservice.domain.member.Member;
import com.example.itemservice.web.argumentresolver.Login;
import com.example.itemservice.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SessionManager sessionManager;

    @GetMapping("/custom-session/home")
    public String customSessionHome(HttpServletRequest request, Model model) {
        Member loginMember = (Member) sessionManager.getSession(request);
        if (loginMember == null) return "home";

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/http-session/home")
    public String httpSessionHome(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        if (loginMember == null) return "home";
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String argumentResolverHome(@Login Member loginMember, Model model) {
        if (loginMember == null) return "home";
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
