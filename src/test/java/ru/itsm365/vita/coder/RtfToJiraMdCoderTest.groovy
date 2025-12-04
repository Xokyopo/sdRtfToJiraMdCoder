package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoderTest {
    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();
    private final TestValue simpleText = new TestValue("/simpleText/md.txt", "/simpleText/rtf.txt");

    private Coder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf_ReturnExpectedResult_WhenConvertOriginalText() {
        TestValue testValue = new TestValue("/originalText/md.txt", "/expectedText/rtf.txt");
        String expectedRtfText = testValue.rtf;
        String actualRtfTest = this.coder.toRtf(testValue.md);
        Assert.assertEquals(expectedRtfText, actualRtfTest);
    }

    @Test
    void toMd_ReturnExpectedResult_WhenConvertOriginalText() {
        TestValue testValue = new TestValue("/expectedText/md.txt", "/originalText/rtf.txt");
        String expectedMdText = testValue.md;
        String actualMdTest = this.coder.toMd(testValue.rtf);
        Assert.assertEquals(expectedMdText, actualMdTest);
    }

    //Полный реверс не достижим
//    @Test
//    void reverse_rtf_simpleText() {
//        String expectedRtfText = xmlPrettifyCoder.toRtf(this.simpleText.rtf.txt);
//        String actualRtfTest = this.coder.toRtf(this.coder.toMd(expectedRtfText));
//        Assert.assertEquals(expectedRtfText, actualRtfTest);
//    }
//
//    @Test
//    void reverse_md_simpleText() {
//        String expectedMdText = this.simpleText.md.txt;
//        String actualMdTest = this.coder.toMd(this.coder.toRtf(expectedMdText));
//        Assert.assertEquals(expectedMdText, actualMdTest);
//    }

    @Test
    void testToRtf() {
        println(this.coder.toRtf(this.simpleText.md));
    }

    @Test
    void testToMd() {
        println(this.coder.toMd(this.simpleText.rtf));
    }
}
