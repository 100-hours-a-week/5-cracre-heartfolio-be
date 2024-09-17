package com.heartfoilo.demo.domain.ranking.api;

import com.heartfoilo.demo.domain.ranking.dto.responseDto.*;
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
    public ResponseEntity<MonthlyRankResponseDto> getMonthlyRanking(HttpServletRequest request) {
        String userStrId = (String) request.getAttribute("userId");
        Long userId = null;
        if (userStrId != null) {
            userId = Long.parseLong(userStrId);
        }

        MonthlyRankResponseDto ranks = rankService.getMonthlyRanking(userId);
        return ResponseEntity.ok(ranks);
    }

    @GetMapping("/cumulativeRevenue")
    public ResponseEntity<CumulativeRankResponseDto> getCumulativeRanking(HttpServletRequest request) {
        String userStrId = (String) request.getAttribute("userId");
        Long userId = null;
        if (userStrId != null) {
            userId = Long.parseLong(userStrId);
        }

        CumulativeRankResponseDto ranks = rankService.getCumulativeRanking(userId);
        return ResponseEntity.ok(ranks);
    }

    @GetMapping("/donation")
    public ResponseEntity<DonationRankResponseDto> getDonationRanking(HttpServletRequest request) {
        String userStrId = (String) request.getAttribute("userId");
        Long userId = null;
        if (userStrId != null) {
            userId = Long.parseLong(userStrId);
        }

        DonationRankResponseDto ranks = rankService.getDonationRanking(userId);
        return ResponseEntity.ok(ranks);
    }
}
