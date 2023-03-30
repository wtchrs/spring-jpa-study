package com.example.itemservice.web.session;

import com.example.itemservice.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class SessionManagerTest {

    private final SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member("loginId", "password", "name");
        sessionManager.createSession(member, response);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());
        Object sessionObject = sessionManager.getSession(request);

        assertThat(sessionObject).isEqualTo(member);

        sessionManager.expire(request);
        Object expiredSessionObject = sessionManager.getSession(request);

        assertThat(expiredSessionObject).isNull();
    }
}