package com.heartfoilo.demo.domain.stock.service;

import com.heartfoilo.demo.domain.stock.dto.responseDto.PopularStockResponseDto;

import java.util.List;

public interface PopularStockService {
    List<PopularStockResponseDto> getPopularStocks(int limit);
}
