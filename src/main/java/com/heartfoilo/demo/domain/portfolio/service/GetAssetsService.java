package com.heartfoilo.demo.domain.portfolio.service;

import com.heartfoilo.demo.domain.portfolio.dto.responseDto.AssetsResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GetAssetsService {
    public ResponseEntity<AssetsResponseDto> getAssets(long userId);
}
