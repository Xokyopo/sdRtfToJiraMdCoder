package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RowCoderTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private RowCoder coder;

    @Before
    void setUp() {
        this.coder = new RowCoder()
    }

    @Test
    void toRtf() {
        String mdText = "строка1<br>строка1${LINE_SEPARATOR}<table><div>экранирование переноса</div></table>${LINE_SEPARATOR}${LINE_SEPARATOR}<h1>Заголовок<h1>";
        String expected = "<div>строка1<br>строка1</div><table><div>экранирование переноса</div></table><h1>Заголовок<h1>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "<div>строка1<br>строка1</div><table><div>экранирование переноса</div></table><h1>Заголовок<h1>";
        String expected = "строка1<br>строка1${LINE_SEPARATOR}<table><div>экранирование переноса</div></table>${LINE_SEPARATOR}${LINE_SEPARATOR}<h1>Заголовок<h1>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnSeparatedLineByOneSeparator_WhenHaveTwoHeaders() {
        for (i in 1..<8) {
            String rtfText = "<h${i}>Заголовок</h${i}><h${i}>Заголовок</h${i}>";
            String expected = "<h${i}>Заголовок</h${i}>${LINE_SEPARATOR}<h${i}>Заголовок</h${i}>";
            String actual = this.coder.toMd(rtfText);

            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    void toMd_ShouldReturnSeparatedLineByTwoSeparator_WhenHaveTwoTable() {
        String rtfText = "<table><div>экранирование переноса</div></table><table><div>экранирование переноса</div></table>";
        String expected = "<table><div>экранирование переноса</div></table>${LINE_SEPARATOR}${LINE_SEPARATOR}<table><div>экранирование переноса</div></table>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnSeparatedLineByTwoSeparator_WhenHaveTwoMarkedList() {
        String rtfText = "<ul><li>элемент 1</li></ul><ul><li>элемент 1</li></ul>";
        String expected = "<ul><li>элемент 1</li></ul>${LINE_SEPARATOR}${LINE_SEPARATOR}<ul><li>элемент 1</li></ul>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnSeparatedLineByTwoSeparator_WhenHaveTwoNumberedList() {
        String rtfText = "<ol><li>элемент 1</li></ol><ol><li>элемент 1</li></ol>";
        String expected = "<ol><li>элемент 1</li></ol>${LINE_SEPARATOR}${LINE_SEPARATOR}<ol><li>элемент 1</li></ol>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnSeparatedLineByOneSeparator_WhenHaveTwoQuote() {
        String rtfText = "<blockquote>Заголовок</blockquote><blockquote>Заголовок</blockquote>";
        String expected = "<blockquote>Заголовок</blockquote>${LINE_SEPARATOR}<blockquote>Заголовок</blockquote>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnSeparatedLineByOneSeparator_WhenHaveTwoCode() {
        String rtfText = "<pre>Заголовок</pre><pre>Заголовок</pre>";
        String expected = "<pre>Заголовок</pre>${LINE_SEPARATOR}<pre>Заголовок</pre>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNotSeparatedLineInsideBlockquoteTag() {
        String rtfText = "<blockquote><div>строка1</div><div>строка2</div></blockquote>";
        String expected = "<blockquote><div>строка1</div><div>строка2</div></blockquote>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnNotSeparatedLineInsidePreTag() {
        String rtfText = "<pre><div>строка1</div><div>строка2</div></pre>";
        String expected = "<pre><div>строка1</div><div>строка2</div></pre>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldAddDivAroundText_WhenHaveTableBetween() {
        String mdText = "строка1${LINE_SEPARATOR}<table><div>экранирование переноса</div></table>${LINE_SEPARATOR}${LINE_SEPARATOR}строка2";
        String expected = "<div>строка1</div><table><div>экранирование переноса</div></table><div>строка2</div>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnInputString_WhenConvertBlockquoteTag() {
        String mdText = "<blockquote>строка1${LINE_SEPARATOR}строка2${LINE_SEPARATOR}</blockquote>";
        String expected = "<blockquote>строка1${LINE_SEPARATOR}строка2${LINE_SEPARATOR}</blockquote>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnInputString_WhenConvertPreTag() {
        String mdText = "<pre>строка1${LINE_SEPARATOR}строка2${LINE_SEPARATOR}</pre>";
        String expected = "<pre>строка1${LINE_SEPARATOR}строка2${LINE_SEPARATOR}</pre>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnInputString_WhenConvertImgTag() {
        String rtfText = '<img src="./download?uuid=file$1960562362">';
        String expected = rtfText;
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnRemoveSeparated_WhenHaveSeparatedElements() {
        String mdText = "<ol><li>элемент 1</li></ol>${LINE_SEPARATOR}${LINE_SEPARATOR}<ol><li>элемент 1</li></ol>";
        String expected = "<ol><li>элемент 1</li></ol><ol><li>элемент 1</li></ol>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNotAddedDiv_WhenHaveHeaders() {
        for (i in 1..<8) {
            String mdText = "<h${i}>Заголовок</h${i}>";
            String expected = "<h${i}>Заголовок</h${i}>";
            String actual = this.coder.toRtf(mdText);

            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    void toRtf_ShouldReturnAddedDiv_WhenHaveTextLine() {
        String mdText = "Просто текст";
        String expected = "<div>Просто текст</div>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnNotAddedDiv_WhenHaveTable() {
        for (i in 1..<8) {
            String mdText = "<table><div>экранирование переноса</div></table>";
            String expected = "<table><div>экранирование переноса</div></table>";
            String actual = this.coder.toRtf(mdText);

            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    void toRtf_ShouldReturnNotAddedDiv_WhenHaveMarkedList() {
        for (i in 1..<8) {
            String mdText = "<ul><li>элемент 1</li></ul>";
            String expected = "<ul><li>элемент 1</li></ul>";
            String actual = this.coder.toRtf(mdText);

            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    void toRtf_ShouldReturnNotAddedDiv_WhenHaveNumberedList() {
        for (i in 1..<8) {
            String mdText = "<ol><li>элемент 1</li></ol>";
            String expected = "<ol><li>элемент 1</li></ol>";
            String actual = this.coder.toRtf(mdText);

            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    void toRtf_ShouldAddDivBeforeEscapedTagsPair_WhenEscapedTags() {
        String mdText = "<blockquote><div>Цитата</div></blockquote>${LINE_SEPARATOR}💏-какой то смайл";
        String expected = "<blockquote><div>Цитата</div></blockquote><div>💏-какой то смайл</div>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }
}
