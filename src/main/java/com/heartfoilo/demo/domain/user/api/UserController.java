package com.heartfoilo.demo.domain.user.api;

import com.heartfoilo.demo.domain.user.service.UserService;
import com.heartfoilo.demo.domain.user.service.UserServiceImpl;
import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

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
        if (userStrId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 빈 Map 반환
        }

        return userService.getMypageInfo(Long.valueOf(userStrId));



    }

    @PostMapping("/fixNickname")
    public ResponseEntity<?> fixUserInfo(HttpServletRequest request,@RequestBody Map<String, String> requestBody){
        String nickname = requestBody.get("nickname");
        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 빈 Map 반환
        }

        return userService.fixNickname(Long.valueOf(userStrId),nickname);




    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String userStrId = (String) request.getAttribute("userId");
        if (userStrId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 빈 Map 반환
        }

        return userService.removeRefreshToken(Long.valueOf(userStrId));
    }

}
