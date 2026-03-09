package com.ss.batch.logging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Log4j 1.x API 브릿지 테스트.
 *
 * <p>ASIS: log4j 1.2.16 (org.apache.log4j.Logger 직접 사용)<br>
 * TOBE: log4j-1.2-api (Log4j2 브릿지, Boot 관리)</p>
 *
 * <p>전환 이유: Log4j 1.x EOL(CVE-2019-17571). log4j-1.2-api 브릿지를 통해
 * 기존 org.apache.log4j.Logger API를 유지하면서 실제 로깅은 Logback으로 라우팅한다.
 * 기존 배치 코드에서 Log4j 1.x를 직접 사용하는 경우 코드 변경 없이 보안 문제를 해결할 수 있다.</p>
 */
class Log4jBridgeTest {

    @Test
    @DisplayName("[TOBE] log4j-1.2-api - org.apache.log4j.Logger 클래스 로딩 확인")
    void log4jLoggerClassLoadable() throws ClassNotFoundException {
        // log4j-1.2-api 브릿지가 org.apache.log4j 패키지를 제공하는지 확인한다
        Class<?> loggerClass = Class.forName("org.apache.log4j.Logger");
        assertNotNull(loggerClass, "org.apache.log4j.Logger 클래스가 로딩되어야 한다");
    }

    @Test
    @DisplayName("[TOBE] log4j-1.2-api - Logger 인스턴스 생성 및 로그 출력 확인")
    void log4jLoggerUsable() {
        // 기존 Log4j 1.x API로 Logger를 생성하고 로그를 출력한다
        // log4j-1.2-api 브릿지가 이를 SLF4J/Logback으로 라우팅한다
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Log4jBridgeTest.class);
        assertNotNull(logger, "Logger 인스턴스가 null이면 안 된다");

        // 예외 없이 로그 출력이 가능해야 한다
        assertDoesNotThrow(() -> {
            logger.info("Log4j 1.x API 브릿지 테스트 - 배치 시스템 info 레벨");
            logger.debug("Log4j 1.x API 브릿지 테스트 - 배치 시스템 debug 레벨");
            logger.warn("Log4j 1.x API 브릿지 테스트 - 배치 시스템 warn 레벨");
        }, "Log4j 1.x API를 통한 로그 출력이 예외 없이 동작해야 한다");
    }
}
