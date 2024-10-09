package com.heartfoilo.demo.domain.portfolio.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockResponseDto {
    private Long id;
    private String stockName;
    private long evalPrice;
    private String sector;
}