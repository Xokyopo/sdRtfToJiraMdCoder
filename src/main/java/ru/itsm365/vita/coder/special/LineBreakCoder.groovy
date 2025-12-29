package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

class LineBreakCoder implements Coder {
    private static final String MD_LINE_BREAKER = System.lineSeparator();
    private static final String RTF_LINE_BREAKER_TAG_TOKEN = "(?i)<br *\\/?>";

    @Override
    String toRtf(String s) {
        return s;
    }

    @Override
    String toMd(String s) {
        return s.replaceAll(RTF_LINE_BREAKER_TAG_TOKEN, MD_LINE_BREAKER);
    }
}
