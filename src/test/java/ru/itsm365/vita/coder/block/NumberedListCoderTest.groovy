package ru.itsm365.vita.coder.block

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NumberedListCoderTest {
    private NumberedListCoder coder;

    @Before
    void setUp() {
        this.coder = new NumberedListCoder();
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
        String expected = "<ol><li>первый элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "<ol><li>первый элемент</li></ol>";
        String expected = "1. первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotAddNexLineBefore_WhenElementIsFirst() {
        String mdText = "1. первый элемент";
        String expected = "<ol><li>первый элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldNotAddNexLineBefore_WhenElementIsAlone() {
        String rtfText = "<ol><li>первый элемент</li></ol>";
        String expected = "1. первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotAddedEmptyLine_WhenElementIsLast() {
        String mdText = "1. первый элемент";
        String expected = "<ol><li>первый элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTwoElement_WhenRun() {
        String mdText = "1. первый элемент\n2. второй элемент";
        String expected = "<ol><li>первый элемент</li><li>второй элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTwoElement_WhenRun() {
        String rtfText = "<ol><li>первый элемент</li><li>второй элемент</li></ol>";
        String expected = "1. первый элемент\n2. второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNestedListInListItem_WhenRun() {
        String mdText = "1. первый элемент\n   1. вложенный элемент\n2. второй элемент";
        String expected = "<ol><li>первый элемент<ol><li>вложенный элемент</li></ol></li><li>второй элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNestedListInListItem_WhenRun() {
        String rtfText = "<ol><li>первый элемент<ol><li>вложенный элемент</li></ol></li><li>второй элемент</li></ol>";
        String expected = "1. первый элемент\n   1. вложенный элемент\n2. второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNestedListInNestedListItem_WhenRun() {
        String mdText = "1. первый элемент\n   1. вложенный элемент\n      2. вложенный во вложенный элемент\n2. второй элемент";
        String expected = "<ol><li>первый элемент<ol><li>вложенный элемент<ol><li>вложенный во вложенный элемент</li></ol></li></ol></li><li>второй элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNestedListInNestedListItem_WhenRun() {
        String rtfText = "<ol><li>первый элемент<ol><li>вложенный элемент<ol><li>вложенный во вложенный элемент</li></ol></li></ol></li><li>второй элемент</li></ol>";
        String expected = "1. первый элемент\n   1. вложенный элемент\n      1. вложенный во вложенный элемент\n2. второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowList_WhenHaveTwoList() {
        String mdText = "1. первый список\n\n1. второй список";
        String expected = "<ol><li>первый список</li></ol><ol><li>второй список</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowList_WhenHaveTwoList() {
        String rtfText = "<ol><li>первый список</li></ol><ol><li>второй список</li></ol>";
        String expected = "1. первый список\n\n1. второй список";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowListWithText_WhenHaveTextBetweenLists() {
        String mdText = "1. первый список\n\nТекст между списками\n1. второй список";
        String expected = "<ol><li>первый список</li></ol>Текст между списками\n<ol><li>второй список</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowListWithText_WhenHaveTextBetweenLists() {
        String rtfText = "<ol><li>первый список</li></ol>Текст между списками<ol><li>второй список</li></ol>";
        String expected = "1. первый список\n\nТекст между списками\n1. второй список";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConverterList_WhenHaveCamelCaseTag() {
        String rtfText = "<OL><LI>первый элемент</LI></OL>";
        String expected = "1. первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
