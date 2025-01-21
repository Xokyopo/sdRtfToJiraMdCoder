package ru.itsm365.vita.coder.inline

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class BoldTextStyleCoderTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Coder coder;

    @Before
    void setUp() throws Exception {
        this.coder = new BoldTextStyleCoder();
    }

    @Test
    void reversTest() {
        String expected = "simple *test* string";
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = "simple *test* string";
        String expected = "simple <strong>test</strong> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "simple <strong>test</strong> string";
        String expected = "simple *test* string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveUpperCaseTag() {
        String rtfText = "simple <strong>test</strong> string";
        String expected = "simple *test* string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveInsideTag() {
        String rtfText = "simple <strong>test<div></div></strong> string";
        String expected = "simple *test<div></div>* string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveOutsideTag() {
        String rtfText = "simple <div><strong>test</strong></div> string";
        String expected = "simple <div>*test*</div> string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotModifiedString_WhenHaveInsideLineBreaker() {
        String mdText = "simple *te${LINE_SEPARATOR}st* string";
        String expected = "simple *te${LINE_SEPARATOR}st* string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveOnsideLineBreaker() {
        String mdText = "simple *test* ${LINE_SEPARATOR}string";
        String expected = "simple <strong>test</strong> ${LINE_SEPARATOR}string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsidestrongptyLine() {
        String mdText = "simple *te${LINE_SEPARATOR}${LINE_SEPARATOR}st* string";
        String expected = "simple *te${LINE_SEPARATOR}${LINE_SEPARATOR}st* string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveOutsidestrongptyLine() {
        String mdText = "simple *test*${LINE_SEPARATOR}${LINE_SEPARATOR}string";
        String expected = "simple <strong>test</strong>${LINE_SEPARATOR}${LINE_SEPARATOR}string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideLink() {
        String mdText = "simple *te [ссылка в никуда](/root/file) st* string";
        String expected = "simple <strong>te [ссылка в никуда](/root/file) st</strong> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideInvalidItalicToken() {
        String mdText = "simple *te *f st* string";
        String expected = "simple <strong>te *f st</strong> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideInvalidBoldToken() {
        String mdText = "simple *te *ff st* string";
        String expected = "simple <strong>te *ff st</strong> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideItalicToken() {
        String mdText = "simple *te _italic_ st* string";
        String expected = "simple <strong>te _italic_ st</strong> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveItalicBoldToken() {
        String mdText = "simple *_test_* string";
        String expected = "simple <strong>_test_</strong> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveInvalidStartToken() {
        String mdText = "simple * test* string";
        String expected = "simple * test* string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveInvalidEndToken() {
        String mdText = "simple *test * string";
        String expected = "simple *test * string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveNotClosingTag() {
        String rtfText = "simple <strong>test string";
        String expected = "simple <strong>test string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveNotOpeningTag() {
        String rtfText = "simple test</strong> string";
        String expected = "simple test</strong> string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenOnlyTokenizeStringPresent() {
        String mdText = "*test*";
        String expected = "<strong>test</strong>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenOnlyTokenizeStringPresent() {
        String rtfText = "<strong>test</strong>";
        String expected = "*test*";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedText_WhenHaveTagAttributes() {
        String rtfText = "<strong fr-original-style=\"\" style=\"font-weight: 700;\">test</strong>";
        String expected = "*test*";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedText_WhenHaveUppercaseTag() {
        String rtfText = "<STRONG>test</STRONG>";
        String expected = "*test*";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }
}
