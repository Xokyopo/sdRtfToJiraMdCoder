package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder
import ru.itsm365.vita.coder.model.RtfTag

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Обрабатывает строки, которые в RTF окружаются тегом div, а в MD выражаются переносом строки.
 * Вызывается после основных кодеров, и обрабатывает оставшиеся объекты как строки.
 * в блоке кода нельзя трогать разметку, оставлять без изменений все что внутри.. (pre)
 */
class RowCoder implements Coder {
    private static final String MD_LINE_SEPARATOR = System.lineSeparator();
    private static final String RTF_TAG_PATTERN_STRING = "<[^>]+>";
    private static final Pattern RTF_TAG_PATTERN = Pattern.compile(RTF_TAG_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
    private static final String CONVERTED_MD_OPEN_TAG_PATTERN_STRING = "^\\h*<[^>]+>";
    private static final Pattern CONVERTED_MD_OPEN_TAG_PATTERN = Pattern.compile(CONVERTED_MD_OPEN_TAG_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
    private static final String CONVERTED_MD_CLOSE_TAG_PATTERN_STRING = "<\\\\[^>]+>\\h*\$";
    private static final Pattern CONVERTED_MD_CLOSE_TAG_PATTERN = Pattern.compile(CONVERTED_MD_CLOSE_TAG_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
    private static final String DEFAULT_RTF_LINE_TAG_NAME = "div";
    private static final List<String> RTF_ROW_TAGS = [
            DEFAULT_RTF_LINE_TAG_NAME, "h1", "h2", "h3", "h4", "h5", "h6", "h7", "pre", "blockquote", "p"
    ];
    private static final List<String> RTF_BLOCK_TAGS = [
            "table", "ul", "ol"
    ];
    private static final List<String> ESCAPED_RTF_PAIRED_TAGS = RTF_BLOCK_TAGS + [
            "pre", "blockquote"
    ];
    private static final List<String> ESCAPED_MD_TAGS = [
            "pre", "blockquote"
    ];

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> lines = s.split(MD_LINE_SEPARATOR);

        Stack<RtfTag> openedEscapedTags = [];
        for (String line in lines) {
            if (!openedEscapedTags.isEmpty()) {
                Matcher escapeTagMatcher = CONVERTED_MD_CLOSE_TAG_PATTERN.matcher(line);
                if (escapeTagMatcher.find()) {
                    String tag = escapeTagMatcher.group();
                    RtfTag rtfTag = new RtfTag(tag);
                    if (openedEscapedTags.last().name == rtfTag.name) {
                        openedEscapedTags.pop();
                    }
                }

                result.append(MD_LINE_SEPARATOR);
                result.append(line);
                continue;
            }

            Matcher matcher = CONVERTED_MD_OPEN_TAG_PATTERN.matcher(line);
            if (matcher.find()) {
                String tag = matcher.group();
                RtfTag rtfTag = new RtfTag(tag);
                boolean escapedTag = ESCAPED_MD_TAGS.contains(rtfTag.name);
                if (escapedTag) {
                    openedEscapedTags.push(rtfTag);
                    result.append(line);
                    continue;
                }

                boolean rowTag = RTF_ROW_TAGS.contains(rtfTag.name);
                boolean blockTag = RTF_BLOCK_TAGS.contains(rtfTag.name);
                if (rowTag || blockTag) {
                    result.append(line);
                } else {
                    result.append(createTag(DEFAULT_RTF_LINE_TAG_NAME, line));
                }
            } else if (!line.isBlank()) {
                result.append(createTag(DEFAULT_RTF_LINE_TAG_NAME, line));
            }
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();
        Matcher matcher = RTF_TAG_PATTERN.matcher(s);

        int cursor = 0;
        Stack<RtfTag> openedEscapedTags = [];
        while (matcher.find(cursor)) {
            RtfTag rtfTag = new RtfTag(matcher.group());

            if (ESCAPED_RTF_PAIRED_TAGS.contains(rtfTag.name)) {
                if (rtfTag.open) {
                    openedEscapedTags.push(rtfTag);
                } else if (openedEscapedTags.last().name == rtfTag.name) {
                    openedEscapedTags.pop();
                }
            }

            if (!openedEscapedTags.isEmpty()) {
                result.append(s.substring(cursor, matcher.end()));
                cursor = matcher.end();
                continue;
            }

            if (rtfTag.name == DEFAULT_RTF_LINE_TAG_NAME) {
                result.append(s.substring(cursor, matcher.start()));
                if (!rtfTag.open && matcher.end() < s.length()) {
                    result.append(MD_LINE_SEPARATOR);
                }
            } else if (RTF_ROW_TAGS.contains(rtfTag.name)) {
                result.append(s.substring(cursor, matcher.end()));
                if (!rtfTag.open && matcher.end() < s.length()) {
                    result.append(MD_LINE_SEPARATOR);
                }
            } else if (RTF_BLOCK_TAGS.contains(rtfTag.name)) {
                result.append(s.substring(cursor, matcher.end()));
                if (!rtfTag.open && matcher.end() < s.length()) {
                    result.append(MD_LINE_SEPARATOR);
                    result.append(MD_LINE_SEPARATOR);
                }
            } else {
                result.append(s.substring(cursor, matcher.end()));
            }

            cursor = matcher.end()
        }

        if (cursor < s.size()) {
            result.append(s.substring(cursor));
        }

        return result.toString()
    }

    private static String createTag(String tagName, String content) {
        return "<${tagName}>${content}</${tagName}>";
    }

}
