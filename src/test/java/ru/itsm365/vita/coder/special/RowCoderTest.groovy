package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RowCoderTest extends GroovyTestCase {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private RowCoder coder;

    @Before
    void setUp() {
        this.coder = new RowCoder()
    }

    @Test
    void testToRtf() {
        String mdText = "строка1<br>строка1${LINE_SEPARATOR}<table><div>экранирование переноса</div></table>${LINE_SEPARATOR}${LINE_SEPARATOR}<h1>Заголовок<h1>";
        String expected = "<div>строка1<br>строка1</div><table><div>экранирование переноса</div></table><h1>Заголовок<h1>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void testToMd() {
        String rtfText = "<div>строка1<br>строка1</div><table><div>экранирование переноса</div></table><h1>Заголовок<h1>";
        String expected = "строка1<br>строка1${LINE_SEPARATOR}<table><div>экранирование переноса</div></table>${LINE_SEPARATOR}${LINE_SEPARATOR}<h1>Заголовок<h1>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void testToMd_ShouldReturnSeparatedLineByOneSeparator_WhenHavTwoHeaders() {
        for (i in 1..<8) {
            String rtfText = "<h${i}>Заголовок</h${i}><h${i}>Заголовок</h${i}>";
            String expected = "<h${i}>Заголовок</h${i}>${LINE_SEPARATOR}<h${i}>Заголовок</h${i}>";
            String actual = this.coder.toMd(rtfText);

            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    void testToMd_ShouldReturnSeparatedLineByTwoSeparator_WhenHavTwoTable() {
        String rtfText = "<table><div>экранирование переноса</div></table><table><div>экранирование переноса</div></table>";
        String expected = "<table><div>экранирование переноса</div></table>${LINE_SEPARATOR}${LINE_SEPARATOR}<table><div>экранирование переноса</div></table>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void testToMd_ShouldReturnSeparatedLineByTwoSeparator_WhenHavTwoMarkedList() {
        String rtfText = "<ul><li>элемент 1</li></ul><ul><li>элемент 1</li></ul>";
        String expected = "<ul><li>элемент 1</li></ul>${LINE_SEPARATOR}${LINE_SEPARATOR}<ul><li>элемент 1</li></ul>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void testToMd_ShouldReturnSeparatedLineByTwoSeparator_WhenHavTwoNumberedList() {
        String rtfText = "<ol><li>элемент 1</li></ol><ol><li>элемент 1</li></ol>";
        String expected = "<ol><li>элемент 1</li></ol>${LINE_SEPARATOR}${LINE_SEPARATOR}<ol><li>элемент 1</li></ol>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void testToMd_ShouldReturnSeparatedLineByOneSeparator_WhenHavTwoQuote() {
        String rtfText = "<blockquote>Заголовок</blockquote><blockquote>Заголовок</blockquote>";
        String expected = "<blockquote>Заголовок</blockquote>${LINE_SEPARATOR}<blockquote>Заголовок</blockquote>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void testToMd_ShouldReturnSeparatedLineByOneSeparator_WhenHavTwoCode() {
        String rtfText = "<pre>Заголовок</pre><pre>Заголовок</pre>";
        String expected = "<pre>Заголовок</pre>${LINE_SEPARATOR}<pre>Заголовок</pre>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void testToMd_ShouldReturnSeparatedLineBySeparatorInsideTag_WhenHavTwoQuote() {
        String rtfText = "<blockquote><div>строка1</div><div>строка2</div></blockquote>";
        String expected = "<blockquote>строка1${LINE_SEPARATOR}строка2${LINE_SEPARATOR}</blockquote>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }
}
