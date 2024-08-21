package com.heartfoilo.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("https://heartfolio:3000")
            .allowedOriginPatterns("*")
            .allowedHeaders("*")
            .allowCredentials(false)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
    }

}