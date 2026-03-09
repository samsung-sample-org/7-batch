# 7. 배치 시스템 - JDK 17 마이그레이션 라이브러리 검증

Spring Batch 3.0.3 → Spring Batch 5.x 전환을 핵심으로 하는 JDK 17 + Spring Boot 3 마이그레이션 라이브러리 호환성 검증 프로젝트다.

## Spring Batch 5 핵심 API 변경점

### JobBuilderFactory / StepBuilderFactory 제거

Spring Batch 5에서 가장 중요한 변경 사항은 `JobBuilderFactory`와 `StepBuilderFactory`가 완전히 제거된 것이다.

**ASIS (Spring Batch 3/4):**
```java
@Autowired
private JobBuilderFactory jobBuilderFactory;
@Autowired
private StepBuilderFactory stepBuilderFactory;

@Bean
public Job sampleJob() {
    return jobBuilderFactory.get("sampleJob")
        .start(sampleStep())
        .build();
}
```

**TOBE (Spring Batch 5):**
```java
// JobRepository를 직접 주입받는다
@Bean
public Job sampleJob(JobRepository jobRepository, Step sampleStep) {
    return new JobBuilder("sampleJob", jobRepository)
        .start(sampleStep)
        .build();
}

@Bean
public Step sampleStep(JobRepository jobRepository, PlatformTransactionManager tm) {
    return new StepBuilder("sampleStep", jobRepository)
        .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, tm)
        .build();
}
```

### @EnableBatchProcessing 주의사항

Spring Boot 3.x + Spring Batch 5에서는 `@EnableBatchProcessing` **없이** auto-configuration 사용을 권장한다.
`@EnableBatchProcessing`을 함께 사용하면 auto-configuration과 충돌이 발생할 수 있다.

## 라이브러리 매트릭스

| 라이브러리 | ASIS | TOBE | 변경 사항 |
|-----------|------|------|-----------|
| Spring Batch | 3.0.3 | 5.x (Boot 관리) | Job/Step API 대폭 변경, javax.batch 제거 |
| Spring Boot | - | 3.5.11 | Jakarta EE 10, JDK 17+ |
| H2 | 1.4.185 | 2.2+ (Boot 관리) | Spring Batch 5 메타 DDL 변경 반영 |
| Quartz | 2.2.1 | 2.5+ (Boot 관리) | spring-boot-starter-quartz 자동 구성 |
| AspectJ | 1.8.14 | 1.9.21+ (Boot 관리) | Spring 6 AOP 호환 |
| commons-configuration | 1.10 | 2.10.1 | artifactId 변경: commons-configuration2 |
| commons-io | 2.4 | 2.16.1 | JDK 8+ NIO 지원 강화 |
| commons-lang | 2.6 | lang3 (Boot 관리) | 패키지 변경: org.apache.commons.lang3 |
| commons-math3 | 3.6.1 | 3.6.1 | 동일 버전 유지 |
| commons-pool | 1.x | pool2 2.12.0 | GenericObjectPool 재설계 |
| commons-codec | 1.4 | Boot 관리 | - |
| httpclient | 4.3.1 | httpclient5 (Boot 관리) | groupId/패키지 완전 변경 |
| jettison | 1.2 | 1.5.4 | 보안 패치 |
| json-simple | 유지 | 1.1.1 | 유지 (비권장, 신규 개발 시 Jackson/Gson 권장) |
| xstream | 1.4.7 | 1.4.21 | CVE 다수 패치 |
| activation | 1.1 | jakarta.activation-api (Boot 관리) | javax → jakarta |
| log4j | 1.2.16 | log4j-1.2-api 브릿지 (Boot 관리) | API 유지, SLF4J/Logback으로 라우팅 |
| c3p0 | 0.9.1.1 | 0.10.1 | JDK 17 호환 |
| snakeyaml | 1.14 | Boot 관리 (2.x) | CVE-2022-1471 패치 |
| spring-retry | 1.1.0 | Boot 관리 | - |

## 제거된 라이브러리

| 라이브러리 | 대체 |
|-----------|------|
| commons-lang 2.6 | commons-lang3 (패키지 변경) |
| commons-pool 1.x | commons-pool2 |
| commons-dbcp 1.4 | HikariCP (Boot 기본) |
| commons-logging 1.2 | SLF4J (spring-jcl 브릿지) |
| log4j 1.2.16 | Logback + log4j-1.2-api 브릿지 |
| servlet-api 2.5 (javax) | Jakarta Servlet 6 (Boot 내장) |
| jackson 1.9.x | Jackson 2.x (Boot 관리) |
| spring-batch 3.0.3 | spring-boot-starter-batch (Batch 5) |
| javax.batch-api | Spring Batch 5 Jakarta 기반 |

## 솔루션/자체 라이브러리 목록 (7_batch_library.md 기준)

아래 라이브러리는 사내/솔루션 의존성으로 Maven Central에 없어 별도 검토가 필요하다.

| 라이브러리 | 비고 |
|-----------|------|
| anyframe-core | 삼성 내부 프레임워크 |
| com.ibm.jbatch-tck-spi | IBM JSR-352 TCK, Batch 5 전환 시 불필요 |
| jconn3 | Sybase JDBC 드라이버 |
| lzo | LZO 압축 라이브러리 |
| ss-common | 사내 공통 라이브러리 |
| ss-security | 사내 보안 라이브러리 |

## 실행 방법

```bash
# 로컬 빌드 및 테스트
mvn clean test

# 애플리케이션 실행
mvn spring-boot:run

# Docker 실행
docker-compose up --build
```

## 포트

- 애플리케이션: `8082`
- H2 Console: `http://localhost:8082/h2-console`
