package com.heartfoilo.demo.login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String nickname;
    private KakaoTokenResponseDto token;
    private String profileImageUrl;
    public LoginResponse(Long id, String nickname, KakaoTokenResponseDto token,String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.token = token;
    }
}