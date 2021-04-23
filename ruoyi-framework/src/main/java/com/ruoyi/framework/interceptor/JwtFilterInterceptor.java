package com.ruoyi.framework.interceptor;

import com.ruoyi.framework.shiro.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtFilterInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        final String authHeader = request.getHeader("Authorization");
        System.out.println("进入jwt拦截器");
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            final String token = authHeader.substring(7);
            Claims claims = jwtUtils.parseJwt(token);
            if (claims != null){
                if ("admin".equals(claims.get("roles"))){
                    request.setAttribute("admin_claims", claims);//如果是管理员
                }
                if("user".equals(claims.get("roles"))){//如果是用户
                    request.setAttribute("user_claims", claims);
                }
            }
        }
        return true;
    }


}
