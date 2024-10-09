package com.heartfoilo.demo.login.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoTokenResponseDto {
    @JsonProperty("token_type") // JSON 파일과의 매칭
    public String tokenType;
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("id_token")
    public String idToken;
    @JsonProperty("expires_in")
    public Long expiresIn;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("refresh_token_expires_in")
    public Integer refreshTokenExpiresIn;
    @JsonProperty("scope")
    public String scope;

    public KakaoTokenResponseDto(String accessToken, String refreshToken, String tokenType, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }


    public static KakaoTokenResponseDto AuthTokens(String accessToken, String refreshToken,String tokenType,Long expiresIn){
        return new KakaoTokenResponseDto(accessToken,refreshToken,tokenType,expiresIn);
    }
}
