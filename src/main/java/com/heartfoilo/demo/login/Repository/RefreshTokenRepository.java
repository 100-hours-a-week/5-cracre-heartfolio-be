package com.heartfoilo.demo.login.Repository;

import com.heartfoilo.demo.login.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(Long userId);

    Optional<RefreshToken> findByUserId(Long userId);
}
