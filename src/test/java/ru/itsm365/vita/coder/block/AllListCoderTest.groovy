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
        String mdText = "* первый элемент${System.lineSeparator()}## вложенный элемент${System.lineSeparator()}* второй элемент";
        String expected = "<ul><li>первый элемент<ol><li>вложенный элемент</li></ol></li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedList_WhenHaveNumericListInsideMarkedList() {
        String rtfText = "<ul><li>первый элемент<ol><li>вложенный элемент</li></ol></li><li>второй элемент</li></ul>";
        String expected = "* первый элемент${System.lineSeparator()}## вложенный элемент${System.lineSeparator()}* второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnConvertedList_WhenHaveNumericListWithMarkedInsideMarkedList() {
        String mdText = "* первый элемент${System.lineSeparator()}## вложенный элемент${System.lineSeparator()}** вложенный элемент${System.lineSeparator()}* второй элемент";
        String expected = "<ul><li>первый элемент<ol><li>вложенный элемент</li></ol><ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ul>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedList_WhenHaveNumericListWithMarkedInsideMarkedList() {
        String rtfText = "<ul><li>первый элемент<ol><li>вложенный элемент</li></ol><ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ul>";
        String expected = "* первый элемент${System.lineSeparator()}## вложенный элемент${System.lineSeparator()}** вложенный элемент${System.lineSeparator()}* второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnConvertedList_WhenHaveMarkedListInsideNumericList() {
        String mdText = "# первый элемент${System.lineSeparator()}** вложенный элемент${System.lineSeparator()}# второй элемент";
        String expected = "<ol><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ol>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedList_WhenHaveMarkedListInsideNumericList() {
        String rtfText = "<ol><li>первый элемент<ul><li>вложенный элемент</li></ul></li><li>второй элемент</li></ol>";
        String expected = "# первый элемент${System.lineSeparator()}** вложенный элемент${System.lineSeparator()}# второй элемент";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
