package com.heartfoilo.demo.domain.ranking.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DonationRankResponseDto {
    private Integer personalRank;
    private List<RankingDonationResponseDto> userRanking;
}
