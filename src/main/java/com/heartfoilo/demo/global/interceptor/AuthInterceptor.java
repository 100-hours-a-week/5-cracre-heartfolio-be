package com.goorm.jeff.global.interceptor;

import com.goorm.jeff.global.exception.ForbiddenAuthException;
import com.goorm.jeff.global.exception.NoAuthInfoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;



@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${key.api}")
    private String apiKey;

    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String clientApiKey = request.getHeader("x-api-key");
        if(clientApiKey == null){
            throw new NoAuthInfoException();
        }

        if(!clientApiKey.equals(apiKey)){
            System.out.println(apiKey);
            System.out.println(clientApiKey);

            throw new ForbiddenAuthException();
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}