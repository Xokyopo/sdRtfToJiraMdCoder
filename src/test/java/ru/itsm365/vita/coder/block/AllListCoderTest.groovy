package ru.itsm365.vita.coder.block

import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AllListCoderTest {

    private AllListCoder coder;

    @BeforeEach
    void setUp() {
        this.coder = new AllListCoder();
    }

    @Test
    void toRtf_ShouldReturnConvertedList_WhenHaveNumericListInsideMarkedList() {
        String mdText = " * первый элемент${System.lineSeparator()} ## вложенный элемент${System.lineSeparator()} * второй элемент";
        String expected = "<ul><li>первый элемент<ol><li>вложенный элемент</li></ol></li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedList_WhenHaveNumericListInsideMarkedList() {
        String rtfText = "<ul><li>первый элемент<ol><li>вложенный элемент</li></ol></li><li>второй элемент</li></ul>";
        String expected = " * первый элемент${System.lineSeparator()} ## вложенный элемент${System.lineSeparator()} * второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnConvertedList_WhenHaveNumericListWithMarkedInsideMarkedList() {
        String mdText = " * первый элемент${System.lineSeparator()} ## вложенный элемент${System.lineSeparator()} ** вложенный элемент${System.lineSeparator()}* второй элемент";
        String expected = "<ul><li>первый элемент<ol><li>вложенный элемент</li></ol><ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedList_WhenHaveNumericListWithMarkedInsideMarkedList() {
        String rtfText = "<ul><li>первый элемент<ol><li>вложенный элемент</li></ol><ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ul>";
        String expected = " * первый элемент${System.lineSeparator()} ## вложенный элемент${System.lineSeparator()} ** вложенный элемент${System.lineSeparator()} * второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnConvertedList_WhenHaveMarkedListInsideNumericList() {
        String mdText = " # первый элемент${System.lineSeparator()} ** вложенный элемент${System.lineSeparator()} # второй элемент";
        String expected = "<ol><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedList_WhenHaveMarkedListInsideNumericList() {
        String rtfText = "<ol><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ol>";
        String expected = " # первый элемент${System.lineSeparator()} ** вложенный элемент${System.lineSeparator()} # второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldConvertOnlyListInTable_WhenAloneMultiplyInTable() {
        String mdText = "| # первый элемент|5 * 7|";
        String expected = "|<ol><li>первый элемент</li></ol>|5 * 7|";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldConvertOnlyListInTable_WhenAloneMultiplyInTable() {
        String rtfText = "|<ol><li>первый элемент</li></ol>|5 * 7|";
        String expected = "| # первый элемент|5 * 7|";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldConvertMultilineListInTable_WhenHaveSeveralItemsInCell() {
        String mdText = "| # первый элемент${System.lineSeparator()} # второй элемент|5 * 7|";
        String expected = "|<ol><li>первый элемент</li><li>второй элемент</li></ol>|5 * 7|";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldConvertMultilineListInTable_WhenHaveSeveralItemsInCell() {
        String rtfText = "|<ol><li>первый элемент</li><li>второй элемент</li></ol>|5 * 7|";
        String expected = "| # первый элемент${System.lineSeparator()} # второй элемент|5 * 7|";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

        @Test
    void toRtf_ShouldConvertMultilineListInTable_WhenHaveSeveralItemsInCell2() {
        String mdText = "текст${System.lineSeparator()}| # первый элемент${System.lineSeparator()} # второй элемент|5 * 7|";
        String expected = "текст${System.lineSeparator()}|<ol><li>первый элемент</li><li>второй элемент</li></ol>|5 * 7|";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldConvertMultilineListInTable_WhenHaveSeveralItemsInCell2() {
        String rtfText = "текст${System.lineSeparator()}|<ol><li>первый элемент</li><li>второй элемент</li></ol>|5 * 7|";
        String expected = "текст${System.lineSeparator()}| # первый элемент${System.lineSeparator()} # второй элемент|5 * 7|";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldConvertComplexDocument_WhenHaveNestedListsInTwoTablesSeparatedByEmptyLine() {
        String nl = System.lineSeparator();
        String empty = nl * 2;
        String mdText = "текст перед${nl}| # первый элемент${nl} ** вложенный элемент${nl} # второй элемент| * маркированный|5 * 7|${empty}| # элемент таблицы два${nl} ** вложенный два|ячейка|";
        String expected = "текст перед${nl}|<ol><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ol>|<ul><li>маркированный</li></ul>|5 * 7|${empty}|<ol><li>элемент таблицы два<ul><li>вложенный два</li></ul></li></ol>|ячейка|";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldConvertComplexDocument_WhenHaveNestedListsInTwoTablesSeparatedByEmptyLine() {
        String nl = System.lineSeparator();
        String empty = nl * 2;
        String rtfText = "текст перед${nl}|<ol><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ol>|<ul><li>маркированный</li></ul>|5 * 7|${empty}|<ol><li>элемент таблицы два<ul><li>вложенный два</li></ul></li></ol>|ячейка|";
        String expected = "текст перед${nl}| # первый элемент${nl} ** вложенный элемент${nl} # второй элемент| * маркированный|5 * 7|${empty}| # элемент таблицы два${nl} ** вложенный два|ячейка|";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

}
