package com.github.jhinslog.kma;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jhinslog.kma.dto.ApiResponse;
import com.github.jhinslog.kma.exception.KmaApiAuthException;
import com.github.jhinslog.kma.exception.KmaApiBadRequestException;
import com.github.jhinslog.kma.exception.KmaApiServerException;
import okhttp3.*;

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
    private final String baseUrl;   // 기상청 API 서비스들의 기본 URL

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

    public ApiResponse getUltraSrtNcst(String baseDate, String baseTime, int nx, int ny) throws IOException {
        final String SERVICE_PATH = "/VilageFcstInfoService_2.0";
        final String ENDPOINT = "/getUltraSrtNcst";

        /*1. 요청 URL과 쿼리 파라미터들을 생성*/
        HttpUrl url = HttpUrl.parse(this.baseUrl + SERVICE_PATH + ENDPOINT).newBuilder()
                .addQueryParameter("authKey", this.apiKey)
                .addQueryParameter("pageNo", "1")
                .addQueryParameter("numOfRows", "100") // 초단기실황은 8개의 항목만 반환 중.
                .addQueryParameter("dataType", "JSON")
                .addQueryParameter("base_date", baseDate)
                .addQueryParameter("base_time", baseTime)
                .addQueryParameter("nx", String.valueOf(nx))
                .addQueryParameter("ny", String.valueOf(ny))
                .build();

        return executeRequest(url);
    }

    /*중복 사용을 염두하여 분리*/
    private ApiResponse executeRequest(HttpUrl url) throws IOException {
        /*2. OkHttp를 사용하여 HTTP GET 요청 객체를 생성.*/
        Request request = new Request.Builder()
                .url(url)
                .get() // GET 요청임을 명시
                .build();

        /*3. 요청 실행 및 응답 처리.*/
        try (Response response = this.client.newCall(request).execute()) {
            /*3-1. 응답이 성공적이지 않으면 예외를 발생.*/
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No response body";
                String errorMessage = "API 호출 실패: " + response.code() + " " + response.message() + ", Body: " + errorBody;

                int code = response.code();
                if (code == 401 || code == 403) {
                    throw new KmaApiAuthException(errorMessage);
                } else if (code >= 400 && code < 500) {
                    throw new KmaApiBadRequestException(errorMessage);
                } else if (code >= 500 && code < 600) {
                    throw new KmaApiServerException(errorMessage);
                } else {
                    throw new IOException(errorMessage); // 그 외의 예외는 기존과 동일하게 처리
                }
            }

            /*3-2. 응답 본문이 비어있는 경우를 처리.*/
            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("API 응답 본문이 비어있습니다.");
            }

            /*4. 응답받은 JSON 문자열을 ApiResponse DTO 객체로 변환.*/
            String jsonString = body.string();
            return this.objectMapper.readValue(jsonString, ApiResponse.class);
        }
    }
}