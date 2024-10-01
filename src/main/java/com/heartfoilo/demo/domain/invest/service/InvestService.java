package com.heartfoilo.demo.domain.invest.service;

import com.heartfoilo.demo.domain.invest.dto.requestDto.InvestRequestDto;
import com.heartfoilo.demo.domain.invest.entity.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface InvestService {

    public Order createOrder(Long userId, String orderCategory, Long nowQuantity, int nowAvgPrice, Long stockId);
    public ResponseEntity<?> sell(InvestRequestDto getInfoRequestDto, long userId);
}
