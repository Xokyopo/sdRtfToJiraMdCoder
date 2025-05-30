package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RtfNBSPCoderTest {
    private static final LINE_SEPARATOR = System.lineSeparator();
    private RtfNBSPCoder coder;

    @Before
    void setUp() {
        this.coder = new RtfNBSPCoder()
    }

    @Test
    void toRtf() {
        String mdText = "<h1> Заголовок 1 </h1>";
        String expected = "<h1>&nbsp;Заголовок 1&nbsp;</h1>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String mdText = "<h1>&nbsp;Заголовок 1&nbsp;</h1>";
        String expected = "<h1> Заголовок 1 </h1>";
        String actual = this.coder.toMd(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowNBSP_WhenHaveTowSpaces() {
        String mdText = "<h1>  Заголовок 1  </h1>";
        String expected = "<h1>&nbsp;&nbsp;Заголовок 1&nbsp;&nbsp;</h1>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowNBSP_WhenHaveTowSpaces() {
        String mdText = "<h1>&nbsp;&nbsp;Заголовок 1&nbsp;&nbsp;</h1>";
        String expected = "<h1>  Заголовок 1  </h1>";
        String actual = this.coder.toMd(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNBSP_WhenHaveLineSeparator() {
        String mdText = "<h1>${LINE_SEPARATOR} Заголовок 1 ${LINE_SEPARATOR}</h1>";
        String expected = "<h1>${LINE_SEPARATOR}&nbsp;Заголовок 1&nbsp;${LINE_SEPARATOR}</h1>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNBSP_WhenHaveLineSeparator() {
        String mdText = "<h1>${LINE_SEPARATOR}&nbsp;Заголовок 1&nbsp;${LINE_SEPARATOR}</h1>";
        String expected = "<h1>${LINE_SEPARATOR} Заголовок 1 ${LINE_SEPARATOR}</h1>";
        String actual = this.coder.toMd(mdText);

        Assert.assertEquals(expected, actual);
    }
}
