package com.example.itemservice.web.interceptor;

import com.example.itemservice.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AuthenticateInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        log.info("AuthenticateInterceptor.preHandle start: {}", requestURI);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            response.sendRedirect("/login?redirectURL=" + requestURI);
            log.info("AuthenticateInterceptor.preHandle redirected: Not authenticated");
            return false;
        }

        log.info("AuthenticateInterceptor.preHandle end: {}", requestURI);
        return true;
    }
}
