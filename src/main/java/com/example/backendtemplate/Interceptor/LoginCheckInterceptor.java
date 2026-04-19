package com.example.backendtemplate.Interceptor;

import com.example.backendtemplate.Utils.JwtUtil;
import com.example.backendtemplate.Utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authToken = request.getHeader("Authorization");

        // 设置响应格式
        response.setContentType("application/json;charset=UTF-8");

        if (authToken == null || authToken.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录\"}");
            return false;
        }

        try {
            Map<String, Object> claims = JwtUtil.parseJWT(authToken);
            UserContext.setUser(claims);
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.getWriter().write("{\"code\":401,\"msg\":\"Authorization无效\"}");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
