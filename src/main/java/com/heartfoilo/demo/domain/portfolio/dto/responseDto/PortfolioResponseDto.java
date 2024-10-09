package com.heartfoilo.demo.domain.portfolio.dto.responseDto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class PortfolioResponseDto {

    private long id;
    private String stockName;
    private double percentage;
    private long totalPurchase;
    private long totalAmount;
    private long totalValue;
    private double profitRate;


}
