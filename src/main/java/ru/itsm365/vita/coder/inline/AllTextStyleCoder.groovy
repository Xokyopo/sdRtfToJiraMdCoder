package ru.itsm365.vita.coder.inline


import ru.itsm365.vita.coder.Coder
import ru.itsm365.vita.coder.model.RtfTag

import java.util.regex.Matcher
import java.util.regex.Pattern

class AllTextStyleCoder implements Coder {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final List<String> REG_EXP_OPERATORS = ["*", "^", "+"]
    private static final Map<String, String> RTF_TAG_NANE_BY_MD_TAGS = [
            "*": "strong",
            "_": "em",
            "-": "s",
            "~": "sub",
            "^": "sup",
            "+": "u"
    ];
    private static final Pattern MD_OPEN_TAGS_PATTERN
    private static final Map<String, Pattern> MD_CLOSE_TAG_PATTERN_BY_MD_TAGS
    private static final Map<String, String> MD_TAG_BY_RTF_TAG_NAMES
    private static final Pattern RTF_OPEN_TAGS_PATTERN
    private static final Map<String, Pattern> RTF_CLOSE_TAG_PATTERNS_BY_RTF_TAG_NAMES
    private static final String MD_NEXT_LINE = System.lineSeparator();

    static {
        MD_TAG_BY_RTF_TAG_NAMES = RTF_TAG_NANE_BY_MD_TAGS.collectEntries { key, value -> [value, key] }

        String rtfOpenTagsRegEx = "<(?:${MD_TAG_BY_RTF_TAG_NAMES.collect { it.key }.join("|")})(?= |>)[^><]*>\\s*"
        RTF_OPEN_TAGS_PATTERN = Pattern.compile(rtfOpenTagsRegEx, Pattern.CASE_INSENSITIVE)
        RTF_CLOSE_TAG_PATTERNS_BY_RTF_TAG_NAMES = MD_TAG_BY_RTF_TAG_NAMES.collectEntries { key, value ->
            [key, Pattern.compile("\\s*</${key}>", Pattern.CASE_INSENSITIVE)]
        }

        MD_CLOSE_TAG_PATTERN_BY_MD_TAGS = [:]
        RTF_TAG_NANE_BY_MD_TAGS.forEach { key, value ->
            String escapedMgTag = REG_EXP_OPERATORS.contains(key) ? "\\${key}" : key
            MD_CLOSE_TAG_PATTERN_BY_MD_TAGS.put(key.toString(), Pattern.compile("(?<!\\h)${escapedMgTag}(?=\\s|\$|\\|)", Pattern.CASE_INSENSITIVE));
            MD_CLOSE_TAG_PATTERN_BY_MD_TAGS.put("{${key}}".toString(), Pattern.compile("\\{${escapedMgTag}}", Pattern.CASE_INSENSITIVE));
        }

        List<String> mdOpenTags = RTF_TAG_NANE_BY_MD_TAGS.collect {
            return REG_EXP_OPERATORS.contains(it.key) ? "\\${it.key}" : it.key
        }

        String escapedStartMdTagPattern = "(?:${mdOpenTags.collect { "\\{${it}}" }.join("|")})"
        String aloneStartMdTagPattern = "((?<=\\s|^|\\|)(?:${mdOpenTags.join("|")})(?!\\s|\\*+\\h))"
        MD_OPEN_TAGS_PATTERN = Pattern.compile("${escapedStartMdTagPattern}|${aloneStartMdTagPattern}", Pattern.CASE_INSENSITIVE)
    }

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> lines = s.split(MD_NEXT_LINE);

