package com.heartfoilo.demo.login.Generator;

import com.heartfoilo.demo.domain.user.repository.UserRepository;
import com.heartfoilo.demo.login.Entity.RefreshToken;
import com.heartfoilo.demo.login.Repository.RefreshTokenRepository;
import com.heartfoilo.demo.login.dto.KakaoTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;	//TODO: 일단 60분 설정해둠
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일로 설정

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;
    public KakaoTokenResponseDto generate(Long id){
        long now = (new Date().getTime());
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = jwtTokenProvider.accessTokenGenerate(String.valueOf(id), accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.refreshTokenGenerate(refreshTokenExpiredAt);

        Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findByUserId(id);

        if (!existingRefreshToken.isPresent()) {
            // 리프레시 토큰 생성 및 저장
            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setUser(userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"))); // 유저 정보 조회
            newRefreshToken.setToken(refreshToken);
            newRefreshToken.setExpiryDate(refreshTokenExpiredAt);

            refreshTokenRepository.save(newRefreshToken); // DB에 리프레시 토큰 저장
        } else {
            // 이미 존재하는 리프레시 토큰을 업데이트할 경우
            RefreshToken tokenToUpdate = existingRefreshToken.get();
            tokenToUpdate.setToken(refreshToken);
            tokenToUpdate.setExpiryDate(refreshTokenExpiredAt);

            refreshTokenRepository.save(tokenToUpdate); // DB에 업데이트된 리프레시 토큰 저장
        }
        return KakaoTokenResponseDto.AuthTokens(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }
}
