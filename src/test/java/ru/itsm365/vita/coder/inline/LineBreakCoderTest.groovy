package ru.itsm365.vita.coder.inline

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LineBreakCoderTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String MD_NBSP = " ";

    private LineBreakCoder lineBreakCoder;

    @Before
    void initial() {
        this.lineBreakCoder = new LineBreakCoder();
    }

    @Test
    void toMd() {
        String rtfText = "Строка 1<br>Строка 2";
        String expected = "Строка 1${LINE_SEPARATOR}Строка 2";
        String actual = lineBreakCoder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNotJoinedLine_WhenHaveLineSeparatorInsideListElement() {
        String mdText = " * Строка 1${LINE_SEPARATOR}Строка 2";
        String expected = " * Строка 1${LINE_SEPARATOR}Строка 2";
        String actual = lineBreakCoder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnBr_WhenHaveEmptyLineBefore() {
        String mdText = "${LINE_SEPARATOR}${LINE_SEPARATOR}Строка 2";
        String expected = "${LINE_SEPARATOR}${LINE_SEPARATOR}Строка 2";
        String actual = lineBreakCoder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnBr_WhenHaveLineSeparatorInsideTableElement() {
        String mdText = "|Заголовок 1| Заголовок 2|${LINE_SEPARATOR}|Строка 1${LINE_SEPARATOR}Строка 2|Столбец 2|";
        String expected = "|Заголовок 1| Заголовок 2|${LINE_SEPARATOR}|Строка 1<br>Строка 2|Столбец 2|";
        String actual = lineBreakCoder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }


    @Test
    void toMd_ShouldReturnNbsp_WhenHaveEmptyLine() {
        String rtfText = "Строка 1${LINE_SEPARATOR}<br>${LINE_SEPARATOR}Строка 2";
        String expected = "Строка 1${LINE_SEPARATOR}${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}${LINE_SEPARATOR}Строка 2";
        String actual = lineBreakCoder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturn_WhenHaveManyLinesInTableCell() {
        String rtfText = "|Строка 1${LINE_SEPARATOR}Строка 2${LINE_SEPARATOR}Строка 3${LINE_SEPARATOR}Строка 4|";
        String expected = "|Строка 1<br>Строка 2<br>Строка 3<br>Строка 4|";
        String actual = lineBreakCoder.toRtf(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturn_WhenHaveManyLineBreakerInTable() {
        String rtfText = "<td>Строка 1<br>Строка 2<br><br>Строка 3<br><br><br>Строка 4<td>";
        String expected = "<td>Строка 1${LINE_SEPARATOR}Строка 2${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}Строка 3${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}Строка 4<td>";
        String actual = lineBreakCoder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturn_WhenHaveManyLineBreakerInTable() {
        String rtfText = "|Строка 1${LINE_SEPARATOR}Строка 2${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}Строка 3${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}Строка 4|";
        String expected = "|Строка 1<br>Строка 2<br><br>Строка 3<br><br><br>Строка 4|";
        String actual = lineBreakCoder.toRtf(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNbsp_WhenHaveTwoEmptyLine() {
        String rtfText = "Строка 1${LINE_SEPARATOR}<br>${LINE_SEPARATOR}<br>${LINE_SEPARATOR}Строка 2";
        String expected = "Строка 1${LINE_SEPARATOR}${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}${LINE_SEPARATOR}Строка 2";
        String actual = lineBreakCoder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNbsp_WhenHaveEmptyLine() {
        String mdText = "Строка 1${LINE_SEPARATOR}${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}${LINE_SEPARATOR}Строка 2";
        String expected = "Строка 1${LINE_SEPARATOR}<br>${LINE_SEPARATOR}Строка 2";
        String actual = lineBreakCoder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNbsp_WhenHaveTwoEmptyLine() {
        String mdText = "Строка 1${LINE_SEPARATOR}${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}${LINE_SEPARATOR}${MD_NBSP}${LINE_SEPARATOR}${LINE_SEPARATOR}Строка 2";
        String expected = "Строка 1${LINE_SEPARATOR}<br>${LINE_SEPARATOR}<br>${LINE_SEPARATOR}Строка 2";
        String actual = lineBreakCoder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldUnchangedNbsp_WhenLineWithBasicNbsp() {
        String mdText = "h1. ${MD_NBSP} Заголовок 1${MD_NBSP}${MD_NBSP}";
        String expected = "h1. ${MD_NBSP} Заголовок 1${MD_NBSP}${MD_NBSP}";
        String actual = lineBreakCoder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }
}
