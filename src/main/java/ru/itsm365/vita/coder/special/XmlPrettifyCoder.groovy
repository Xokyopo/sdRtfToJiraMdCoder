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
    private final String ANY_TAG_REGEXP = "<[^>]+>";
    private final Pattern ANY_TAG_PATTERN = Pattern.compile(ANY_TAG_REGEXP, Pattern.CASE_INSENSITIVE);
    private final String LINE_SEPARATOR = System.lineSeparator();
    private final String TAB = "\t";
    private final List<String> RTF_ROW_TAGS = [
            "div", "h1", "h2", "h3", "h4", "h5", "h6", "h7", "pre", "p", "li", "td", "th"
    ];
    private final List<String> RTF_BLOCK_TAGS = [
            "table", "ul", "ol", "tbody", "tr", "thead", "blockquote"
    ];
    private final BR_TAG_NAME = "br";

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
                    if (!openTags.isEmpty()) {
                        openTags.pop()
                        append(TAB, openTags.size(), result);
                    };
                    result.append(minifiedXml.substring(matcher.start(), matcher.end()));
                }
                result.append(LINE_SEPARATOR)
                cursor = matcher.end();
                continue
            }


            if (RTF_ROW_TAGS.contains(currentTag.name)) {
                result.append(minifiedXml.substring(cursor, matcher.start()));
                if (currentTag.open) {
                    if (!openTags.isEmpty() && !RTF_ROW_TAGS.contains(openTags.last().name)) {
                        append(TAB, openTags.size(), result);
                    }
                    result.append(minifiedXml.substring(matcher.start(), matcher.end()));
                    openTags.push(currentTag)
                } else {
                    openTags.pop()
                    if (result.endsWithAny(LINE_SEPARATOR) && (openTags.isEmpty() || !RTF_ROW_TAGS.contains(openTags.last().name))) {
                        append(TAB, openTags.size(), result)
                    }
                    result.append(minifiedXml.substring(matcher.start(), matcher.end()));
                    if (openTags.isEmpty() || !RTF_ROW_TAGS.contains(openTags.last().name)) {
                        result.append(LINE_SEPARATOR)
                    }
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
        String result = s;
        // 1. Удаляем пробелы вокруг открывающими тегов блоков
        for (String tag in RTF_BLOCK_TAGS) {
            result = result.replaceAll("\\s*(?=<${tag}[^>]*)\\s*", "");
        }

        // 2. Удаляем пробелы вокруг закрывающих тегов блоков
        for (String tag in RTF_BLOCK_TAGS) {
            result = result.replaceAll("\\s*(?<=</${tag}>)\\s*", "");
        }

        // 3. Удаляем пробелы перед открывающими тегами строк
        for (String tag in RTF_ROW_TAGS) {
            result = result.replaceAll("\\s+(?=<${tag})", "");
        }
        // 4. Удаляем пробелы после закрывающих тегов строк
        for (String tag in RTF_ROW_TAGS) {
            result = result.replaceAll("(?<=/${tag}>)\\s+", "");
        }

        // 5. Удаляем пробелы перед </td> или </th>
        result = result.replaceAll("\\s+(?=</td>)", "");
        result = result.replaceAll("\\s+(?=</th>)", "");

        //6. удаляем пробелы перед БР`кой
        result = result.replaceAll("\\s+(?=<${BR_TAG_NAME})", "")

        // 7. Удаляем все переносы строк
        return result.replaceAll(LINE_SEPARATOR, "");
    }

    private void append(String text, int count, StringBuilder stringBuilder) {
        for (i in 0..<count) {
            stringBuilder.append(text);
        }
    }
}
