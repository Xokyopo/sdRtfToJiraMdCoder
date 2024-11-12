package ru.itsm365.vita.coder.line

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class SubscriptTextStyleCoderTest {
    private Coder coder;

    @Before
    void setUp() throws Exception {
        this.coder = new SubscriptTextStyleCoder();
    }

    @Test
    void reversTest() {
        String expected = "simple ~test~ string";
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = "simple ~test~ string";
        String expected = "simple <sub>test</sub> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = "simple <sub>test</sub> string";
        String expected = "simple ~test~ string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveUpperCaseTag() {
        String rtfText = "simple <sub>test</sub> string";
        String expected = "simple ~test~ string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveInsideTag() {
        String rtfText = "simple <sub>test<div></div></sub> string";
        String expected = "simple ~test<div></div>~ string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTrue_WhenHaveOutsideTag() {
        String rtfText = "simple <div><sub>test</sub></div> string";
        String expected = "simple <div>~test~</div> string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldNotModifiedString_WhenHaveInsideLineBreaker() {
        String mdText = "simple ~te\nst~ string";
        String expected = "simple ~te\nst~ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveOnsideLineBreaker() {
        String mdText = "simple ~test~ \nstring";
        String expected = "simple <sub>test</sub> \nstring";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideEmptyLine() {
        String mdText = "simple ~te\n\nst~ string";
        String expected = "simple ~te\n\nst~ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveOutsideEmptyLine() {
        String mdText = "simple ~test~\n\nstring";
        String expected = "simple <sub>test</sub>\n\nstring";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideLink() {
        String mdText = "simple ~te [ссылка в никуда](/root/file) st~ string";
        String expected = "simple <sub>te [ссылка в никуда](/root/file) st</sub> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideInvalidItalicToken() {
        String mdText = "simple ~te ~ st~ string";
        String expected = "simple <sub>te ~ st</sub> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideInvalidBoldToken() {
        String mdText = "simple ~te ~~ff st~ string";
        String expected = "simple <sub>te ~~ff st</sub> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenHaveInsideBoldToken() {
        String mdText = "simple ~te *bold* st~ string";
        String expected = "simple <sub>te *bold* st</sub> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveItalicBoldToken() {
        String mdText = "simple ~*test*~ string";
        String expected = "simple <sub>*test*</sub> string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveBoldToken() {
        String mdText = "simple *test* string";
        String expected = "simple *test* string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveInvalidStartToken() {
        String mdText = "simple ~ test~ string";
        String expected = "simple ~ test~ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnUnmodifiedText_WhenHaveInvalidEndToken() {
        String mdText = "simple ~test ~ string";
        String expected = "simple ~test ~ string";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveNotClosingTag() {
        String rtfText = "simple <sub>test string";
        String expected = "simple <sub>test string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenHaveNotOpeningTag() {
        String rtfText = "simple test</sub> string";
        String expected = "simple test</sub> string";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTrue_WhenOnlyTokenizeStringPresent() {
        String mdText = "~test~";
        String expected = "<sub>test</sub>";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnUnmodifiedText_WhenOnlyTokenizeStringPresent() {
        String rtfText = "<sub>test</sub>";
        String expected = "~test~";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedText_WhenHaveTagAttributes() {
        String rtfText = "<sub fr-original-style=\"\" style=\"font-weight: 700;\">test</sub>";
        String expected = "~test~";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnConvertedText_WhenHaveUppercaseTag() {
        String rtfText = "<sub>test</sub>";
        String expected = "~test~";
        String actual = this.coder.toMd(rtfText);

        Assert.assertEquals(expected, actual);
    }
}
