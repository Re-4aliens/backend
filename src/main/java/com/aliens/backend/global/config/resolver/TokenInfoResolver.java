package com.aliens.backend.global.config.resolver;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.service.TokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenInfoResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    public TokenInfoResolver(final TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(LoginMember.class)
                && parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("Authorization");
        return tokenProvider.getLoginMemberFromToken(accessToken);
    }
}