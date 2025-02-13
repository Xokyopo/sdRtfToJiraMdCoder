package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Обрабатывает строки, которые в RTF окружаются тегом div, а в MD выражаются переносом строки.
 * Вызывается после основных кодеров, и обрабатывает оставшиеся объекты как строки.
 */
class RowCoder implements Coder {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String RTF_TAG_PATTERN_STRING = "<\\/?[^>\\/]+\\/?>";
    private static final Pattern RTF_TAG_PATTERN = Pattern.compile(RTF_TAG_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
    private static final String DEFAULT_RTF_LINE_TAG_NAME = "div";
    private static final List<String> RTF_ROW_TAGS = [
            DEFAULT_RTF_LINE_TAG_NAME, "h1", "h2", "h3", "h4", "h5", "h6", "h7", "pre", "blockquote"
    ];
    private static final List<String> RTF_BLOCK_TAGS = [
            "table", "ul", "ol"
    ];
    private static final List<String> ESCAPED_RTF_TAGS = RTF_BLOCK_TAGS + [
            "img"
    ];

    private static final String ESCAPED_MD_TAGS = [
            //TODO что то будет только что.
    ];

    @Override
    String toRtf(String s) {
        return null
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();
        Matcher matcher = RTF_TAG_PATTERN.matcher(s);

        int cursor = 0;
        Stack<RtfTag> openedEscapedTags = [];
        while (matcher.find(cursor)) {
            RtfTag rtfTag = new RtfTag(matcher.group());

            if (rtfTag.name == DEFAULT_RTF_LINE_TAG_NAME) {
                if (openedEscapedTags.isEmpty()) {
                    result.append(s.substring(cursor, matcher.start()));
                    if (!rtfTag.isOpen() && matcher.end() < s.length()) {
                        result.append(LINE_SEPARATOR);
                    }
                } else {
                    result.append(s.substring(cursor, matcher.end()));
                }
            } else if (RTF_ROW_TAGS.contains(rtfTag.name)) {
                result.append(s.substring(cursor, matcher.end()));
                if (openedEscapedTags.isEmpty() && !rtfTag.isOpen() && matcher.end() < s.length()) {
                    result.append(LINE_SEPARATOR);
                }
            } else {
                result.append(s.substring(cursor, matcher.end()));
                if (ESCAPED_RTF_TAGS.contains(rtfTag.name)) {
                    if (rtfTag.isOpen()) {
                        openedEscapedTags.push(rtfTag);
                    } else if (openedEscapedTags.last().name == rtfTag.name) {
                        openedEscapedTags.pop();
                    }
                }

                if (!rtfTag.isOpen() && RTF_BLOCK_TAGS.contains(rtfTag.name) && matcher.end() < s.length()) {
                    result.append(LINE_SEPARATOR);
                    result.append(LINE_SEPARATOR);
                }
            }

            cursor = matcher.end()
        }

        if (cursor < s.size()) {
            result.append(s.substring(cursor));
        }

        return result.toString()
    }

    static class RtfTag {
        private static final String CLOSE_PREFIX = "</";
        private static final String SOLO_POSTFIX = "/>";
        private static final String NAME_TOKEN_STRING = "(?<=<\\/?)[^>\\/\\h]+";
        private static final Pattern NAME_TOKEN = Pattern.compile(NAME_TOKEN_STRING, Pattern.CASE_INSENSITIVE);

        private static final List<String> SOLO_TAGS = [
                "br", "hr", "input", "img"
        ]
        String name = "";
        boolean open = false;

        RtfTag(String tag) {
            Matcher matcher = NAME_TOKEN.matcher(tag);
            this.name = matcher.find()
                    ? matcher.group()
                    : tag

            boolean closeTag = tag.startsWith(CLOSE_PREFIX) || tag.endsWith(SOLO_POSTFIX) || SOLO_TAGS.contains(this.name);
            this.open = !closeTag;
        }
    }
}
