package com.heartfoilo.demo.domain.fortune.repository;

import com.heartfoilo.demo.domain.fortune.entity.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FortuneRepository extends JpaRepository<Fortune, Long> {

}
