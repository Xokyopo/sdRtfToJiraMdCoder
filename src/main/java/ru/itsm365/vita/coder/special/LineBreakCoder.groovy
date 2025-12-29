package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

class LineBreakCoder implements Coder {
    private static final String MD_LINE_SEPARATOR = System.lineSeparator();
    private static final String RTF_LINE_BREAKER_TAG_TOKEN = "(?i)<br *\\/?>";
    private static final String MD_NBSP = " "
    private static final String MD_LINE_JOINER_TOKEN = "(?<!\\h*>)${MD_LINE_SEPARATOR}(?!${MD_LINE_SEPARATOR}|\\h*[\\*#]+\\h+|\\h*\\||\\h*<)";

    @Override
    String toRtf(String s) {
        String withEmptyLines = s.replaceAll("${MD_LINE_SEPARATOR}${MD_NBSP}${MD_LINE_SEPARATOR}", "<div><br></div>");
        return withEmptyLines.replaceAll(MD_LINE_JOINER_TOKEN, "<br>");
    }

    @Override
    String toMd(String s) {
        String replacedEmptyLine = s.replaceAll("(?i)<div>\\s*<br>\\s*</div>", "${MD_LINE_SEPARATOR}${MD_NBSP}${MD_LINE_SEPARATOR}");
        return replacedEmptyLine.replaceAll(RTF_LINE_BREAKER_TAG_TOKEN, MD_LINE_SEPARATOR);
    }
}
