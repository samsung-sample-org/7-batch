package com.ss.batch.commons;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * commons-pool2 테스트.
 *
 * <p>ASIS: commons-pool 1.x (commons-pool:commons-pool)<br>
 * TOBE: commons-pool2 2.12.0 (org.apache.commons:commons-pool2)</p>
 *
 * <p>전환 이유: pool 1.x EOL, 2.x에서 GenericObjectPool 완전 재설계.
 * 배치 처리에서 객체 풀링을 통한 리소스 재사용 시 활용된다.</p>
 */
class CommonsPool2Test {

    @Test
    @DisplayName("[TOBE] commons-pool2 2.12.0 - GenericObjectPoolConfig 생성 확인")
    void genericObjectPoolConfigCreate() {
        // ASIS: commons-pool 1.x의 GenericObjectPool.Config
        // TOBE: commons-pool2의 GenericObjectPoolConfig (별도 클래스로 분리됨)
        GenericObjectPoolConfig<Object> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10);
        config.setMinIdle(2);
        config.setMaxIdle(5);

        assertNotNull(config, "GenericObjectPoolConfig가 null이면 안 된다");
        assertEquals(10, config.getMaxTotal(), "maxTotal이 10이어야 한다");
        assertEquals(2, config.getMinIdle(), "minIdle이 2여야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-pool2 2.12.0 - 패키지 경로 확인")
    void pool2PackageVerification() throws ClassNotFoundException {
        // ASIS: org.apache.commons.pool.impl.GenericObjectPool
        // TOBE: org.apache.commons.pool2.impl.GenericObjectPool (패키지 변경)
        Class<?> clazz = Class.forName("org.apache.commons.pool2.impl.GenericObjectPool");
        assertNotNull(clazz, "commons-pool2의 GenericObjectPool 클래스가 로딩되어야 한다");
    }
}
