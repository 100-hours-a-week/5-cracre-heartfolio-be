package com.heartfoilo.demo.domain.invest.repository;
import com.heartfoilo.demo.domain.invest.dto.responseDto.GetInfoResponseDto;
import com.heartfoilo.demo.domain.invest.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvestRepository extends JpaRepository<Order, Long> {

    @Query("SELECT new com.heartfoilo.demo.domain.invest.dto.responseDto.GetInfoResponseDto(s.name, o.orderCategory, o.orderDate, o.orderAmount, o.orderPrice, o.totalAmount, o.id) " +
            "FROM Order o " +
            "JOIN o.user u " +
            "JOIN o.stock s " +
            "WHERE u.id = :userId")
    List<GetInfoResponseDto> findByUserId(@Param("userId") Long userId);

}

