package ru.itsm365.vita.coder.inline

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SpanCoderTest {

    private SpanCoder coder;

    @Before
    void setUp() throws Exception {
        this.coder = new SpanCoder();
    }

    @Test
    void toRtf() {
        String mdText = "test";
        String expected = "test";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "<span>test</span>";
        String expected = "test";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnOnlyText_WhenTagHaveAttributes() {
        String rtfText = '<span color="">test</span>';
        String expected = "test";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnOriginalText_WhenHaveTagNameWithPrefixSpan() {
        String rtfText = '<spand color="">test</spand>';
        String expected = "<spand color=\"\">test</spand>";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }
}
