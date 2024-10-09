package com.heartfoilo.demo.domain.stock.service;

import com.heartfoilo.demo.domain.stock.constant.ErrorMessage;
import com.heartfoilo.demo.domain.stock.dto.responseDto.PopularStockResponseDto;
import com.heartfoilo.demo.domain.stock.entity.Stock;
import com.heartfoilo.demo.domain.stock.repository.StockRepository;
import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.global.exception.PopularStockNotFoundException;
import com.heartfoilo.demo.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularStockServiceImpl implements PopularStockService{

    private final StockRepository stockRepository;

    private final RedisUtil redisUtil;

    @Override
    public List<PopularStockResponseDto> getPopularStocks(int limit) {
        List<Stock> popularStocks=  stockRepository.findAll();



        List<PopularStockResponseDto> sortedStocks = popularStocks.stream()
                .map(stock -> {
                    int curPrice = 0;
                    int earningValue = 0;
                    float earningRate = 0;

                    // Redis 연결 실패 시 예외 처리
                    if (redisUtil.hasKeyStockInfo(stock.getSymbol())) {
                        StockSocketInfoDto stockInfo = redisUtil.getStockInfoTemplate(stock.getSymbol());
                        curPrice = stockInfo.getCurPrice();
                        earningValue = stockInfo.getEarningValue();
                        earningRate = stockInfo.getEarningRate();
                    }

                    return new PopularStockResponseDto(
                            stock.getId(),
                            0, // 순위
                            stock.getName(),
                            stock.getEnglishName(),
                            curPrice, // 현재가
                            earningValue,
                            earningRate,
                            stock.getSector()
                    );
                })
                // earningRate 기준으로 정렬
                .sorted(Comparator.comparing(PopularStockResponseDto::getEarningRate).reversed()) // 내림차순 정렬
                .limit(limit) // 결과를 제한
                .collect(Collectors.toList());

        AtomicInteger rankCounter = new AtomicInteger(1);
        sortedStocks.forEach(stock -> stock.setRank(rankCounter.getAndIncrement()));
        return sortedStocks;
    }
}
