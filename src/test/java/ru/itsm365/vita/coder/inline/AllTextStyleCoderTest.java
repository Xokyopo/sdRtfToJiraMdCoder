package ru.itsm365.vita.coder.inline;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllTextStyleCoderTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private AllTextStyleCoder coder;

    @BeforeEach
    void setUp() {
        this.coder = new AllTextStyleCoder();
    }

    @Test
    void toMd_ShouldSafeSpace_WhenSpaceInsideTag() {
        String rtfText = "<s>Привет </s><strong>мир </strong>в jira";
        String expected = "{-}Привет {-}{*}мир {*}в jira";
        String actual = this.coder.toMd(rtfText);
        Assert.assertEquals(expected, actual);
    }
}
