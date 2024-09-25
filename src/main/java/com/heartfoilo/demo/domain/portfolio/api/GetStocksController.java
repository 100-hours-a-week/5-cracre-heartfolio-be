package com.heartfoilo.demo.domain.portfolio.api;

import com.heartfoilo.demo.domain.portfolio.service.GetStocksServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.Path;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
public class GetStocksController {
    @Autowired
    private GetStocksServiceImpl getStocksServiceImpl;
    @GetMapping("/stock") // 자산 구성 조회 API
    public ResponseEntity<?> getStocks(HttpServletRequest request){
        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            // 비로그인 사용자 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = (String) request.getAttribute("token");

        if (token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return getStocksServiceImpl.getStocks(Long.parseLong(userStrId));

    }
    @GetMapping("/stock/{userId}") // 자산 구성 조회 API
    public ResponseEntity<?> getUserStocks(HttpServletRequest request,@PathVariable ("userId") Long userId){
        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } // 로그인 안된 사용자는 401로 return
        return getStocksServiceImpl.getStocks(userId);

    }
}
