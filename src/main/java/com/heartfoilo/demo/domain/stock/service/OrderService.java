package com.heartfoilo.demo.domain.stock.service;

import com.heartfoilo.demo.domain.stock.dto.responseDto.OrderHistoryResponseDto;
import com.heartfoilo.demo.domain.stock.dto.responseDto.StockSearchResponseDto;

import java.util.List;

public interface  OrderService {
    List<OrderHistoryResponseDto> getOrderHistory(Long userId, Long stockId);
    List<StockSearchResponseDto> getStockSearch(String keyword);
}
