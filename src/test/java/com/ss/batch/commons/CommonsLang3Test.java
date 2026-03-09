package com.ss.batch.commons;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * commons-lang3 테스트.
 *
 * <p>ASIS: commons-lang 2.6 (commons-lang:commons-lang, org.apache.commons.lang 패키지)<br>
 * TOBE: commons-lang3 (Boot 관리 버전, org.apache.commons.lang3 패키지)</p>
 *
 * <p>전환 이유: lang 2.x → lang3으로 패키지 변경(org.apache.commons.lang3).
 * commons-lang 2.x는 더 이상 유지보수되지 않으며, 기존 코드의 import 경로 변경이 필요하다.</p>
 */
class CommonsLang3Test {

    @Test
    @DisplayName("[TOBE] commons-lang3 - StringUtils 기본 동작 확인")
    void stringUtilsBasic() {
        assertTrue(StringUtils.isBlank(""), "빈 문자열은 blank여야 한다");
        assertFalse(StringUtils.isBlank("batch"), "non-empty 문자열은 blank가 아니어야 한다");
        assertEquals("BATCH", StringUtils.upperCase("batch"),
                "upperCase가 올바르게 동작해야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-lang3 - NumberUtils 확인")
    void numberUtilsBasic() {
        assertTrue(NumberUtils.isCreatable("123"), "숫자 문자열은 isCreatable이어야 한다");
        assertFalse(NumberUtils.isCreatable("abc"), "문자 문자열은 isCreatable이 아니어야 한다");
        assertEquals(42, NumberUtils.toInt("42"), "toInt 변환이 올바르게 동작해야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-lang3 - 패키지 경로 확인 (org.apache.commons.lang3)")
    void lang3PackageVerification() throws ClassNotFoundException {
        // ASIS: org.apache.commons.lang.StringUtils
        // TOBE: org.apache.commons.lang3.StringUtils (패키지 변경)
        Class<?> clazz = Class.forName("org.apache.commons.lang3.StringUtils");
        assertNotNull(clazz, "org.apache.commons.lang3.StringUtils 클래스가 로딩되어야 한다");
    }
}
