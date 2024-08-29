package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LineBreakCoderTest {

    private LineBreakCoder lineBreakCoder;

    @Before
    void initial() {
        this.lineBreakCoder = new LineBreakCoder();
    }

    @Test
    void reversTest() {
        String expected = "dffdgfd\ndfgfdgdgffd\ndfsdfsdfsd \nsdfsdfsdfdf";
        String actual = lineBreakCoder.toMd(lineBreakCoder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void testToRtf_ShouldReturnTrue_WhenRun() {
        String mdText = "dffdgfd\ndfgfdgdgffd\ndfsdfsdfsd \nsdfsdfsdfdf";
        String expected = "dffdgfd<br/>dfgfdgdgffd<br/>dfsdfsdfsd <br/>sdfsdfsdfdf";
        String actual = lineBreakCoder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void testToMd_ShouldReturnTrue_WhenRun() {
        String rtfText = "dffdgfd<br/>dfgfdgdgffd<br/>dfsdfsdfsd <br/>sdfsdfsdfdf";
        String expected = "dffdgfd\ndfgfdgdgffd\ndfsdfsdfsd \nsdfsdfsdfdf";
        String actual = lineBreakCoder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
