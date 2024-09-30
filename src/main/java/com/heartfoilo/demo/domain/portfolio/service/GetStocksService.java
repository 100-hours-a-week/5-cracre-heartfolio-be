package com.heartfoilo.demo.domain.portfolio.service;

import com.heartfoilo.demo.domain.portfolio.dto.responseDto.StockResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GetStocksService {
    public ResponseEntity<List<StockResponseDto>> getStocks(long userId);
}
