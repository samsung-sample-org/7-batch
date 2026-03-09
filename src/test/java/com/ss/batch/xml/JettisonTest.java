package com.ss.batch.xml;

import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Jettison JSON/XML 변환 테스트.
 *
 * <p>ASIS: jettison 1.2<br>
 * TOBE: jettison 1.5.4</p>
 *
 * <p>전환 이유: jettison 1.2에 XML 파싱 보안 취약점 존재.
 * 1.5.4에서 보안 패치 및 안정화가 적용되었다.</p>
 */
class JettisonTest {

    @Test
    @DisplayName("[TOBE] jettison 1.5.4 - JSONObject 생성 확인")
    void createJsonObject() throws Exception {
        JSONObject json = new JSONObject();
        json.put("batchName", "sampleBatch");
        json.put("status", "COMPLETED");
        json.put("count", 100);

        assertNotNull(json, "JSONObject가 null이면 안 된다");
        assertEquals("sampleBatch", json.getString("batchName"),
                "batchName이 일치해야 한다");
        assertEquals("COMPLETED", json.getString("status"),
                "status가 일치해야 한다");
        assertEquals(100, json.getInt("count"), "count가 일치해야 한다");
    }

    @Test
    @DisplayName("[TOBE] jettison 1.5.4 - JSON 문자열 파싱 확인")
    void parseJsonString() throws Exception {
        String jsonStr = "{\"jobName\":\"exportJob\",\"exitCode\":\"COMPLETED\"}";
        JSONObject json = new JSONObject(jsonStr);

        assertEquals("exportJob", json.getString("jobName"),
                "jobName이 일치해야 한다");
        assertEquals("COMPLETED", json.getString("exitCode"),
                "exitCode가 일치해야 한다");
    }
}
