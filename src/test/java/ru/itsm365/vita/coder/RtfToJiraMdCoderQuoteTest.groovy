package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoderQuoteTest {
    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();
    private final TestValue originalText = new TestValue("/originalText/quote/md.txt", "/originalText/quote/rtf.txt");
    private final TestValue expectedText = new TestValue("/expectedText/quote/md.txt", "/expectedText/quote/rtf.txt");

    private Coder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf_ComplexQuote_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalText.md;
        String actual = this.coder.toRtf(expected);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expectedText.rtf), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd_ComplexLQuote_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalText.rtf;
        String actual = this.coder.toMd(expected);
        Assert.assertEquals(this.expectedText.md, actual);
    }

    @Test
    void toMd_ReverseComplexQuote_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalText.md;
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(this.expectedText.md, actual);
    }

    @Test
    void toRtf_ReverseComplexQuote_ReturnExpectedResult_WhenConvertOriginalText() {
        String expected = this.originalText.rtf;
        String actual = this.coder.toRtf(this.coder.toMd(expected));
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expectedText.rtf), this.xmlPrettifyCoder.toRtf(actual));
    }
}
