package com.heartfoilo.demo.domain.stock.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderHistoryResponseDto {
    private Long id;              // order_id
    private String orderCategory; // order_category (buy, sell)
    private LocalDateTime orderDate; // order_date
    private Long orderAmount;      // order_amount (수량)
    private int orderPrice;       // order_price (평단가)
}
