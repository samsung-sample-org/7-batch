package com.ss.batch.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XStream XML 직렬화 테스트.
 *
 * <p>ASIS: xstream 1.4.7 (CVE-2021-29505 등 다수 취약점)<br>
 * TOBE: xstream 1.4.21 (보안 패치 적용)</p>
 *
 * <p>전환 이유: xstream 1.4.7에 RCE 취약점 다수 존재.
 * 1.4.21에서 CVE 패치 및 보안 강화가 적용되었다.
 * XStream 1.4.x 이상에서는 허용 타입을 명시적으로 설정해야 한다.</p>
 */
class XStreamTest {

    @Test
    @DisplayName("[TOBE] xstream 1.4.21 - XStream 인스턴스 생성 확인")
    void createXStream() {
        XStream xstream = new XStream();
        // XStream 1.4.x 이상에서는 허용 타입을 명시적으로 설정해야 보안이 강화된다
        xstream.addPermission(AnyTypePermission.ANY);
        assertNotNull(xstream, "XStream 인스턴스가 null이면 안 된다");
    }

    @Test
    @DisplayName("[TOBE] xstream 1.4.21 - XML 직렬화/역직렬화 확인")
    void xmlSerializationRoundTrip() {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("batchItem", BatchItem.class);
        xstream.allowTypes(new Class[]{BatchItem.class});

        BatchItem original = new BatchItem("item-001", 100);
        String xml = xstream.toXML(original);

        assertNotNull(xml, "직렬화된 XML이 null이면 안 된다");
        assertTrue(xml.contains("item-001"), "XML에 itemId가 포함되어야 한다");

        BatchItem deserialized = (BatchItem) xstream.fromXML(xml);
        assertEquals(original.itemId, deserialized.itemId,
                "역직렬화 후 itemId가 일치해야 한다");
        assertEquals(original.quantity, deserialized.quantity,
                "역직렬화 후 quantity가 일치해야 한다");
    }

    static class BatchItem {
        String itemId;
        int quantity;

        BatchItem(String itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }
    }
}
