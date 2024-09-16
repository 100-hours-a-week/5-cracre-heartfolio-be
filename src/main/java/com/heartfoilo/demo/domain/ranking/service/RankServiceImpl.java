package com.heartfoilo.demo.domain.ranking.service;

import com.heartfoilo.demo.domain.ranking.constant.ErrorMessage;
import com.heartfoilo.demo.domain.ranking.dto.responseDto.RankingDonationResponseDto;
import com.heartfoilo.demo.domain.ranking.dto.responseDto.RankingResponseDto;
import com.heartfoilo.demo.domain.ranking.dto.responseDto.UserRankingResponseDto;
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
    private final UserRepository userRepository;

    @Override
    public List<RankingResponseDto> getMonthlyRanking() {
        List<Ranking> ranks = rankingRepository.findTop10ByOrderByMonthlyReturnDesc();
        if (ranks.isEmpty()) {
            return Collections.emptyList();
        }
        return ranks.stream()
                .map(rank -> new RankingResponseDto(
                        rank.getUser().getProfileImageUrl(),
                        rank.getUser().getName(),
                        rank.getMonthlyReturn()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<RankingResponseDto> getCumulativeRanking() {
        List<Ranking> ranks = rankingRepository.findTop10ByOrderBySumReturnDesc();
        if (ranks.isEmpty()) {
            return Collections.emptyList();
        }
        return ranks.stream()
                .map(rank -> new RankingResponseDto(
                        rank.getUser().getProfileImageUrl(),
                        rank.getUser().getName(),
                        rank.getSumReturn()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<RankingDonationResponseDto> getDonationRanking() {
        List<Ranking> ranks = rankingRepository.findTop10ByOrderByDonationDesc();
        if (ranks.isEmpty()) {
            return Collections.emptyList();
        }
        return ranks.stream()
                .map(rank -> new RankingDonationResponseDto(
                        rank.getUser().getProfileImageUrl(),
                        rank.getUser().getName(),
                        rank.getDonation()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public UserRankingResponseDto getUserRanking(Long userId) {
        Integer monthlyRank = rankingRepository.findUserRankByMonthlyReturn(userId).orElse(-1);
        Integer sumRank = rankingRepository.findUserRankBySumReturn(userId).orElse(-1);
        Integer donationRank = rankingRepository.findUserRankByDonation(userId).orElse(-1);

        return new UserRankingResponseDto(monthlyRank, sumRank, donationRank);
    }
    
}
