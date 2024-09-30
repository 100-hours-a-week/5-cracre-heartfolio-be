package com.heartfoilo.demo.domain.portfolio.service;

import com.heartfoilo.demo.domain.portfolio.dto.responseDto.TotalAssetsResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GetTotalStocksService {
    public ResponseEntity<List<TotalAssetsResponseDto>> getTotalStocks(long userId);
}
