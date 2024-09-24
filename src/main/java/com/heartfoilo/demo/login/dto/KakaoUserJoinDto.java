package com.heartfoilo.demo.login.dto;
import com.heartfoilo.demo.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
public class KakaoUserJoinDto {

    private final String name;
    private final String email;
    private final String nickname;
    // private final String profile;

    @Builder
    public KakaoUserJoinDto(String name,String email, String nickname){
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }
}
