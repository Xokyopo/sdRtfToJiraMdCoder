package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RtfToJiraMdCoderTest {
    private Coder coder;

    private final TestValue simpleText = new TestValue("/simpleText/md", "/simpleText/rtf");

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void reverse_rtf_simpleText() {
        String expectedRtfText = this.simpleText.rtf
        String actualRtfTest = this.coder.toRtf(this.coder.toMd(expectedRtfText));
        Assert.assertEquals(expectedRtfText, actualRtfTest);
    }

    @Test
    void testToRtf() {
        println(this.coder.toRtf(this.simpleText.md));
    }

    @Test
    void testToMd() {
        println(this.coder.toMd(this.simpleText.rtf));
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
