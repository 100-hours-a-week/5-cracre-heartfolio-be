package com.heartfoilo.demo.domain.stock.api;

import com.heartfoilo.demo.domain.stock.dto.responseDto.LikeStockResponseDto;
import com.heartfoilo.demo.domain.stock.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/stock/favorites")
@RequiredArgsConstructor
public class LikeStockController {

    private final LikeService likeService;


    @PostMapping("/{stockId}")
    public ResponseEntity<Void> addFavorite(@PathVariable("stockId") Long stockId, HttpServletRequest request) {

        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            // 비로그인 사용자 처리
            return ResponseEntity.status(401).build(); // 기본값 반환
        }
        Long userId = Long.parseLong(userStrId);


        likeService.addFavorite(userId, stockId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<LikeStockResponseDto>> getFavorite(HttpServletRequest request) {

        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            // 비로그인 사용자 처리
            return ResponseEntity.ok(Collections.emptyList()); // 기본값 반환
        }
        Long userId = Long.parseLong(userStrId);
        List<LikeStockResponseDto> favorites = likeService.getFavorites(userId);

        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable("stockId") Long stockId, HttpServletRequest request) {

        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            // 비로그인 사용자 처리
            return ResponseEntity.status(401).build(); // 기본값 반환
        }
        Long userId = Long.parseLong(userStrId);
        if (userId == null) {
            return ResponseEntity.status(401).build(); // Unauthorized 처리
        }

        likeService.removeFavorite(userId, stockId);
        return ResponseEntity.ok().build();
    }
}
