package com.heartfoilo.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOauthDto {
    private String grant_type;
    private String appkey;
    private String appsecret;
}
