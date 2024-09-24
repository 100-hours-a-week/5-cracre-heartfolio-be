package com.heartfoilo.demo.login.service;

import com.heartfoilo.demo.domain.portfolio.entity.Account;
import com.heartfoilo.demo.domain.portfolio.repository.PortfolioRepository;
import com.heartfoilo.demo.domain.user.entity.User;
import com.heartfoilo.demo.domain.user.repository.UserRepository;
import com.heartfoilo.demo.login.Generator.AuthTokensGenerator;
import com.heartfoilo.demo.login.dto.KakaoTokenResponseDto;
import com.heartfoilo.demo.login.dto.KakaoUserInfoResponseDto;
import com.heartfoilo.demo.login.dto.LoginResponse;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.Random;
import java.util.HashMap;

@RequiredArgsConstructor
@Slf4j
@Service
public class KakaoService{

    private String clientId;
    private final String KAUTH_TOKEN_URL_HOST;
    private final String KAUTH_USER_URL_HOST;
    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;
    @Autowired
    private AuthTokensGenerator authTokensGenerator;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    public KakaoService(@Value("${kakao.client_id}") String clientId){
        this.clientId = clientId;
        KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    }

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {

        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.BAD_REQUEST.value(),
                        clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(status -> status.value() == HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
        log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }
    public String getAccessTokenFromKakao(String code) {



        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("redirect_uri", kakaoRedirectUri)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(status -> status.value() == HttpStatus.BAD_REQUEST.value(),
                        clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(status -> status.value() == HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        log.info(code);
        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh To`" +
                "ken ------> {}", kakaoTokenResponseDto.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());
        log.info(" [Kakao Service] expires_in ------> {}", kakaoTokenResponseDto.getExpiresIn());



        return kakaoTokenResponseDto.getAccessToken();
    }

    public LoginResponse kakaoUserLogin(KakaoUserInfoResponseDto kakaoUserInfoResponseDto){

        Long id = kakaoUserInfoResponseDto.getId();
        String name = kakaoUserInfoResponseDto.getKakaoAccount().getProfile().getNickName(); // 일단 본명이긴 한데 이름이긴 함...
        // 닉네임은 랜덤으로 부여해야 함
        Random random = new Random();  // Random 클래스의 인스턴스를 생성합니다.

        String profileImageUrl = kakaoUserInfoResponseDto.getKakaoAccount().getProfile().getProfileImageUrl();
        Optional<User> kakaoUser = userRepository.findById(id);

        String nickname = "사용자" + random.nextInt(99999) + 1;
        if (!kakaoUser.isPresent()) {
            User newUser = new User(id,name, nickname,profileImageUrl);
            Account account = new Account(newUser,10000000L,0L);
            userRepository.save(newUser); // email, name, nickname 세 개로 가입 진행
            portfolioRepository.save(account);
        }
        else{
            Optional<User> optionalUser = userRepository.findById(id);
            User user = optionalUser.get();
            User newUser = new User(user.getId(), user.getName(),user.getNickname(),profileImageUrl);
            userRepository.save(newUser);
            nickname = kakaoUser.get().getNickname();
        }
        KakaoTokenResponseDto token=authTokensGenerator.generate(id);
        return new LoginResponse(id,nickname,token,profileImageUrl);// 여기서부터 다시할것
    }



}