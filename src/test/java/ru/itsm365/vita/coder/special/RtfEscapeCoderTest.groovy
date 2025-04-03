package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class RtfEscapeCoderTest {
    private Coder coder;

    @Before
    void setUp() throws Exception {
        coder = new RtfEscapeCoder();
    }

    @Test
    void toRtfOneSign() {
        String mdText = '⊗'
        String expected = '&otimes;'
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtfOneTest() {
        String mdText = '\\*<div>'
        String expected = '\\*&lt;div&gt;'
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = '⊗♦ 151651 \\#\\|\\*\\+♥ 1651 dvs ⌊'
        String expected = '&otimes;&diams; 151651 \\#\\|\\*\\+&hearts; 1651 dvs &lfloor;'
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMdOneSign() {
        String rtfText = '&otimes;'
        String expected = '⊗'
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMdTest() {
        String rtfText = '\\*&lt;div&gt;'
        String expected = '\\*<div>'
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMdLine() {
        String rtfText = '&otimes;&diams;&nbsp;151651 ###+++  sdsdfsd * - _ ~ |&hearts; 1651 dvs &lfloor;'
        String expected = '⊗♦ 151651 ###+++  sdsdfsd * - _ ~ |♥ 1651 dvs ⌊'
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}