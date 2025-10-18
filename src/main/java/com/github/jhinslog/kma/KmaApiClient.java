package com.github.jhinslog.kma;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 한국 기상청(KMA) API 클라이언트
 */
public class KmaApiClient {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    // 기상청 API 서비스들의 기본 URL
    private final String baseUrl;

    public KmaApiClient() throws IOException {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();

        Properties props = new Properties();
        try (InputStream input = KmaApiClient.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("application.properties 파일을 찾을 수 없습니다.");
            }
            props.load(input);
        }

        this.apiKey = props.getProperty("kma.api.key");
        this.baseUrl = props.getProperty("kma.api.baseurl");

        if (this.apiKey == null || this.apiKey.equals("YOUR_API_KEY_HERE") || this.apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API 키가 설정되지 않았습니다");
        }

        if (this.baseUrl == null || this.baseUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("기본 URL이 설정되지 않았습니다");
        }
    }

}