package com.github.jhinslog.kma.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastItem {

    public String baseDate; // 발표일자
    public String baseTime; // 발표시각
    public String category; // 자료구분 코드
    public String fcstDate; // 예보일자
    public String fcstTime; // 예보시각
    public String fcstValue; // 예보 값
    public String obsrValue;// 실황 값(초단기실황)
    public int nx;          // 예보지점 X 좌표
    public int ny;          // 예보지점 Y 좌표
}
