package com.sparta.myselectshop.mvc;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class MockSpringSecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {


        SecurityContextHolder.getContext() // 인증 객체를 받아온다
                .setAuthentication((Authentication) ((HttpServletRequest) req).getUserPrincipal());
        // 가짜 인증 객체를 만들어서 시큐리티 필터를 통과한다.
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        SecurityContextHolder.clearContext();
    }
}