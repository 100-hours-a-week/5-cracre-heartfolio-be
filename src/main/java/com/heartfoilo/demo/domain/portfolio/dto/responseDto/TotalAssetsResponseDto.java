package com.heartfoilo.demo.domain.portfolio.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalAssetsResponseDto {
    private Long stockId;
    private String stockName;
    private Long totalQuantity;
    private Long purchaseAvgPrice;
    private Long totalPurchasePrice;
    private Long evalValue;
    private Long evalProfit;
    private Double profitPercentage;
}