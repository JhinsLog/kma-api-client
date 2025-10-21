package com.github.jhinslog.kma.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastItems {
    @JsonProperty("item")
    public List<ForecastItem> itemList; // 개별 예보 항목의 리스트
}
