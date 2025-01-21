package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class BlockBreakCoderTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Coder coder;

    @Before
    void setUp() {
        coder = new BlockBreakCoder();
    }

    @Test
    void toRtf() {
        String textRtf = "<ul><li>элемент1</li></ul>${LINE_SEPARATOR}${LINE_SEPARATOR}<ul><li>элемент2</li></ul>"
        String expected = "<ul><li>элемент1</li></ul><ul><li>элемент2</li></ul>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String textRtf = "<ul><li>элемент1</li></ul><ul><li>элемент2</li></ul>"
        String expected = "<ul><li>элемент1</li></ul>${LINE_SEPARATOR}${LINE_SEPARATOR}<ul><li>элемент2</li></ul>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void reverseTest() {
        String expected = "<ul><li>элемент1</li></ul>${LINE_SEPARATOR}${LINE_SEPARATOR}<ul><li>элемент2</li></ul>";
        String actual = coder.toMd(coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenList_WhenHaveManyTag() {
        String textRtf = "<ul><li>элемент1</li></ul>${LINE_SEPARATOR}${LINE_SEPARATOR}<ul><li>элемент2</li></ul>"
        String expected = "<ul><li>элемент1</li></ul><ul><li>элемент2</li></ul>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenList_WhenHaveManyTag() {
        String textRtf = "<ul><li>элемент1</li></ul><ul><li>элемент2</li></ul>"
        String expected = "<ul><li>элемент1</li></ul>${LINE_SEPARATOR}${LINE_SEPARATOR}<ul><li>элемент2</li></ul>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenTable_WhenHaveManyTag() {
        String textRtf = "<table>Фэйковая таблица</table>${LINE_SEPARATOR}${LINE_SEPARATOR}<table>Фэйковая таблица2</table>"
        String expected = "<table>Фэйковая таблица</table><table>Фэйковая таблица2</table>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenTable_WhenHaveManyTag() {
        String textRtf = "<table>Фэйковая таблица</table><table>Фэйковая таблица2</table>"
        String expected = "<table>Фэйковая таблица</table>${LINE_SEPARATOR}${LINE_SEPARATOR}<table>Фэйковая таблица2</table>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenOuterTable_WhenHaveTableInsideTable() {
        String textRtf = "<table><table>Фэйковая таблица в таблице</table>Фэйковая таблица</table>${LINE_SEPARATOR}${LINE_SEPARATOR}<table>Фэйковая таблица2</table>"
        String expected = "<table><table>Фэйковая таблица в таблице</table>Фэйковая таблица</table><table>Фэйковая таблица2</table>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenOuterTable_WhenHaveTableInsideTable() {
        String textRtf = "<table><table>Фэйковая таблица в таблице</table>Фэйковая таблица</table><table>Фэйковая таблица2</table>"
        String expected = "<table><table>Фэйковая таблица в таблице</table>Фэйковая таблица</table>${LINE_SEPARATOR}${LINE_SEPARATOR}<table>Фэйковая таблица2</table>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenOuterList_WhenHaveListInsideList() {
        String textRtf = "<ul><li><ul><li>элемент1</li></ul>элемент1</li></ul>${LINE_SEPARATOR}${LINE_SEPARATOR}<ul><li>элемент2</li></ul>"
        String expected = "<ul><li><ul><li>элемент1</li></ul>элемент1</li></ul><ul><li>элемент2</li></ul>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenOuterList_WhenHaveListInsideList() {
        String textRtf = "<ul><li><ul><li>элемент1</li></ul>элемент1</li></ul><ul><li>элемент2</li></ul>"
        String expected = "<ul><li><ul><li>элемент1</li></ul>элемент1</li></ul>${LINE_SEPARATOR}${LINE_SEPARATOR}<ul><li>элемент2</li></ul>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldRemoveEmptyLineBetweenNumberedList_WhenHaveManyTag() {
        String textRtf = "<ol><li>элемент1</ol></ul>${LINE_SEPARATOR}${LINE_SEPARATOR}<ol><li>элемент2</li></ol>"
        String expected = "<ol><li>элемент1</ol></ul><ol><li>элемент2</li></ol>";
        String actual = coder.toRtf(textRtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldAddEmptyLineBetweenNumberedList_WhenHaveManyTag() {
        String textRtf = "<ol><li>элемент1</li></ol><ol><li>элемент2</li></ol>"
        String expected = "<ol><li>элемент1</li></ol>${LINE_SEPARATOR}${LINE_SEPARATOR}<ol><li>элемент2</li></ol>";
        String actual = coder.toMd(textRtf);
        Assert.assertEquals(expected, actual);
    }
}
