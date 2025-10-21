package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoderTableTest {
    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();
    private final TestValue originalTableText = new TestValue("/originalText/table/md", "/originalText/table/rtf");
    private final TestValue expectedTableText = new TestValue("/expectedText/table/md", "/expectedText/table/rtf");

    private Coder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtfComplexTable_ReturnExpectedResult_WhenConvertOriginalText() {
        String expectedRtfText = this.originalTableText.md;
        String actualRtfTest = this.coder.toRtf(expectedRtfText);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expectedTableText.rtf), this.xmlPrettifyCoder.toRtf(actualRtfTest));
    }

    @Test
    void toMdComplexTable_ReturnExpectedResult_WhenConvertOriginalText() {
        String expectedMdText = this.originalTableText.rtf;
        String actualMdTest = this.coder.toMd(expectedMdText);
        Assert.assertEquals(this.originalTableText.md, actualMdTest);
    }
}
