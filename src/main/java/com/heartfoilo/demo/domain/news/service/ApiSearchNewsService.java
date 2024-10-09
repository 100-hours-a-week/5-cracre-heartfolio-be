package com.heartfoilo.demo.domain.news.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartfoilo.demo.domain.news.dto.responseDto.NewsItemDto;
import com.heartfoilo.demo.domain.news.dto.responseDto.NewsResponseDto;
import com.heartfoilo.demo.dto.DailyNewsDto;
import com.heartfoilo.demo.util.RedisUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiSearchNewsService {

    @Value("${newsapi.client-id}")
    private String CLIENT_ID; // 애플리케이션 클라이언트 아이디
    @Value("${newsapi.client-secret}")
    private String CLIENT_SECRET; // 애플리케이션 클라이언트 시크릿
    private final RedisUtil redisUtil;
    LevenshteinDistance levenshtein = new LevenshteinDistance();

    public NewsResponseDto searchNews(String query) {
        if(redisUtil.hasDailyNews(query)){
            return redisUtil.getDailyNewsTemplate(query).getNewsResponseDto();
        }
        String encodedQuery;
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + encodedQuery
                +"&display=50&sort=sim";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", CLIENT_ID);
        requestHeaders.put("X-Naver-Client-Secret", CLIENT_SECRET);

        String responseBody = get(apiURL, requestHeaders);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NewsResponseDto responseDto = objectMapper.readValue(responseBody, NewsResponseDto.class);

            List<NewsItemDto> items = responseDto.getItems().stream()
                    .filter(item -> containsKorean(item.getTitle()))
                    .peek(item -> {
                        item.setTitle(cleanText(item.getTitle())); // 제목에서 불필요한 태그 제거
                        item.setDescription(cleanText(item.getDescription())); // 설명에서 불필요한 태그 제거
                        item.setFormattedPubDate(item.getPubDate()); // 날짜 형식 변경
                    })
                    .collect(Collectors.toList());

            List<NewsItemDto> filteredItems = new ArrayList<>();
            for(NewsItemDto newItem : items) {
                boolean isDuplicate = false;
                String newItemTitle = extractKorean(newItem.getTitle());
                for (NewsItemDto existingItem : filteredItems) {
                    String existingItemTitle = extractKorean(existingItem.getTitle());
                    int distance = levenshtein.apply(existingItemTitle, newItemTitle);
                    int maxLen = Math.max(existingItemTitle.length(), newItemTitle.length());

                    // 제목 유사도 10% 이하 차이면 중복으로 간주
                    if (distance <= 0.5 * maxLen) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (!isDuplicate) {
                    filteredItems.add(newItem);
                }
                if(filteredItems.size() == 20) break;
            }

            responseDto.setItems(filteredItems);
            redisUtil.setDailyNewsTemplate(query, DailyNewsDto.builder().date(LocalDate.now()).newsResponseDto(responseDto).build());
            return responseDto;
        } catch (IOException e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    private boolean containsKorean(String text) {
        return text != null && text.matches(".*[\\uAC00-\\uD7A3].*");
    }

    private String removePunctuation(String text) {
        if (text == null) return "";
        return text.replaceAll("[\\p{Punct}]", "").replaceAll("\\s+", " ").trim();
    }

    // 한글만 추출하는 메서드
    private String extractKorean(String text) {
        if (text == null) return "";
        return text.replaceAll("[^\\uAC00-\\uD7A3]", ""); // 한글만 남기고 모두 제거
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private String cleanText(String text) {
        if (text == null) {
            return "";
        }
        // HTML 태그와 특수문자 제거
        return text.replaceAll("<[^>]*>", "").replace("&quot;", "\"").replace("&apos;", "'").replace("&amp;", "&");
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }
}
