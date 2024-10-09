package com.heartfoilo.demo.domain.webSocket.api;

import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisUtil redisUtil;

    @MessageMapping("/stock/{symbol}")
    public void stockInfo(@DestinationVariable(value = "symbol") String symbol){
        if(redisUtil.hasKeyStockInfo(symbol)){
            messagingTemplate.convertAndSend("/from/stock/"+symbol, redisUtil.getStockInfoTemplate(symbol));
            return;
        }
        messagingTemplate.convertAndSend("/from/stock/"+symbol, StockSocketInfoDto.builder().symbol("AAPL").curPrice(2263600)
            .openPrice(2200000).highPrice(2400000).lowPrice(2100000).earningValue(11000).earningRate(11.1f).build());
    }
}
