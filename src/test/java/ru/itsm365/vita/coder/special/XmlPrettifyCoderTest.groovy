package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class XmlPrettifyCoderTest {

    private XmlPrettifyCoder coder;

    private final TestValue simpleText = new TestValue("/simpleText/md", "/simpleText/rtf");


    @Before
    void setUp() {
        this.coder = new XmlPrettifyCoder();
    }

    @Test
    void testToRtf() {
        coder.toRtf(simpleText.rtf);
        Assert.assertTrue(true);
    }

    @Test
    void testToMd() {
        coder.toMd(simpleText.rtf);
        Assert.assertTrue(true);
    }

    static class TestValue {
        public final String md;
        public final String rtf;

        TestValue(String md, String rtf) {
            this.md = loadResource(md)
            this.rtf = loadResource(rtf)
        }

        private static String loadResource(String text) {
            InputStream inputStream = TestValue.class.getResourceAsStream(text)
            try {
                if (inputStream == null) {
                    throw new IllegalArgumentException("Ресурс с таким именем не найден или доступ к ресурсу запрещен менеджером безопасности");
                }

                return inputStream.text
            } finally {
                if (inputStream) {
                    inputStream.close();
                }
            }
        }
    }
}
