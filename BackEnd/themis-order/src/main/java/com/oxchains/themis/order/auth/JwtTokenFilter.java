package com.oxchains.themis.order.auth;

import com.oxchains.themis.common.auth.AuthorizationConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * create by huohuo
 * @author huohuo
 */
@Component
public class JwtTokenFilter implements Filter {

    private final JwtService jwtService;
    @Autowired
    public JwtTokenFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String authorization = servletRequest.getHeader(AuthorizationConst.AUTHORIZATION_HEADER);
        if (authorization != null && authorization.startsWith(AuthorizationConst.AUTHORIZATION_START)) {
            JwtAuthentication jwtAuthentication = jwtService.parse(authorization.replaceAll("Bearer ", ""));
            if(jwtAuthentication!=null){
                SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
