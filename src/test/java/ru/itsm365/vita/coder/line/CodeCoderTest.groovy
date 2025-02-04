package ru.itsm365.vita.coder.line

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

public class CodeCoderTest {
    private Coder coder;
    private final String CODE_START = "{code:java}"
    private final String CODE_END = "{code}"
    private final String CODE_OPENING_TAG = "<pre fr-original-style=\"\" style=\"white-space: pre-wrap; overflow-wrap: break-word; overflow: visible;\">";
    private final String CODE_CLOSING_TAG = "</pre>";

    private final String LINE_SEPARATOR = "${System.lineSeparator()}";

    @Before
    public void setUp() throws Exception {
        coder = new CodeCoder();
    }


    @Test
    void revers() {
        String mdText = "0123456789${CODE_START}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210";
        String expected = "0123456789${CODE_START}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210";
        String actual = this.coder.toMd(this.coder.toRtf(mdText));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = "0123456789${CODE_START}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210";
        String expected = "0123456789" + "${CODE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_CLOSING_TAG}9876543210";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_OneWorkedCodeAndBrokenCode() {
        String mdText = "0123456789${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210${LINE_SEPARATOR}" +
                "0123456789${LINE_SEPARATOR}${CODE_START}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210${LINE_SEPARATOR}" +
                "0123456789${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210${LINE_SEPARATOR}" +
                "0123456789${LINE_SEPARATOR}${CODE_START}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210";
        String expected = "0123456789${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210${LINE_SEPARATOR}" +
                "0123456789${LINE_SEPARATOR}${CODE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_CLOSING_TAG}9876543210${LINE_SEPARATOR}" +
                "0123456789${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210${LINE_SEPARATOR}" +
                "0123456789${LINE_SEPARATOR}${CODE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_CLOSING_TAG}9876543210";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "0123456789${CODE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_CLOSING_TAG}9876543210";
        String expected = "0123456789${CODE_START}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_OneWorkedCodeAndBrokenCode() {
        String rtfText = "0123456789${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_CLOSING_TAG}9876543210" +
                "0123456789${CODE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_CLOSING_TAG}9876543210" +
                "0123456789${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_CLOSING_TAG}9876543210" +
                "0123456789${CODE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_CLOSING_TAG}9876543210";
        String expected = "0123456789${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_CLOSING_TAG}9876543210" +
                "0123456789${CODE_START}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210" +
                "0123456789${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_CLOSING_TAG}9876543210" +
                "0123456789${CODE_START}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${CODE_END}9876543210";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}