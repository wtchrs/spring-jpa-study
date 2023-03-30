package com.example.itemservice.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "customSessionId";
    private final Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();

        synchronized (sessionStore) {
            while (sessionStore.containsKey(sessionId)) {
                sessionId = UUID.randomUUID().toString();
            }
            sessionStore.put(sessionId, value);
        }

        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public Object getSession(HttpServletRequest request) {
        Cookie cookie = findCookie(request.getCookies(), SESSION_COOKIE_NAME);
        if (cookie == null) return null;
        return sessionStore.get(cookie.getValue());
    }

    public void expire(HttpServletRequest request) {
        Cookie cookie = findCookie(request.getCookies(), SESSION_COOKIE_NAME);
        if (cookie != null) {
            sessionStore.remove(cookie.getValue());
        }
    }

    private Cookie findCookie(Cookie[] cookies, String cookieName) {
        if (cookies == null) return null;
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieName)).findAny().orElse(null);
    }
}
