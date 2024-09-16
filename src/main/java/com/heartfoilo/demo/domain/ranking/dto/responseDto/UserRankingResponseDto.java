package com.heartfoilo.demo.domain.ranking.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRankingResponseDto {
    private Integer monthlyRank;
    private Integer sumRank;
    private Integer donationRank;
}
