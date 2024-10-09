package com.heartfoilo.demo.util;

import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.dto.DailyNewsDto;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, StockSocketInfoDto> stockInfoTemplate;
    private final RedisTemplate<String, DailyNewsDto> dailyNewsTemplate;

    public void setStockInfoTemplate(String key, StockSocketInfoDto stockSocketInfoDto) {
        stockInfoTemplate.opsForValue().set(key, stockSocketInfoDto, 100, TimeUnit.MINUTES);
    }

    public StockSocketInfoDto getStockInfoTemplate(String key) {
        return stockInfoTemplate.opsForValue().get(key);
    }

    public boolean hasKeyStockInfo(String key) {
        return Boolean.TRUE.equals(stockInfoTemplate.hasKey(key));
    }

    public void setDailyNewsTemplate(String key, DailyNewsDto dailyNewsDto) {
        key = "news" + key;
        dailyNewsTemplate.opsForValue().set(key, dailyNewsDto, 1, TimeUnit.DAYS);
    }

    public DailyNewsDto getDailyNewsTemplate(String key) {
        key = "news" + key;
        return dailyNewsTemplate.opsForValue().get(key);
    }

    public boolean hasDailyNews(String key) {
        return Boolean.TRUE.equals(dailyNewsTemplate.hasKey("news" + key)) && getDailyNewsTemplate(key).getDate().equals(
            LocalDate.now());
    }

}