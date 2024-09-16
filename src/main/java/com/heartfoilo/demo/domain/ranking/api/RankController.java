package com.heartfoilo.demo.domain.ranking.api;

import com.heartfoilo.demo.domain.ranking.dto.responseDto.RankingDonationResponseDto;
import com.heartfoilo.demo.domain.ranking.dto.responseDto.RankingResponseDto;
import com.heartfoilo.demo.domain.ranking.dto.responseDto.UserRankingResponseDto;
import com.heartfoilo.demo.domain.ranking.service.RankService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    @GetMapping("/month")
    public ResponseEntity<List<RankingResponseDto>> getMonthlyRanking() {
        List<RankingResponseDto> ranks = rankService.getMonthlyRanking();
        return ResponseEntity.ok(ranks);
    }

    @GetMapping("/cumulativeRevenue")
    public ResponseEntity<List<RankingResponseDto>> getCumulativeRanking() {
        List<RankingResponseDto> ranks = rankService.getCumulativeRanking();
        return ResponseEntity.ok(ranks);
    }

    @GetMapping("/donation")
    public ResponseEntity<List<RankingDonationResponseDto>> getDonationRanking() {
        List<RankingDonationResponseDto> ranks = rankService.getDonationRanking();
        return ResponseEntity.ok(ranks);
    }

    //사용자 순위
    @GetMapping("/user")
    public ResponseEntity<UserRankingResponseDto> getUserRanking(HttpServletRequest request) {
        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            return ResponseEntity.ok(new UserRankingResponseDto(null, null, null));
        }
        Long userId = Long.parseLong(userStrId);
        return ResponseEntity.ok(rankService.getUserRanking(userId));
    }
}
