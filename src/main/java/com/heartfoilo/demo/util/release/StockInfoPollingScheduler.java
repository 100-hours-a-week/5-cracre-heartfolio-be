package com.heartfoilo.demo.util.release;

import com.heartfoilo.demo.domain.stock.entity.Stock;
import com.heartfoilo.demo.domain.stock.repository.StockRepository;
import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.dto.RequestOauthDto;
import com.heartfoilo.demo.util.RedisUtil;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "scheduler.polling", havingValue = "realServer")
public class StockInfoPollingScheduler {

   private final StockRepository stockRepository;
   private final RedisUtil redisUtil;
   private final SimpMessagingTemplate simpMessagingTemplate;
   private List<Stock> stocks;
   static private WebClient webClient = WebClient.builder().build();
   static int idx;



   @Value("${korea-invest.host}")
   private String host;

   @Value("${korea-invest.port}")
   private String port;

   @Value("${korea-invest.url}")
   private String url;

   @Value("${korea-invest.oauth-url}")
   private String oauthUrl;

   @Value("${korea-invest.app-key}")
   private String appKey;

   @Value("${korea-invest.app-secret}")
   private String appSecret;

   @Value("${korea-invest.tr-id}")
   private String trId;

   private String token;

   @PostConstruct
   public void init() {
       getStocks();
       token = getOauthToken();
   }



   @Scheduled(initialDelayString = "1000", fixedRate = 550)
   public void pollingStockInfo() {
       String type = "NYS";
       idx = (idx + 1) % stocks.size();
       if (stocks.get(idx).getType().equals("NASDAQ")) {
           type = "NAS";
       }
       updateStockInfo(type, stocks.get(idx).getSymbol());
   }

   @Scheduled(cron = "0 0 23 * * ?")
   public void updateToken() {
       token = getOauthToken();
   }

   private void updateStockInfo(String type, String symbol) {
       try {
           Map result = webClient
                   .get()
                   .uri(uriBuilder ->
                           uriBuilder
                                   .scheme("https")
                                   .host(host)
                                   .port(port)
                                   .path(url)
                                   .queryParam("AUTH", "")
                                   .queryParam("EXCD", type)
                                   .queryParam("SYMB", symbol)
                                   .build())
                   .header(HttpHeaders.AUTHORIZATION,
                           "Bearer " + token)
                   .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                   .header("appkey", appKey)
                   .header("appsecret", appSecret)
                   .header("tr_id", trId)
                   .retrieve()
                   .bodyToMono(Map.class)
                   .block();
           redisUtil.setStockInfoTemplate(symbol, new StockSocketInfoDto(
                   (Map<String, String>) result.get("output")));
           StockSocketInfoDto stockSocketInfoDto = redisUtil.getStockInfoTemplate(symbol);
           simpMessagingTemplate.convertAndSend("/from/stock/"+stockSocketInfoDto.getSymbol(), stockSocketInfoDto);
       } catch (Exception e) {
           log.error(e.getMessage());
       }

   }

   private String getOauthToken() {
       Map result;
       try {
           result = webClient
               .post()
               .uri(uriBuilder ->
                   uriBuilder
                       .scheme("https")
                       .host(host)
                       .port(port)
                       .path(oauthUrl)
                       .build())
               .bodyValue(RequestOauthDto.builder().grant_type("client_credentials").appkey(appKey)
                   .appsecret(appSecret).build())
               .retrieve()
               .bodyToMono(Map.class)
               .block();
       }catch (Exception e){
           log.error(e.getMessage());
           return null;
       }
       return String.valueOf(result.get("access_token"));
   }

    private void getStocks(){
        stocks = stockRepository.findAll().stream()
            .filter(a -> a.getId() > 0 && a.getId() <= 12).toList();
    }
}
