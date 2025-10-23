package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoderHeaderTest {
    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();

    private RtfToJiraMdCoder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf_reverseTest() {
        TestValue testValues = new TestValue("/originalText/header/md.txt", "");
        TestValue fixedExpectedTestValues = new TestValue("/expectedText/header/md_reversTest.txt", "");
        String expected = fixedExpectedTestValues.md;
        String actual = this.coder.toMd(this.coder.toRtf(testValues.md));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_reverseTest() {
        TestValue testValues = new TestValue("", "/originalText/header/rtf.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(this.coder.toMd(testValues.rtf));
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toRtf() {
        TestValue testValues = new TestValue("/originalText/header/md.txt", "/expectedText/header/rtf.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(testValues.md);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd() {
        TestValue testValues = new TestValue("/expectedText/header/md.txt", "/originalText/header/rtf.txt");
        String expected = testValues.md;
        String actual = this.coder.toMd(testValues.rtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_italicTest() {
        TestValue testValues = new TestValue("/originalText/header/md_italic.txt", "/expectedText/header/rtf_italic.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(testValues.md);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd_italicTest() {
        TestValue testValues = new TestValue("/expectedText/header/md_italic.txt", "/originalText/header/rtf_italic.txt");
        String expected = testValues.md;
        String actual = this.coder.toMd(testValues.rtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_coloredTest() {
        TestValue testValues = new TestValue("/originalText/header/md_colored.txt", "/expectedText/header/rtf_colored.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(testValues.md);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd_coloredTest() {
        TestValue testValues = new TestValue("/expectedText/header/md_colored.txt", "/originalText/header/rtf_colored.txt");
        String expected = testValues.md;
        String actual = this.coder.toMd(testValues.rtf);
        Assert.assertEquals(expected, actual);
    }
}
