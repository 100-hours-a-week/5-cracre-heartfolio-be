package com.cracre.heartfoilo.domain.portfolio.api;

import com.cracre.heartfoilo.domain.portfolio.dto.responseDto.GetInfoResponseDto;
import com.cracre.heartfoilo.domain.portfolio.service.PortfolioService;
import com.cracre.heartfoilo.domain.portfolio.service.PortfolioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioServiceImpl portfolioServiceImpl;


    @GetMapping("/{userId}") // 보유 자산 조회 API
    public GetInfoResponseDto getAssets(@PathVariable ("userId") long userId){

        return portfolioServiceImpl.getAssets(userId);
    }
    @GetMapping("/{userId}/stock") // 자산 구성 조회 API
    public GetInfoResponseDto getStocks(@PathVariable ("userId") long userId){

        return portfolioServiceImpl.getStocks(userId);

    }




}
