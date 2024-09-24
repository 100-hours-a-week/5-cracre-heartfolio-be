package com.heartfoilo.demo.domain.donation.dto.requestDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCallbackRequestDto {
    private String paymentUid;
    private String orderUid;
}
// 결제 이후 서버가 전달받는 데이터를 정의
