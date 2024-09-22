package com.heartfoilo.demo.domain.donation.service;

import com.heartfoilo.demo.domain.donation.dto.requestDto.PaymentCallbackRequestDto;
import com.heartfoilo.demo.domain.donation.dto.requestDto.RequestPayDto;
import com.heartfoilo.demo.domain.donation.entity.Donation;
import com.heartfoilo.demo.domain.donation.payment.PaymentStatus;
import com.heartfoilo.demo.domain.donation.repository.DonationRepository;
import com.heartfoilo.demo.domain.donation.repository.PaymentRepository;
import com.heartfoilo.demo.domain.invest.entity.Order;
import com.heartfoilo.demo.domain.invest.repository.OrderRepository;
import com.heartfoilo.demo.domain.portfolio.entity.Account;
import com.heartfoilo.demo.domain.portfolio.repository.PortfolioRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final DonationRepository donationRepository;
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;
    private final PortfolioRepository portfolioRepository;
    @Override
    public RequestPayDto findRequestDto(String orderUid) {
        Donation donation = donationRepository.findOrderAndPaymentAndMember(orderUid).orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));


        return RequestPayDto.builder()
                .buyerName(donation.getUser().getName())
                .buyerEmail("test1@example.com")
                .buyerAddress("지옥의 구름스퀘어")
                .paymentPrice(donation.getPayment().getPrice())
                .itemName("캐시충전")
                .orderUid(donation.getOrderUid())
                .build();


    }

    @Override // 결제검증 메소드
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequestDto request) {

        try {
            // 결제 단건 조회(아임포트)
            System.out.println(request);
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());
            // 주문내역 조회
            Donation donation = donationRepository.findOrderAndPayment(request.getOrderUid())
                    .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));
            if (!iamportResponse.getResponse().getStatus().equals("paid")) {
                // 주문, 결제 삭제
                donationRepository.delete(donation);
                paymentRepository.delete(donation.getPayment());

                throw new RuntimeException("결제 미완료"); // 결제 미완료로 인해 DB 내에서 삭제
            }

            Long price = donation.getPayment().getPrice(); // DB 내에 저장되어있는 금액

            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();
            if (price != iamportPrice) { // 결제 내역 검증 과정에서 두 가격이 같지 않다면 ? -> 기록 삭제 필요함
                donationRepository.delete(donation);
                paymentRepository.delete(donation.getPayment());


                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));
                // 결제금액 위조로 의심되는 결제금액 취소 코드
                throw new RuntimeException("결제금액 불일치");

            }
            Long userId = donation.getUser().getId(); // 여기까지 왔으면 donation 금액 추가
            Account account = portfolioRepository.findByUserId(userId);
            if (price == 1000L){
                account.ChangeCash(1200000);
                account.ChangeDonationPayment(1000L);
            }
            else if(price == 2000L){
                account.ChangeCash(2400000);
                account.ChangeDonationPayment(2000L);
            }

            portfolioRepository.save(account);

            donation.getPayment().changePaymentBySuccess(PaymentStatus.OK, iamportResponse.getResponse().getImpUid());




            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
