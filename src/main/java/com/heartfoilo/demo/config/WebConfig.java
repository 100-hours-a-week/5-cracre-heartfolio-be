package com.heartfoilo.demo.config;

import com.heartfoilo.demo.Handler.HeartfolioInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private HeartfolioInterceptor heartfolioInterceptor;
    public WebConfig(HeartfolioInterceptor heartfolioInterceptor) {
        this.heartfolioInterceptor = heartfolioInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("https://heartfolio.site:3000", "http://heartfolio.site:3000", "http://localhost:3000","https://heartfolio.site")
            .allowedOriginPatterns("*")
            .allowedHeaders("*")
            .allowCredentials(false)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(heartfolioInterceptor)

                .addPathPatterns("/api/stock/favorites/**", "/api/stock/order/**","/api/portfolio/**","/api/invest/**","/api/rank/**"); // Interceptor가 적용될 경로를 지정

    }

}
