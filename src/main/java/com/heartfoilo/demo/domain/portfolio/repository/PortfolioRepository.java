package com.heartfoilo.demo.domain.portfolio.repository;

import com.heartfoilo.demo.domain.invest.dto.responseDto.GetInfoResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import com.heartfoilo.demo.domain.portfolio.entity.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a JOIN a.user u WHERE u.id = :userId")
    Account findByUserId(@Param("userId") long userId);

}
