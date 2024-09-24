package com.heartfoilo.demo.domain.ranking.entity;

import com.heartfoilo.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private float sumReturn;

    @Column(nullable = false)
    private float monthlyReturn;

    @Column(nullable = false)
    private Long donation;

    public Ranking(User user) {
        this.user = user;
        this.sumReturn = 0.0f;
        this.monthlyReturn = 0.0f;
        this.donation = 0L;
    }
    public void updateMonthlyReturn(float newMonthlyReturn) {
        this.monthlyReturn = newMonthlyReturn;
    }

    // 수익률 갱신 메서드: 누적 수익률 갱신
    public void updateSumReturn(float newSumReturn) {
        this.sumReturn = newSumReturn;
    }

    public void updateDonation(Long donation) { this.donation = donation; }
}
