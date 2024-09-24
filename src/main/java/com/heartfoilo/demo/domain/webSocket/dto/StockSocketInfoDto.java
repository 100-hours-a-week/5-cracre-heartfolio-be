package com.heartfoilo.demo.domain.webSocket.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockSocketInfoDto {

    private String symbol;

    private int curPrice;

    private int openPrice;

    private int highPrice;

    private int lowPrice;

    private int earningValue;

    private float earningRate;

    private float changeRate;

    public StockSocketInfoDto(Map<String, String> input){
        this.symbol = input.get("rsym").substring(4);
        this.changeRate = Float.parseFloat(input.get("t_rate"));
        this.curPrice = Integer.parseInt(input.get("t_xprc"));
        this.openPrice = Math.round(Float.parseFloat(input.get("open")) * changeRate);
        this.highPrice = Math.round(Float.parseFloat(input.get("high")) * changeRate);
        this.lowPrice = Math.round(Float.parseFloat(input.get("low")) * changeRate);
        this.earningValue = curPrice - Math.round(Float.parseFloat(input.get("base")) * changeRate);
        this.earningRate = earningValue * 100.0f / (Float.parseFloat(input.get("base")) * changeRate);
    }


}

