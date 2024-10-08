package com.heartfoilo.demo.domain.ranking.service;

import com.heartfoilo.demo.domain.ranking.constant.ErrorMessage;
import com.heartfoilo.demo.domain.ranking.dto.responseDto.*;
import com.heartfoilo.demo.domain.ranking.entity.Ranking;
import com.heartfoilo.demo.domain.ranking.repository.RankingRepository;
import com.heartfoilo.demo.domain.user.repository.UserRepository;
import com.heartfoilo.demo.global.exception.OrderHistoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {

    private final RankingRepository rankingRepository;

    @Override
    public MonthlyRankResponseDto getMonthlyRanking(Long userId) {
        Integer monthlyRank = (userId == null) ? -1 : rankingRepository.findUserRankByMonthlyReturn(userId).orElse(-1);

        List<Ranking> ranks = rankingRepository.findTop10MonthlyRankings();
        if (ranks.isEmpty()) {
            return new MonthlyRankResponseDto(monthlyRank, Collections.emptyList());
        }

        List<RankingResponseDto> rankingResponseDtos = ranks.stream()
                .map(rank -> new RankingResponseDto(
                        rank.getUser().getId(),
                        rank.getUser().getProfileImageUrl(),
                        rank.getUser().getNickname(),
                        rank.getMonthlyReturn()
                ))
                .collect(Collectors.toList());
        return new MonthlyRankResponseDto(monthlyRank, rankingResponseDtos);
    }

    @Override
    public CumulativeRankResponseDto getCumulativeRanking(Long userId) {
        Integer CumulativeRank = (userId == null) ? -1 : rankingRepository.findUserRankBySumReturn(userId).orElse(-1);

        List<Ranking> ranks = rankingRepository.findTop10SumRankings();
        if (ranks.isEmpty()) {
            return new CumulativeRankResponseDto(CumulativeRank, Collections.emptyList());
        }
        List<RankingResponseDto> rankingResponseDtos = ranks.stream()
                .map(rank -> new RankingResponseDto(
                        rank.getUser().getId(),
                        rank.getUser().getProfileImageUrl(),
                        rank.getUser().getNickname(),
                        rank.getSumReturn()
                ))
                .collect(Collectors.toList());

        return new CumulativeRankResponseDto(CumulativeRank, rankingResponseDtos);
    }

    @Override
    public DonationRankResponseDto getDonationRanking(Long userId) {
        Integer DonationRank = (userId == null) ? -1 : rankingRepository.findUserRankByDonation(userId).orElse(-1);

        List<Ranking> ranks = rankingRepository.findTop10DonationRankings();
        if (ranks.isEmpty()) {
            return new DonationRankResponseDto(DonationRank, Collections.emptyList());
        }
        List<RankingDonationResponseDto> donationRankResponseDtos = ranks.stream()
                .map(rank -> new RankingDonationResponseDto(
                        rank.getUser().getId(),
                        rank.getUser().getProfileImageUrl(),
                        rank.getUser().getNickname(),
                        rank.getDonation()
                ))
                .collect(Collectors.toList());
        return new DonationRankResponseDto(DonationRank, donationRankResponseDtos);
    }

}
