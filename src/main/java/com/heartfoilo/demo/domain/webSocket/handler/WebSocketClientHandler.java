package com.heartfoilo.demo.domain.webSocket.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartfoilo.demo.domain.webSocket.dto.StockSocketInfoDto;
import com.heartfoilo.demo.util.RedisUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketClientHandler extends TextWebSocketHandler {

    private final RedisUtil redisUtil;
    private final SimpMessagingTemplate simpMessagingTemplate;
    static private final int EXCHANGE_RATE = 1300;
    Map<String, String> header = new HashMap<>();
    Map<String, String> input = new HashMap<>();
    Map<String, Map<String, String>> body = new HashMap<>();
    Map<String, Object> request = new HashMap<>();

    @Value("${websocket.approval_key")
    static String approvalKey;
    static final ObjectMapper objectMapper = new ObjectMapper();
    static String value_iv;
    static String value_key;
    static List<Map<String, Object>> requests = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("connect success");

        header.put("approval_key", "467845ef-28ba-4c37-a12a-0dbd4bc45517");
        header.put("custtype", "P");
        header.put("tr_type", "1");
        header.put("content-type", "utf-8");

        body.put("input", input);
        request.put("header", header);
        request.put("body", body);
        List<String> types = List.of(
            "NAS", "NAS", "NAS", "NAS", "NAS", "NYS", "NAS", "NYS", "NYS", "NYS", "NYS", "NYS", "NYS", "NYS", "NYS", "NYS", "NAS", "NYS", "NYS", "NAS", "NAS", "NAS", "NYS", "NYS", "NAS", "NYS", "NAS", "NYS", "NYS", "NYS", "NYS", "NAS", "NYS", "NYS", "NYS", "NYS", "NYS", "NYS", "NYS", "NYS", "NYS", "NYS", "NAS"
        );

        List<String> stocks = List.of(
            "AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "BRK.B", "NVDA", "JPM", "JNJ", "V",
            "WMT", "PG", "UNH", "DIS", "MA", "HD", "PYPL", "BAC", "VZ", "ADBE",
            "CMCSA", "NFLX", "PFE", "KO", "PEP", "MRK", "INTC", "T", "ABBV", "XOM",
            "CVX", "CSCO", "MCD", "NKE", "ABT", "WFC",
            "LLY", "DHR", "CRM", "ORCL", "ACN", "MDT", "LIN"
//            "UPS", "NEE", "PM", "QCOM", "COST", "TXN", "AVGO"
        );
//        for (int i=0; i<stocks.size(); i++) {
//            input.put("tr_id", "HDFSCNT0");
//            input.put("tr_key", "D"+types.get(i)+stocks.get(i));
//            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(request)));
//            requests.add(request);
//
//        }
//        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(requests)));
    }



    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
        throws Exception {
        String s = message.getPayload().toString();
        System.out.println("s :"+s);
        handleData(s);

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
        throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
        throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    public static StockSocketInfoDto parseStockData(String data) {
        String[] mainParts = data.split("\\|");
        if (mainParts.length < 4) {
            throw new IllegalArgumentException("Invalid data format");
        }

        String stockInfo = mainParts[3];
        String[] details = stockInfo.split("\\^");

        if (details.length < 15) {
            throw new IllegalArgumentException("Stock details segment is too short");
        }

        // 추출된 데이터를 DTO에 매핑
        return StockSocketInfoDto.builder()
            .symbol(details[1])
            .curPrice((int) (Float.parseFloat(details[11])* EXCHANGE_RATE))
            .openPrice((int) (Float.parseFloat(details[8]) * EXCHANGE_RATE))
            .highPrice((int) (Float.parseFloat(details[9]) * EXCHANGE_RATE))
            .lowPrice((int) (Float.parseFloat(details[10]) * EXCHANGE_RATE))
            .earningValue(Math.round(Float.parseFloat(details[12])) * EXCHANGE_RATE)
            .earningRate(Float.parseFloat(details[13]))
            .build();
    }

    private static void getData(String encryptedData){
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(adjustByteLength(value_iv, 16));
            SecretKeySpec skeySpec = new SecretKeySpec(adjustByteLength(value_key, 32), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
            byte[] original = cipher.doFinal(adjustByteLength(encryptedData, 16*1000));
            String originalString = new String(original);
            System.out.println("Original string: " + originalString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static byte[] adjustByteLength(String hex, int length) {
        byte[] data = new byte[length];
        byte[] temp = hexStringToByteArray(hex);

        int copyLength = Math.min(temp.length, data.length);
        System.arraycopy(temp, 0, data, 0, copyLength);
        return data;
    }
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public void handleData(String data) {
        if (!data.trim().startsWith("{")) {
            StockSocketInfoDto stockSocketInfoDto = parseStockData(data);
            redisUtil.setStockInfoTemplate(stockSocketInfoDto.getSymbol(), stockSocketInfoDto);
            log.info("redis :현재가 "+ redisUtil.getStockInfoTemplate(stockSocketInfoDto.getSymbol()).getCurPrice());
            simpMessagingTemplate.convertAndSend("/from/stock/"+stockSocketInfoDto.getSymbol(), stockSocketInfoDto);
        }
    }

    public static void parseJson(String jsonData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(jsonData);
            JsonNode headerNode = rootNode.path("header");
            JsonNode bodyNode = rootNode.path("body");
            JsonNode outputNode = bodyNode.path("output");

            String trId = headerNode.path("tr_id").asText();
            String rtCd = bodyNode.path("rt_cd").asText();
            String msgCd = bodyNode.path("msg_cd").asText();
            String msg = bodyNode.path("msg1").asText();
            String iv = outputNode.path("iv").asText();
            String key = outputNode.path("key").asText();

            value_iv = iv;
            value_key = key;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
