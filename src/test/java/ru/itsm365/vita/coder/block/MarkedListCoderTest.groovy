package ru.itsm365.vita.coder.block

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MarkedListCoderTest {
    private MarkedListCoder coder;

    @Before
    void setUp() {
        this.coder = new MarkedListCoder();
    }

    @Test
    void reversTest() {
        String expected = "текст до списка\n1. первый список\n\nтекст между списками\n1. второй список";
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = "1. первый элемент";
        String expected = "<ul><li>первый элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "<ul><li>первый элемент</li></ul>";
        String expected = "1. первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotAddNexLineBefore_WhenElementIsFirst() {
        String mdText = "1. первый элемент";
        String expected = "<ul><li>первый элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldNotAddNexLineBefore_WhenElementIsAlone() {
        String rtfText = "<ul><li>первый элемент</li></ul>";
        String expected = "1. первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotAddedEmptyLine_WhenElementIsLast() {
        String mdText = "1. первый элемент";
        String expected = "<ul><li>первый элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTwoElement_WhenRun() {
        String mdText = "1. первый элемент\n2. второй элемент";
        String expected = "<ul><li>первый элемент</li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTwoElement_WhenRun() {
        String rtfText = "<ul><li>первый элемент</li><li>второй элемент</li></ul>";
        String expected = "1. первый элемент\n2. второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNestedListInListItem_WhenRun() {
        String mdText = "1. первый элемент\n   1. вложенный элемент\n2. второй элемент";
        String expected = "<ul><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNestedListInListItem_WhenRun() {
        String rtfText = "<ul><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ul>";
        String expected = "1. первый элемент\n   1. вложенный элемент\n2. второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNestedListInNestedListItem_WhenRun() {
        String mdText = "1. первый элемент\n   1. вложенный элемент\n      2. вложенный во вложенный элемент\n2. второй элемент";
        String expected = "<ul><li>первый элемент<ul><li>вложенный элемент<ul><li>вложенный во вложенный элемент</li></ul></li></ul></li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNestedListInNestedListItem_WhenRun() {
        String rtfText = "<ul><li>первый элемент<ul><li>вложенный элемент<ul><li>вложенный во вложенный элемент</li></ul></li></ul></li><li>второй элемент</li></ul>";
        String expected = "1. первый элемент\n   1. вложенный элемент\n      1. вложенный во вложенный элемент\n2. второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowList_WhenHaveTwulist() {
        String mdText = "1. первый список\n\n1. второй список";
        String expected = "<ul><li>первый список</li></ul><ul><li>второй список</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowList_WhenHaveTwulist() {
        String rtfText = "<ul><li>первый список</li></ul><ul><li>второй список</li></ul>";
        String expected = "1. первый список\n\n1. второй список";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowListWithText_WhenHaveTextBetweenLists() {
        String mdText = "1. первый список\n\nТекст между списками\n1. второй список";
        String expected = "<ul><li>первый список</li></ul>Текст между списками\n<ul><li>второй список</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowListWithText_WhenHaveTextBetweenLists() {
        String rtfText = "<ul><li>первый список</li></ul>Текст между списками<ul><li>второй список</li></ul>";
        String expected = "1. первый список\n\nТекст между списками\n1. второй список";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
