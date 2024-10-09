package com.heartfoilo.demo.domain.news.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NewsItemDto> items;
}