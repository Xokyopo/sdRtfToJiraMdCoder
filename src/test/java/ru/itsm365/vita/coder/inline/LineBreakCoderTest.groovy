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
    void toRtf_ShouldReturnBr_WhenHaveLineSeparatorInsideListElement() {
        String mdText = " * Строка 1${LINE_SEPARATOR}Строка 2";
        String expected = " * Строка 1<br>Строка 2";
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
}
