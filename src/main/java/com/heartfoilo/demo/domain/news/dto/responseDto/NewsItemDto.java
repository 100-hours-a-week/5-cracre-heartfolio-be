package com.heartfoilo.demo.domain.news.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsItemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String originallink;
    private String link;
    private String description;
    private String pubDate;

    public void setFormattedPubDate(String pubDate) {
        // 기존 날짜 형식을 2024.09.13 형태로 변환
        this.pubDate = formatPubDate(pubDate);
    }

    private String formatPubDate(String pubDate) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(pubDate, inputFormatter);
            return zonedDateTime.format(outputFormatter);
        } catch (Exception e) {
            // 오류가 발생하면 기존 날짜 그대로 반환
            return pubDate;
        }
    }
}