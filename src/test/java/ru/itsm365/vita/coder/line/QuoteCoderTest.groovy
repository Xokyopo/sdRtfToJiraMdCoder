package ru.itsm365.vita.coder.line

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class QuoteCoderTest {
    private static final String LINE_SEPARATOR = "${System.lineSeparator()}";
    private static final String BLOCKQUOTE_STYLE = new QuoteCoder().BLOCKQUOTE_STYLE;
    private static final String QUOTE_OPENING_TAG = "<blockquote style=\"border-color: rgb(153, 153, 153); border-left-width: 2px; border-left-style: solid; margin-left: 0px; padding-left: 5px; color: rgb(153, 153, 153);\">";
    private static final String QUOTE_CLOSING_TAG = "</blockquote>";

    private QuoteCoder coder;

    @Before
    void setUp() throws Exception {
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

    @Test
    void toMd_ShouldReturnConvertQuotes_WhenDividedText() {
        String rtfText = "<blockquote>цитата1</blockquote>текст между цитатами<blockquote>цитата2</blockquote>";
        String expected = "{quote}цитата1{quote}текст между цитатами{quote}цитата2{quote}";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnConvertQuotes_WhenDividedText() {
        String defaultRtfQuoteStyle = [
                'style="border-color: rgb(153, 153, 153); border-left-width: 2px; ',
                'border-left-style: solid; margin-left: 0px; padding-left: 5px; color: rgb(153, 153, 153);"'
        ].join("")
        String mdText = "{quote}цитата1{quote}${LINE_SEPARATOR}текст между цитатами${LINE_SEPARATOR}{quote}цитата2{quote}";
        String expected = [
                "<blockquote $defaultRtfQuoteStyle>цитата1</blockquote>${LINE_SEPARATOR}текст между ",
                "цитатами${LINE_SEPARATOR}<blockquote $defaultRtfQuoteStyle>цитата2</blockquote>"
        ].join("")
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_shouldHandleSimpleQuote() {
        String mdText = "{quote}Простая цитата{quote}"
        String expected = "<blockquote style=\"${BLOCKQUOTE_STYLE}\">Простая цитата</blockquote>"
        String actual = this.coder.toRtf(mdText)
        Assert.assertEquals(expected, actual)
    }

    @Test
    void toRtf_shouldHandleQuoteWithColorInside() {
        // Сочетание цитаты и цвета через inlineTexCoder
        String mdText = "{quote}Цветной {color:#ff0000}текст{color} в цитате{quote}"
        String actual = this.coder.toRtf(mdText)

        Assert.assertTrue(actual.startsWith("<blockquote"))
        Assert.assertTrue(actual.contains("{color:#ff0000}текст{color}"))
        Assert.assertTrue(actual.endsWith("</blockquote>"))
    }

    @Test
    void toRtf_shouldHandleMultipleQuotesInOneText() {
        // Две независимые цитаты в одном тексте
        String mdText = "{quote}Раз{quote} и {quote}Два{quote}"
        String actual = this.coder.toRtf(mdText)

        Assert.assertEquals(2, actual.count("<blockquote"))
        Assert.assertTrue(actual.contains("Раз</blockquote> и <blockquote"))
    }

    @Test
    void toRtf_shouldHandleEmptyQuote() {
        // Пустая цитата не должна ломать парсер
        String mdText = "{quote}{quote}"
        String expected = "<blockquote style=\"${BLOCKQUOTE_STYLE}\"></blockquote>"
        String actual = this.coder.toRtf(mdText)
        Assert.assertEquals(expected, actual)
    }

    @Test
    void toMd_shouldConvertSimpleBlockquote() {
        String rtfText = "<blockquote>Текст цитаты</blockquote>"
        String expected = "{quote}Текст цитаты{quote}"
        String actual = this.coder.toMd(rtfText)
        Assert.assertEquals(expected, actual)
    }

    @Test
    void toMd_shouldIgnoreAttributesInBlockquote() {
        // Проверка, что любые стили/классы в теге blockquote игнорируются
        String rtfText = "<blockquote class=\"jira-quote\" style=\"color: red;\" data-id=\"123\">Контент</blockquote>"
        String expected = "{quote}Контент{quote}"
        String actual = this.coder.toMd(rtfText)
        Assert.assertEquals(expected, actual)
    }

    @Test
    void toMd_shouldHandleBlockquoteWithInternalColor() {
        // Цитата со вложенным цветом при возврате в MD
        String rtfText = "<blockquote><span style=\"color: rgb(0, 0, 255);\">Синий</span></blockquote>"
        String expected = "{quote}<span style=\"color: rgb(0, 0, 255);\">Синий</span>{quote}"
        String actual = this.coder.toMd(rtfText)
        Assert.assertEquals(expected, actual)
    }

    @Test
    void toMd_shouldPreserveTextOutsideQuotes() {
        // Проверка, что текст до и после цитаты остается нетронутым
        String rtfText = "Автор сказал: <blockquote>Я приду</blockquote> в полдень."
        String expected = "Автор сказал: {quote}Я приду{quote} в полдень."
        String actual = this.coder.toMd(rtfText)
        Assert.assertEquals(expected, actual)
    }
}
