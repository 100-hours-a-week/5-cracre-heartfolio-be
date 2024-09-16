package com.heartfoilo.demo.domain.news.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartfoilo.demo.domain.news.dto.responseDto.NewsItemDto;
import com.heartfoilo.demo.domain.news.dto.responseDto.NewsResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiSearchNewsService {

    @Value("${newsapi.client-id}")
    private String CLIENT_ID; // 애플리케이션 클라이언트 아이디
    @Value("${newsapi.client-secret}")
    private String CLIENT_SECRET; // 애플리케이션 클라이언트 시크릿


    public NewsResponseDto searchNews(String query) {
        String encodedQuery;
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + encodedQuery
                +"&display=20&sort=sim";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", CLIENT_ID);
        requestHeaders.put("X-Naver-Client-Secret", CLIENT_SECRET);

        String responseBody = get(apiURL, requestHeaders);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NewsResponseDto responseDto = objectMapper.readValue(responseBody, NewsResponseDto.class);

            List<NewsItemDto> filteredItems = responseDto.getItems().stream()
                    .filter(item -> containsKorean(item.getTitle()))
                    .peek(item -> item.setFormattedPubDate(item.getPubDate())) // 날짜 형식 변경
                    .collect(Collectors.toList());

            responseDto.setItems(filteredItems);

            return responseDto;
        } catch (IOException e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    private boolean containsKorean(String text) {
        return text != null && text.matches(".*[\\uAC00-\\uD7A3].*");
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
