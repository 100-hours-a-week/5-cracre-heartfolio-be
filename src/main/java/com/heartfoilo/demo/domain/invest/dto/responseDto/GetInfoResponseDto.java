package com.heartfoilo.demo.domain.invest.dto.responseDto;

import com.heartfoilo.demo.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetInfoResponseDto {
    private String orderCategory;
    private LocalDateTime orderDate;
    private Long orderAmount;
    private int orderPrice;
    private Long totalAmount;
    private Long id;
    private String name;
    public GetInfoResponseDto(String name, String orderCategory, LocalDateTime orderDate, Long orderAmount, int orderPrice, Long totalAmount, Long id) {
        this.name = name;
        this.orderCategory = orderCategory;
        this.orderDate = orderDate;
        this.orderAmount = orderAmount;
        this.orderPrice = orderPrice;
        this.totalAmount = totalAmount;
        this.id = id;
    }
}
