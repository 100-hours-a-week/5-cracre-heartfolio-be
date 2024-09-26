package com.heartfoilo.demo.domain.portfolio.api;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class GetAssetsController {

    private final GetAssetsService getAssetsService;

    @GetMapping // 보유 자산 조회 API
    public ResponseEntity<Map<String,Object>> getAssets(HttpServletRequest request){
        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            // 토큰이 아예 없는경우 , "Bearer" 문자열만 옴
            return ResponseEntity.ok(Collections.emptyMap());
        }
        return getAssetsService.getAssets(Long.valueOf(userStrId));
    }

    @GetMapping("/{userId}")
        public ResponseEntity<Map<String,Object>> getUserAssets(HttpServletRequest request, @PathVariable("userId") Long userId){
        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } // 로그인 안된 사용자는 401로 return

        return getAssetsService.getAssets(userId);
    }
}
