package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue

class RtfToJiraMdCoderTableTest {

    private Coder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf_ComplexTable_ReturnExpectedResult_WhenConvertOriginalText() {
        TestValue testValue = new TestValue("/originalText/table/md.txt", "/expectedText/table/rtf.txt");
        String expected = testValue.rtf;
        String actual = this.coder.toRtf(testValue.md);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ComplexTable_ReturnExpectedResult_WhenConvertOriginalText() {
        TestValue testValue = new TestValue("/expectedText/table/md.txt", "/originalText/table/rtf.txt");
        String expected = testValue.md;
        String actual = this.coder.toMd(testValue.rtf);
        Assert.assertEquals(expected, actual);
    }
}
