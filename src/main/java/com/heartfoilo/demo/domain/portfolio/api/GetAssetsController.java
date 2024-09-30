package com.heartfoilo.demo.domain.portfolio.api;

import com.heartfoilo.demo.domain.portfolio.dto.responseDto.AssetsResponseDto;
import com.heartfoilo.demo.domain.portfolio.service.GetAssetsService;
import com.heartfoilo.demo.domain.portfolio.service.GetAssetsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
<<<<<<< HEAD
import java.util.List;
=======
import java.util.HashMap;
>>>>>>> dev
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class GetAssetsController {

    private final GetAssetsService getAssetsService;

    @GetMapping // 보유 자산 조회 API
<<<<<<< HEAD
    public ResponseEntity<?> getAssets(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            // 비로그인 사용자 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } // 이경우는 AccessToken이 만료된 경우
        String token = (String) request.getAttribute("token");

        if (token == null){
            return ResponseEntity.ok(Collections.emptyMap());
        } // 이경우가 Bearer만 간 경우(토큰이 없는 경우)
        return getAssetsService.getAssets(userId);

    }

    @GetMapping("/{userId}")
        public ResponseEntity<?> getUserAssets(HttpServletRequest request, @PathVariable("userId") Long userId){
        Long userStrId = (Long) request.getAttribute("userId");
        if (userStrId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } // 로그인 안된 사용자는 401로 return

        return getAssetsService.getAssets(userId);
    }
}
