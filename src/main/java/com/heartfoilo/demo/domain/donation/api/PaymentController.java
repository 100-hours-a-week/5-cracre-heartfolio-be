package com.heartfoilo.demo.domain.donation.api;

import com.heartfoilo.demo.domain.donation.dto.requestDto.PaymentCallbackRequestDto;
import com.heartfoilo.demo.domain.donation.dto.requestDto.RequestPayDto;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.heartfoilo.demo.domain.donation.service.PaymentService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/donation")
public class PaymentController {

    private final PaymentService paymentService;


    @ResponseBody
    @PostMapping("/payment/success")
    public ResponseEntity<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallbackRequestDto request){
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);

        log.info("결제 응답={}", iamportResponse.getResponse().toString());

        return new ResponseEntity<>(iamportResponse, HttpStatus.OK);

    }



}
