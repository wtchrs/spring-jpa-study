package com.example.servletstudy.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("RequestHeaderServlet.service");
        loggingStartLine(req);
        loggingHeaders(req);
        loggingHeaderUtils(req);
        loggingEtc(req);

        resp.getWriter().write("ok");
    }

    private static void loggingStartLine(HttpServletRequest request) {
        log.info("--- REQUEST-LINE - START ---");
        log.info("request.getMethod() = {}", request.getMethod());
        log.info("request.getProtocol() = {}", request.getProtocol());
        log.info("request.getScheme() = {}", request.getScheme());
        log.info("request.getRequestURL() = {}", request.getRequestURL());
        log.info("request.getRequestURI() = {}", request.getRequestURI());
        log.info("request.getQueryString() = {}", request.getQueryString());
        log.info("request.isSecure() = {}", request.isSecure());
        log.info("--- REQUEST-LINE - END ---");
    }

    private static void loggingHeaders(HttpServletRequest request) {
        log.info("--- HEADERS - START ---");
        request.getHeaderNames().asIterator()
            .forEachRemaining(headerName -> log.info("headerName: {}", headerName));
        log.info("--- HEADERS - END ---");
    }

    private void loggingHeaderUtils(HttpServletRequest request) {
        log.info("--- Header 편의 조회 start ---");
        log.info("[Host 편의 조회]");
        log.info("request.getServerName() = " +
                           request.getServerName()); //Host 헤더
        log.info("request.getServerPort() = " +
                           request.getServerPort()); //Host 헤더
        log.info("");
        log.info("[Accept-Language 편의 조회]");
        request.getLocales().asIterator()
               .forEachRemaining(locale -> log.info("locale = " +
                                                              locale));
        log.info("request.getLocale() = " + request.getLocale());
        log.info("");
        log.info("[cookie 편의 조회]");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                log.info(cookie.getName() + ": " + cookie.getValue());
            }
        }
        log.info("");
        log.info("[Content 편의 조회]");
        log.info("request.getContentType() = " +
                           request.getContentType());
        log.info("request.getContentLength() = " +request.getContentLength());
        log.info("request.getCharacterEncoding() = " +
                           request.getCharacterEncoding());
        log.info("--- Header 편의 조회 end ---");
        log.info("");
    }

    private void loggingEtc(HttpServletRequest request) {
        log.info("--- 기타 조회 start ---");
        log.info("[Remote 정보]");
        log.info("request.getRemoteHost() = " + request.getRemoteHost());
        log.info("request.getRemoteAddr() = " + request.getRemoteAddr());
        log.info("request.getRemotePort() = " + request.getRemotePort());
        log.info("");
        log.info("[Local 정보]");
        log.info("request.getLocalName() = " + request.getLocalName());
        log.info("request.getLocalAddr() = " + request.getLocalAddr());
        log.info("request.getLocalPort() = " + request.getLocalPort());
        log.info("--- 기타 조회 end ---");
        log.info("");
    }
}
