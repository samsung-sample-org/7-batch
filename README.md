# 7. 배치 시스템 - JDK 17 마이그레이션 라이브러리 검증

## 목차

1. [개요](#1-개요)
2. [기술 스택](#2-기술-스택)
3. [라이브러리 전환 현황 (ASIS → TOBE)](#3-라이브러리-전환-현황-asis--tobe)
   - [전환 원칙](#전환-원칙)
   - [전환 요약](#전환-요약)
   - [전체 라이브러리 매트릭스](#전체-라이브러리-매트릭스)
4. [솔루션/자체 라이브러리](#4-솔루션자체-라이브러리)
5. [주요 결정 사항과 근거](#5-주요-결정-사항과-근거)
6. [프로젝트 구조](#6-프로젝트-구조)
7. [실행 방법](#7-실행-방법)
8. [테스트 실행](#8-테스트-실행)
9. [알려진 제약사항](#9-알려진-제약사항)

---

## 1. 개요

기존 배치 시스템(Spring Batch 3.0.3, JDK 8)을 JDK 17 + Spring Boot 3.5.11로 전환하기 위한 **라이브러리 호환성 검증 프로젝트**이다.

원본 시스템에서 사용 중인 오픈소스 라이브러리를 분석하여, Spring Boot 3 환경에서의 호환 여부를 Docker 기반으로 실증 검증한다. 각 라이브러리에 대해 단위 테스트를 작성하고, JDK 17 + Jakarta EE 10 환경에서 정상 동작함을 확인하는 것이 목적이다.

**핵심 전환 포인트**: Spring Batch 3.0.3 → Spring Batch 5.x. `JobBuilderFactory` / `StepBuilderFactory`가 완전 제거되어 `JobRepository`를 직접 주입하는 방식으로 전환이 필요하다.

---

## 2. 기술 스택

| 항목 | 선택 | 선택 이유 |
|------|------|-----------|
| JDK | 17 (Adoptium Temurin) | Spring Boot 3의 최소 요구사항. LTS 버전으로 장기 지원 보장 |
| Framework | Spring Boot 3.5.11 | Jakarta EE 10 기반, Spring Framework 6.2 내장 |
| Batch | Spring Batch 5.x (Boot 관리) | Boot 3.x 기본 Batch 버전. Job/Step API 전면 개편 |
| 빌드 도구 | Maven | 기존 시스템과 동일한 빌드 도구 유지 |
| 패키징 | JAR | 배치 시스템 특성상 내장 컨테이너로 직접 실행 |
| 컨테이너 OS | Docker (CentOS 7) | JDK 6/7/8/17 모두 지원 가능. 레거시 환경 재현에 적합 |
| DB (로컬) | H2 인메모리 | Spring Batch 메타 테이블 + 배치 테스트용 인메모리 DB |

---

## 3. 라이브러리 전환 현황 (ASIS → TOBE)

### 전환 원칙

1. **기존 기술 최대 유지**: 있던 라이브러리를 그대로 가져간다. 마이그레이션 공수를 최소화한다.
2. **버전업 우선**: 동일 라이브러리의 최신 버전으로 업그레이드하는 것을 1순위로 한다.
3. **교체는 불가피한 경우만**: 버전업만으로 대응할 수 없을 때(프로젝트 폐기, Jakarta 미호환 등)에만 대체 라이브러리를 고려한다.
4. **Spring Batch 5 API 전환**: `JobBuilderFactory` / `StepBuilderFactory` 제거에 따른 필수 코드 변경.

### 전환 요약

| 전환 방식 | 건수 | 설명 |
|----------|------|------|
| Boot 내장 | 9건 | Spring Boot Starter/BOM에 포함. 별도 의존성 관리 불필요 |
| 버전업 | 8건 | 동일 계열 라이브러리 최신 버전으로 업그레이드 |
| 교체 | 1건 | 버전업 불가, 대체 라이브러리로 전환 (불가피) |
| 제거 | 9건 | 프로젝트 폐기/EOL/Boot 3 전환으로 불필요 |
| **합계** | **~27건** | 솔루션/자체 라이브러리 6건 별도 |

### 전체 라이브러리 매트릭스

> **범례** — Boot 내장: Spring Boot Starter가 관리 | 버전업: 동일 계열 최신 버전 | 교체: 대체 라이브러리 전환(불가피) | 제거: EOL/폐기/불필요

#### Framework / Batch

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 1 | Spring Framework 3.x | Spring 6.2+ | Boot 내장 | starter-web. javax→jakarta 전환 |
| 2 | spring-batch 3.0.3 | Spring Batch 5.x | Boot 내장 | starter-batch. **Job/Step API 전면 변경** |
| 3 | AspectJ 1.8.14 | AspectJ 1.9.21+ | Boot 내장 | starter-aop. JDK 17 호환 버전 자동 관리 |
| 4 | spring-retry 1.1.0 | spring-retry (Boot 관리) | Boot 내장 | Boot 3 BOM 버전 자동 관리 |
| 5 | javax.batch-api (JSR-352) | (제거) | 제거 | Spring Batch 5에서 Jakarta EE 기반으로 전환. starter-batch에 포함 |

#### Scheduler

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 6 | quartz 2.2.1 | Quartz 2.5+ | Boot 내장 | starter-quartz. Boot 자동 구성으로 설정 간소화 |

#### DB

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 7 | H2 1.4.185 | H2 2.2+ | Boot 내장 | Spring Batch 5 메타 테이블 DDL 변경 반영 |

#### HTTP

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 8 | httpclient 4.3.1 | httpclient5 | Boot 내장 | **groupId 완전 변경**. org.apache.http→org.apache.hc |

#### Logging

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 9 | log4j 1.2.16 | log4j-1.2-api (Log4j2 브릿지) | 교체 | CVE-2019-17571. 기존 org.apache.log4j API 유지, Logback으로 라우팅 |
| 10 | commons-logging 1.2 | (제거) | 제거 | SLF4J로 대체. spring-jcl 브릿지 내장 |

#### XML / JSON

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 11 | jettison 1.2 | jettison 1.5.4 | 버전업 | XML↔JSON 변환 보안 패치 |
| 12 | json-simple (구버전) | json-simple 1.1.1 | 버전업 | 기존 코드 호환 유지. 신규 개발 시 Jackson/Gson 권장 |
| 13 | xstream 1.4.7 | xstream 1.4.21 | 버전업 | CVE-2021-29505 등 다수 CVE 패치. JDK 17 호환 |
| 14 | jackson 1.9.x (org.codehaus) | (제거) | 제거 | Boot Jackson 2.x(com.fasterxml)로 대체 |

#### Apache Commons

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 15 | commons-codec 1.4 | commons-codec (Boot 관리) | Boot 내장 | Boot BOM 버전 자동 관리 |
| 16 | commons-configuration 1.10 | commons-configuration2 2.10.1 | 버전업 | **artifactId 변경**. configuration→configuration2. API 재설계 |
| 17 | commons-io 2.4 | commons-io 2.16.1 | 버전업 | JDK 8+ NIO 지원 강화 |
| 18 | commons-lang 2.6 | commons-lang3 (Boot 관리) | 제거→Boot 내장 | **패키지 변경**: org.apache.commons.lang→lang3. import 수정 필요 |
| 19 | commons-math3 3.6.1 | commons-math3 3.6.1 | 버전업 | 동일 버전 유지 (JDK 17 호환 확인) |
| 20 | commons-pool 1.x | commons-pool2 2.12.0 | 버전업 | **artifactId 변경**. GenericObjectPool 재설계 |
| 21 | commons-dbcp 1.4 | (제거) | 제거 | HikariCP(Boot 기본)로 대체. 성능/안정성 우수 |

#### Connection Pool

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 22 | c3p0 0.9.1.1 | c3p0 0.10.1 | 버전업 | JDK 17 호환 버전. 기존 설정 유지 가능 |

#### Utility

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 23 | activation 1.1 (JAF) | jakarta.activation-api | Boot 내장 | JDK 17에서 javax.activation 제거. jakarta 네임스페이스 전환 |
| 24 | snakeyaml 1.14 | snakeyaml (Boot 관리) | Boot 내장 | CVE-2022-1471 패치 |
| 25 | servlet-api 2.5 (javax) | (제거) | 제거 | Boot 내장 Jakarta Servlet 6으로 대체 |

---

## 4. 솔루션/자체 라이브러리

바이너리가 Maven Central에 배포되어 있지 않거나, 소스 코드를 확보할 수 없는 라이브러리이다. 별도 절차를 통해 JDK 17 호환 여부를 확인해야 한다.

| 파일명 | 추정 용도 | 향후 조치 |
|--------|----------|----------|
| anyframe-core | 삼성SDS Anyframe 내부 프레임워크 | SDS에 Spring Boot 3 호환 버전 문의 |
| com.ibm.jbatch-tck-spi | IBM JSR-352 TCK | Spring Batch 5 전환 시 불필요, 제거 검토 |
| jconn3 | Sybase JDBC 드라이버 | SAP에 JDK 17 호환 드라이버 확인 |
| lzo | LZO 압축 라이브러리 | JDK 17 호환 버전 또는 대체 라이브러리 검토 |
| ss-common | 사내 공통 라이브러리 | JDK 17 호환 버전 빌드 요청 |
| ss-security | 사내 보안 라이브러리 | JDK 17 호환 버전 빌드 요청 |

---

## 5. 주요 결정 사항과 근거

### 5.1 Spring Batch 5 API 전환 (핵심)

Spring Batch 5에서 가장 중요한 변경은 `JobBuilderFactory`와 `StepBuilderFactory`의 **완전 제거**이다.

**ASIS (Spring Batch 3/4):**
```java
@Autowired
private JobBuilderFactory jobBuilderFactory;

@Bean
public Job sampleJob() {
    return jobBuilderFactory.get("sampleJob")
        .start(sampleStep())
        .build();
}
```

**TOBE (Spring Batch 5):**
```java
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

### 5.2 @EnableBatchProcessing 제거

Spring Boot 3.x + Spring Batch 5에서는 `@EnableBatchProcessing` **없이** auto-configuration 사용을 권장한다. 함께 사용하면 auto-configuration과 충돌하여 `JobRepository` 중복 등록 오류가 발생한다.

### 5.3 Log4j 1.x → log4j-1.2-api 브릿지

Log4j 1.x는 CVE-2019-17571 등 심각한 취약점이 존재하여 그대로 사용할 수 없다. `log4j-1.2-api` 브릿지를 통해 기존 `org.apache.log4j.Logger` API 호환성을 유지하면서 실제 로깅은 Logback(Boot 기본)으로 라우팅한다.

### 5.4 commons-configuration 1.x → 2.x

`commons-configuration` 1.x에서 2.x로 전환 시 artifactId가 `commons-configuration2`로 변경된다. API도 재설계되어 `PropertiesConfiguration`, `XMLConfiguration` 등의 생성 방식이 변경된다.

### 5.5 commons-lang 2.x → commons-lang3

`commons-lang` 2.x에서 3.x로 전환 시 패키지명이 `org.apache.commons.lang` → `org.apache.commons.lang3`으로 변경된다. 기존 코드의 모든 import 수정이 필요하다.

### 5.6 H2 1.x → 2.x (Spring Batch 메타 테이블)

H2 2.x는 Spring Batch 5의 메타 테이블 DDL 변경을 반영한다. H2 1.x에서 동작하던 배치 메타 테이블 스크립트가 2.x에서 일부 변경되므로 확인이 필요하다.

---

## 6. 프로젝트 구조

```
7-batch/
├── Dockerfile                              # Multi-stage 빌드 (Maven → CentOS 7 + JDK 17)
├── docker-compose.yml                      # 배치 앱 단독 실행 (H2 인메모리)
├── pom.xml                                 # 전체 의존성 정의 (ASIS→TOBE 주석 포함)
├── README.md
└── src/
    ├── main/
    │   ├── java/com/ss/batch/
    │   │   ├── BatchApplication.java
    │   │   ├── config/
    │   │   │   ├── BatchJobConfig.java         # Spring Batch 5 Job/Step 구성
    │   │   │   └── QuartzConfig.java           # Quartz 스케줄러
    │   │   └── job/
    │   │       └── SampleBatchJob.java         # Tasklet 구현체
    │   └── resources/
    │       ├── application.yml                 # H2 + Batch + 포트 8082
    │       └── logback-spring.xml
    └── test/java/com/ss/batch/
        ├── batch/                              # Spring Batch 5 Job 실행 테스트
        ├── boot/                               # Spring Boot 컨텍스트 로딩
        ├── commons/                            # Configuration2, IO, Lang3, Math3, Pool2
        ├── connpool/                           # c3p0
        ├── http/                               # HttpClient 5
        ├── logging/                            # Log4j 브릿지
        ├── removed/                            # 제거 라이브러리 부재 확인
        ├── scheduler/                          # Quartz
        └── xml/                                # Jettison, XStream
```

---

## 7. 실행 방법

### 로컬 실행 (H2 인메모리 DB)

```bash
cd 7-batch
mvn spring-boot:run
```

- 애플리케이션: http://localhost:8082
- H2 콘솔: http://localhost:8082/h2-console (JDBC URL: `jdbc:h2:mem:batchdb`)

### Docker 실행

```bash
cd 7-batch
docker-compose up --build
```

- 애플리케이션: http://localhost:8082

---

## 8. 테스트 실행

```bash
cd 7-batch
mvn clean test
```

### 테스트 그룹별 검증 대상

| 테스트 그룹 | 검증 대상 라이브러리 |
|------------|-------------------|
| batch/ | Spring Batch 5.x Job 실행 (JobBuilder/StepBuilder API) |
| boot/ | Spring Boot 3.5.11 컨텍스트 로딩 |
| commons/ | configuration2 2.10.1, io 2.16.1, lang3, math3 3.6.1, pool2 2.12.0 |
| connpool/ | c3p0 0.10.1 |
| http/ | HttpClient 5 (클라이언트 생성, 요청 객체) |
| logging/ | Log4j 1.x API 브릿지 (log4j-1.2-api) |
| removed/ | 제거 라이브러리 ClassPath 부재 확인 (commons-dbcp, commons-lang 2.x 등) |
| scheduler/ | Quartz 2.5+ (Boot 자동 구성) |
| xml/ | Jettison 1.5.4, XStream 1.4.21 |

---

## 9. 알려진 제약사항

1. **솔루션/자체 라이브러리 미검증 (6건)**: anyframe, jconn3, lzo, ss-common, ss-security는 바이너리 미확보로 본 프로젝트에 포함되지 않았다. 각 담당 부서/벤더사에 JDK 17 호환 버전을 별도 요청해야 한다.

2. **Spring Batch 5 API 변경 — 코드 수정 필수**: `JobBuilderFactory` / `StepBuilderFactory`가 완전 제거되어 기존 Batch 3/4 Job 설정 코드를 전면 수정해야 한다.

3. **commons-configuration 2.x API 변경**: 1.x → 2.x 전환 시 `PropertiesConfiguration` 생성 방식 등이 변경된다. 기존 configuration 1.x 사용 코드는 마이그레이션이 필요하다.

4. **commons-lang3 패키지 변경**: `org.apache.commons.lang` → `org.apache.commons.lang3`으로 패키지가 변경되어 전체 import 수정이 필요하다.

5. **Sybase JDBC (jconn3.jar)**: SAP에서 JDK 17 호환 드라이버를 제공하는지 확인이 필요하다. 로컬 테스트는 H2를 사용한다.

6. **CentOS 7 EOL**: Docker 런타임 CentOS 7은 2024년 6월 지원 종료. 운영 환경에서는 Rocky Linux 9 / AlmaLinux 9 권장.
