package com.ss.batch.connpool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.PropertyVetoException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * c3p0 커넥션 풀 테스트.
 *
 * <p>ASIS: c3p0 0.9.1.1<br>
 * TOBE: c3p0 0.10.1</p>
 *
 * <p>전환 이유: JDK 17 호환 버전으로 업그레이드.
 * Boot 기본 HikariCP와 병행하여 기존 c3p0 설정 유지 가능 여부를 확인한다.</p>
 */
class C3p0Test {

    @Test
    @DisplayName("[TOBE] c3p0 0.10.1 - ComboPooledDataSource 생성 및 설정 확인")
    void createDataSource() throws PropertyVetoException {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass("org.h2.Driver");
        ds.setJdbcUrl("jdbc:h2:mem:c3p0batchtest");
        ds.setUser("sa");
        ds.setPassword("");
        ds.setMinPoolSize(1);
        ds.setMaxPoolSize(5);

        assertNotNull(ds, "ComboPooledDataSource가 null이면 안 된다");
        assertEquals(1, ds.getMinPoolSize(), "최소 풀 크기가 1이어야 한다");
        assertEquals(5, ds.getMaxPoolSize(), "최대 풀 크기가 5여야 한다");

        ds.close();
    }
}
