package com.heartfoilo.demo.domain.portfolio.api;

import com.heartfoilo.demo.domain.portfolio.service.GetInvestInfoServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
@RestController
@RequestMapping("/api/portfolio")
public class GetInvestInfoController {
    @Autowired
    private GetInvestInfoServiceImpl getInvestInfoServiceImpl;
    @GetMapping("/investInfo")
    public ResponseEntity<?> getInvestInfo(HttpServletRequest request){
        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            // 비로그인 사용자 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = (String) request.getAttribute("token");

        if (token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(getInvestInfoServiceImpl.getInvestInfo(Long.parseLong(userStrId)));
    }
}
