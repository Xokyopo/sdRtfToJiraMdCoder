package ru.itsm365.vita.coder.inline

import ru.itsm365.vita.coder.Coder

class LineBreakCoder implements Coder {
    private static final String MD_LINE_SEPARATOR = System.lineSeparator();
    private static final String RTF_LINE_SEPARATOR = "<br>";
    private static final String RTF_LINE_BREAKER_TAG_TOKEN = "(?i)<br *\\/?>";
    private static final String MD_SEPARATED_LINE_TOKEN = "(?<!${MD_LINE_SEPARATOR})${MD_LINE_SEPARATOR}(?!\\s)(?![\\*#]+|\\|)";

    @Override
    String toRtf(String s) {
        return s.replaceAll(MD_SEPARATED_LINE_TOKEN, RTF_LINE_SEPARATOR);
    }

    @Override
    String toMd(String s) {
        return s.replaceAll(RTF_LINE_BREAKER_TAG_TOKEN, MD_LINE_SEPARATOR);
    }
}
