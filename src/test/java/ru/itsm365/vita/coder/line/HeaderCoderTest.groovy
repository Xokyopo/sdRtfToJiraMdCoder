package ru.itsm365.vita.coder.line

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class HeaderCoderTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Coder coder;

    @Before
    void setUp() {
        this.coder = new HeaderCoder();
    }

    @Test
    void toRtf() {
        String mdText = "h1. Заголовок 1";
        String expected = "<h1>Заголовок 1</h1>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "<h1>Заголовок 1</h1>";
        String expected = "h1. Заголовок 1";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void reverseTest() {
        String expectedRtf = "<h1>Заголовок 1</h1>";
        String actual = this.coder.toRtf(this.coder.toMd(expectedRtf));

        Assert.assertEquals(expectedRtf, actual);
    }

    @Test
    void toMd_ShouldReturnMdHeaderWithMaxRtfLevel_WhenRTFHeaderHaveMaxLevel() {
        String rtfText = "<h4>Заголовок 4</h4>";
        String expected = "h4. Заголовок 4";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnRtfHeaderWithMaxRtfLevel_WhenMDHeaderHaveMaxLevel() {
        String mdText = "h6. Заголовок 6";
        String expected = "<h4>Заголовок 6</h4>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldSaveInnerTag_WhenHaveInnerTag() {
        String mdText = "h1. <em>Заголовок 1</em>";
        String expected = "<h1><em>Заголовок 1</em></h1>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldSaveInnerTag_WhenHaveInnerTag() {
        String rtfText = "<h1><em>Заголовок 1</em></h1>";
        String expected = "h1. <em>Заголовок 1</em>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldIgnoreAttributes_WhenHaveAnyAttribute() {
        String rtfText = '<h1 id="234">Заголовок 1</h1>';
        String expected = "h1. Заголовок 1";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldSaveEmptyLineBefore() {
        String mdText = "${LINE_SEPARATOR}h1. Заголовок 1";
        String expected = "${LINE_SEPARATOR}<h1>Заголовок 1</h1>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldSaveEmptyLineBefore() {
        String rtfText = "${LINE_SEPARATOR}<h1>Заголовок 1</h1>";
        String expected = "${LINE_SEPARATOR}h1. Заголовок 1";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldSaveEmptyLineAfter() {
        String mdText = "h1. Заголовок 1${LINE_SEPARATOR}";
        String expected = "<h1>Заголовок 1</h1>${LINE_SEPARATOR}";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldSaveEmptyLineAfter() {
        String rtfText = "<h1>Заголовок 1</h1>${LINE_SEPARATOR}";
        String expected = "h1. Заголовок 1${LINE_SEPARATOR}";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }


    @Test
    void toRtf_ShouldConvertTwoHeader_WhenHaveTwoHeader() {
        String mdText = "h1. Заголовок 1${LINE_SEPARATOR}h2. Заголовок 2";
        String expected = "<h1>Заголовок 1</h1>${LINE_SEPARATOR}<h2>Заголовок 2</h2>";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldConvertTwoHeader_WhenHaveTwoHeader() {
        String rtfText = "<h1>Заголовок 1</h1>${LINE_SEPARATOR}<h2>Заголовок 2</h2>";
        String expected = "h1. Заголовок 1${LINE_SEPARATOR}h2. Заголовок 2";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }
}
