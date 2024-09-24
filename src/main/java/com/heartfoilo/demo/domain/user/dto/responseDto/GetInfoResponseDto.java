package com.heartfoilo.demo.domain.user.dto.responseDto;

import com.heartfoilo.demo.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
public class GetInfoResponseDto {
    private final String name;

    private final String nickname;
   // private final String profile;
    private final long cash;
    private final long donate;

    @Builder
    public GetInfoResponseDto(User user, long cash, long donate){
        this.name = user.getName();

        this.nickname = user.getNickname();
        // this.profile = user.getProfile();
        this.cash = cash;
        this.donate = donate;
    }
}
