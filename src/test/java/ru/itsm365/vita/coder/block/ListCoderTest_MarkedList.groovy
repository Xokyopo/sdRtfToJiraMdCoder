package ru.itsm365.vita.coder.block

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class ListCoderTest_MarkedList {
    private Coder coder;

    @Before
    void setUp() {
        this.coder = new ListCoder();
    }

    @Test
    void reversTest_MarkedList() {
        String expected = "текст до списка\n* первый список\n\nтекст между списками\n* второй список";
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_MarkedList() {
        String mdText = "* первый элемент";
        String expected = "<ul><li>первый элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_MarkedList() {
        String rtfText = "<ul><li>первый элемент</li></ul>";
        String expected = "* первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotAddNexLineBefore_WhenElementIsFirst() {
        String mdText = "* первый элемент";
        String expected = "<ul><li>первый элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldNotAddNexLineBefore_WhenElementIsAlone() {
        String rtfText = "<ul><li>первый элемент</li></ul>";
        String expected = "* первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotAddedEmptyLine_WhenElementIsLast() {
        String mdText = "* первый элемент";
        String expected = "<ul><li>первый элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTwoElement_WhenRun() {
        String mdText = "* первый элемент\n* второй элемент";
        String expected = "<ul><li>первый элемент</li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTwoElement_WhenRun() {
        String rtfText = "<ul><li>первый элемент</li><li>второй элемент</li></ul>";
        String expected = "* первый элемент\n* второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNestedListInListItem_WhenRun() {
        String mdText = "* первый элемент\n    * вложенный элемент\n* второй элемент";
        String expected = "<ul><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNestedListInListItem_WhenRun() {
        String rtfText = "<ul><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ul>";
        String expected = "* первый элемент\n    * вложенный элемент\n* второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNestedListInNestedListItem_WhenRun() {
        String mdText = "* первый элемент\n    * вложенный элемент\n        * вложенный во вложенный элемент\n* второй элемент";
        String expected = "<ul><li>первый элемент<ul><li>вложенный элемент<ul><li>вложенный во вложенный элемент</li></ul></li></ul></li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNestedListInNestedListItem_WhenRun() {
        String rtfText = "<ul><li>первый элемент<ul><li>вложенный элемент<ul><li>вложенный во вложенный элемент</li></ul></li></ul></li><li>второй элемент</li></ul>";
        String expected = "* первый элемент\n    * вложенный элемент\n        * вложенный во вложенный элемент\n* второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowList_WhenHaveTwulist() {
        String mdText = "* первый список\n\n* второй список";
        String expected = "<ul><li>первый список</li></ul><ul><li>второй список</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowList_WhenHaveTwulist() {
        String rtfText = "<ul><li>первый список</li></ul><ul><li>второй список</li></ul>";
        String expected = "* первый список\n\n* второй список";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowListWithText_WhenHaveTextBetweenLists() {
        String mdText = "* первый список\n\nТекст между списками\n* второй список";
        String expected = "<ul><li>первый список</li></ul>Текст между списками\n<ul><li>второй список</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowListWithText_WhenHaveTextBetweenLists() {
        String rtfText = "<ul><li>первый список</li></ul>Текст между списками<ul><li>второй список</li></ul>";
        String expected = "* первый список\n\nТекст между списками\n* второй список";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnConverterList_WhenHaveItalicTextBeforeList() {
        String mdText = "*курсив который должен выжить*\n* первый элемент";
        String expected = "*курсив который должен выжить*\n<ul><li>первый элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConverterList_WhenHaveCamelCaseTag() {
        String rtfText = "<UL><LI>первый элемент</LI></UL>";
        String expected = "* первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
