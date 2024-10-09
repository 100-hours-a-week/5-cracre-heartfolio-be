package com.heartfoilo.demo.domain.invest.api;

import com.heartfoilo.demo.domain.invest.dto.requestDto.InvestRequestDto;
import com.heartfoilo.demo.domain.invest.service.InvestServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/invest")
public class InvestController {

    @Autowired
    private InvestServiceImpl investServiceImpl;
  
    @PostMapping("/order")
    public ResponseEntity<?> order(@RequestBody InvestRequestDto getInfoRequestDto, HttpServletRequest request){

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(Collections.emptyMap()); // 빈 Map 반환
        }
        return investServiceImpl.order(getInfoRequestDto, userId);

    }

    @DeleteMapping("/order")
    public ResponseEntity<?> sell(@RequestBody InvestRequestDto getInfoRequestDto, HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(Collections.emptyMap()); // 빈 Map 반환
        }
        return investServiceImpl.sell(getInfoRequestDto, userId);
    }

}
