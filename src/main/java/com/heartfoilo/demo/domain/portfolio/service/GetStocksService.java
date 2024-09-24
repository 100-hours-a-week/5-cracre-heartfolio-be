package com.heartfoilo.demo.domain.portfolio.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface GetStocksService {
    public ResponseEntity<Map<String, Object>> getStocks(long userId);
}
