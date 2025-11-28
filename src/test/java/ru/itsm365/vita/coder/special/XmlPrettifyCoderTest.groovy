package ru.itsm365.vita.coder.special

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.itsm365.vita.coder.model.TestValue

class XmlPrettifyCoderTest {

    private XmlPrettifyCoder coder;

    @Before
    void setUp() {
        this.coder = new XmlPrettifyCoder();
    }

    @Test
    void toRtf() {
        TestValue testValue = new TestValue("/prettifyCoder/toRtf/input.txt", "/prettifyCoder/toRtf/expected.txt");
        String expected = testValue.rtf;
        String actual = coder.toRtf(testValue.md);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toRtf_print() {
        TestValue testValue = new TestValue("/prettifyCoder/toRtf/input.txt", "/prettifyCoder/toRtf/expected.txt");
        String actual = coder.toRtf(testValue.md);
        println(actual);
    }

    @Test
    void toMd() {
        TestValue testValue = new TestValue("/prettifyCoder/toMd/expected.txt", "/prettifyCoder/toMd/input.txt");
        String expected = testValue.md;
        String actual = coder.toMd(testValue.rtf);
        Assert.assertEquals(expected, actual);
    }

    @Test
    void toMd_print() {
        TestValue testValue = new TestValue("/prettifyCoder/toMd/expected.txt", "/prettifyCoder/toMd/input.txt");
        String actual = coder.toMd(testValue.rtf);
        println(actual)
    }
}
