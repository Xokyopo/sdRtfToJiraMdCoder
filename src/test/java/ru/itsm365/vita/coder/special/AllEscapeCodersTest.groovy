package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class AllEscapeCodersTest {
    private Coder firstEscapeCoder;
    private Coder lastEscapeCoder;

    @Before
    void setUp() throws Exception {
        firstEscapeCoder = new FirstEscapeCoder();
        lastEscapeCoder = new LastEscapeCoder();
    }

    @Test
    void MdReverse() {
        String mdText = '⊗♦ 151651 \\#\\|\\*\\+♥ 1651 dvs ⌊'
        String actual = this.lastEscapeCoder.toMd(this.firstEscapeCoder.toMd(this.lastEscapeCoder.toRtf(this.firstEscapeCoder.toRtf(mdText))));
        Assert.assertEquals(mdText, actual)
    }

    @Test
    void RtfReverse() {
        String rtfText = '&otimes;&diams; 151651 ###+++  sdsdfsd * - _ ~ |&hearts; 1651 dvs &lfloor;';
        String actual = this.lastEscapeCoder.toRtf(this.firstEscapeCoder.toRtf(this.lastEscapeCoder.toMd(this.firstEscapeCoder.toMd(rtfText))));
        Assert.assertEquals(rtfText, actual);
    }

    @Test
    void toRtfOneSign() {
        String mdText = '⊗'
        String expected = '&otimes;'
        String actual = this.lastEscapeCoder.toRtf(this.firstEscapeCoder.toRtf(mdText));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = '⊗♦ 151651 \\#\\|\\*\\+♥ 1651 dvs ⌊'
        String expected = '&otimes;&diams; 151651 #|*+&hearts; 1651 dvs &lfloor;'
        String actual = this.lastEscapeCoder.toRtf(this.firstEscapeCoder.toRtf(mdText));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMdOneSign() {
        String rtfText = '&otimes;'
        String expected = '⊗'
        String actual = this.lastEscapeCoder.toMd(this.firstEscapeCoder.toMd(rtfText));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMdOne() {
        String rtfText = '&otimes;&diams;&nbsp;151651 ###+++  sdsdfsd * - _ ~ |&hearts; 1651 dvs &lfloor;'
        String expected = '⊗♦ 151651 \\#\\#\\#\\+\\+\\+  sdsdfsd \\* \\- \\_ \\~ \\|♥ 1651 dvs ⌊'
        String actual = this.lastEscapeCoder.toMd(this.firstEscapeCoder.toMd(rtfText));
        Assert.assertEquals(expected, actual);
    }
}