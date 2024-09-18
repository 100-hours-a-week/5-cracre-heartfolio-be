package com.heartfoilo.demo.domain.donation.repository;

import com.heartfoilo.demo.domain.donation.entity.Donation;
import com.heartfoilo.demo.domain.invest.entity.Order;
import com.heartfoilo.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("select o from Donation o" +
            " left join fetch o.payment p" +
            " left join fetch o.user u" +
            " where o.orderUid = :orderUid")
    Optional<Donation> findOrderAndPaymentAndMember(String orderUid); // 주문번호
    //
    @Query("select o from Donation o" +
            " left join fetch o.payment p" +
            " where o.orderUid = :orderUid")
    Optional<Donation> findOrderAndPayment(String orderUid);
}
