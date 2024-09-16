package com.heartfoilo.demo.domain.ranking.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingDonationResponseDto {
    private String profile;
    private String name;
    private Long amount;
}
