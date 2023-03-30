package com.example.itemservice.web.login;

import com.example.itemservice.domain.login.LoginService;
import com.example.itemservice.domain.member.Member;
import com.example.itemservice.web.login.form.LoginForm;
import com.example.itemservice.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/custom-session")
@RequiredArgsConstructor
public class CustomSessionLoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String login(@ModelAttribute("login") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String loginProcess(HttpServletResponse response,
                               @Validated @ModelAttribute("login") LoginForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "login/loginForm";

        Optional<Member> loginResult = loginService.login(form.getLoginId(), form.getPassword());

        if (loginResult.isEmpty()) {
            bindingResult.reject("loginFail");
            return "login/loginForm";
        }

        sessionManager.createSession(loginResult.get(), response);
        return "redirect:/custom-session/home";
    }

    @PostMapping("/logout")
    public String logoutProcess(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/custom-session/home";
    }
}
