package ru.itsm365.vita.coder.inline


import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class LinkCoder implements Coder {
    private static final String MD_REGEXP_PATTERN = "\\[[^\\|]+\\|?[^\\[\\|]*\\]";
    private static final String MD_NEXT_LINE = System.lineSeparator();
    private static final String MD_OPEN_TAG = "[";
    private static final String MD_CLOSE_TAG = "]";
    private static final String MD_PART_DELIMITER_TAG = "|";
    private static final String RTF_TAG_NAME = "a";
    private static final String RTF_OPEN_LINK_TAG_REGEXP_TOKEN = "<[aA]\\s[^><]*>";
    private static final String RTF_CLOSE_LINK_TAG_REGEXP_TOKEN = "</[aA]>";
    private static final String RTF_BOTH_TAG_REGEXP_PATTERN = "(<[aA]\\s[^><]*>)|(</[aA]>)";
    private static final String RTF_HREF_REGEXP_TOKEN = '(?<=href=\\")[^\\"]+';

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> lines = s.split(MD_NEXT_LINE);

        Pattern mdToken = Pattern.compile(MD_REGEXP_PATTERN);

        for (String line in lines) {
            Integer cursor = 0;

            Matcher matcher = mdToken.matcher(line);

            while (matcher.find(cursor)) {
                if (cursor < matcher.start()) {
                    result.append(line.substring(cursor, matcher.start()));
                }

                result.append(convertLinkMdToRtf(line.substring(matcher.start(), matcher.end())));

                cursor = matcher.end();
            }

            if (cursor < line.length()) {
                result.append(line.substring(cursor));
            }

            result.append(MD_NEXT_LINE);
        }

        //Исправляем перевод строки в последней.
        if (result.size() > 0 && !s.endsWith(MD_NEXT_LINE)) {
            result.setLength(result.size() - MD_NEXT_LINE.length());
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();

        Pattern pattern = Pattern.compile(RTF_BOTH_TAG_REGEXP_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        Integer cursor = 0;
        Integer foundOpenTagCursor = null;
        while (matcher.find()) {
            String tag = matcher.group();

            if (tag.matches(RTF_OPEN_LINK_TAG_REGEXP_TOKEN)) {
                foundOpenTagCursor = matcher.start();
            } else if (foundOpenTagCursor != null && tag.matches(RTF_CLOSE_LINK_TAG_REGEXP_TOKEN)) {
                if (cursor < foundOpenTagCursor) {
                    result.append(s.substring(cursor, foundOpenTagCursor));
                }

                result.append(convertLinkRtfToMd(s.substring(foundOpenTagCursor, matcher.end())));
                cursor = matcher.end();
                foundOpenTagCursor = null;
            }
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor));
        }

        return result.toString();
    }

    private static String convertLinkMdToRtf(String link) {
        Integer delimiterIndex = link.indexOf(MD_PART_DELIMITER_TAG);

        if (delimiterIndex < 1) {
            String href = link.substring(1, link.length() - 1);
            return createRtfLink(href, href);
        }

        String text = link.substring(1, delimiterIndex);
        String href = link.substring(delimiterIndex + 1, link.length() - 1);
        return createRtfLink(href, text);
    }

    private static String convertLinkRtfToMd(String link) {
        String text = link.replaceAll(RTF_BOTH_TAG_REGEXP_PATTERN, "");
        String href = "#";

        Pattern hrefPattern = Pattern.compile(RTF_HREF_REGEXP_TOKEN, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(link);
        if (hrefMatcher.find()) {
            href = hrefMatcher.group();
        }

        return createMdLink(href, text);
    }

    private static String createRtfLink(String href, String text) {
        return "<${RTF_TAG_NAME} href=\"${href}\">${text}</${RTF_TAG_NAME}>"
    }

    private static String createMdLink(String href, String text) {
        return "${MD_OPEN_TAG}${text}${MD_PART_DELIMITER_TAG}${href}${MD_CLOSE_TAG}"
    }
}
