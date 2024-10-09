package com.heartfoilo.demo.config;

import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.dto.DailyNewsDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.data.cluster.nodes}")
    private List<String> clusterNodes;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisClusterConfiguration());
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.data")
    public RedisClusterConfiguration redisClusterConfiguration() {
        return new RedisClusterConfiguration(clusterNodes);
    }

    @Bean
    public RedisTemplate<String, StockSocketInfoDto> redisTemplateStock() {
        RedisTemplate<String, StockSocketInfoDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(StockSocketInfoDto.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, DailyNewsDto> redisTemplateNews() {
        RedisTemplate<String, DailyNewsDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(DailyNewsDto.class));
        return redisTemplate;
    }

}
