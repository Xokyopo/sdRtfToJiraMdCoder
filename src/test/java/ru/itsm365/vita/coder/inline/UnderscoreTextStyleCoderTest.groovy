package ru.itsm365.vita.coder.inline

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class UnderscoreTextStyleCoderTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Coder coder;

    @Before
    void setUp() throws Exception {
        this.coder = new AllTextStyleCoder();
    }

    @Test
    void reversTest() {
        String expected = "simple +test+ string";
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = "simple +test+ string";
        String expected = "simple <u>test</u> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "simple <u>test</u> string";
        String expected = "simple +test+ string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveUpperCaseTag() {
        String rtfText = "simple <u>test</u> string";
        String expected = "simple +test+ string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveInsideTag() {
        String rtfText = "simple <u>test<div></div></u> string";
        String expected = "simple +test<div></div>+ string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnEscapedMdTag_WhenHaveOutsideTag() {
        String rtfText = "simple <div><u>test</u></div> string";
        String expected = "simple <div>{+}test{+}</div> string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotModifiedString_WhenHaveInsideLineBreaker() {
        String mdText = "simple +te${LINE_SEPARATOR}st+ string";
        String expected = "simple +te${LINE_SEPARATOR}st+ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveOnsideLineBreaker() {
        String mdText = "simple +test+ ${LINE_SEPARATOR}string";
        String expected = "simple <u>test</u> ${LINE_SEPARATOR}string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideEmptyLine() {
        String mdText = "simple +te${LINE_SEPARATOR}${LINE_SEPARATOR}st+ string";
        String expected = "simple +te${LINE_SEPARATOR}${LINE_SEPARATOR}st+ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveOutsideEmptyLine() {
        String mdText = "simple +test+${LINE_SEPARATOR}${LINE_SEPARATOR}string";
        String expected = "simple <u>test</u>${LINE_SEPARATOR}${LINE_SEPARATOR}string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideLink() {
        String mdText = "simple +te [ссылка в никуда](/root/file) st+ string";
        String expected = "simple <u>te [ссылка в никуда](/root/file) st</u> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideInvalidItalicToken() {
        String mdText = "simple +te + st+ string";
        String expected = "simple <u>te + st</u> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideInvalidBoldToken() {
        String mdText = "simple +te ++ff st+ string";
        String expected = "simple <u>te ++ff st</u> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideBoldToken() {
        String mdText = "simple +te *bold* st+ string";
        String expected = "simple <u>te <strong>bold</strong> st</u> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnModifiedInsideText_WhenHaveItalicBoldToken() {
        String mdText = "simple +*test*+ string";
        String expected = "simple <u><strong>test</strong></u> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveInvalidStartToken() {
        String mdText = "simple + test+ string";
        String expected = "simple + test+ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveInvalidEndToken() {
        String mdText = "simple +test + string";
        String expected = "simple +test + string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveNotClosingTag() {
        String rtfText = "simple <u>test string";
        String expected = "simple <u>test string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveNotOpeningTag() {
        String rtfText = "simple test</u> string";
        String expected = "simple test</u> string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenOnlyTokenizeStringPresent() {
        String mdText = "+test+";
        String expected = "<u>test</u>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenOnlyTokenizeStringPresent() {
        String rtfText = "<u>test</u>";
        String expected = "+test+";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedText_WhenHaveTagAttributes() {
        String rtfText = "<u fr-original-style=\"\" style=\"font-weight: 700;\">test</u>";
        String expected = "+test+";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedText_WhenHaveUppercaseTag() {
        String rtfText = "<u>test</u>";
        String expected = "+test+";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnescapedMdTags_WhenStyledTextInTable() {
        String mdText = "|+test+|";
        String expected = "|<u>test</u>|";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnEscapedMdTags_WhenStyledTextInTable() {
        String rtfText = '<td style="d"><u>test</u></td>';

        String expected = '<td style="d">{+}test{+}</td>';
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }
}
