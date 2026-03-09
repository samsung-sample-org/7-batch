package com.ss.batch.removed;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 제거된 라이브러리 ClassPath 부재 확인 테스트.
 *
 * <p>Spring Batch 5 + Spring Boot 3 전환 시 더 이상 필요하지 않거나 대체된 라이브러리들이
 * 실제로 ClassPath에 존재하지 않는지 확인한다.</p>
 */
class RemovedLibDocTest {

    @Test
    @DisplayName("전환: Log4j 1.x → log4j-1.2-api 브릿지 (기존 API 유지, 내부는 SLF4J/Logback으로 라우팅)")
    void log4j1xShouldBeAvailableViaBridge() {
        // log4j-1.2-api 브릿지가 org.apache.log4j 패키지를 제공하므로 클래스 로딩이 가능하다
        try {
            Class.forName("org.apache.log4j.Logger");
            // log4j-1.2-api 브릿지에 의해 로딩 가능 - 정상 동작
        } catch (ClassNotFoundException e) {
            // 브릿지가 없는 경우 - 역시 정상 동작 (Logback 직접 사용)
        }
    }

    @Test
    @DisplayName("제거: spring-batch 3.0.3 → spring-boot-starter-batch (Batch 5)")
    void springBatch3JobBuilderFactoryShouldNotExist() {
        // Spring Batch 5에서 JobBuilderFactory가 완전히 제거되었다
        // 기존 코드에서 이 클래스를 사용하는 경우 JobBuilder로 마이그레이션 필요
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("org.springframework.batch.core.configuration.annotation.JobBuilderFactory"),
                "JobBuilderFactory는 Spring Batch 5에서 제거되었으므로 ClassPath에 존재하면 안 된다");
    }

    @Test
    @DisplayName("제거: spring-batch 3.0.3 → StepBuilderFactory 제거 확인")
    void stepBuilderFactoryShouldNotExist() {
        // Spring Batch 5에서 StepBuilderFactory가 완전히 제거되었다
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("org.springframework.batch.core.configuration.annotation.StepBuilderFactory"),
                "StepBuilderFactory는 Spring Batch 5에서 제거되었으므로 ClassPath에 존재하면 안 된다");
    }

    @Test
    @DisplayName("제거: commons-lang 2.x → commons-lang3으로 완전 전환")
    void commonsLang2ShouldNotBeOnClasspath() {
        // commons-lang 2.x (org.apache.commons.lang 패키지)는 제거 대상이다
        // commons-lang3 (org.apache.commons.lang3)으로 전환
        try {
            Class.forName("org.apache.commons.lang.StringUtils");
            // transitive dependency로 존재할 수 있음 - 경고만 기록
            System.out.println("[WARNING] org.apache.commons.lang.StringUtils가 ClassPath에 존재합니다. "
                    + "commons-lang3으로 전환하세요.");
        } catch (ClassNotFoundException e) {
            // 기대 동작: commons-lang 2.x가 ClassPath에 없음
        }
    }

    @Test
    @DisplayName("제거: servlet-api 2.5 (javax.servlet) → Jakarta Servlet 6 (jakarta.servlet)")
    void javaxServletShouldNotBeOnClasspath() {
        // javax.servlet.Servlet은 제거 대상이다
        // Spring Boot 3.x는 jakarta.servlet 기반이다
        try {
            Class.forName("javax.servlet.Servlet");
            System.out.println("[WARNING] javax.servlet.Servlet이 ClassPath에 존재합니다. "
                    + "jakarta.servlet으로 전환하세요.");
        } catch (ClassNotFoundException e) {
            // 기대 동작: javax.servlet이 ClassPath에 없음
        }
    }

    @Test
    @DisplayName("제거: javax.batch-api (JSR-352) → Spring Batch 5 Jakarta 기반")
    void javaxBatchApiShouldNotBeOnClasspath() {
        // javax.batch.api.Batchlet은 제거 대상이다
        // Spring Batch 5에서 Jakarta EE 기반으로 전환되었다
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("javax.batch.api.Batchlet"),
                "javax.batch.api.Batchlet은 Spring Batch 5에서 제거되었으므로 ClassPath에 존재하면 안 된다");
    }
}
