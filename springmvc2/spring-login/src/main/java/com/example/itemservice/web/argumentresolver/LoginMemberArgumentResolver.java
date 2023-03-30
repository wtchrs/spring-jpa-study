package com.example.itemservice.web.argumentresolver;

import com.example.itemservice.domain.member.Member;
import com.example.itemservice.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("LoginMemberArgumentResolver.supportsParameter");
        return Member.class.isAssignableFrom(parameter.getParameterType())
               && parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("LoginMemberArgumentResolver.resolveArgument");
        HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = nativeRequest.getSession(false);
        return session != null ? session.getAttribute(SessionConst.LOGIN_MEMBER) : null;
    }
}
