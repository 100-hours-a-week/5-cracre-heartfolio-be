package com.heartfoilo.demo.domain.stock.repository;

import com.heartfoilo.demo.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findAllByOrderByEarningRateDesc(Pageable pageable);

    @Query("SELECT s FROM Stock s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.symbol) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.englishName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Stock> searchStock(String keyword);

}

