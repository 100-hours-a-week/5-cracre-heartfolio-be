package com.heartfoilo.demo.domain.fortune.api;

import com.heartfoilo.demo.domain.fortune.exception.NotRegisterDailyFortune;
import com.heartfoilo.demo.domain.fortune.service.FortuneService;
import com.heartfoilo.demo.global.exception.NoAuthInfoException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fortune")
@RequiredArgsConstructor
public class FortuneController {

    private final FortuneService userService;

    @GetMapping
    public ResponseEntity<String> getDailyFortune(HttpServletRequest request)
        throws NoAuthInfoException, NotRegisterDailyFortune {
        String userId = (String)(request.getAttribute("userId"));
        if(userId == null){
            throw new NoAuthInfoException();
        }
        return ResponseEntity.ok(userService.getDailyFortune(Long.parseLong(userId)));
    }

    @PostMapping
    public ResponseEntity<String> registDailyFortune(HttpServletRequest request)
        throws NoAuthInfoException {
        String userId = (String)(request.getAttribute("userId"));
        if(userId == null){
            throw new NoAuthInfoException();
        }
        return ResponseEntity.ok(userService.registDailyFortune(Long.parseLong(userId)));
    }
}
