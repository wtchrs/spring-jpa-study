package com.example.itemservice.web.filter;

import com.example.itemservice.web.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class AuthenticateFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/*", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        log.info("Authenticate filter start: {}", requestURI);

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("Authenticate filter processing: {}", requestURI);
            if (isAuthenticateRequired(requestURI)) {
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("Authenticate filter end: {}", requestURI);
        }
    }

    private boolean isAuthenticateRequired(String requestUri) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestUri);
    }
}
