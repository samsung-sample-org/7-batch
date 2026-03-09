package com.ss.batch.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Quartz를 통해 Spring Batch Job을 트리거하는 샘플 잡 클래스.
 *
 * <p>ASIS: Quartz 2.2.1 + Spring Batch 3.0.3 (별도 설정으로 연동)<br>
 * TOBE: spring-boot-starter-quartz (Quartz 2.5+) + spring-boot-starter-batch (Batch 5)</p>
 *
 * <p>전환 이유: {@code QuartzJobBean}을 상속하면 Spring의 의존성 주입(DI)을
 * Job 내부에서 활용할 수 있으며, Spring Boot의 자동 구성과 자연스럽게 통합된다.
 * 실제 프로젝트에서는 여기서 {@code JobLauncher}를 주입받아 Spring Batch Job을 실행한다.</p>
 */
public class SampleBatchJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(SampleBatchJob.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Quartz Job 실행 로직.
     *
     * <p>실제 운영에서는 {@code JobLauncher}를 주입받아 Spring Batch Job을 실행한다.</p>
     *
     * @param context Quartz 실행 컨텍스트
     * @throws JobExecutionException Job 실행 중 예외 발생 시
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String currentTime = LocalDateTime.now().format(FORMATTER);
        log.info("[TOBE] Quartz → Spring Batch 5 트리거 실행: {}", currentTime);
    }
}
