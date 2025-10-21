package com.github.jhinslog.kma.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBody {

    public String dataType;     // 데이터 타입
    public ForecastItems items; // 예보 객체
    public int pageNo;       // 페이지 번호
    public int numOfRows;    // 페이지당 갯수
    public int totalCount;   // 전체 갯수
}
