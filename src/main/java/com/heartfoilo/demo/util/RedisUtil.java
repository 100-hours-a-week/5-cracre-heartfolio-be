package com.heartfoilo.demo.util;

import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.dto.DailyNewsDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;
    private final RedisTemplate<String, Object> stockInfoTemplate;
    private final RedisTemplate<String, Object> dailyNewsTemplate;

//    public RedisUtil(RedisTemplate<String, Object> redisTemplate,
//                     RedisTemplate<String, Object> redisBlackListTemplate,
//                     RedisTemplate<String, Object> stockInfoTemplate) {
//        this.redisTemplate = redisTemplate;
//        this.redisBlackListTemplate = redisBlackListTemplate;
//        this.stockInfoTemplate = stockInfoTemplate;
//    }
//    @Value("${jwt.expmin}")
    private int expMin;

    public void set(String key, Object o, int minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(o.getClass()));
        redisTemplate.opsForValue().set(key, o, minutes, TimeUnit.MINUTES);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void setExcludeList(String key, Object o) {
        redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(o.getClass()));
        redisBlackListTemplate.opsForValue().set(key, o, expMin, TimeUnit.MINUTES);
    }

    public Object getExcludeList(String key) {
        return redisBlackListTemplate.opsForValue().get(key);
    }

    public boolean deleteExcludeList(String key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.delete(key));
    }

    public boolean hasKeyExcludeList(String key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.hasKey(key));
    }

    public void setStockInfoTemplate(String key, Object o) {
        stockInfoTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(o.getClass()));
        stockInfoTemplate.opsForValue().set(key, o, 100, TimeUnit.MINUTES);
    }

    public StockSocketInfoDto getStockInfoTemplate(String key) {
        stockInfoTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(StockSocketInfoDto.class));
        return (StockSocketInfoDto) stockInfoTemplate.opsForValue().get(key);
    }

    public boolean hasKeyStockInfo(String key) {
        return Boolean.TRUE.equals(stockInfoTemplate.hasKey(key));
    }

    public void setDailyNewsTemplate(String key, Object o) {
        key = "news" + key;
        dailyNewsTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(o.getClass()));
        dailyNewsTemplate.opsForValue().set(key, o, 1, TimeUnit.DAYS);
    }

    public DailyNewsDto getDailyNewsTemplate(String key) {
        key = "news" + key;
        dailyNewsTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(DailyNewsDto.class));
        return (DailyNewsDto) dailyNewsTemplate.opsForValue().get(key);
    }

    public boolean hasDailyNews(String key) {
        return Boolean.TRUE.equals(dailyNewsTemplate.hasKey("news" + key)) && getDailyNewsTemplate(key).getDate().equals(
            LocalDate.now());
    }



}