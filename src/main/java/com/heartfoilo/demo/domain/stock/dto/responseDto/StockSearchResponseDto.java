package com.heartfoilo.demo.domain.stock.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockSearchResponseDto {
    private Long stockId;
    private String koreanName;
    private String englishName;
}
