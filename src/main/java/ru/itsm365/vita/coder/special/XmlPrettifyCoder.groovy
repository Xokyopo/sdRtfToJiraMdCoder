package ru.itsm365.vita.coder.special


import ru.itsm365.vita.coder.Coder
import ru.itsm365.vita.coder.model.RtfTag

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
    private static final List<String> RTF_ROW_TAGS = [
            "div", "h1", "h2", "h3", "h4", "h5", "h6", "h7", "pre", "blockquote", "p", "li", "td"
    ];
    private static final List<String> RTF_BLOCK_TAGS = [
            "table", "ul", "ol", "tbody", "tr"
    ];

    @Override
    String toRtf(String s) {
        String minifiedXml = this.toMd(s);
        StringBuilder result = new StringBuilder();
        Matcher matcher = ANY_TAG_PATTERN.matcher(minifiedXml);

        int cursor = 0;
        Stack<RtfTag> openTags = new Stack<>()
        while (matcher.find(cursor)) {
            RtfTag currentTag = new RtfTag(matcher.group());

            if (RTF_BLOCK_TAGS.contains(currentTag.name)) {
                result.append(minifiedXml.substring(cursor, matcher.start()));
                if (currentTag.open) {
                    if (cursor != matcher.start()) {
                        result.append(LINE_SEPARATOR)
                    }
                    append(TAB, openTags.size(), result);
                    result.append(minifiedXml.substring(matcher.start(), matcher.end()));
                    openTags.push(currentTag);
                } else {
                    openTags.pop();
                    append(TAB, openTags.size(), result);
                    result.append(minifiedXml.substring(matcher.start(), matcher.end()));
                }
                result.append(LINE_SEPARATOR)
                cursor = matcher.end();
                continue
            }


            if (RTF_ROW_TAGS.contains(currentTag.name)) {
                result.append(minifiedXml.substring(cursor, matcher.start()));
                if (currentTag.open) {
                    append(TAB, openTags.size(), result);
                    result.append(minifiedXml.substring(matcher.start(), matcher.end()));
                    openTags.push(currentTag)
                } else {
                    openTags.pop()
                    if (result.endsWithAny(LINE_SEPARATOR)) {
                        append(TAB, openTags.size(), result)
                    }
                    result.append(minifiedXml.substring(matcher.start(), matcher.end()));
                    result.append(LINE_SEPARATOR)
                }
                cursor = matcher.end();
                continue;
            }

            result.append(minifiedXml.substring(cursor, matcher.end()));
            cursor = matcher.end();
        }

        if (cursor < minifiedXml.size()) {
            result.append(minifiedXml.substring(cursor));
        }

        while (!result.isEmpty() && result.endsWithAny(LINE_SEPARATOR)) {
            result.setLength(result.size() - LINE_SEPARATOR.size());
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
