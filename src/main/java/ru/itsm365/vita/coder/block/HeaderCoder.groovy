package ru.itsm365.vita.coder.block

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern


class HeaderCoder implements Coder {
    private static final String MD_TAG_NAME = "h";
    private static final Pattern MD_PATTERN = Pattern.compile("(?m)^\\s*h\\d\\.\\s*.*\$", Pattern.CASE_INSENSITIVE);
    private static final Pattern MD_HEADER_LEVEL_TOKEN = Pattern.compile("(?<=h)\\d", Pattern.CASE_INSENSITIVE);
    private static final Pattern MD_HEADER_VALUE_TOKEN = Pattern.compile("(?<=h\\d.).*", Pattern.CASE_INSENSITIVE);
    private static final int MD_HEADER_LEVEL_MAX = 6;

    private static final String RTF_TAG_NAME = "h";
    private static final Pattern CLOSE_RTF_TOKEN = Pattern.compile("</${RTF_TAG_NAME}\\d>", Pattern.CASE_INSENSITIVE);
    private static final Pattern OPEN_RTF_TOKEN = Pattern.compile("<${RTF_TAG_NAME}\\d(?= |>)[^><]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern RTF_HEADER_LEVEL_TOKEN = Pattern.compile("(?<=<${RTF_TAG_NAME})\\d", Pattern.CASE_INSENSITIVE);
    private static final Pattern RTF_HEADER_VALUE_TOKEN = Pattern.compile("(?<=>).*(?=<)", Pattern.CASE_INSENSITIVE);
    private static final int RTF_HEADER_LEVEL_MAX = 4;

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        int cursor = 0;
        Matcher matcher = MD_PATTERN.matcher(s);
        while (matcher.find(cursor)) {
            if (cursor < matcher.start()) {
                result.append(s.substring(cursor, matcher.end()));
            }
            result.append(convertMdToRtfHeader(matcher.group()));
            cursor = matcher.end();
        }
        if (cursor < s.length()) {
            result.append(s.substring(cursor));
        }
        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();

        Integer cursor = 0;

        Matcher openMatcher = OPEN_RTF_TOKEN.matcher(s);
        Matcher closeMatcher = CLOSE_RTF_TOKEN.matcher(s)

        while (openMatcher.find(cursor)) {
            if (cursor < openMatcher.start()) {
                result.append(s.substring(cursor, openMatcher.start()));
                cursor = openMatcher.start();
            }

            if (closeMatcher.find(openMatcher.end())) {
                result.append(convertRtfToMdHeader(s.substring(openMatcher.start(), closeMatcher.end())));
                cursor = closeMatcher.end();
            } else {
                break;
            }
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor));
        }

        return result.toString();
    }

    private static String convertRtfToMdHeader(String value) {
        String text = Optional.of(RTF_HEADER_VALUE_TOKEN.matcher(value))
                .filter { it.find() }
                .map { it.group()?.trim() }
                .orElse("");
        int level = Optional.of(RTF_HEADER_LEVEL_TOKEN.matcher(value))
                .filter { it.find() }
                .map { parseLevel(it.group()) }
                .orElse(1);

        return createMdHeader(Math.min(level, MD_HEADER_LEVEL_MAX), text);
    }

    private static String convertMdToRtfHeader(String value) {
        String text = Optional.of(MD_HEADER_VALUE_TOKEN.matcher(value))
                .filter { it.find() }
                .map { it.group()?.trim() }
                .orElse("")
        int level = Optional.of(MD_HEADER_LEVEL_TOKEN.matcher(value))
                .filter { it.find() }
                .map { parseLevel(it.group()) }
                .orElse(1);

        return createRTFHeader(Math.min(level, RTF_HEADER_LEVEL_MAX), text);
    }

    private static int parseLevel(String level) {
        return Optional.of(level)
                .filter { it.isInteger() }
                .map { it.toInteger() }
                .orElse(1);
    }

    private static String createMdHeader(int level, String value) {
        return "${MD_TAG_NAME}${level}. ${value}";
    }

    private static String createRTFHeader(int level, String value) {
        return "<${RTF_TAG_NAME}${level}>${value}</${RTF_TAG_NAME}${level}>";
    }
}
