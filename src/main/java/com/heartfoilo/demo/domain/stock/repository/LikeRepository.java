package com.heartfoilo.demo.domain.stock.repository;

import com.heartfoilo.demo.domain.stock.entity.Like;
import com.heartfoilo.demo.domain.stock.entity.Stock;
import com.heartfoilo.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("SELECT l FROM Like l WHERE l.user.id = :userId")
    List<Like> findByUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.stock.id = :stockId")
    Optional<Like> findByUserIdAndStockId(@Param("userId") Long userId, @Param("stockId") Long stockId);
}