        for (String line in lines) {
            Integer cursor = 0;

            Matcher openMatcher = MD_OPEN_TAGS_PATTERN.matcher(line);

            while (openMatcher.find(cursor)) {
                if (cursor < openMatcher.start()) {
                    result.append(line.substring(cursor, openMatcher.start()));
                    cursor = openMatcher.start();
                }

                MdTag mdTag = new MdTag(openMatcher.group());
                Pattern mdCloseTagPattern = MD_CLOSE_TAG_PATTERN_BY_MD_TAGS.get(mdTag.tag)
                if (mdCloseTagPattern) {
                    Matcher closeMatcher = mdCloseTagPattern.matcher(line)
                    if (closeMatcher.find(openMatcher.end())) {
                        result.append(convertToRtfTag(mdTag));
                        if (closeMatcher.start() > openMatcher.end()) {
                            result.append(toRtf(line.substring(openMatcher.end(), closeMatcher.start())));
                        }
                        result.append(convertToRtfTag(mdTag, true));
                        cursor = closeMatcher.end();
                        continue;
                    }
                }

                result.append(line.substring(cursor, openMatcher.end()));
                cursor = openMatcher.end();
            }

            if (cursor < line.length()) {
                result.append(line.substring(cursor));
            }

            result.append(MD_NEXT_LINE);
        }

        //Исправляем перевод строки в последней.
        if (result.size() > 0) {
            result.setLength(result.size() - MD_NEXT_LINE.length());
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();

        Integer cursor = 0;

        Matcher openMatcher = RTF_OPEN_TAGS_PATTERN.matcher(s);
        while (openMatcher.find(cursor)) {
            if (cursor < openMatcher.start()) {
                result.append(s.substring(cursor, openMatcher.start()));
                cursor = openMatcher.start();
            }

            RtfTag rtfTag = new RtfTag(openMatcher.group())
            Pattern rtfCloseTagPattern = RTF_CLOSE_TAG_PATTERNS_BY_RTF_TAG_NAMES.get(rtfTag.name)
            if (rtfCloseTagPattern) {
                Matcher closeMatcher = rtfCloseTagPattern.matcher(s)
                if (closeMatcher.find(openMatcher.end())) {
                    Closure<String> tagConverter = this.&convertToMdEscapedTag
                    boolean emptyBeforeStart = openMatcher.start() == 0 || s[openMatcher.start() - 1] == " ";
                    emptyBeforeStart = emptyBeforeStart || startWith(LINE_SEPARATOR, s, openMatcher.start());
                    boolean emptyAfterEnd = closeMatcher.end() == s.length() || s[closeMatcher.end()] == " ";
                    emptyAfterEnd = emptyAfterEnd || s.startsWith(LINE_SEPARATOR, closeMatcher.end());
                    if (emptyBeforeStart && emptyAfterEnd) {
                        tagConverter = this.&convertToMdTag;
                    }
                    result.append(tagConverter(rtfTag));
                    if (closeMatcher.start() > openMatcher.end()) {
                        result.append(toMd(s.substring(openMatcher.end(), closeMatcher.start())));
                    }
                    result.append(tagConverter(rtfTag));
                    cursor = closeMatcher.end();
                    continue;
                }
            }

            result.append(s.substring(cursor, openMatcher.end()));
            cursor = openMatcher.end();
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor, s.length()));
        }

        return result.toString();
    }

    private static String convertToMdEscapedTag(RtfTag rtfTag) {
        String mdTagName = MD_TAG_BY_RTF_TAG_NAMES.get(rtfTag.name);
        return mdTagName ? "{${mdTagName}}" : rtfTag.tag;
    }

    private static String convertToMdTag(RtfTag rtfTag) {
        String mdTagName = MD_TAG_BY_RTF_TAG_NAMES.get(rtfTag.name);
        return mdTagName ? "${mdTagName}" : rtfTag.tag;
    }

    private static String convertToRtfTag(MdTag mdTagName, boolean close = false) {
        String rtfTagName = RTF_TAG_NANE_BY_MD_TAGS.get(mdTagName.name);
        if (rtfTagName) {
            return close ? "</${rtfTagName}>" : "<${rtfTagName}>"
        }
        return mdTagName.tag;
    }

    private static boolean startWith(String prefix, String text, int lastIndex) {
        return text.startsWith(prefix, Math.max(lastIndex - prefix.length(), 0));
    }

    protected static class MdTag {
        private static final Pattern mdTagToken = Pattern.compile("[^}{\\s]+", Pattern.CASE_INSENSITIVE)
        public final String tag;
        public final String name;

        MdTag(String inputStringWithTag) {
            this.tag = inputStringWithTag.trim();
            Matcher matcher = mdTagToken.matcher(tag);
            this.name = matcher.find()
                    ? matcher.group()
                    : tag
        }
    }
}
