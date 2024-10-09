package com.heartfoilo.demo.domain.portfolio.repository;

import com.heartfoilo.demo.domain.portfolio.entity.TotalAssets;
import com.heartfoilo.demo.domain.stock.entity.Like;
import com.heartfoilo.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


public interface TotalAssetsRepository extends JpaRepository<TotalAssets, Long> {
    Optional<List<TotalAssets>> findByUserId(long userId);
    TotalAssets findByStockId(long stockId);

    TotalAssets findByStockIdAndUserId(long stockId,Long userId);

    Optional<TotalAssets> findByUserIdAndStockId(long userId, long stockId);


}
