package com.heartfoilo.demo.domain.ranking.dto.responseDto;



import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RankingResponseDto {
    private Long userId;
    private String profile;
    private String name;
    private float percentage;
}
