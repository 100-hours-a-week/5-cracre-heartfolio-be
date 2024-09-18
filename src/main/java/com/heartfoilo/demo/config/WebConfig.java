package com.heartfoilo.demo.config;

import com.heartfoilo.demo.Handler.HeartfolioInterceptor;
import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

                .addPathPatterns("/api/stock/favorites/**", "/api/stock/order/**","/api/portfolio/**","/api/invest/**","/api/donation/**"); // Interceptor가 적용될 경로를 지정

    }
    @Value("${portone.api_key}")
    private String apiKey;
    @Value("${portone.secret_key}")
    private String secretKey;

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
// PortOne API와의 통신을 위한 클라이언트를 생성

}
