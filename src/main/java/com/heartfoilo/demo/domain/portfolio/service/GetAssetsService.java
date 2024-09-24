package com.heartfoilo.demo.domain.portfolio.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface GetAssetsService {
    public ResponseEntity<Map<String, Object>> getAssets(long userId);
}
