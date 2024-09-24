package com.heartfoilo.demo.domain.donation.service;

import com.heartfoilo.demo.domain.donation.entity.Donation;
import com.heartfoilo.demo.domain.donation.entity.Payment;
import com.heartfoilo.demo.domain.donation.payment.PaymentStatus;
import com.heartfoilo.demo.domain.donation.repository.DonationRepository;
import com.heartfoilo.demo.domain.donation.repository.PaymentRepository;
import com.heartfoilo.demo.domain.invest.repository.OrderRepository;
import com.heartfoilo.demo.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Donation makeDontaion(User user,Long cash){ // token을 통해 Controller에서 Id를 가져온다

        Payment payment = Payment.builder().price(cash) // TODO: 하드코딩 풀기 필요
                .status(PaymentStatus.READY)
                .build();

        paymentRepository.save(payment);

        Donation donation = Donation.builder()
                .user(user)
                .price(cash) // TODO: 하드코딩 풀기 필요
                .itemName("캐시충전")
                .orderUid(UUID.randomUUID().toString()) // 주문번호
                .payment(payment)
                .build();

        return donationRepository.save(donation);


    } // 결제내역 및 기부내역 저장

}
