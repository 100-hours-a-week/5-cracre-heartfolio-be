package com.heartfoilo.demo.domain.stock.api;

import com.heartfoilo.demo.domain.stock.dto.responseDto.PopularStockResponseDto;
import com.heartfoilo.demo.domain.stock.service.PopularStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock/popular")
@RequiredArgsConstructor
public class PopularStockController {

    private final PopularStockService popularStockService;

    @GetMapping
    public ResponseEntity<List<PopularStockResponseDto>> getPopularStocks(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<PopularStockResponseDto> popularStocks = popularStockService.getPopularStocks(limit);
        return ResponseEntity.ok(popularStocks);
    }
}
