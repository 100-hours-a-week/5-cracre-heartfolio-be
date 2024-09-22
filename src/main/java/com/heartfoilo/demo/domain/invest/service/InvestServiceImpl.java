package com.heartfoilo.demo.domain.invest.service;

import com.heartfoilo.demo.domain.invest.dto.requestDto.InvestRequestDto;
import com.heartfoilo.demo.domain.invest.entity.Order;
import com.heartfoilo.demo.domain.invest.repository.InvestRepository;
import com.heartfoilo.demo.domain.portfolio.entity.Account;
import com.heartfoilo.demo.domain.portfolio.entity.TotalAssets;
import com.heartfoilo.demo.domain.portfolio.repository.PortfolioRepository;
import com.heartfoilo.demo.domain.portfolio.repository.TotalAssetsRepository;
import com.heartfoilo.demo.domain.stock.entity.Stock;
import com.heartfoilo.demo.domain.stock.repository.StockRepository;
import com.heartfoilo.demo.domain.user.entity.User;
import com.heartfoilo.demo.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class InvestServiceImpl implements InvestService{

    @Autowired
    private TotalAssetsRepository totalAssetsRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private InvestRepository investRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserRepository userRepository;

    public Order createOrder(Long userId, String orderCategory, Long nowQuantity, int nowAvgPrice, Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found with id: " + stockId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Order orders = new Order();
        orders.setUser(user);
        orders.setOrderCategory(orderCategory);
        orders.setOrderDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        orders.setOrderAmount(nowQuantity);
        orders.setOrderPrice(nowAvgPrice);
        orders.setTotalAmount(nowQuantity * nowAvgPrice);
        orders.setStock(stock);
        orders.setUser(user);
        return orders;
    } // order 객체 새로 만들어주는 기능

    public ResponseEntity<?> order(InvestRequestDto getInfoRequestDto,long userId){
        Long stockId = getInfoRequestDto.getStockId();
        Long quantity = getInfoRequestDto.getQuantity();
        long price = getInfoRequestDto.getPrice();

        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new RuntimeException("Stock not found")); // 예외처리는 언제나!^_^
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));


        TotalAssets totalAssets = totalAssetsRepository.findByStockIdAndUserId(stockId,userId);
        if (totalAssets == null) {
            totalAssets = TotalAssets.builder()
                    .stock(stock)
                    .user(user)
                    .totalQuantity(quantity)
                    .purchaseAvgPrice(price)
                    .build(); // Setter 사용 지양 -> builder로 변경
            Order orders = createOrder(userId, "buy", quantity, (int) price,stockId); // 조치완료

            investRepository.save(orders);

            Account account =  portfolioRepository.findByUserId(userId); // TODO : userId JWT로 대체

            Long cash = account.getCash();
            Long totalPurchase = account.getTotalPurchase();
            if (cash >= quantity * price) {
                account.ChangeCash(quantity * price); // 여기서 cash , totalPurchase 모두 변경 가능

            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잔액이 부족합니다.");
            }
            portfolioRepository.save(account);
            totalAssetsRepository.save(totalAssets);
            return ResponseEntity.ok("자산이 성공적으로 업데이트되었습니다.");
        }
        Long nowQuantity = totalAssets.getTotalQuantity();
        Long nowAvgPrice = totalAssets.getPurchaseAvgPrice();

        nowAvgPrice = ((nowQuantity * nowAvgPrice) + (quantity * price)) / (nowQuantity + quantity);
        nowQuantity = nowQuantity + quantity ;
        totalAssets.ChangeQuantity(nowQuantity);
        totalAssets.ChangeAvgPrice(nowAvgPrice);



        // orders 엔티티에 있는 내역 업데이트.
        Order orders = createOrder(userId, "buy", quantity, (int) price,stockId); // 조치완료

        investRepository.save(orders);

        // Account 엔티티 업데이트
        Account account =  portfolioRepository.findByUserId(userId); // TODO : userId JWT로 대체

        Long cash = account.getCash();
        Long totalPurchase = account.getTotalPurchase();
        if (cash >= quantity * price) {
            account.ChangeCash(quantity * price);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잔액이 부족합니다.");
        }
        portfolioRepository.save(account);



        totalAssetsRepository.save(totalAssets);
        return ResponseEntity.ok("buy order successfully processed and total assets updated.");
    }


    public ResponseEntity<?> sell(InvestRequestDto getInfoRequestDto,long userId){
        Long stockId = getInfoRequestDto.getStockId();
        Long quantity = getInfoRequestDto.getQuantity(); // 요청한 수량
        long price = getInfoRequestDto.getPrice();

        TotalAssets totalAssets = totalAssetsRepository.findByStockIdAndUserId(stockId,userId);
        if (totalAssets == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 자산을 찾을 수 없습니다.");
        } // 팔려는데 Null이면 그건 문제가 있는거임
        Long nowQuantity = totalAssets.getTotalQuantity();
        Long nowAvgPrice = totalAssets.getPurchaseAvgPrice(); // 현재 평단가
        nowQuantity = nowQuantity - quantity ; // 판만큼 빼주고 , 판매시 평단가는 그대로임
        Order orders = createOrder( userId, "sell", quantity, (int)price,stockId);
        if (nowQuantity < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("판매할 수량이 보유 수량보다 많습니다.");
        }
        investRepository.save(orders);

        Account account = portfolioRepository.findByUserId(userId); // 임시 코드
        Long cash = account.getCash(); // 현재 잔액 조회
        Long totalPurchase = account.getTotalPurchase();

        account.ChangeCash(quantity * price);


        portfolioRepository.save(account);
        if (nowQuantity == 0) {
            totalAssetsRepository.delete(totalAssets);
            return ResponseEntity.ok("자산이 전부 판매되어 삭제되었습니다.");
        } else {
            // total_quantity가 0이 아니면 업데이트 후 저장
            totalAssets.ChangeQuantity(nowQuantity); // quantity 변경
            totalAssetsRepository.save(totalAssets); // 항목들 변경해주고 save
            return ResponseEntity.ok("Sell order successfully processed and total assets updated.");
        }



    } // 만약 총수량이 0이된다면 레포지토리에서 삭제해야 하나?
}
