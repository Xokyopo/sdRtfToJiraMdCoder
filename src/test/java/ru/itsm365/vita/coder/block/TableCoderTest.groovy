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
    private static final String TR_START = "<tr >"
    private static final String TR_END = "</tr>"
    private static final String TH_START = "<th style=\"background: rgb(230, 230, 230);border: 1px solid rgb(221, 221, 221);\">"
    private static final String TH_END = "</th>"
    private static final String TD_START = "<td style=\"border: 1px solid rgb(221, 221, 221);\">"
    private static final String TD_END = "</td>"
    private static final String LINK = "Итог согласования \"<a fr-original-style=\" href=\"https://aptekivita-test.itsm365.com/sd/operator/?anchor=uuid:negotiation\$1859526701\" style=\"user-select: auto;\" target=\"_top\">согласование на Руководителя</a>\": <span class=\"catItemCircleAndTitle\"><div class=\"colorCircle\" title=\"Цвет элемента справочника не задан\" style=\"background-color:#669933;width:8px;height:8px;margin:0 0 1px 0;border-radius:50%;display:inline-block;margin-right:5px;\"></div><strong fr-original-style=\" style=\"font-weight: 700;\">Согласовано</strong></span><br><a fr-original-style=\" href=\"https://aptekivita-test.itsm365.com/sd/operator/?anchor=uuid:employee\$147264722\" style=\"user-select: auto;\" target=\"_top\">Бобров Александр Олегович</a>, <a fr-original-style=\" href=\"https://aptekivita-test.itsm365.com/sd/operator/?anchor=uuid:ou\$1273738182\" style=\"user-select: auto;\" target=\"_top\">Группа управления рабочим временем розничной сети</a> - <span class=\"catItemCircleAndTitle\"><div class=\"colorCircle\" title=\"Цвет элемента справочника не задан\" style=\"background-color:#669933;width:8px;height:8px;margin:0 0 1px 0;border-radius:50%;display:inline-block;margin-right:5px;\"></div><strong fr-original-style=\" style=\"font-weight: 700;\">Согласовано</strong></span>";
    private static final String FILE = "<div>dvvvdfvfdvfd<u>dfvdfvdfvdfv<em>dfvdfvdfvdfv<strong fr-original-style=\"\" style=\"font-weight: 700;\">dfvdfvdf</strong></em></u><em><strong fr-original-style=\"\" style=\"font-weight: 700;\">dfvdfv</strong></em><strong fr-original-style=\"\" style=\"font-weight: 700;\">dfvdf</strong>dfvdfv&nbsp;<sub>dfvdfvdfv</sub><sup>dfvdfvddfv</sup>fvfdvf</div><div><img src=\"data:image/png;base64,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\" style=\"display: inline-block; vertical-align: bottom; margin-right: 5px; margin-left: 5px; max-width: calc(100% - 10px); text-align: center; cursor: pointer; position: relative;\" temp_width=\"700px\" temp_height=\"346px\" width=\"700\" height=\"346\" fr-original-style=\"display: inline-block; vertical-align: bottom; margin-right: 5px; margin-left: 5px; max-width: calc(100% - 10px); text-align: center;\" fr-original-class=\"fr-draggable\"></div>"

    @Before
    void setUp() {
        this.coder = new TableCoder();
    }

    @Test
    void toRtf() {
        String mdText = 'fdfbdfb\n|11|12|\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }


    @Test
    void toMd() {
        String rtfText = 'fdfbdfb' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String expected = 'fdfbdfb|11|12|\n|21|22|'
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void reverse() {
        String expected = """           ||11||_12_||13||
                |[привязать заголовок|http://example.com]|22|23|
            |31|32|!screenshot-1.png|thumbnail!|
        """
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }


    @Test
    void toRtf_onlyTableWithHeadTag() {
        String mdText = '||11||12||\n|21|22|'
        String expected = TABLE_START + THEAD_START + TR_START + TH_START + '11' + TH_END + TH_START + '12' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }


    @Test
    void toRtf_saveEmptyLineBeforeTable() {
        String mdText = '\n|11|12|\n|21|22|'
        String expected = '\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_saveEmptyLineAfterTable() {
        String mdText = '|11|12|\n|21|22|\n'
        String expected = TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END + '\n';
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_brokenTableIgnoreTableWithoutFirstRightSlash() {
        String mdText = '11|12|'
        String expected = '11|12|'
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_brokenTableUnevenQuantityCells() {
        String mdText = """
        |11|12|13|14|
        |21|22|23|
        |31|32|
        """
        String expected = """
        |11|12|13|14|
        |21|22|23|
        |31|32|
        """
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_checkTableWithOneCell() {
        String mdText = "|11|"
        String expected = "|11|"
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }


    @Test
    void toRtf_checkTwoTables() {
        String mdText = "  fdfbdfb\n" +
                "     |11|12|\n" +
                "  |21|22|\n" +
                "fdfbdfb\n" +
                "|11|12|\n" +
                "|21|22|"
        String expected = "  fdfbdfb\n" + TABLE_START + TBODY_START + "     " + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + "  " + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END + "\nfdfbdfb\n" + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_checkThreeTables() {
        String mdText = 'fdfbdfb\n|11|12|\n|21|22|\nfdfbdfb\n|11|12|\n|21|22|\nfdfbdfb\n|11|12|\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END + '\nfdfbdfb\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END + '\nfdfbdfb\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_containsRightSlashInCell() {
        String mdText = 'fdfbdfb\n|11|12\\|21|\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12|21' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }


    @Test
    void toRtf_containsLinkInCell() {
        String mdText = 'fdfbdfb\n|11|12[link title|http://example.com/] 21|\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12[link title|http://example.com/] 21' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_containsTagHead() {
        String mdText = 'fdfbdfb\n||11||12 [link title|http://example.com/] 21||\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + THEAD_START + TR_START + TH_START + '11' + TH_END + TH_START + '12 [link title|http://example.com/] 21' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_containsNewLineInCell(){
        String mdText = 'fdfbdfb\n||1\n1\n1||12 [link title|http://example.com/] 21||\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + THEAD_START + TR_START + TH_START + '1\n1\n1' + TH_END + TH_START + '12 [link title|http://example.com/] 21' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_containsScreenshotInCell() {
        String mdText = 'fdfbdfb\n||1 \n!screenshot-1.png|thumbnail!\n 1||12 [link title|http://example.com/] 21||\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + THEAD_START + TR_START + TH_START + '1 \n!screenshot-1.png|thumbnail!\n 1' + TH_END + TH_START + '12 [link title|http://example.com/] 21' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_containTabInCell() {
        String mdText = 'fdfbdfb\n||\t\t!screenshot-1.png|thumbnail!||12 [link title|http://example.com/] 21||\n|21|22|'
        String expected = 'fdfbdfb\n' + TABLE_START + THEAD_START + TR_START + TH_START + '\t\t!screenshot-1.png|thumbnail!' + TH_END + TH_START + '12 [link title|http://example.com/] 21' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_startWithNewLine() {
        String rtfText = '\n' + TABLE_START + THEAD_START + TR_START + TH_START + '11' + TH_END + TH_START + '12' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END;
        String expected = "\n"+
                          "||11||12||\n"+
                          "|21|22|"
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }


    @Test
    void toMd_newLine() {
        String rtfText = 'fdfbdfb\n' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END + "\n0123456789";
        String expected = 'fdfbdfb\n|11|12|\n|21|22|\n0123456789'
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_containsLinkInCell() {
        String rtfText = 'fdfbdfb' + TABLE_START + THEAD_START + TR_START + TH_START + '1 ' + LINK + ' 1' + TH_END + TH_START + '12' + TH_END + TR_END+ THEAD_END +TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END + "0123456789";
        String expected = 'fdfbdfb||1 ' + LINK + ' 1||12||\n|21|22|0123456789'
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_containsFileInCell() {
        String rtfText = 'fdfbdfb' + TABLE_START + TBODY_START + TR_START + TD_START + '1 ' + FILE + ' 1' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END + "0123456789";
        String expected = 'fdfbdfb|1 ' + FILE + ' 1|12|\n|21|22|0123456789'
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_3Tables() {
        String rtfText = 'fdfbdfb' + TABLE_START + TBODY_START + TR_START + TD_START + '11' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END + '0123456789' + 'fdfbdfb' + TABLE_START + TBODY_START + TR_START + TD_START + '1 ' + FILE + ' 1' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END + '0123456789' + 'fdfbdfb' + TABLE_START + TBODY_START + TR_START + TD_START + '1' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END + '0123456789';
        String expected = 'fdfbdfb|11|12|\n|21|22|0123456789' + 'fdfbdfb|1 ' + FILE + ' 1|12|\n|21|22|0123456789' + 'fdfbdfb|1|12|\n|21|22|0123456789'
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_brokenTableWithoutLastTagTable() {
        String rtfText = TABLE_START + TBODY_START + TR_START + TD_START + '1 ' + LINK + ' 1' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END
        String expected = TABLE_START + TBODY_START + TR_START + TD_START + '1 ' + LINK + ' 1' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_brokenTableWithoutFirstTagTable() {
        String rtfText = TBODY_START + TR_START + TD_START + '1 ' + LINK + ' 1' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END
        String expected = TBODY_START + TR_START + TD_START + '1 ' + LINK + ' 1' + TD_END + TD_START + '12' + TD_END + TR_END + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_brokenTableWithoutFirstTagTR() {
        String rtfText = TABLE_START + THEAD_START + TH_START + '11' + TH_END + TH_START + '12' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END
        String expected = TABLE_START + THEAD_START + TH_START + '11' + TH_END + TH_START + '12' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TD_START + '22' + TD_END + TR_END + TBODY_END + TABLE_END
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_brokenTableUnevenQuantity() {
        String rtfText = TABLE_START + THEAD_START + TH_START + '11' + TH_END + TH_START + '12' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TR_END + TBODY_END + TABLE_END
        String expected = TABLE_START + THEAD_START + TH_START + '11' + TH_END + TH_START + '12' + TH_END + TR_END + THEAD_END + TBODY_START + TR_START + TD_START + '21' + TD_END + TR_END + TBODY_END + TABLE_END
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
