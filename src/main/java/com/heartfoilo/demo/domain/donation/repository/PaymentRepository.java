package com.heartfoilo.demo.domain.donation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.heartfoilo.demo.domain.donation.entity.Payment;
public interface PaymentRepository extends JpaRepository<Payment,Long> {


}
