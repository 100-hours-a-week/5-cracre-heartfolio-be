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
        throws NotRegisterDailyFortune {
        Long userId = (Long) request.getAttribute("userId");
        if(userId == null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(userService.getDailyFortune(userId));
    }

    @PostMapping
    public ResponseEntity<String> registDailyFortune(HttpServletRequest request)
        throws NoAuthInfoException {
        Long userId = (Long) request.getAttribute("userId");
        if(userId == null){
            throw new NoAuthInfoException();
        }
        return ResponseEntity.ok(userService.registDailyFortune(userId));
    }
}
