package com.heartfoilo.demo.domain.portfolio.service;

import com.heartfoilo.demo.domain.portfolio.entity.TotalAssets;
import com.heartfoilo.demo.domain.portfolio.repository.TotalAssetsRepository;
import com.heartfoilo.demo.domain.stock.entity.Stock;
import com.heartfoilo.demo.domain.stock.repository.StockRepository;
import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.util.RedisUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GetStocksServiceImpl implements GetStocksService{



    private final RedisUtil redisUtil;

    private final TotalAssetsRepository totalAssetsRepository;

    private final StockRepository stockRepository;

    @Override
    public ResponseEntity<Map<String, Object>> getStocks(long userId) {
        Optional<List<TotalAssets>> allAssets = totalAssetsRepository.findByUserId(userId);
        // TODO : 오류 수정
        if(allAssets.get().isEmpty()){
            return ResponseEntity.ok(Collections.emptyMap()); // emptyMap 반환
        }



        List<Map<String, Object>> stockList = new ArrayList<>();
        for (TotalAssets asset : allAssets.get()) {

            Stock findStock = asset.getStock();
            String stockName = findStock.getName();

            Stock stock = stockRepository.findById(asset.getStock().getId())
                    .orElseThrow(() -> new EntityNotFoundException("주식 섹터 검색 실패"));


            String sector = stock.getSector();

            StockSocketInfoDto stockInfo = redisUtil.getStockInfoTemplate(asset.getStock().getSymbol()); // redis 정보 가져오는 코드
            long evalPrice = stockInfo.getCurPrice() * asset.getTotalQuantity(); // 현재가 * 보유 수량
            Map<String, Object> stockMap = new HashMap<>();
            stockMap.put("id", findStock.getId());
            stockMap.put("stockName", stockName);
            stockMap.put("evalPrice", evalPrice);
            stockMap.put("sector", sector);

            // 리스트에 추가
            stockList.add(stockMap);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("stocks", stockList);

        return ResponseEntity.ok(response);


    }

}
