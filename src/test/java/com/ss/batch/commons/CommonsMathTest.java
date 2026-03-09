package com.ss.batch.commons;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.MathUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * commons-math3 테스트.
 *
 * <p>ASIS: commons-math3 3.6.1<br>
 * TOBE: commons-math3 3.6.1 (동일 버전 유지)</p>
 *
 * <p>배치 처리에서 통계 계산 등에 활용되는 수학 라이브러리의 JDK 17 호환성을 확인한다.</p>
 */
class CommonsMathTest {

    @Test
    @DisplayName("[TOBE] commons-math3 3.6.1 - DescriptiveStatistics 기본 동작 확인")
    void descriptiveStatistics() {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        stats.addValue(10.0);
        stats.addValue(20.0);
        stats.addValue(30.0);

        assertEquals(20.0, stats.getMean(), 0.001, "평균이 20.0이어야 한다");
        assertEquals(3, stats.getN(), "데이터 수가 3이어야 한다");
        assertEquals(30.0, stats.getMax(), 0.001, "최대값이 30.0이어야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-math3 3.6.1 - MathUtils 클래스 로딩 확인")
    void mathUtilsClassLoadable() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("org.apache.commons.math3.util.MathUtils");
        assertNotNull(clazz, "MathUtils 클래스가 로딩되어야 한다");
    }
}
