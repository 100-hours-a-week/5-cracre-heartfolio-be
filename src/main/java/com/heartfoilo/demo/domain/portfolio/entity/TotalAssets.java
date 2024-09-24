package com.heartfoilo.demo.domain.portfolio.entity;


import com.heartfoilo.demo.domain.stock.entity.Stock;
import com.heartfoilo.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalAssets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "total_quantity", nullable = false)
    private Long totalQuantity;

    @Column(name = "purchase_avg_price", nullable = false)
    private Long purchaseAvgPrice;

    public void ChangeQuantity(Long totalQuantity){
        this.totalQuantity = totalQuantity;
    }
    public void ChangeAvgPrice(Long purchaseAvgPrice){
        this.purchaseAvgPrice = purchaseAvgPrice;
    }


}
