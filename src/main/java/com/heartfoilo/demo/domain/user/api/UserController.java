package com.heartfoilo.demo.domain.user.api;

import com.heartfoilo.demo.domain.user.service.UserService;
import com.heartfoilo.demo.domain.user.service.UserServiceImpl;
import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RedisUtil redisUtil;
    @GetMapping("/test")
    public void test(){
        redisUtil.setStockInfoTemplate("AAPL", StockSocketInfoDto.builder().symbol("AAPL").curPrice(2263600)
            .openPrice(2200000).highPrice(2400000).lowPrice(2100000).earningValue(11000).earningRate(11.1f).build());
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request){
        String userStrId = (String) request.getAttribute("userId");
        System.out.println(userStrId);
        if (userStrId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 빈 Map 반환
        }

        return userService.getInfo(Long.valueOf(userStrId));



    }

}
