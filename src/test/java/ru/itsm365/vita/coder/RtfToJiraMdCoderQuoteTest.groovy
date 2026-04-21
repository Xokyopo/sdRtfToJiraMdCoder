package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoderQuoteTest {
    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();
    private Coder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf() {
        TestValue testValue = new TestValue("/originalText/quote/md.txt", "/expectedText/quote/rtf.txt");
        String expected = testValue.rtf;
        String actual = this.coder.toRtf(testValue.md);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        TestValue testValue = new TestValue("/expectedText/quote/md.txt", "/originalText/quote/rtf.txt");
        String expected = testValue.md;
        String actual = this.coder.toMd(testValue.rtf);

        Assert.assertEquals(expected, actual);
    }
}
