package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoderTest {
    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();
    private final TestValue simpleText = new TestValue("/simpleText/md", "/simpleText/rtf");
    private final TestValue originalText = new TestValue("/originalText/md", "/originalText/rtf");
    private final TestValue expectedText = new TestValue("/expectedText/md", "/expectedText/rtf");

    private Coder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void reverse_rtf_simpleText() {
        String expectedRtfText = xmlPrettifyCoder.toRtf(this.simpleText.rtf);
        String actualRtfTest = this.coder.toRtf(this.coder.toMd(expectedRtfText));
        Assert.assertEquals(expectedRtfText, actualRtfTest);
    }

    @Test
    void reverse_md_simpleText() {
        String expectedMdText = this.simpleText.md;
        String actualMdTest = this.coder.toMd(this.coder.toRtf(expectedMdText));
        Assert.assertEquals(expectedMdText, actualMdTest);
    }

    @Test
    void toRtf_ReturnExpectedResult_WhenConvertOriginalText() {
        String expectedRtfText = this.expectedText.rtf;
        String actualRtfTest = this.coder.toRtf(this.originalText.md);
        Assert.assertEquals(expectedRtfText, actualRtfTest);
    }

    @Test
    void toMd_ReturnExpectedResult_WhenConvertOriginalText() {
        String expectedMdText = this.expectedText.md;
        String actualMdTest = this.coder.toMd(this.originalText.rtf);
        Assert.assertEquals(expectedMdText, actualMdTest);
    }

    @Test
    void testToRtf() {
        println(this.coder.toRtf(this.simpleText.md));
    }

    @Test
    void testToMd() {
        println(this.coder.toMd(this.simpleText.rtf));
    }

    @Test
    void formRgbToHexCoder() {
        //#00875a
        int r = 16;
        int g = 15;
        int b = 17;

        String hexR = String.format("%s%s", r < 16 ? "0" : "", Integer.toHexString(r));
        String hexG = String.format("%s%s", g < 16 ? "0" : "", Integer.toHexString(g));
        String hexB = String.format("%s%s", b < 16 ? "0" : "", Integer.toHexString(b));
        println("#${hexR}${hexG}${hexB}");
    }

    @Test
    void formHexToRgbCoder() {
        String hex = "#00875a"
        String hexR = "00";
        String hexG = "87";
        String hexB = "5a";

        int r = Integer.parseInt(hexR, 16);
        int g = Integer.parseInt(hexG, 16);
        int b = Integer.parseInt(hexB, 16);

        println("rgp(${r}, ${g}, ${b})")
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
