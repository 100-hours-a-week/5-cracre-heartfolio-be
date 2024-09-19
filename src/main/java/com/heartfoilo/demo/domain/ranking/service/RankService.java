package com.heartfoilo.demo.domain.ranking.service;

import com.heartfoilo.demo.domain.ranking.dto.responseDto.*;

import java.util.List;

public interface RankService {
    MonthlyRankResponseDto getMonthlyRanking(Long userId);
    CumulativeRankResponseDto getCumulativeRanking(Long userId);
    DonationRankResponseDto getDonationRanking(Long userId);
}
