package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue
import ru.itsm365.vita.coder.special.XmlPrettifyCoder

class RtfToJiraMdCoderSpacesTest {
    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();

    private RtfToJiraMdCoder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf_reverseTest() {
        TestValue testValues = new TestValue("/originalText/spaces/md.txt", "");
        String expected = testValues.md;
        String actual = this.coder.toMd(this.coder.toRtf(testValues.md));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_reverseTest() {
        TestValue testValues = new TestValue("", "/originalText/spaces/rtf.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(this.coder.toMd(testValues.rtf));
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toRtf() {
        TestValue testValues = new TestValue("/originalText/spaces/md.txt", "/expectedText/spaces/rtf.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(testValues.md);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd() {
        TestValue testValues = new TestValue("/expectedText/spaces/md.txt", "/originalText/spaces/rtf.txt");
        String expected = testValues.md;
        String actual = this.coder.toMd(testValues.rtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_double() {
        TestValue testValues = new TestValue("/originalText/spaces/md_double_spaces.txt", "/expectedText/spaces/rtf_double_spaces.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(testValues.md);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd_double() {
        TestValue testValues = new TestValue("/expectedText/spaces/md_double_spaces.txt", "/originalText/spaces/rtf_double_spaces.txt");
        String expected = testValues.md;
        String actual = this.coder.toMd(testValues.rtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_triple() {
        TestValue testValues = new TestValue("/originalText/spaces/md_triple_spaces.txt", "/expectedText/spaces/rtf_triple_spaces.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(testValues.md);
        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expected), this.xmlPrettifyCoder.toRtf(actual));
    }

    @Test
    void toMd_triple() {
        TestValue testValues = new TestValue("/expectedText/spaces/md_triple_spaces.txt", "/originalText/spaces/rtf_triple_spaces.txt");
        String expected = testValues.md;
        String actual = this.coder.toMd(testValues.rtf);
        Assert.assertEquals(expected, actual);
    }
}
