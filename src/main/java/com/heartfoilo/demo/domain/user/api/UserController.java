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

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 빈 Map 반환
        }

        return userService.getMypageInfo(userId);



    }

    @PostMapping("/fixNickname")
    public ResponseEntity<?> fixUserInfo(HttpServletRequest request,@RequestBody Map<String, String> requestBody){
        String nickname = requestBody.get("nickname");
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 빈 Map 반환
        }

        return userService.fixNickname(userId,nickname);




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
