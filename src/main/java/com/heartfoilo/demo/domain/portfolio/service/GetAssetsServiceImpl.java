package com.heartfoilo.demo.domain.portfolio.service;

import com.heartfoilo.demo.domain.portfolio.dto.responseDto.AssetsResponseDto;
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
    public ResponseEntity<AssetsResponseDto> getAssets(long userId) { // 보유 자산 조회 API
        Map<String, Object> responseMap = new HashMap<>();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Account account = portfolioRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Account not found"));

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
        long totalAmount = totalValue + cash; // 총 자산
        double profitRate = totalPurchase > 0 ? (double) (totalValue - totalPurchase) * 100 / totalPurchase : 0;

        // 수익률 포맷팅
        DecimalFormat df = new DecimalFormat("#.##");
        profitRate = Double.parseDouble(df.format(profitRate));

        // DTO 생성
        AssetsResponseDto responseDto = new AssetsResponseDto(cash, totalPurchase, totalAmount, totalValue, profitRate);
        return ResponseEntity.ok(responseDto);
    }
}
