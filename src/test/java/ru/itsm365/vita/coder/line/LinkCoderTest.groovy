package ru.itsm365.vita.coder.line

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class LinkCoderTest {
    private Coder coder;

    @Before
    void setUp() {
        this.coder = new LinkCoder();
    }

    @Test
    void reversTest() {
        String expected = '[привязать заголовок|http://example.com]';
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = '[привязать заголовок|http://example.com]';
        String expected = '<a href="http://example.com">привязать заголовок</a>';
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = '<a href="http://example.com">привязать заголовок</a>';
        String expected = '[привязать заголовок|http://example.com]';
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
