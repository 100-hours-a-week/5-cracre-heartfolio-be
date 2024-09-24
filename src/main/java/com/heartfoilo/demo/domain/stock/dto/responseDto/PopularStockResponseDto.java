package com.heartfoilo.demo.domain.stock.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PopularStockResponseDto {
    private Long stockId;
    private Integer rank;
    private String koreanName;
    private String englishName;
    private int currentPrice;  // 현재가 필드 추가
    private Integer earningValue;
    private float earningRate;
    private String sector;
}
