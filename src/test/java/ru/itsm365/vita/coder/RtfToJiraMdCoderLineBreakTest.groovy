package ru.itsm365.vita.coder

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue

class RtfToJiraMdCoderLineBreakTest {
    private RtfToJiraMdCoder coder;

    @Before
    void setUp() {
        this.coder = new RtfToJiraMdCoder();
    }

    @Test
    void toRtf() {
        TestValue testValues = new TestValue("/originalText/lineBreak/md.txt", "/expectedText/lineBreak/rtf.txt");
        String expected = testValues.rtf;
        String actual = this.coder.toRtf(testValues.md);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        TestValue testValues = new TestValue("/expectedText/lineBreak/md.txt", "/originalText/lineBreak/rtf.txt");
        String expected = testValues.md;
        String actual = this.coder.toMd(testValues.rtf);
        Assert.assertEquals(expected, actual);
    }
}
