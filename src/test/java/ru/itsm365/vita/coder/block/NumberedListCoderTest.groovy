package ru.itsm365.vita.coder.block

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class NumberedListCoderTest {
    private Coder coder;

    @Before
    void setUp() {
        this.coder = new NumberedListCoder();
    }

    @Test
    void reversTest() {
        String expected = "текст до списка${System.lineSeparator()}# первый список${System.lineSeparator()}${System.lineSeparator()}текст между списками${System.lineSeparator()}# второй список";
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = "# первый элемент";
        String expected = "<ol><li>первый элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "<ol><li>первый элемент</li></ol>";
        String expected = "# первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotAddNexLineBefore_WhenElementIsFirst() {
        String mdText = "# первый элемент";
        String expected = "<ol><li>первый элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldNotAddNexLineBefore_WhenElementIsAlone() {
        String rtfText = "<ol><li>первый элемент</li></ol>";
        String expected = "# первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotAddedEmptyLine_WhenElementIsLast() {
        String mdText = "# первый элемент";
        String expected = "<ol><li>первый элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTwoElement_WhenRun() {
        String mdText = "# первый элемент${System.lineSeparator()}# второй элемент";
        String expected = "<ol><li>первый элемент</li><li>второй элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTwoElement_WhenRun() {
        String rtfText = "<ol><li>первый элемент</li><li>второй элемент</li></ol>";
        String expected = "# первый элемент${System.lineSeparator()}# второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNestedListInListItem_WhenRun() {
        String mdText = "# первый элемент${System.lineSeparator()}    # вложенный элемент${System.lineSeparator()}# второй элемент";
        String expected = "<ol><li>первый элемент<ol><li>вложенный элемент</li></ol></li><li>второй элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNestedListInListItem_WhenRun() {
        String rtfText = "<ol><li>первый элемент<ol><li>вложенный элемент</li></ol></li><li>второй элемент</li></ol>";
        String expected = "# первый элемент${System.lineSeparator()}    # вложенный элемент${System.lineSeparator()}# второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNestedListInNestedListItem_WhenRun() {
        String mdText = "# первый элемент${System.lineSeparator()}    # вложенный элемент${System.lineSeparator()}        # вложенный во вложенный элемент${System.lineSeparator()}# второй элемент";
        String expected = "<ol><li>первый элемент<ol><li>вложенный элемент<ol><li>вложенный во вложенный элемент</li></ol></li></ol></li><li>второй элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNestedListInNestedListItem_WhenRun() {
        String rtfText = "<ol><li>первый элемент<ol><li>вложенный элемент<ol><li>вложенный во вложенный элемент</li></ol></li></ol></li><li>второй элемент</li></ol>";
        String expected = "# первый элемент${System.lineSeparator()}    # вложенный элемент${System.lineSeparator()}        # вложенный во вложенный элемент${System.lineSeparator()}# второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowList_WhenHaveTwoList() {
        String mdText = "# первый список${System.lineSeparator()}${System.lineSeparator()}# второй список";
        String expected = "<ol><li>первый список</li></ol>${System.lineSeparator()}${System.lineSeparator()}<ol><li>второй список</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowList_WhenHaveTwoList() {
        String rtfText = "<ol><li>первый список</li></ol>${System.lineSeparator()}${System.lineSeparator()}<ol><li>второй список</li></ol>";
        String expected = "# первый список${System.lineSeparator()}${System.lineSeparator()}# второй список";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTowListWithText_WhenHaveTextBetweenLists() {
        String mdText = "# первый список${System.lineSeparator()}${System.lineSeparator()}Текст между списками${System.lineSeparator()}# второй список";
        String expected = "<ol><li>первый список</li></ol>${System.lineSeparator()}${System.lineSeparator()}Текст между списками${System.lineSeparator()}<ol><li>второй список</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTowListWithText_WhenHaveTextBetweenLists() {
        String rtfText = "<ol><li>первый список</li></ol>${System.lineSeparator()}${System.lineSeparator()}Текст между списками${System.lineSeparator()}<ol><li>второй список</li></ol>";
        String expected = "# первый список${System.lineSeparator()}${System.lineSeparator()}Текст между списками${System.lineSeparator()}# второй список";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConverterList_WhenHaveCamelCaseTag() {
        String rtfText = "<OL><LI>первый элемент</LI></OL>";
        String expected = "# первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldIgnoreTagAttributes_WhenItHave() {
        String rtfText = '<ol data-name="list"><li id="index_1">первый элемент</li></ol>';
        String expected = "# первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldConvertWithSavingEmptyLineBefore() {
        String mdText = "${System.lineSeparator()}# первый элемент";
        String expected = "${System.lineSeparator()}<ol><li>первый элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldConvertWithSavingEmptyLineBefore() {
        String rtfText = "${System.lineSeparator()}<ol><li>первый элемент</li></ol>";
        String expected = "${System.lineSeparator()}# первый элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldConvertWithSavingEmptyLineAfter() {
        String mdText = "# первый элемент${System.lineSeparator()}";
        String expected = "<ol><li>первый элемент</li></ol>${System.lineSeparator()}";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldConvertWithSavingEmptyLineAfter() {
        String rtfText = "<ol><li>первый элемент</li></ol>${System.lineSeparator()}";
        String expected = "# первый элемент${System.lineSeparator()}";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnOneListWithTwoItem_WhenHaveSpaceBeforeListItem() {
        String mdText = " # Первый элемент нумерованного списка.${System.lineSeparator()}" +
                " # Второй элемент нумерованного списка.";
        String expected = "<ol><li>Первый элемент нумерованного списка.</li><li>Второй элемент нумерованного списка.</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }
}
