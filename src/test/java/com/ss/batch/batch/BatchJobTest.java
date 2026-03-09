package com.ss.batch.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Spring Batch 5 Job 실행 테스트.
 *
 * <p>ASIS: spring-batch 3.0.3</p>
 * <pre>
 *   // ASIS 방식 (Spring Batch 3/4)
 *   {@literal @}Autowired JobBuilderFactory jobBuilderFactory;
 *   {@literal @}Autowired StepBuilderFactory stepBuilderFactory;
 *   // Job/Step 생성 후 JobLauncher로 직접 실행
 * </pre>
 *
 * <p>TOBE: Spring Batch 5.x</p>
 * <pre>
 *   // TOBE 방식 (Spring Batch 5)
 *   // @SpringBatchTest + JobLauncherTestUtils 조합으로 테스트
 *   // JobBuilderFactory/StepBuilderFactory 제거됨
 *   // JobBuilder/StepBuilder + JobRepository 직접 주입
 * </pre>
 *
 * <p>전환 이유: Spring Batch 5에서 테스트 지원도 개선되어
 * {@code @SpringBatchTest}와 {@code JobLauncherTestUtils}를 활용한 통합 테스트가 권장된다.</p>
 */
@SpringBatchTest
@SpringBootTest
class BatchJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    @DisplayName("[TOBE] Spring Batch 5 - Job 실행 검증")
    void testBatchJob() throws Exception {
        // Spring Batch 5 방식으로 Job을 실행하고 COMPLETED 상태를 확인한다
        JobExecution execution = jobLauncherTestUtils.launchJob();
        assertEquals(BatchStatus.COMPLETED, execution.getStatus(),
                "Batch Job이 COMPLETED 상태로 완료되어야 한다");
    }

    @Test
    @DisplayName("[TOBE] Spring Batch 5 - Step 실행 검증")
    void testBatchStep() throws Exception {
        // 개별 Step 실행 검증
        JobExecution execution = jobLauncherTestUtils.launchStep("sampleStep");
        assertEquals(BatchStatus.COMPLETED, execution.getStatus(),
                "sampleStep이 COMPLETED 상태로 완료되어야 한다");
    }
}
