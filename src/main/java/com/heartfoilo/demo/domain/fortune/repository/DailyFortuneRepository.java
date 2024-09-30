package com.heartfoilo.demo.domain.fortune.repository;

import com.heartfoilo.demo.domain.fortune.entity.DailyFortune;
import com.heartfoilo.demo.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyFortuneRepository extends JpaRepository<DailyFortune, Long> {

    Optional<DailyFortune> findByUser(User user);
}
