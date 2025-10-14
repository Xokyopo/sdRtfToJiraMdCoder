package ru.itsm365.vita.coder.block

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class MarkedAllListCoderTest {
    private Coder coder;

    @Before
    void setUp() {
        this.coder = new AllListCoder();
    }

    @Test
    void reversTest() {
        String expected = "текст до списка${System.lineSeparator()}* первый список${System.lineSeparator()}${System.lineSeparator()}текст между списками${System.lineSeparator()}* второй список";
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = "* первый элемент";
        String expected = "<ul><li>первый элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
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
        String mdText = "* первый элемент${System.lineSeparator()}* второй элемент";
        String expected = "<ul><li>первый элемент</li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTwoElement_WhenRun() {
        String rtfText = "<ul><li>первый элемент</li><li>второй элемент</li></ul>";
        String expected = "* первый элемент${System.lineSeparator()}* второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNestedListInListItem_WhenRun() {
        String mdText = "* первый элемент${System.lineSeparator()}** вложенный элемент${System.lineSeparator()}* второй элемент";
        String expected = "<ul><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNestedListInListItem_WhenRun() {
        String rtfText = "<ul><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ul>";
        String expected = "* первый элемент${System.lineSeparator()}** вложенный элемент${System.lineSeparator()}* второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNestedListInNestedListItem_WhenRun() {
        String mdText = "* первый элемент${System.lineSeparator()}** вложенный элемент${System.lineSeparator()}*** вложенный во вложенный элемент${System.lineSeparator()}* второй элемент";
        String expected = "<ul><li>первый элемент<ul><li>вложенный элемент<ul><li>вложенный во вложенный элемент</li></ul></li></ul></li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNestedListInNestedListItem_WhenRun() {
        String rtfText = "<ul><li>первый элемент<ul><li>вложенный элемент<ul><li>вложенный во вложенный элемент</li></ul></li></ul></li><li>второй элемент</li></ul>";
        String expected = "* первый элемент${System.lineSeparator()}** вложенный элемент${System.lineSeparator()}*** вложенный во вложенный элемент${System.lineSeparator()}* второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowList_WhenHaveTwoList() {
        String mdText = "* первый список${System.lineSeparator()}${System.lineSeparator()}* второй список";
        String expected = "<ul><li>первый список</li></ul>${System.lineSeparator()}${System.lineSeparator()}<ul><li>второй список</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowList_WhenHaveTwoList() {
        String rtfText = "<ul><li>первый список</li></ul>${System.lineSeparator()}${System.lineSeparator()}<ul><li>второй список</li></ul>";
        String expected = "* первый список${System.lineSeparator()}${System.lineSeparator()}* второй список";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowListWithText_WhenHaveTextBetweenLists() {
        String mdText = "* первый список${System.lineSeparator()}${System.lineSeparator()}Текст между списками${System.lineSeparator()}* второй список";
        String expected = "<ul><li>первый список</li></ul>${System.lineSeparator()}${System.lineSeparator()}Текст между списками${System.lineSeparator()}<ul><li>второй список</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowListWithText_WhenHaveTextBetweenLists() {
        String rtfText = "<ul><li>первый список</li></ul>${System.lineSeparator()}${System.lineSeparator()}Текст между списками${System.lineSeparator()}<ul><li>второй список</li></ul>";
        String expected = "* первый список${System.lineSeparator()}${System.lineSeparator()}Текст между списками${System.lineSeparator()}* второй список";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnConverterList_WhenHaveItalicTextBeforeList() {
        String mdText = "*курсив который должен выжить*${System.lineSeparator()}* первый элемент";
        String expected = "*курсив который должен выжить*${System.lineSeparator()}<ul><li>первый элемент</li></ul>";
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

    @Test
    void toMd_ShouldIgnoreTagAttributes_WhenItHave() {
        String rtfText = '<ul data-name="list"><li id="index_1">первый элемент</li></ul>';
        String expected = "* первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldConvertWithSavingEmptyLineBefore() {
        String mdText = "${System.lineSeparator()}* первый элемент";
        String expected = "${System.lineSeparator()}<ul><li>первый элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldConvertWithSavingEmptyLineBefore() {
        String rtfText = "${System.lineSeparator()}<ul><li>первый элемент</li></ul>";
        String expected = "${System.lineSeparator()}* первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldConvertWithSavingEmptyLineAfter() {
        String mdText = "* первый элемент${System.lineSeparator()}";
        String expected = "<ul><li>первый элемент</li></ul>${System.lineSeparator()}";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldConvertWithSavingEmptyLineAfter() {
        String rtfText = "<ul><li>первый элемент</li></ul>${System.lineSeparator()}";
        String expected = "* первый элемент${System.lineSeparator()}";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
