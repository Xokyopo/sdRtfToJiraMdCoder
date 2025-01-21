package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LineBreakCoderTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private LineBreakCoder lineBreakCoder;

    @Before
    void initial() {
        this.lineBreakCoder = new LineBreakCoder();
    }

    @Test
    void reversTest() {
        String expected = "dffdgfd${LINE_SEPARATOR}dfgfdgdgffd${LINE_SEPARATOR}dfsdfsdfsd ${LINE_SEPARATOR}sdfsdfsdfdf";
        String actual = lineBreakCoder.toMd(lineBreakCoder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenRun() {
        String mdText = "dffdgfd${LINE_SEPARATOR}dfgfdgdgffd${LINE_SEPARATOR}dfsdfsdfsd ${LINE_SEPARATOR}sdfsdfsdfdf";
        String expected = "dffdgfd<br/>dfgfdgdgffd<br/>dfsdfsdfsd <br/>sdfsdfsdfdf";
        String actual = lineBreakCoder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenRun() {
        String rtfText = "dffdgfd<br/>dfgfdgdgffd<br/>dfsdfsdfsd <br/>sdfsdfsdfdf";
        String expected = "dffdgfd${LINE_SEPARATOR}dfgfdgdgffd${LINE_SEPARATOR}dfsdfsdfsd ${LINE_SEPARATOR}sdfsdfsdfdf";
        String actual = lineBreakCoder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenTagIsUpperCase() {
        String rtfText = "dffdgfd<BR/>dfgfdgdgffd<Br/>dfsdfsdfsd <bR/>sdfsdfsdfdf";
        String expected = "dffdgfd${LINE_SEPARATOR}dfgfdgdgffd${LINE_SEPARATOR}dfsdfsdfsd ${LINE_SEPARATOR}sdfsdfsdfdf";
        String actual = lineBreakCoder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenInsideAOtherTag() {
        String rtfText = "<p>dffdgfd<BR/></p><p>dfgfdgdgffd<Br/></p>dfsdfsdfsd <p><bR/></p>sdfsdfsdfdf";
        String expected = "<p>dffdgfd${LINE_SEPARATOR}</p><p>dfgfdgdgffd${LINE_SEPARATOR}</p>dfsdfsdfsd <p>${LINE_SEPARATOR}</p>sdfsdfsdfdf";
        String actual = lineBreakCoder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
