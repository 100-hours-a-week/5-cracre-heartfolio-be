package com.heartfoilo.demo.domain.stock.service;

import com.heartfoilo.demo.domain.stock.constant.ErrorMessage;
import com.heartfoilo.demo.domain.stock.dto.responseDto.LikeStockResponseDto;
import com.heartfoilo.demo.domain.stock.dto.responseDto.PopularStockResponseDto;
import com.heartfoilo.demo.domain.stock.entity.Like;
import com.heartfoilo.demo.domain.stock.entity.Stock;
import com.heartfoilo.demo.domain.stock.repository.LikeRepository;
import com.heartfoilo.demo.domain.stock.repository.StockRepository;
import com.heartfoilo.demo.domain.user.entity.User;
import com.heartfoilo.demo.domain.user.repository.UserRepository;
import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.global.exception.LikeStockNotFoundException;
import com.heartfoilo.demo.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final RedisUtil redisUtil;


    @Override
    public Like addFavorite(Long userId, Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.STOCK_NOT_FOUND));

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND));
        //테이블에 존재하는지 확인
        Optional<Like> existingLike = likeRepository.findByUserIdAndStockId(userId, stockId);
        if (existingLike.isPresent()) {
            // 이미 존재하는 경우, 그대로 반환
            return existingLike.get();
        }
        // 새로운 Like 엔티티 생성 및 저장
        Like like = Like.builder()
                .user(user)
                .stock(stock)
                .build();

        return likeRepository.save(like);
    }

    @Override
    public List<LikeStockResponseDto> getFavorites(Long userId) {
        List<Like> likes = likeRepository.findByUserId(userId);

        return likes.stream()
                .map(like -> {
                    int curPrice = 0;
                    int earningValue = 0;
                    float earningRate = 0;
                    //TODO: Redis연결 실패했을 때 예외처리
                    if (redisUtil.hasKeyStockInfo(like.getStock().getSymbol())) {
                        StockSocketInfoDto stockInfo = redisUtil.getStockInfoTemplate(like.getStock().getSymbol());
                        curPrice = stockInfo.getCurPrice();
                        earningValue = stockInfo.getEarningValue();
                        earningRate = stockInfo.getEarningRate();
                    }

                    return new LikeStockResponseDto(
                            like.getStock().getId(),
                            like.getStock().getName(),
                            like.getStock().getEnglishName(),
                            curPrice,
                            earningValue,
                            earningRate,
                            like.getStock().getSector()
                    );
                }).collect(Collectors.toList());
    }

    @Override
    public void removeFavorite(Long userId, Long stockId) {
        Like like = likeRepository.findByUserIdAndStockId(userId, stockId)
                .orElseThrow(() -> new LikeStockNotFoundException(ErrorMessage.DELETE_STOCK_NOT_FOUND));

        // 좋아요 항목을 삭제합니다.
        likeRepository.delete(like);
    }
}
