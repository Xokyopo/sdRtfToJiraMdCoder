package ru.itsm365.vita.coder.inline

import ru.itsm365.vita.coder.Coder

class LineBreakCoder implements Coder {
    private static final String MD_LINE_SEPARATOR = System.lineSeparator();
    private static final String RTF_LINE_SEPARATOR = "<br>";
    private static final String RTF_LINE_BREAKER_TAG_TOKEN = "(?i)<br *\\/?>";
    private static final String MD_SEPARATED_LINE_TOKEN = "(?<!${MD_LINE_SEPARATOR})${MD_LINE_SEPARATOR}(?!\\s)(?![\\*#]+|\\|)";
    private static final String MD_NBSP = " "

    @Override
    String toRtf(String s) {
        String mergedText = s.replaceAll(MD_SEPARATED_LINE_TOKEN, RTF_LINE_SEPARATOR);
        String withEmptyLines = mergedText.replaceAll("(?<=${MD_LINE_SEPARATOR})${MD_NBSP}(?=${MD_LINE_SEPARATOR})", RTF_LINE_SEPARATOR);
        String removedBefore = withEmptyLines.replaceAll("(\\r?\\n)+(?=${RTF_LINE_SEPARATOR})", MD_LINE_SEPARATOR);
        return removedBefore.replaceAll("(?<=${RTF_LINE_SEPARATOR})(\\r?\\n)+", MD_LINE_SEPARATOR);
    }

    @Override
    String toMd(String s) {
        String replacedEmptyLine = s.replaceAll("(?<=${MD_LINE_SEPARATOR})${RTF_LINE_SEPARATOR}(?=${MD_LINE_SEPARATOR})", "${MD_LINE_SEPARATOR}${MD_NBSP}${MD_LINE_SEPARATOR}");
        String fixedDoubleSeparetion = replacedEmptyLine.replaceAll("(?<=${MD_NBSP})(\\r?\\n)+(?=${MD_NBSP})", "${MD_LINE_SEPARATOR}${MD_LINE_SEPARATOR}");
        return fixedDoubleSeparetion.replaceAll(RTF_LINE_BREAKER_TAG_TOKEN, MD_LINE_SEPARATOR);
    }
}
