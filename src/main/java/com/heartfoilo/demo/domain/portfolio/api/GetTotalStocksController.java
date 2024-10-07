package com.heartfoilo.demo.domain.portfolio.api;

import com.heartfoilo.demo.domain.portfolio.service.GetTotalStocksService;
import com.heartfoilo.demo.domain.portfolio.service.GetTotalStocksServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
public class GetTotalStocksController {
    @Autowired
    private GetTotalStocksService getTotalStocksService;
    @GetMapping("/totalStocks")
    public ResponseEntity<?> getTotalStocks(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            // 비로그인 사용자 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = (String) request.getAttribute("token");

        if (token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return getTotalStocksService.getTotalStocks(userId);
    }

    @GetMapping("/totalStocks/{userId}")
    public ResponseEntity<?> getUserTotalStocks(HttpServletRequest request, @PathVariable("userId") Long userId){
        Long userStrId = (Long) request.getAttribute("userId");
        if (userStrId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } // 토큰이 만료된 경우 -> 401 return
        return getTotalStocksService.getTotalStocks(userId);

    }
}
