package ru.itsm365.vita.coder.inline

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class StrikethroughTextStyleCoderTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Coder coder;

    @Before
    void setUp() throws Exception {
        this.coder = new AllTextStyleCoder();
    }

    @Test
    void reversTest() {
        String expected = "simple -test- string";
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = "simple -test- string";
        String expected = "simple <s>test</s> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "simple <s>test</s> string";
        String expected = "simple -test- string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveUpperCaseTag() {
        String rtfText = "simple <s>test</s> string";
        String expected = "simple -test- string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveInsideTag() {
        String rtfText = "simple <s>test<div></div></s> string";
        String expected = "simple -test<div></div>- string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnEscapedMdTags_WhenHaveOutsideTag() {
        String rtfText = "simple <div><s>test</s></div> string";
        String expected = "simple <div>{-}test{-}</div> string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotModifiedString_WhenHaveInsideLineBreaker() {
        String mdText = "simple -te${LINE_SEPARATOR}st- string";
        String expected = "simple -te${LINE_SEPARATOR}st- string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveOnsideLineBreaker() {
        String mdText = "simple -test- ${LINE_SEPARATOR}string";
        String expected = "simple <s>test</s> ${LINE_SEPARATOR}string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideEmptyLine() {
        String mdText = "simple -te${LINE_SEPARATOR}${LINE_SEPARATOR}st- string";
        String expected = "simple -te${LINE_SEPARATOR}${LINE_SEPARATOR}st- string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveOutsideEmptyLine() {
        String mdText = "simple -test-${LINE_SEPARATOR}${LINE_SEPARATOR}string";
        String expected = "simple <s>test</s>${LINE_SEPARATOR}${LINE_SEPARATOR}string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideLink() {
        String mdText = "simple -te [ссылка в никуда](/root/file) st- string";
        String expected = "simple <s>te [ссылка в никуда](/root/file) st</s> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideInvalidItalicToken() {
        String mdText = "simple -te - st- string";
        String expected = "simple <s>te - st</s> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideInvalidBoldToken() {
        String mdText = "simple -te --ff st- string";
        String expected = "simple <s>te --ff st</s> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideBoldToken() {
        String mdText = "simple -te *bold* st- string";
        String expected = "simple <s>te <strong>bold</strong> st</s> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnModifiedInsideText_WhenHaveItalicBoldToken() {
        String mdText = "simple -*test*- string";
        String expected = "simple <s><strong>test</strong></s> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveInvalidStartToken() {
        String mdText = "simple - test- string";
        String expected = "simple - test- string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveInvalidEndToken() {
        String mdText = "simple -test - string";
        String expected = "simple -test - string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveNotClosingTag() {
        String rtfText = "simple <s>test string";
        String expected = "simple <s>test string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveNotOpeningTag() {
        String rtfText = "simple test</s> string";
        String expected = "simple test</s> string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenOnlyTokenizeStringPresent() {
        String mdText = "-test-";
        String expected = "<s>test</s>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenOnlyTokenizeStringPresent() {
        String rtfText = "<s>test</s>";
        String expected = "-test-";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedText_WhenHaveTagAttributes() {
        String rtfText = "<s fr-original-style=\"\" style=\"font-weight: 700;\">test</s>";
        String expected = "-test-";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedText_WhenHaveUppercaseTag() {
        String rtfText = "<s>test</s>";
        String expected = "-test-";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldIgnoreLinkText_WhenHaveImageMdTag() {
        String mdText = "!image-2025-{-}01{-}-17-11-23-48-789.png!";
        String expected = "!image-2025-<s>01</s>-17-11-23-48-789.png!";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldIgnoreLinkText_WhenHaveShortLinkMdTag() {
        String mdText = "[https://y-a-f.ru/]";
        String expected = "[https://y-a-f.ru/]";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldIgnoreLinkText_WhenHaveFullLinkMdTag() {
        String mdText = "[ya.ru|https://y-a-f.ru/]";
        String expected = "[ya.ru|https://y-a-f.ru/]";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldIgnoreNameText_WhenHaveFullLinkMdTag() {
        String mdText = "[y-a-f.ru|https://y-a-f.ru/]";
        String expected = "[y-a-f.ru|https://y-a-f.ru/]";
        String actual = this.coder.toRtf(mdText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnescapedMdTags_WhenStyledTextInTable() {
        String mdText = "|-test-|";
        String expected = "|<s>test</s>|";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnEscapedMdTags_WhenStyledTextInTable() {
        String rtfText = '<td style="d"><s>test</s></td>';

        String expected = '<td style="d">{-}test{-}</td>';
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }
}
