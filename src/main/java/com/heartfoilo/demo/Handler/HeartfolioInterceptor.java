package com.heartfoilo.demo.Handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.security.Key;

@Component
public class HeartfolioInterceptor implements HandlerInterceptor {
    private final Key key;

    public HeartfolioInterceptor(@Value("${spring.custom.jwt.secretkey}") String secretKey) {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes); // JwtTokenProvider와 동일한 방식으로 Key 생성
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        System.out.println("내가 first token" + token);
        if (token == null || token.equals("Bearer null") || !token.startsWith("Bearer ")) {
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
            request.setAttribute("userId", null);
            return true;
        } else {
            token = token.substring(7);


            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String userId = claims.getSubject();
                request.setAttribute("userId", userId);

            } catch (ExpiredJwtException e) {
                // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 반환
                response.getWriter().write("{\"message\": \"Access token has expired\", \"status\": 401}");
                response.getWriter().flush();
                request.setAttribute("token",null);
                return false;
            } // 여기서 401 요청시 프론트엔드에서 RefreshToken 요청

            request.setAttribute("token",token);

            return true;
        }


        // other methods...

    }
}