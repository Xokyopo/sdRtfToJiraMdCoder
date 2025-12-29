package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue

class RtfToJiraMdCoderListTest {
    private RtfToJiraMdCoder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf_ComplexList_ReturnExpectedResult_WhenConvertOriginalText() {
        TestValue testValue = new TestValue("/originalText/list/md.txt", "/expectedText/list/rtf.txt");
        String expected = testValue.rtf;
        String actual = this.coder.toRtf(testValue.md);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ComplexList_ReturnExpectedResult_WhenConvertOriginalText() {
        TestValue testValue = new TestValue("/expectedText/list/md.txt", "/originalText/list/rtf.txt");
        String expected = testValue.md;
        String actual = this.coder.toMd(testValue.rtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ReverseComplexList_ReturnExpectedResult_WhenConvertOriginalText() {
        TestValue originalTestValue = new TestValue("/originalText/list/md.txt", "/originalText/list/rtf.txt");
        TestValue expectedTestValue = new TestValue("/expectedText/list/md.txt", "/expectedText/list/rtf.txt");
        String expected = expectedTestValue.md;
        String actual = this.coder.toMd(this.coder.toRtf(originalTestValue.md));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ReverseComplexList_ReturnExpectedResult_WhenConvertOriginalText() {
        TestValue originalTestValue = new TestValue("/originalText/list/md.txt", "/originalText/list/rtf.txt");
        TestValue expectedTestValue = new TestValue("/expectedText/list/md.txt", "/expectedText/list/rtf.txt");
        String expected = expectedTestValue.rtf;
        String actual = this.coder.toRtf(this.coder.toMd(originalTestValue.rtf));
        Assert.assertEquals(expected, actual);
    }
}
