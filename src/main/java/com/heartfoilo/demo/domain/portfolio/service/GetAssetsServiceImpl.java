package com.heartfoilo.demo.domain.portfolio.service;

import com.heartfoilo.demo.domain.portfolio.entity.Account;
import com.heartfoilo.demo.domain.portfolio.entity.TotalAssets;
import com.heartfoilo.demo.domain.portfolio.repository.PortfolioRepository;
import com.heartfoilo.demo.domain.portfolio.repository.TotalAssetsRepository;
import com.heartfoilo.demo.domain.user.entity.User;
import com.heartfoilo.demo.domain.user.repository.UserRepository;
import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GetAssetsServiceImpl implements GetAssetsService{
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final TotalAssetsRepository totalAssetsRepository;
    private final RedisUtil redisUtil;
    @Override
    public ResponseEntity<Map<String,Object>> getAssets(long userId) { // 보유 자산 조회 API
        Map<String, Object> responseMap = new HashMap<>();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Account account = portfolioRepository.findByUserId(userId);

        if (account == null) {
            return ResponseEntity.ok(Collections.emptyMap());
            // FIX :존재하지 않는다면 ,,, 근데 사실 존재하지 않으면 그건 데이터 설계가 잘못된거임
        }


        long cash = account.getCash();
        long totalPurchase = account.getTotalPurchase();



        // 이미 리스트로 반환됨
        Optional<List<TotalAssets>> totalAssetsList = totalAssetsRepository.findByUserId(userId);
        Long totalValue = 0L;
        if (totalAssetsList == null){
            totalValue = 0L;
        }// 만약 유저가 주식을 하나도 구매하지 않은 경우
        else {
            for (TotalAssets asset : totalAssetsList.get()) {
                // 각 totalAsset에 대해 처리할 로직 작성

                StockSocketInfoDto stockInfo = redisUtil.getStockInfoTemplate(asset.getStock().getSymbol()); // TODO: NULL값 에러 발생
                if (stockInfo != null) {
                    totalValue += (asset.getTotalQuantity() * stockInfo.getCurPrice());
                }

            }
        }
        // 값들을 Map에 추가
        responseMap.put("cash", cash); // 보유 캐시
        responseMap.put("totalPurchase", totalPurchase); // 총매수금액
        responseMap.put("totalAmount", totalValue + cash); // 총 자산
        responseMap.put("totalValue", totalValue); // 총평가금액
        double profitRate = totalPurchase > 0 ? (double)(totalValue - totalPurchase) * 100 / totalPurchase : 0;
        DecimalFormat df = new DecimalFormat("#.##");
        profitRate = Double.parseDouble(df.format(profitRate));
        responseMap.put("profitRate", profitRate);

        return ResponseEntity.ok(responseMap);
    }
}
