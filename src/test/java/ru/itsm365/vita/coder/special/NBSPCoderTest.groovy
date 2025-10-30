package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NBSPCoderTest {
    private NBSPCoder coder;

    @Before
    void setUp() {
        this.coder = new NBSPCoder()
    }

    @Test
    void toRtf_revers() {
        String mdText = " ";
        String expected = mdText
        String actual = this.coder.toMd(this.coder.toRtf(mdText));

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_revers() {
        String mdText = "&nbsp;";
        String expected = mdText;
        String actual = this.coder.toRtf(this.coder.toMd(mdText));

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = " ";
        String expected = "&nbsp;";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String mdText = "&nbsp;";
        String expected = " ";
        String actual = this.coder.toMd(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowNBSP_WhenHaveTowSpaces() {
        String mdText = "<h1>  Заголовок 1  </h1>";
        String expected = "<h1>&nbsp; Заголовок 1&nbsp;&nbsp;</h1>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowNBSP_WhenHaveTowSpaces() {
        String mdText = "<h1>&nbsp; Заголовок 1&nbsp;&nbsp;</h1>";
        String expected = "<h1>  Заголовок 1  </h1>";
        String actual = this.coder.toMd(mdText);

        Assert.assertEquals(expected, actual);
    }
}
