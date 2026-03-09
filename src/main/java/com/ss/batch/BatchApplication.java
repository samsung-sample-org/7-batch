package com.ss.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 배치 시스템 애플리케이션 진입점.
 *
 * <p>본 프로젝트는 JDK 17 + Spring Boot 3 환경에서
 * 기존(ASIS) 배치 관련 라이브러리들의 TOBE 전환 호환성을 검증하기 위한 샘플 애플리케이션이다.</p>
 *
 * <p>핵심 검증 항목:</p>
 * <ul>
 *   <li>Spring Batch 3.0.3 → Spring Batch 5.x 전환
 *       (JobBuilderFactory/StepBuilderFactory 제거 → JobRepository 직접 주입)</li>
 *   <li>Quartz 2.2.1 → 2.5+ (spring-boot-starter-quartz 자동 구성)</li>
 *   <li>H2 1.4.185 → 2.2+ (Spring Batch 5 메타 테이블 DDL 변경 반영)</li>
 *   <li>commons-configuration 1.10 → commons-configuration2 2.10.1</li>
 *   <li>xstream 1.4.7 → 1.4.21 (CVE 패치)</li>
 *   <li>c3p0 0.9.1.1 → 0.10.1 (JDK 17 호환)</li>
 * </ul>
 *
 * <p>Spring Boot 3.x + Spring Batch 5에서는 {@code @EnableBatchProcessing} 없이
 * auto-configuration 사용을 권장한다.</p>
 *
 * @author SS Sample
 */
@SpringBootApplication
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}
