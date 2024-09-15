package com.heartfoilo.demo.domain.portfolio.api;

import com.heartfoilo.demo.domain.portfolio.service.GetTotalStocksServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
public class GetTotalStocksController {
    @Autowired
    private GetTotalStocksServiceImpl getTotalStocksServiceImpl;
    @GetMapping("/totalStocks")
    public ResponseEntity<Map<String,Object>> getTotalStocks(HttpServletRequest request){
        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            // 비로그인 사용자 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = (String) request.getAttribute("token");

        if (token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return getTotalStocksServiceImpl.getTotalStocks(Long.parseLong(userStrId));
    }
}
