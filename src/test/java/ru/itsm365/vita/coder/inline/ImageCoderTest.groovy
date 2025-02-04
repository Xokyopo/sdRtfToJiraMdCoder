package ru.itsm365.vita.coder.inline

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.Coder

class ImageCoderTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Coder coder;

    @Before
    void setUp() {
        this.coder = new ImageCoder();
    }

    @Test
    void reversTest() {
        String expected = '!screenshot-1.png|thumbnail!';
        String actual = this.coder.toMd(this.coder.toRtf(expected));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf() {
        String mdText = '!screenshot-1.png|thumbnail!';
        String expected = '<img src="screenshot-1.png">';
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_WithoutThumbnail() {
        String mdText = '!screenshot-1.png!';
        String expected = '<img src="screenshot-1.png">';
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd() {
        String rtfText = '<img src="screenshot-1.png">';
        String expected = '!screenshot-1.png|thumbnail!';
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnImage_WhenSurroundedByText() {
        String mdText = 'до картики !screenshot-1.png|thumbnail! и после картинки';
        String expected = 'до картики<img src="screenshot-1.png">и после картинки';
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnImage_WhenSurroundedByText() {
        String rtfText = 'до картики<img src="screenshot-1.png">и после картинки';
        String expected = 'до картики !screenshot-1.png|thumbnail! и после картинки';
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldSafeNewLine_WhenHaveTheLine() {
        String mdText = "до картики${LINE_SEPARATOR}!screenshot-1.png|thumbnail!${LINE_SEPARATOR}и после картинки";
        String expected = "до картики${LINE_SEPARATOR}<img src=\"screenshot-1.png\">${LINE_SEPARATOR}и после картинки";
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldSafeNewLine_WhenHaveTheLine() {
        String rtfText = "до картики${LINE_SEPARATOR}<img src=\"screenshot-1.png\">${LINE_SEPARATOR}и после картинки";
        String expected = "до картики${LINE_SEPARATOR}!screenshot-1.png|thumbnail!${LINE_SEPARATOR}и после картинки";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldIgnoreOtherTagAttribute_WhenImageHaveAdditionalAttributes() {
        String rtfText = '<img src="screenshot-1.png" data-size="height:20px">';
        String expected = '!screenshot-1.png|thumbnail!';
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_ShouldReturnTwoImage_WhenHaveTwoImage() {
        String rtfText = '<img src="screenshot-1.png"><img src="screenshot-1.png">';
        String expected = '!screenshot-1.png|thumbnail! !screenshot-1.png|thumbnail!';
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_ShouldReturnTwoImage_WhenHaveTwoImage() {
        String mdText = '!screenshot-1.png|thumbnail! !screenshot-1.png|thumbnail!';
        String expected = '<img src="screenshot-1.png"><img src="screenshot-1.png">';
        String actual = this.coder.toRtf(mdText);
        Assert.assertEquals(expected, actual);
    }
}
