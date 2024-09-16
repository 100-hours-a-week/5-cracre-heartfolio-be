package com.heartfoilo.demo.domain.ranking.service;

import com.heartfoilo.demo.domain.ranking.dto.responseDto.RankingDonationResponseDto;
import com.heartfoilo.demo.domain.ranking.dto.responseDto.RankingResponseDto;
import com.heartfoilo.demo.domain.ranking.dto.responseDto.UserRankingResponseDto;

import java.util.List;

public interface RankService {
    List<RankingResponseDto> getMonthlyRanking();
    List<RankingResponseDto> getCumulativeRanking();
    List<RankingDonationResponseDto> getDonationRanking();
    UserRankingResponseDto getUserRanking(Long userId);
}
