package com.example.itemservice.web.login;

import com.example.itemservice.domain.login.LoginService;
import com.example.itemservice.domain.member.Member;
import com.example.itemservice.web.SessionConst;
import com.example.itemservice.web.login.form.LoginForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HttpSessionLoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String login(@ModelAttribute("login") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String loginProcess(@Validated @ModelAttribute("login") LoginForm form, BindingResult bindingResult,
                               @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {
        if (bindingResult.hasErrors()) return "login/loginForm";

        Optional<Member> loginResult = loginService.login(form.getLoginId(), form.getPassword());

        if (loginResult.isEmpty()) {
            bindingResult.reject("loginFail");
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginResult.get());

        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logoutProcess(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/";
    }
}
