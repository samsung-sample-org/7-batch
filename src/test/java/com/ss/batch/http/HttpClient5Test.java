package com.ss.batch.http;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Apache HttpClient 5 테스트.
 *
 * <p>ASIS: httpclient 4.3.1 (org.apache.httpcomponents:httpclient, org.apache.http 패키지)<br>
 * TOBE: httpclient5 (org.apache.httpcomponents.client5:httpclient5, org.apache.hc.client5 패키지)</p>
 *
 * <p>전환 이유: HttpClient 4.x EOL 예정, 5.x에서 HTTP/2 지원과
 * groupId가 완전히 변경되었다.</p>
 */
class HttpClient5Test {

    @Test
    @DisplayName("[TOBE] httpclient5 - CloseableHttpClient 생성 확인")
    void createHttpClient() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            assertNotNull(httpClient, "CloseableHttpClient가 null이면 안 된다");
        }
    }

    @Test
    @DisplayName("[TOBE] httpclient5 - HttpGet 요청 객체 생성 확인")
    void createHttpGetRequest() throws Exception {
        // ASIS: org.apache.http.client.methods.HttpGet
        // TOBE: org.apache.hc.client5.http.classic.methods.HttpGet (패키지 변경)
        String testUri = "https://example.com/api/batch/test";
        HttpGet httpGet = new HttpGet(testUri);

        assertNotNull(httpGet, "HttpGet 객체가 null이면 안 된다");
        assertEquals("GET", httpGet.getMethod(), "HTTP 메서드는 GET이어야 한다");
        assertEquals(testUri, httpGet.getUri().toString(), "요청 URI가 일치해야 한다");
    }
}
