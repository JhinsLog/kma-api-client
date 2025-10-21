package com.github.jhinslog.kma.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseHeader {

    public String resultCode;   // 결과 코드
    public String resultMsg;    // 결과 메시지
}
