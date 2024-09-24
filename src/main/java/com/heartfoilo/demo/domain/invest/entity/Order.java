package com.heartfoilo.demo.domain.invest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.heartfoilo.demo.domain.stock.entity.Stock;
import com.heartfoilo.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")  // 테이블 이름을 'orders'로 변경
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;


    @Column(name = "order_category", nullable = false, length = 4)
    private String orderCategory;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;


    @Column(name = "order_amount", nullable = false)
    private Long orderAmount;

    @Column(name = "order_price", nullable = false)
    private int orderPrice;


    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    @JsonIgnore
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}