package com.heartfoilo.demo.domain.invest.repository;

import com.heartfoilo.demo.domain.invest.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdAndStockId(Long userId, Long stockId);

}
