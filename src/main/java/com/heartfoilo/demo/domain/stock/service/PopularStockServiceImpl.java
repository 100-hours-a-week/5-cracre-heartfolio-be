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
        Pageable pageable = PageRequest.of(0, limit);
        List<Stock> popularStocks=  stockRepository.findAllByOrderByEarningRateDesc(pageable);

        AtomicInteger rankCounter = new AtomicInteger(1);

        return popularStocks.stream()
                .map(stock -> {
                    int curPrice = 0;
                    int earningValue = 0;
                    float earningRate = 0;
                    //TODO: Redis연결 실패했을 때 예외처리
                    if (redisUtil.hasKeyStockInfo(stock.getSymbol())) {
                        StockSocketInfoDto stockInfo = redisUtil.getStockInfoTemplate(stock.getSymbol());
                        curPrice = stockInfo.getCurPrice();
                        earningValue = stockInfo.getEarningValue();
                        earningRate = stockInfo.getEarningRate();
                    }

                    return new PopularStockResponseDto(
                            stock.getId(),
                            rankCounter.getAndIncrement(), // 순위
                            stock.getName(),
                            stock.getEnglishName(),
                            curPrice, // 현재가
                            earningValue,
                            earningRate,
                            stock.getSector()
                    );
                }).collect(Collectors.toList());
    }
}
