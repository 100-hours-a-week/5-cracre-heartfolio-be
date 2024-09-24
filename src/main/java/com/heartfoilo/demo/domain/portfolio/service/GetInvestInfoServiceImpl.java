package com.heartfoilo.demo.domain.portfolio.service;

import com.heartfoilo.demo.domain.invest.dto.responseDto.GetInfoResponseDto;
import com.heartfoilo.demo.domain.invest.repository.InvestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import com.heartfoilo.demo.domain.invest.repository.InvestRepository;
@RequiredArgsConstructor
@Service
public class GetInvestInfoServiceImpl implements GetInvestInfoService {

    private final InvestRepository investRepository;
    @Override
    public ResponseEntity<?> getInvestInfo(long userId) {

        List<GetInfoResponseDto> order = investRepository.findByUserId(userId); // order 테이블 기반
        if (order == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        Collections.reverse(order); // 리스트를 역순으로 정렬
        return ResponseEntity.ok(order);
    }
}
