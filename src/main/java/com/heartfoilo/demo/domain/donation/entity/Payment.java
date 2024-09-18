package com.heartfoilo.demo.domain.donation.entity;

import com.heartfoilo.demo.domain.donation.payment.PaymentStatus;
import com.heartfoilo.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id") // 변경된 컬럼 이름 매핑
    private Long id;
    private Long price;
    private PaymentStatus status;
    private String paymentUid; // 결제 고유 번호

    @Builder
    public Payment(Long price, PaymentStatus status) {
        this.price = price;
        this.status = status;
    }

    public void changePaymentBySuccess(PaymentStatus status, String paymentUid) {
        this.status = status;
        this.paymentUid = paymentUid;
    }
}
