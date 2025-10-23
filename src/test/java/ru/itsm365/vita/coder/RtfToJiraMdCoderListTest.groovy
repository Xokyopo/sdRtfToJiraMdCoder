package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoderListTest {
    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();
    private final TestValue originalListText = new TestValue("/originalText/list/md.txt", "/originalText/list/rtf.txt");
    private final TestValue expectedListText = new TestValue("/expectedText/list/md.txt", "/expectedText/list/rtf.txt");

    private Coder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf_ComplexList_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalListText.md;
        String actual = this.coder.toRtf(expected);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expectedListText.rtf), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd_ComplexList_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalListText.rtf;
        String actual = this.coder.toMd(expected);
        Assert.assertEquals(this.expectedListText.md, actual);
    }

    @Test
    void toMd_ReverseComplexList_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalListText.md;
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(this.expectedListText.md, actual);
    }

    @Test
    void toRtf_ReverseComplexList_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalListText.rtf;
        String actual = this.coder.toRtf(this.coder.toMd(expected));
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expectedListText.rtf), this.xmlPrettifyCoder.toRtf(actual));
    }
}
