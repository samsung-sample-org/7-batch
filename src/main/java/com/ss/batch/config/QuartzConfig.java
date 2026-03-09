package com.ss.batch.config;

import com.ss.batch.job.SampleBatchJob;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Quartz 스케줄러 설정.
 *
 * <p>ASIS: quartz 2.2.1 (독립 설정)<br>
 * TOBE: spring-boot-starter-quartz (Quartz 2.5+, Spring Boot 자동 구성)</p>
 *
 * <p>전환 이유:</p>
 * <ul>
 *   <li>Spring Boot의 spring-boot-starter-quartz는 {@code SchedulerFactoryBean}을 자동 등록한다.</li>
 *   <li>JobDetail과 Trigger를 Spring Bean으로 등록하면 자동으로 스케줄러에 등록된다.</li>
 *   <li>별도의 스케줄러 초기화 코드가 불필요하여 설정이 간소화된다.</li>
 * </ul>
 */
@Configuration
public class QuartzConfig {

    /**
     * 배치 트리거용 Quartz Job의 상세 정보를 정의한다.
     *
     * @return JobDetail 팩토리 빈
     */
    @Bean
    public JobDetailFactoryBean sampleBatchJobDetail() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(SampleBatchJob.class);
        factory.setName("sampleBatchJob");
        factory.setGroup("batch-group");
        factory.setDescription("샘플 Batch 트리거용 Quartz Job - 라이브러리 호환성 검증용");
        factory.setDurability(true);
        return factory;
    }

    /**
     * 샘플 배치 Job의 트리거를 정의한다 (30초 간격 반복 실행).
     *
     * @param sampleBatchJobDetail 연결할 JobDetail
     * @return SimpleTrigger 팩토리 빈
     */
    @Bean
    public SimpleTriggerFactoryBean sampleBatchJobTrigger(JobDetail sampleBatchJobDetail) {
        SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
        factory.setJobDetail(sampleBatchJobDetail);
        factory.setName("sampleBatchJobTrigger");
        factory.setGroup("batch-group");
        factory.setRepeatInterval(30_000L); // 30초 간격
        factory.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return factory;
    }
}
