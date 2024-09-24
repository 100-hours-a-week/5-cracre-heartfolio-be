package com.heartfoilo.demo.domain.ranking.service;

import com.heartfoilo.demo.domain.ranking.repository.RankingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class RankScheduler {

    private final RankingRepository rankingRepository;

    public RankScheduler(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    // 매달 1일 00:00에 실행 (cron 표현식 사용)
    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void resetMonthlyRankings() {
        // 모든 랭킹의 monthlyReturn을 0으로 초기화
        rankingRepository.resetAllMonthlyReturns();
    }
}
