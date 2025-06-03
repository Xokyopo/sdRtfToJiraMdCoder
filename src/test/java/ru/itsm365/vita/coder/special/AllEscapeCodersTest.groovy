package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class AllEscapeCodersTest {
    private Coder rtfEscapeCoder;
    private Coder mdEscapeCoder;

    @Before
    void setUp() throws Exception {
        rtfEscapeCoder = new RtfEscapeCoder();
        mdEscapeCoder = new MdEscapeCoder();
    }

    @Test
    void MdReverse() {
        String mdText = '>⊗♦ 151651 \\#\\|\\*\\+♥ 1651 dvs ⌊<'
        String actual = this.mdEscapeCoder.toMd(this.rtfEscapeCoder.toMd(this.mdEscapeCoder.toRtf(this.rtfEscapeCoder.toRtf(mdText))));
        Assert.assertEquals(mdText, actual)
    }

    @Test
    void RtfReverse() {
        String rtfText = '&lt;div&gr;&otimes;&diams; 151651 ###+++  sdsdfsd * - _ ~ |&hearts; 1651 dvs &lfloor;';
        String actual = this.mdEscapeCoder.toRtf(this.rtfEscapeCoder.toRtf(this.mdEscapeCoder.toMd(this.rtfEscapeCoder.toMd(rtfText))));
        Assert.assertEquals(rtfText, actual);
    }

    @Test
    void toRtfOneSign() {
        String mdText = '⊗'
        String expected = '&otimes;'
        String actual = this.mdEscapeCoder.toRtf(this.rtfEscapeCoder.toRtf(mdText));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = '⊗♦ 151651 \\#\\|\\*\\+♥ 1651 dvs ⌊'
        String expected = '&otimes;&diams; 151651 #|*+&hearts; 1651 dvs &lfloor;'
        String actual = this.mdEscapeCoder.toRtf(this.rtfEscapeCoder.toRtf(mdText));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMdOneSign() {
        String rtfText = '&otimes;'
        String expected = '⊗'
        String actual = this.mdEscapeCoder.toMd(this.rtfEscapeCoder.toMd(rtfText));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMdOne() {
        String rtfText = '>&otimes;&diams;151651 ###+++  sdsdfsd * - _ ~ |&hearts; 1651 dvs &lfloor;<sdfgsdg+-*+>&otimes;&diams;151651 ###+++  sdsdfsd * - _ ~ |&hearts; 1651 dvs &lfloor;<sdfgsdg+-*+>&otimes;&diams;151651 ###+++  sdsdfsd * - _ ~ |&hearts; 1651 dvs &lfloor;<sdfgsdg+-*+>'
        String expected = '>⊗♦151651 \\#\\#\\#\\+\\+\\+  sdsdfsd \\* \\- \\_ \\~ \\|♥ 1651 dvs ⌊<sdfgsdg+-*+>⊗♦151651 \\#\\#\\#\\+\\+\\+  sdsdfsd \\* \\- \\_ \\~ \\|♥ 1651 dvs ⌊<sdfgsdg+-*+>⊗♦151651 \\#\\#\\#\\+\\+\\+  sdsdfsd \\* \\- \\_ \\~ \\|♥ 1651 dvs ⌊<sdfgsdg+-*+>'
        String actual = this.mdEscapeCoder.toMd(this.rtfEscapeCoder.toMd(rtfText));
        Assert.assertEquals(expected, actual);
    }
}