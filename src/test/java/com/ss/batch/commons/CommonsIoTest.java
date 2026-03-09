package com.ss.batch.commons;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * commons-io 테스트.
 *
 * <p>ASIS: commons-io 2.4<br>
 * TOBE: commons-io 2.16.1</p>
 *
 * <p>전환 이유: FileUtils, IOUtils 등 유틸 클래스 대폭 개선, JDK 8+ NIO 지원 강화.</p>
 */
class CommonsIoTest {

    @Test
    @DisplayName("[TOBE] commons-io 2.16.1 - IOUtils.toString 확인")
    void ioUtilsToString() throws Exception {
        String expected = "배치 시스템 commons-io 테스트";
        InputStream is = new ByteArrayInputStream(expected.getBytes(StandardCharsets.UTF_8));

        String result = IOUtils.toString(is, StandardCharsets.UTF_8);

        assertEquals(expected, result, "IOUtils.toString이 올바른 문자열을 반환해야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-io 2.16.1 - FileUtils 클래스 로딩 확인")
    void fileUtilsClassLoadable() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("org.apache.commons.io.FileUtils");
        assertNotNull(clazz, "FileUtils 클래스가 로딩되어야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-io 2.16.1 - IOUtils.toByteArray 확인")
    void ioUtilsToByteArray() throws Exception {
        byte[] expected = "batch-test".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(expected);

        byte[] result = IOUtils.toByteArray(is);

        assertArrayEquals(expected, result, "IOUtils.toByteArray가 올바른 바이트 배열을 반환해야 한다");
    }
}
