package com.heartfoilo.demo.domain.fortune.service;

import com.heartfoilo.demo.domain.fortune.exception.NotRegisterDailyFortune;
import org.springframework.stereotype.Service;

@Service
public interface FortuneService {

    String getDailyFortune(Long userId) throws NotRegisterDailyFortune;
    String registDailyFortune(Long userId);
}
