package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class MdEscapeCoderTest {
    private Coder coder;

    @Before
    void setUp() throws Exception {
        coder = new MdEscapeCoder();
    }

    @Test
    void toMd_ShouldReturnEscapedPlus_WhenHavePlus() {
        String mdText = '>+<'
        String expected = '>\\+<'
        String actual = this.coder.toMd(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnEscapedMinus_WhenHaveMinus() {
        String mdText = '>-<'
        String expected = '>\\-<'
        String actual = this.coder.toMd(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnEscapedHash_WhenHaveHash() {
        String mdText = '>#<'
        String expected = '>\\#<'
        String actual = this.coder.toMd(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnEscapedTilda_WhenHaveTilda() {
        String mdText = '>~<'
        String expected = '>\\~<'
        String actual = this.coder.toMd(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnEscapedLowLine_WhenHaveLowLine() {
        String mdText = '>_<'
        String expected = '>\\_<'
        String actual = this.coder.toMd(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnEscapedPlus_WhenHavePlus() {
        String rtfText = '\\+'
        String expected = '+'
        String actual = this.coder.toRtf(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnEscapedMinus_WhenHaveMinus() {
        String rtfText = '\\-'
        String expected = '-'
        String actual = this.coder.toRtf(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnEscapedHash_WhenHaveHash() {
        String rtfText = '\\#'
        String expected = '#'
        String actual = this.coder.toRtf(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnEscapedTilda_WhenHaveTilda() {
        String rtfText = '\\~'
        String expected = '~'
        String actual = this.coder.toRtf(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnEscapedTilda_WhenHaveMultiply() {
        String rtfText = '\\*'
        String expected = '*'
        String actual = this.coder.toRtf(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnEscapedLowLine_WhenHaveLowLine() {
        String rtfText = '\\_'
        String expected = '_'
        String actual = this.coder.toRtf(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveMinusInsideAttributeName() {
        String rtfText = '<tr fr-original-style="">'
        String expected = rtfText
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveMinusInsideStyleAttribute() {
        String rtfText = '<tr style="user-select: none;">'
        String expected = rtfText
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveHashInsideAttribute() {
        String rtfText = '<tr style="color: #fff">'
        String expected = rtfText
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
