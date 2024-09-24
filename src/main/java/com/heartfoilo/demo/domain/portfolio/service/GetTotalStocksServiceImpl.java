package com.heartfoilo.demo.domain.portfolio.service;

import com.heartfoilo.demo.domain.portfolio.entity.TotalAssets;
import com.heartfoilo.demo.domain.portfolio.repository.TotalAssetsRepository;
import com.heartfoilo.demo.domain.stock.entity.Stock;
import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class GetTotalStocksServiceImpl implements GetTotalStocksService{

    public Map<String, Object> createStockMap(Long stockId, String stockName, Long totalQuantity,
                                              Long purchaseAvgPrice, Long totalPurchasePrice,
                                              Long evalValue, Long evalProfit, Double profitPercentage) {
        Map<String, Object> stockMap = new HashMap<>();
        stockMap.put("stockId", stockId);
        stockMap.put("name", stockName);
        stockMap.put("totalQuantity", totalQuantity);
        stockMap.put("purchaseAvgPrice", purchaseAvgPrice);
        stockMap.put("totalPurchasePrice", totalPurchasePrice);
        stockMap.put("evalValue", evalValue); //
        stockMap.put("evalProfit", evalProfit); //
        stockMap.put("profitPercentage", profitPercentage); //
        return stockMap;
    }
    private final TotalAssetsRepository totalAssetsRepository;
    private final RedisUtil redisUtil;
    @Override
    public ResponseEntity<Map<String,Object>> getTotalStocks(long userId){
        Optional<List<TotalAssets>> totalAssets = totalAssetsRepository.findByUserId(userId);

        if(totalAssets == null){
            return ResponseEntity.ok(Collections.emptyMap());
        } // 주식 보유 존재x시 예외처리
        List<Map<String, Object>> totalAssetsList = new ArrayList<>();

        TotalAssets[] assetsArray = totalAssets.get().toArray(new TotalAssets[0]);
        for (TotalAssets asset : assetsArray) {

            Stock findStock = asset.getStock();
            String stockName = findStock.getName();
            Long totalQuantity = asset.getTotalQuantity();
            Long purchaseAvgPrice = asset.getPurchaseAvgPrice(); // 여기까지가 정적으로 받아오는 값
            Long totalPurchasePrice = totalQuantity * purchaseAvgPrice; // 총매수값



            // TODO : REDIS 내에 값이 없는 경우 예외처리 필요 //
            StockSocketInfoDto stockInfo = redisUtil.getStockInfoTemplate(asset.getStock().getSymbol()); // TODO: NULL값 에러 발생


            int nowPrice = stockInfo.getCurPrice();
            // 여기서부터 3개는 소켓 변동값 ##
            Long evalValue = totalQuantity * nowPrice; // 현재 평가금액
            Long evalProfit = evalValue - totalPurchasePrice; // 평가금액에서 총매수금액을 뺀게 평가손익
            double profitPercentage = evalProfit * 100/(float)totalPurchasePrice; // 수익률
            DecimalFormat df = new DecimalFormat("#.##");
            profitPercentage = Double.parseDouble(df.format(profitPercentage));

            Map<String, Object> stockMap = createStockMap(findStock.getId(), stockName, totalQuantity, purchaseAvgPrice,
                    totalPurchasePrice, evalValue, evalProfit, profitPercentage);


            // 리스트에 추가
            totalAssetsList.add(stockMap);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("stocks", totalAssetsList);

        return ResponseEntity.ok(response);



    }
}
