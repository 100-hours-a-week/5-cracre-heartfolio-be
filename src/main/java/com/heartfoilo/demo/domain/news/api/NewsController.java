package com.heartfoilo.demo.domain.news.api;

import com.heartfoilo.demo.domain.news.dto.responseDto.NewsResponseDto;
import com.heartfoilo.demo.domain.news.service.ApiSearchNewsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final ApiSearchNewsService apiSearchNewsService;

    @GetMapping
    public NewsResponseDto getNews(@RequestParam(defaultValue = "금융") String query) {
        return apiSearchNewsService.searchNews(query);
    }

}
