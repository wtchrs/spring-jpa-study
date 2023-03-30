package com.example.itemservice.web.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return "Not exists session";
        session.getAttributeNames().asIterator()
               .forEachRemaining(attr -> log.info("attr = {}, value = {}", attr, session.getAttribute(attr)));

        log.info("session.getId() = {}", session.getId());
        log.info("session.getMaxInactiveInterval() = {}", session.getMaxInactiveInterval());
        log.info("new Date(session.getCreationTime()) = {}", new Date(session.getCreationTime()));
        log.info("new Date(session.getLastAccessedTime()) = {}", new Date(session.getLastAccessedTime()));
        log.info("session.isNew() = {}", session.isNew());

        return "Logged session information";
    }
}
