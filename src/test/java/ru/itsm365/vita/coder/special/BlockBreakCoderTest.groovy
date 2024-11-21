package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class BlockBreakCoderTest {
    private Coder coder;

    @Before
    void setUp() {
        coder = new BlockBreakCoder();
    }

    @Test
    void toRtf() {
        String textRtf = "<h1>Заголовок</h1>\n\n<ul><li>элемент1</li></ul>\n\n<ul><li>элемент2</li></ul>"
        String expected = "<h1>Заголовок</h1><ul><li>элемент1</li></ul><ul><li>элемент2</li></ul>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String textRtf = "<div>Просто текст</div><h1>Заголовок<h1><ul><li>элемент1</li></ul><ul><li>элемент2</li></ul>"
        String expected = "<h1>Заголовок</h1>\n\n<ul><li>элемент1</li></ul>\n\n<ul><li>элемент2</li></ul>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void reverseTest() {
        String expected = "<h1>Заголовок</h1>\n\n<ul><li>элемент1</li></ul>\n\n<ul><li>элемент2</li></ul>";
        String actual = coder.toMd(coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenHeader_WhenHaveManyTag() {
        String textRtf = "<h1>Заголовок</h1>\n\n<h1>Заголовок</h1>"
        String expected = "<h1>Заголовок</h1><h1>Заголовок</h1>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenHeader_WhenHaveManyTag() {
        String textRtf = "<h1>Заголовок</h1><h1>Заголовок</h1>"
        String expected = "<h1>Заголовок</h1>\n\n<h1>Заголовок</h1>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenList_WhenHaveManyTag() {
        String textRtf = "<ul><li>элемент1</li></ul>\n\n<ul><li>элемент2</li></ul>"
        String expected = "<ul><li>элемент1</li></ul><ul><li>элемент2</li></ul>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenList_WhenHaveManyTag() {
        String textRtf = "<ul><li>элемент1</li></ul><ul><li>элемент2</li></ul>"
        String expected = "<ul><li>элемент1</li></ul>\n\n<ul><li>элемент2</li></ul>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenTable_WhenHaveManyTag() {
        String textRtf = "<table>Фэйковая таблица</table>\n\n<table>Фэйковая таблица2</table>"
        String expected = "<table>Фэйковая таблица</table><table>Фэйковая таблица2</table>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenTable_WhenHaveManyTag() {
        String textRtf = "<table>Фэйковая таблица</table><table>Фэйковая таблица2</table>"
        String expected = "<table>Фэйковая таблица</table>\n\n<table>Фэйковая таблица2</table>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenOuterTable_WhenHaveTableInsideTable() {
        String textRtf = "<table><table>Фэйковая таблица в таблице</table>Фэйковая таблица</table>\n\n<table>Фэйковая таблица2</table>"
        String expected = "<table><table>Фэйковая таблица в таблице</table>Фэйковая таблица</table><table>Фэйковая таблица2</table>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenOuterTable_WhenHaveTableInsideTable() {
        String textRtf = "<table><table>Фэйковая таблица в таблице</table>Фэйковая таблица</table><table>Фэйковая таблица2</table>"
        String expected = "<table><table>Фэйковая таблица в таблице</table>Фэйковая таблица</table>\n\n<table>Фэйковая таблица2</table>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenOuterList_WhenHaveListInsideList() {
        String textRtf = "<ul><li><ul><li>элемент1</li></ul>элемент1</li></ul>\n\n<ul><li>элемент2</li></ul>"
        String expected = "<ul><li><ul><li>элемент1</li></ul>элемент1</li></ul><ul><li>элемент2</li></ul>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenOuterList_WhenHaveListInsideList() {
        String textRtf = "<ul><li><ul><li>элемент1</li></ul>элемент1</li></ul><ul><li>элемент2</li></ul>"
        String expected = "<ul><li><ul><li>элемент1</li></ul>элемент1</li></ul>\n\n<ul><li>элемент2</li></ul>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenNumberedList_WhenHaveManyTag() {
        String textRtf = "<ol><li>элемент1</ol></ul>\n\n<ol><li>элемент2</li></ol>"
        String expected = "<ol><li>элемент1</ol></ul><ol><li>элемент2</li></ol>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenNumberedList_WhenHaveManyTag() {
        String textRtf = "<ol><li>элемент1</li></ol><ol><li>элемент2</li></ol>"
        String expected = "<ol><li>элемент1</li></ol>\n\n<ol><li>элемент2</li></ol>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }
}
