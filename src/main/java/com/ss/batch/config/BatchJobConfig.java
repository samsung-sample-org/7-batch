package com.ss.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch Job/Step 설정.
 *
 * <p>ASIS: spring-batch 3.0.3</p>
 * <pre>
 *   // ASIS 방식 (Spring Batch 3/4)
 *   {@literal @}Autowired JobBuilderFactory jobBuilderFactory;
 *   {@literal @}Autowired StepBuilderFactory stepBuilderFactory;
 *
 *   public Job sampleJob() {
 *       return jobBuilderFactory.get("sampleJob")
 *           .start(sampleStep())
 *           .build();
 *   }
 * </pre>
 *
 * <p>TOBE: Spring Batch 5.x</p>
 * <pre>
 *   // TOBE 방식 (Spring Batch 5)
 *   // JobBuilderFactory/StepBuilderFactory가 제거됨.
 *   // JobBuilder/StepBuilder를 직접 사용하며,
 *   // JobRepository와 PlatformTransactionManager를 직접 주입한다.
 * </pre>
 *
 * <p>전환 이유:</p>
 * <ul>
 *   <li>Spring Batch 5에서 {@code JobBuilderFactory}/{@code StepBuilderFactory} 완전 제거</li>
 *   <li>{@code JobBuilder}/{@code StepBuilder}를 직접 사용하며 {@code JobRepository}를 생성자에 전달</li>
 *   <li>Spring Boot 3.x + Spring Batch 5에서는 {@code @EnableBatchProcessing} 없이
 *       auto-configuration 사용 권장 (함께 사용 시 충돌 발생 가능)</li>
 *   <li>Spring Batch 5는 Jakarta EE 기반으로 javax.batch-api 제거됨</li>
 * </ul>
 */
@Configuration
public class BatchJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BatchJobConfig.class);

    /**
     * 샘플 Batch Job을 정의한다.
     *
     * <p>TOBE: Spring Batch 5에서는 {@code JobBuilder}를 직접 사용하며
     * {@code JobRepository}를 생성자 인자로 전달한다.</p>
     *
     * @param jobRepository Spring Batch 메타데이터 저장소 (Boot auto-configuration 제공)
     * @param sampleStep    Job에 포함될 Step
     * @return 구성된 Job
     */
    @Bean
    public Job sampleJob(JobRepository jobRepository, Step sampleStep) {
        return new JobBuilder("sampleJob", jobRepository)
                .start(sampleStep)
                .build();
    }

    /**
     * 샘플 Batch Step을 정의한다.
     *
     * <p>TOBE: Spring Batch 5에서는 {@code StepBuilder}를 직접 사용하며
     * {@code JobRepository}와 {@code PlatformTransactionManager}를 생성자 인자로 전달한다.</p>
     *
     * @param jobRepository     Spring Batch 메타데이터 저장소
     * @param transactionManager 트랜잭션 관리자
     * @return 구성된 Step
     */
    @Bean
    public Step sampleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("sampleStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("[TOBE] Spring Batch 5 - sampleStep 실행");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
