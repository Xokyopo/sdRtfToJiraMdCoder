package ru.itsm365.vita.coder.line

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

public class QuoteCoderTest {
    private Coder coder;
    private final String QUOTE_OPENING_TAG = "<blockquote fr-original-style=\"\" style=\"border-color: rgb(153, 153, 153); border-left-width: 2px; border-left-style: solid; margin-left: 0px; padding-left: 5px; color: rgb(153, 153, 153);\">";
    private final String QUOTE_CLOSING_TAG = "</blockquote>";

    private final String LINE_SEPARATOR = "${System.lineSeparator()}";

    @Before
    public void setUp() throws Exception {
        this.coder = new QuoteCoder();
    }

    @Test
    void toRtf() {
        String mdText = "0123456789{quote}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "{quote}9876543210";
        String expected = "0123456789" + "${QUOTE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${QUOTE_CLOSING_TAG}9876543210";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_TwoQuotes() {
        String mdText = "0123456789${LINE_SEPARATOR}" +
                "{quote}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "{quote}${LINE_SEPARATOR}" +
                "9876543210${LINE_SEPARATOR}" +
                "0123456789${LINE_SEPARATOR}" +
                "{quote}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "{quote}${LINE_SEPARATOR}" +
                "9876543210"
        String expected = "0123456789${LINE_SEPARATOR}" +
                "${QUOTE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${QUOTE_CLOSING_TAG}${LINE_SEPARATOR}" +
                "9876543210${LINE_SEPARATOR}" +
                "0123456789${LINE_SEPARATOR}" +
                "${QUOTE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${QUOTE_CLOSING_TAG}${LINE_SEPARATOR}" +
                "9876543210"
        String actual = this.coder.toRtf(mdText);
        println(expected)
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {

        String rtfText = "0123456789" + "${QUOTE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${QUOTE_CLOSING_TAG}9876543210";
        String expected = "0123456789{quote}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "{quote}9876543210";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }


    @Test
    void toMd_TwoQuotes() {
        String rtfText = "0123456789" +
                "${QUOTE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${QUOTE_CLOSING_TAG}9876543210${LINE_SEPARATOR}" +
                "0123456789${LINE_SEPARATOR}" +
                "${QUOTE_OPENING_TAG}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "${QUOTE_CLOSING_TAG}9876543210";
        String expected = "0123456789{quote}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "{quote}9876543210${LINE_SEPARATOR}" +
                "0123456789${LINE_SEPARATOR}" +
                "{quote}${LINE_SEPARATOR}" +
                "    here is quotable${LINE_SEPARATOR}" +
                " content to be quoted${LINE_SEPARATOR}" +
                "{quote}9876543210";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedQuote_WhenHaveSimpleQuote() {
        String rtfText = "<blockquote>простая цитата</blockquote>";
        String expected = "{quote}простая цитата{quote}";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertOutsideQuote_WhenHaveQuoteInsideQuote() {
        String rtfText = "<blockquote>цитата<blockquote>в</blockquote>цитате</blockquote>";
        String expected = "{quote}цитата<blockquote>в</blockquote>цитате{quote}";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
