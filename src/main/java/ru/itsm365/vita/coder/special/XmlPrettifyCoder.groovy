package ru.itsm365.vita.coder.special


import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Используется для того что бы приводить XML в красивый вид, после преобразования кода конвертируем в RTF
 * А так же минифицировать RTF перед преобразваоинем в MD
 */
class XmlPrettifyCoder implements Coder {
    private static final String ANY_TAG_REGEXP = "<\\/?[^>]*[^\\/]>";
    private static final Pattern ANY_TAG_PATTERN = Pattern.compile(ANY_TAG_REGEXP, Pattern.CASE_INSENSITIVE);
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String END_TAG_PREFIX = "</";
    private static final String TAB = "\t";
    private static final List<String> IGNORE_TAG_LIST = [
            "<br", "<img", "<input", "<meta"
    ]

    @Override
    String toRtf(String s) {
        String minifiedXml = this.toMd(s);
        StringBuilder result = new StringBuilder();
        Matcher matcher = ANY_TAG_PATTERN.matcher(minifiedXml);

        int cursor = 0;
        int deepLayer = 0;
        while (matcher.find(cursor)) {
            String tag = matcher.group();

            if (IGNORE_TAG_LIST.find { tag.toLowerCase().startsWith(it) }) {
                if (cursor < matcher.end()) {
                    append(TAB, deepLayer, result);
                    result.append(minifiedXml.substring(cursor, matcher.end()));
                    result.append(LINE_SEPARATOR);
                }
            } else {
                if (tag.startsWith(END_TAG_PREFIX)) {
                    if (cursor < matcher.start()) {
                        append(TAB, deepLayer, result);
                        result.append(minifiedXml.substring(cursor, matcher.start()));
                        result.append(LINE_SEPARATOR);
                    }
                    deepLayer = deepLayer <= 0 ? 0 : deepLayer - 1;
                    append(TAB, deepLayer, result);
                    result.append(tag);
                    result.append(LINE_SEPARATOR);
                } else {
                    if (cursor < matcher.start()) {
                        append(TAB, deepLayer, result);
                        result.append(minifiedXml.substring(cursor, matcher.start()));
                        result.append(LINE_SEPARATOR);
                    }
                    append(TAB, deepLayer, result);
                    result.append(tag);
                    result.append(LINE_SEPARATOR);
                    deepLayer++;
                }
            }

            cursor = matcher.end();
        }

        if (cursor < minifiedXml.size()) {
            result.append(minifiedXml.substring(cursor));
        }

        return result.toString()
    }

    @Override
    String toMd(String s) {
        String withoutTagSpaces = s.replaceAll(">\\s+", ">");
        withoutTagSpaces = withoutTagSpaces.replaceAll("\\s+<", "<");
        String withoutLineSeparator = withoutTagSpaces.replaceAll(LINE_SEPARATOR, "");
        return withoutLineSeparator
    }

    private static void append(String text, int count, StringBuilder stringBuilder) {
        for (i in 0..<count) {
            stringBuilder.append(text);
        }
    }
}
