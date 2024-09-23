package com.heartfoilo.demo.domain.donation.service;

import com.heartfoilo.demo.domain.donation.dto.requestDto.PaymentCallbackRequestDto;
import com.heartfoilo.demo.domain.donation.dto.requestDto.RequestPayDto;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;


public interface PaymentService {
    RequestPayDto findRequestDto(String orderUid); // 결제 요청 데이터 조회

    IamportResponse<Payment> paymentByCallback(PaymentCallbackRequestDto request); // 결제 검증 메소드



}
