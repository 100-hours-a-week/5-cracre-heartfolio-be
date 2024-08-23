package com.heartfoilo.demo.domain.portfolio.service;

import com.heartfoilo.demo.domain.invest.repository.InvestRepository;
import com.heartfoilo.demo.domain.invest.dto.responseDto.GetInfoResponseDto;
import com.heartfoilo.demo.domain.portfolio.entity.TotalAssets;
import com.heartfoilo.demo.domain.portfolio.repository.PortfolioRepository;
import com.heartfoilo.demo.domain.portfolio.repository.TotalAssetsRepository;
import com.heartfoilo.demo.domain.stock.entity.Stock;
import com.heartfoilo.demo.domain.user.entity.User;
import com.heartfoilo.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heartfoilo.demo.domain.stock.repository.StockRepository;
import com.heartfoilo.demo.domain.portfolio.entity.Account;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {


    private final UserRepository userRepository;


    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TotalAssetsRepository totalAssetsRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private InvestRepository investRepository;

    public Map<String, Object> createStockMap(Long stockId, String stockName, Long totalQuantity,
                                              Long purchaseAvgPrice, Long totalPurchasePrice,
                                              Long evalValue, Long evalProfit, Double profitPercentage) {
        Map<String, Object> stockMap = new HashMap<>();
        stockMap.put("stock_id", stockId);
        stockMap.put("name", stockName);
        stockMap.put("total_quantity", totalQuantity);
        stockMap.put("purchase_avg_price", purchaseAvgPrice);
        stockMap.put("total_purchase_price", totalPurchasePrice);
        stockMap.put("evalValue", evalValue);
        stockMap.put("evalProfit", evalProfit);
        stockMap.put("profitPercentage", profitPercentage);

        return stockMap;
    }
    @Override
    public Map<String,Object> getAssets(long userId) { // 보유 자산 조회 API

        User user = userRepository.findById(userId);
        Optional<Account> accountOpt = portfolioRepository.findById(userId);

        Account account = accountOpt.get(); // 꺼내오고

        long cash = account.getCash();
        long totalPurchase = account.getTotalPurchase();


        Map<String, Object> responseMap = new HashMap<>();

        // 값들을 Map에 추가
        responseMap.put("cash", cash);
        responseMap.put("totalPurchase", totalPurchase);
        responseMap.put("totalAmount", 10000000);
        responseMap.put("totalValue", 10000000);
        responseMap.put("profitRate", -10.4);
// 요기 이제 소켓 데이터 들어가면 됨
        return responseMap;
    }


    @Override
    public Map<String,Object> getStocks(long userId) {
        List<TotalAssets> allAssets = totalAssetsRepository.findAll();
        // 여기서 stockId를 가져오고 , 이후에 stock에서 stockName을 찾아온다.


        List<Map<String, Object>> stockList = new ArrayList<>();
        // 3. 배열의 각 항목에 접근하여 필요한 값 가져오기
        for (TotalAssets asset : allAssets) {
            Long stockId = asset.getStockId();
            Optional<Stock> optionalStock = stockRepository.findById(stockId);
            Stock findStock = optionalStock.get();
            String stockName = findStock.getName();
            double percentage = 10.4; // 실시간 소켓 변동값
            Map<String, Object> stockMap = new HashMap<>();
            stockMap.put("id", stockId);
            stockMap.put("stock_name", stockName);
            stockMap.put("percentage", percentage);

            // 리스트에 추가
            stockList.add(stockMap);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("stocks", stockList);

        return response;


    }

    @Override
    public List<GetInfoResponseDto> getInvestInfo(){

        List<GetInfoResponseDto> order = investRepository.findAllGetInfoResponseDto();

        return order;
    }

    public Map<String,Object> getTotalStocks(){
        List <TotalAssets> totalAssets = totalAssetsRepository.findAll();
        // id , userId , stockId , quantity , purchaseAvgPrice
        List<Map<String, Object>> totalAssetsList = new ArrayList<>();

        TotalAssets[] assetsArray = totalAssets.toArray(new TotalAssets[0]);
        for (TotalAssets asset : assetsArray) {
            Long stockId = asset.getStockId(); // stockid get
            Optional<Stock> optionalStock = stockRepository.findById(stockId);
            Stock findStock = optionalStock.get();
            String stockName = findStock.getName();
            Long totalQuantity = asset.getTotalQuantity();
            Long purchaseAvgPrice = asset.getPurchaseAvgPrice(); // 여기까지가 정적으로 받아오는 값
            Long totalPurchasePrice = totalQuantity * purchaseAvgPrice; // 총매수값

            int nowPrice = 1000000; // 소켓 변동값이 없어서 , nowPrice를 기준으로 계산
            // 여기서부터 3개는 소켓 변동값 ##
            Long evalValue = totalQuantity * nowPrice; // 현재 평가금액
            Long evalProfit = evalValue - totalPurchasePrice; // 평가금액에서 총매수금액을 뺀게 평가손익
            double profitPercentage = evalProfit/(float)totalPurchasePrice; // 수익률


            Map<String, Object> stockMap = createStockMap(stockId, stockName, totalQuantity, purchaseAvgPrice,
                    totalPurchasePrice, evalValue, evalProfit, profitPercentage);


            // 리스트에 추가
            totalAssetsList.add(stockMap);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("stocks", totalAssetsList);

        return response;



    }
}
