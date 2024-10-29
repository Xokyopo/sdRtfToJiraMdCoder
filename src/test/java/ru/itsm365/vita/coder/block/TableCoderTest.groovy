package ru.itsm365.vita.coder.block

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class TableCoderTest {

    private Coder coder;
    private static final String TABLE_START = "<table style=\"border-collapse: collapse;\">"
    private static final String TABLE_END = "</table>"
    private static final String THEAD_START = "<thead>"
    private static final String THEAD_END = "</thead>"
    private static final String TBODY_START = "<tbody>"
    private static final String TBODY_END = "</tbody>"
    private static final String TR_START = "<tr>"
    private static final String TR_END = "</tr>"
    private static final String TH_START = "<th style=\"background: rgb(230, 230, 230);border: 1px solid rgb(221, 221, 221);\">"
    private static final String TH_END = "</th>"
    private static final String TD_START = "<td style=\"border: 1px solid rgb(221, 221, 221);\">"
    private static final String TD_END = "</td>"


    @Before
    void setUp() {
        this.coder = new TableCoder();
    }

    @Test
    void toRtf() {
        String mdText = 'fdfbdfb\n|11|12|\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        println actual
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtfWithRigthslash() {
        String mdText = 'fdfbdfb\n|11|12\\|21|\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12|21' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtfWithLink() {
        String mdText = 'fdfbdfb\n|11|12[link title|http://example.com/] 21|\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12[link title|http://example.com/] 21' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtfHead() {
        String mdText = 'fdfbdfb\n||11||12 [link title|http://example.com/] 21||\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + THEAD_START + TR_START + TH_START + '11' + TH_END + TH_START + '12 [link title|http://example.com/] 21' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_n() {
        String mdText = 'fdfbdfb\n||1\n1\n1||12 [link title|http://example.com/] 21||\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + THEAD_START + TR_START + TH_START + '1\n1\n1' + TH_END + TH_START + '12 [link title|http://example.com/] 21' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_Screenshot() {
        String mdText = 'fdfbdfb\n||1 \n!screenshot-1.png|thumbnail!\n 1||12 [link title|http://example.com/] 21||\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + THEAD_START + TR_START + TH_START + '1 \n!screenshot-1.png|thumbnail!\n 1' + TH_END + TH_START + '12 [link title|http://example.com/] 21' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    void toMd() {
    }
}
