package com.heartfoilo.demo.domain.stock.service;

import com.heartfoilo.demo.domain.stock.dto.responseDto.LikeStockResponseDto;
import com.heartfoilo.demo.domain.stock.entity.Like;

import java.util.List;

public interface LikeService {
    Like addFavorite(Long userId, Long stockId);
    List<LikeStockResponseDto> getFavorites(Long userId);
    void removeFavorite(Long userId, Long stockId);
}
