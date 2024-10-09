package com.heartfoilo.demo.domain.portfolio.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssetsResponseDto {
    private long cash;          // 보유 캐시
    private long totalPurchase; // 총 매수 금액
    private long totalAmount;   // 총 자산
    private long totalValue;    // 총 평가 금액
    private double profitRate;  // 수익률
}
