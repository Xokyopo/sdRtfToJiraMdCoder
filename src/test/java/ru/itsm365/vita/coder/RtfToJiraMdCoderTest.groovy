package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue

class RtfToJiraMdCoderTest {

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
}
