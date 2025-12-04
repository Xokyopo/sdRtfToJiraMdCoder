//package ru.itsm365.vita.coder
//
//import org.junit.Assert
//import org.junit.Before
//import org.junit.Test
//import ru.itsm365.vita.coder.model.TestValue
//import ru.itsm365.vita.coder.special.XmlPrettifyCoder
//
//class RtfToJiraMdCoderSpanTest {
//    private final XmlPrettifyCoder xmlPrettifyCoder = new XmlPrettifyCoder();
//    private final TestValue originalText = new TestValue("/originalText/quote/md.txt", "/originalText/quote/rtf.txt");
//    private final TestValue expectedText = new TestValue("/expectedText/quote/md.txt", "/expectedText/quote/rtf.txt");
//
//    private Coder coder;
//
//    @Before
//    void setUp() {
//        this.coder = new RtfToJiraMdCoder();
//    }
//
//
//    @Test
//    void toMd0_ShouldReturnOnlyText_WhenTagHaveAttributes() {
//        String rtfText = '<div><span style="color: rgb(235, 107, 86); --darkreader-inline-color: var(--darkreader-text-eb6b56, #ec6f5b);" data-darkreader-inline-color="" fr-original-style="color: rgb(235, 107, 86); --darkreader-inline-color: var(--darkreader-text-eb6b56, #ec6f5b);">Hello&nbsp;<strong fr-original-style="" style="font-weight: 700;">Bro&nbsp;<em>I&nbsp;<u>am&nbsp;<s>COOL</s></u></em></strong></span></div>';
//        String expected = "{color:#eb6b56}Hello {*}Bro {_}I {+}am {-}COOL{-}{+}{_}{*}{color}";
//        String actual = this.coder.toMd(rtfText);
//
//        Assert.assertEquals(expected, actual);
//    }
//
//    @Test
//    void toMd1_ShouldReturnOnlyText_WhenTagHaveAttributes() {
//        String rtfText = '<span style="color: rgb(184, 49, 47);">красный</span>123<span style="color: rgb(184, 49, 47);">красный</span>';
//        String expected = "{color:#b8312f}красный{color}123{color:#b8312f}красный{color}";
//        String actual = this.coder.toMd(rtfText);
//
//        Assert.assertEquals(expected, actual);
//    }
//
//    @Test
//    void toRtf_ComplexQuote_ReturnExpectedResult_WhenConvertOriginalText() {
//        String expected = this.originalText.md;
//        String actual = this.coder.toRtf(expected);
//        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expectedText.rtf), this.xmlPrettifyCoder.toRtf(actual));
//    }
//
//    @Test
//    void toMd_ComplexLQuote_ReturnExpectedResult_WhenConvertOriginalText() {
//        String expected = this.originalText.rtf;
//        String actual = this.coder.toMd(expected);
//        Assert.assertEquals(this.expectedText.md, actual);
//    }
//
//    @Test
//    void toMd_ReverseComplexQuote_ReturnExpectedResult_WhenConvertOriginalText() {
//        String expected = this.originalText.md;
//        String actual = this.coder.toMd(this.coder.toRtf(expected));
//        Assert.assertEquals(this.expectedText.md, actual);
//    }
//
//    @Test
//    void toRtf_ReverseComplexQuote_ReturnExpectedResult_WhenConvertOriginalText() {
//        String expected = this.originalText.rtf;
//        String actual = this.coder.toRtf(this.coder.toMd(expected));
//        Assert.assertEquals(this.xmlPrettifyCoder.toRtf(expectedText.rtf), this.xmlPrettifyCoder.toRtf(actual));
//    }
//}
