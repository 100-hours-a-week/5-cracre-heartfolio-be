package com.heartfoilo.demo.domain.stock.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @Column(nullable = false, length = 48)
    private String name;

    @Column(nullable = false, length = 64)
    private String sector;

    @Column(nullable = false, length = 64)
    private String symbol;

    @Column(nullable = false, length = 48)
    private String type;



    @Column(name = "earning_rate", nullable = false)
    private float earningRate;

    @Column(name = "english_name", nullable = false)
    private String englishName;

    @Builder
    public Stock(String name, String sector, String symbol, String type, float earningRate, String englishName) {
        this.name = name;
        this.sector = sector;
        this.symbol = symbol;
        this.type = type;
        this.earningRate = earningRate;
        this.englishName = englishName;
    }

}