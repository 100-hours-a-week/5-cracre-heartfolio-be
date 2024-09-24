package com.heartfoilo.demo.domain.portfolio.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface GetInvestInfoService {
    public ResponseEntity<?> getInvestInfo(long userId);
}
