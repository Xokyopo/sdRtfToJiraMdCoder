package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoderTableTest {
    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();
    private final TestValue originalTableText = new TestValue("/originalText/table/md.txt", "/originalText/table/rtf.txt");
    private final TestValue expectedTableText = new TestValue("/expectedText/table/md.txt", "/expectedText/table/rtf.txt");

    private Coder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf_ComplexTable_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalTableText.md;
        String actual = this.coder.toRtf(expected);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expectedTableText.rtf), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd_ComplexTable_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalTableText.rtf;
        String actual = this.coder.toMd(expected);
        Assert.assertEquals(this.originalTableText.md, actual);
    }

    @Test
    void toRtf_ReverseComplexTable_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalTableText.rtf;
        String actual = this.coder.toRtf(this.coder.toMd(expected));
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expectedTableText.rtf), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd_ReverseComplexTable_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalTableText.md;
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(this.originalTableText.md, actual);
    }
}
