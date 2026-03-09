package com.ss.batch.commons;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * commons-configuration2 테스트.
 *
 * <p>ASIS: commons-configuration 1.10 (org.apache.commons:commons-configuration)<br>
 * TOBE: commons-configuration2 2.10.1 (org.apache.commons:commons-configuration2)</p>
 *
 * <p>전환 이유: configuration 1.x EOL, 2.x에서 artifactId 변경 및 API 재설계.
 * Builder 패턴 기반의 설정 로딩으로 변경되었다.</p>
 */
class CommonsConfigTest {

    @Test
    @DisplayName("[TOBE] commons-configuration2 2.10.1 - PropertiesConfiguration 클래스 로딩 확인")
    void commonsConfiguration2ClassLoadable() throws ClassNotFoundException {
        // commons-configuration2의 핵심 클래스가 로딩 가능한지 확인한다
        Class<?> clazz = Class.forName("org.apache.commons.configuration2.PropertiesConfiguration");
        assertNotNull(clazz, "PropertiesConfiguration 클래스가 로딩되어야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-configuration2 2.10.1 - PropertiesConfiguration 인스턴스 생성 확인")
    void createPropertiesConfiguration() {
        // PropertiesConfiguration 인스턴스를 직접 생성하여 사용한다
        PropertiesConfiguration config = new PropertiesConfiguration();
        config.setProperty("test.key", "test.value");
        config.setProperty("batch.name", "sampleBatch");

        assertNotNull(config, "PropertiesConfiguration이 null이면 안 된다");
        assertEquals("test.value", config.getString("test.key"),
                "설정된 값을 정상적으로 읽어야 한다");
        assertEquals("sampleBatch", config.getString("batch.name"),
                "배치 이름이 일치해야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-configuration2 2.10.1 - Parameters 빌더 API 확인")
    void parametersApiAvailable() {
        // commons-configuration2의 새로운 빌더 패턴 API가 사용 가능한지 확인한다
        Parameters params = new Parameters();
        assertNotNull(params, "Parameters 빌더가 null이면 안 된다");
    }
}
