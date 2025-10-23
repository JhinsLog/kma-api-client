package com.github.jhinslog.kma;

import com.github.jhinslog.kma.dto.ApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * KmaApiClient를 테스트하기 위한 클래스
 */
public class KmaApiClientTest {

    private KmaApiClient kmaApiClient;

    @BeforeEach
    void setUp() throws IOException {
        // 각 테스트가 실행되기 전에 KmaApiClient 객체를 생성합니다.
        this.kmaApiClient = new KmaApiClient();
    }

    @Test
    @DisplayName("초단기실황 조회 성공 테스트")
    void getUltraSrtNcst_Success() throws IOException {
        // given: 테스트를 위한 준비
        // 서울시 강남구의 좌표
        int nx = 60;
        int ny = 127;

        // --- 가장 최신의 유효한 데이터를 요청하기 위한 시간 계산 로직 ---
        LocalDateTime now = LocalDateTime.now();
        // 초단기실황 데이터는 매시 40분에 생성되므로, 40분 이전에는 이전 시간의 데이터를 요청해야 함.
        if (now.getMinute() < 40) {
            // 현재 분(minute)이 40분 미만이면, 1시간 전 데이터를 요청.
            now = now.minusHours(1);
        }

        // 포맷터를 사용하여 날짜와 시간을 문자열로 변환
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 초단기실황의 base_time은 정시(HH00)를 사용합니다.
        String baseTime = now.format(DateTimeFormatter.ofPattern("HH")) + "00";
        // --- 시간 계산 로직 끝 ---


        // when: 실제 테스트 대상 메소드 호출
        System.out.println("Requesting with baseDate=" + baseDate + ", baseTime=" + baseTime);
        ApiResponse response = kmaApiClient.getUltraSrtNcst(baseDate, baseTime, nx, ny);

        // then: 결과 검증
        Assertions.assertNotNull(response, "API 응답 객체는 null이 아니어야 합니다.");
        Assertions.assertNotNull(response.response, "응답 데이터의 'response' 필드는 null이 아니어야 합니다.");
        Assertions.assertNotNull(response.response.header, "응답 헤더는 null이 아니어야 합니다.");

        // API 호출 성공 여부 확인 (기상청 성공 코드 "00")
        Assertions.assertEquals("00", response.response.header.resultCode, "API 결과 코드는 '00'(성공)이어야 합니다.");
        System.out.println("API 호출 성공: " + response.response.header.resultMsg);

        // 응답 본문 및 데이터 확인
        Assertions.assertNotNull(response.response.body, "응답 본문은 null이 아니어야 합니다.");
        Assertions.assertNotNull(response.response.body.items, "응답 본문의 'items'는 null이 아니어야 합니다.");

        // 데이터가 없는 경우가 아니라면, 아이템 리스트는 비어있지 않아야 합니다.
        if ("00".equals(response.response.header.resultCode)) {
            Assertions.assertFalse(response.response.body.items.itemList.isEmpty(), "성공 시 예보 아이템 리스트는 비어있지 않아야 합니다.");
            // 첫 번째 예보 아이템 출력
            System.out.println("첫 번째 예보 아이템: " + response.response.body.items.itemList.get(0).toString());
        }
    }
}
