package ru.itsm365.vita.coder.line

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class ImageCoderTest {
    private Coder coder;

    @Before
    void setUp() {
        this.coder = new ImageCoder();
    }

    @Test
    void reversTest() {
        String expected = '!screenshot-1.png|thumbnail!';
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = '!screenshot-1.png|thumbnail!';
        String expected = '<img src="screenshot-1.png">';
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = '<img src="screenshot-1.png">';
        String expected = '!screenshot-1.png|thumbnail!';
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
