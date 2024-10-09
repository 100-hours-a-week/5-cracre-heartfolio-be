package com.heartfoilo.demo.domain.stock.service;

import com.heartfoilo.demo.domain.stock.dto.responseDto.GetAmountResponseDto;

import java.util.Map;

public interface StockInfoService {
    public GetAmountResponseDto getInfo(long userId, long stockId);
    public GetAmountResponseDto getInfoNoUser(long stockId);
}
