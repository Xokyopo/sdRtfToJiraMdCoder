package ru.itsm365.vita.coder.inline

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class ItalicTextStyleCoderTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Coder coder;

    @Before
    void setUp() throws Exception {
        this.coder = new AllTextStyleCoder();
    }

    @Test
    void reversTest() {
        String expected = "simple _test_ string";
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = "simple _test_ string";
        String expected = "simple <em>test</em> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "simple <em>test</em> string";
        String expected = "simple _test_ string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveUpperCaseTag() {
        String rtfText = "simple <em>test</em> string";
        String expected = "simple _test_ string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveInsideTag() {
        String rtfText = "simple <em>test<div></div></em> string";
        String expected = "simple _test<div></div>_ string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnEscapedMdTag_WhenHaveOutsideAnymore() {
        String rtfText = "simple <div><em>test</em></div> string";
        String expected = "simple <div>{_}test{_}</div> string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotModifiedString_WhenHaveInsideLineBreaker() {
        String mdText = "simple _te${LINE_SEPARATOR}st_ string";
        String expected = "simple _te${LINE_SEPARATOR}st_ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveOnsideLineBreaker() {
        String mdText = "simple _test_ ${LINE_SEPARATOR}string";
        String expected = "simple <em>test</em> ${LINE_SEPARATOR}string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideEmptyLine() {
        String mdText = "simple _te${LINE_SEPARATOR}${LINE_SEPARATOR}st_ string";
        String expected = "simple _te${LINE_SEPARATOR}${LINE_SEPARATOR}st_ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveOutsideEmptyLine() {
        String mdText = "simple _test_${LINE_SEPARATOR}${LINE_SEPARATOR}string";
        String expected = "simple <em>test</em>${LINE_SEPARATOR}${LINE_SEPARATOR}string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideLink() {
        String mdText = "simple _te [ссылка в никуда](/root/file) st_ string";
        String expected = "simple <em>te [ссылка в никуда](/root/file) st</em> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideInvalidItalicToken() {
        String mdText = "simple _te _ st_ string";
        String expected = "simple <em>te _ st</em> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideInvalidBoldToken() {
        String mdText = "simple _te __ff st_ string";
        String expected = "simple <em>te __ff st</em> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideBoldToken() {
        String mdText = "simple _te *bold* st_ string";
        String expected = "simple <em>te <strong>bold</strong> st</em> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveItalicBoldToken() {
        String mdText = "simple _*test*_ string";
        String expected = "simple <em><strong>test</strong></em> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveInvalidStartToken() {
        String mdText = "simple _ test_ string";
        String expected = "simple _ test_ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveInvalidEndToken() {
        String mdText = "simple _test _ string";
        String expected = "simple _test _ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveNotClosingTag() {
        String rtfText = "simple <em>test string";
        String expected = "simple <em>test string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveNotOpeningTag() {
        String rtfText = "simple test</em> string";
        String expected = "simple test</em> string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenOnlyTokenizeStringPresent() {
        String mdText = "_test_";
        String expected = "<em>test</em>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenOnlyTokenizeStringPresent() {
        String rtfText = "<em>test</em>";
        String expected = "_test_";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedText_WhenHaveTagAttributes() {
        String rtfText = "<em fr-original-style=\"\" style=\"font-weight: 700;\">test</em>";
        String expected = "_test_";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedText_WhenHaveUppercaseTag() {
        String rtfText = "<EM>test</EM>";
        String expected = "_test_";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnescapedMdTags_WhenStyledTextInTable() {
        String mdText = "|_test_|";
        String expected = "|<em>test</em>|";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnEscapedMdTags_WhenStyledTextInTable() {
        String rtfText = '<td style="d"><em>test</em></td>';

        String expected = '<td style="d">{_}test{_}</td>';
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }
}
