package com.heartfoilo.demo.domain.donation.entity;

import com.heartfoilo.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "donation")
@NoArgsConstructor
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long price;
    private String itemName;
    private String orderUid; // 주문 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public Donation(Long price, String itemName, String orderUid, User user, Payment payment) {
        this.price = price;
        this.itemName = itemName;
        this.orderUid = orderUid;
        this.user = user;
        this.payment = payment;
    }


}