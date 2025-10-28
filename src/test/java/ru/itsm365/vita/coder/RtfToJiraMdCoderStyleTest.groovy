package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoderStyleTest {
    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();

    private RtfToJiraMdCoder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf_reverseTest() {
        TestValue testValues = new TestValue("/originalText/style/md.txt", "");
        String expected = testValues.md;
        String actual = this.coder.toMd(this.coder.toRtf(testValues.md));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_reverseTest() {
        TestValue testValues = new TestValue("", "/originalText/style/rtf.txt");
        TestValue expectedTextValues = new TestValue("", "/expectedText/style/rtf.txt");
        String expected = expectedTextValues.rtf;
        String actual = this.coder.toRtf(this.coder.toMd(testValues.rtf));
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toRtf() {
        TestValue testValues = new TestValue("/originalText/style/md.txt", "/expectedText/style/rtf.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(testValues.md);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd() {
        TestValue testValues = new TestValue("/expectedText/style/md.txt", "/originalText/style/rtf.txt");
        String expected = testValues.md;
        String actual = this.coder.toMd(testValues.rtf);
        Assert.assertEquals(expected, actual);
    }

    //TODO добавить тесты с цветами.
    @Test
    void toRtf_coloredTest() {
        TestValue testValues = new TestValue("/originalText/style/md_colored.txt", "/expectedText/style/rtf_colored.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(testValues.md);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd_coloredTest() {
        TestValue testValues = new TestValue("/expectedText/style/md_colored.txt", "/originalText/style/rtf_colored.txt");
        String expected = testValues.md;
        String actual = this.coder.toMd(testValues.rtf);
        Assert.assertEquals(expected, actual);
    }
}
