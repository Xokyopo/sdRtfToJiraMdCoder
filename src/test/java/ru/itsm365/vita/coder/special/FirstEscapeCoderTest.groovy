package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class FirstEscapeCoderTest {
    private Coder coder;

    @Before
    void setUp() throws Exception {
        coder = new FirstEscapeCoder();
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
        String mdText = '&otimes;'
        String expected = '&otimes;'
        String actual = this.coder.toMd(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMdOneTest() {
        String mdText = '*&lt;div&gt;'
        String expected = '\\*&lt;div&gt;'
        String actual = this.coder.toMd(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMdOne() {
        String mdText = '&otimes;&diams;&nbsp;151651 ###+++  sdsdfsd * - _ ~ |&hearts; 1651 dvs &lfloor;'
        String expected = '&otimes;&diams;&nbsp;151651 \\#\\#\\#\\+\\+\\+  sdsdfsd \\* \\- \\_ \\~ \\|&hearts; 1651 dvs &lfloor;'
        String actual = this.coder.toMd(mdText);
        Assert.assertEquals(expected, actual);
    }
}