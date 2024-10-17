package ru.itsm365.vita.coder.line

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class LinkCoder implements Coder {
    private static final String MD_REGEXP_PATTERN = "\\[[^\\|]+\\|[^\\[\\|]*\\]";
    private static final String MD_NEXT_LINE = "\n";
    private static final String MD_OPEN_TAG = "[";
    private static final String MD_CLOSE_TAG = "]";
    private static final String MD_PART_DELIMITER_TAG = "|";
    private static final String RTF_TAG_NAME = "a";

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
                    cursor = matcher.start();
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
        if (result.size() > 0) {
            result.setLength(result.size() - MD_NEXT_LINE.length());
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        return null
    }

    private static String convertLinkMdToRtf(String link) {
        Integer delimiterIndex = link.indexOf(MD_PART_DELIMITER_TAG);
        String text = link.substring(1, delimiterIndex);
        String href = link.substring(delimiterIndex + 1, link.length() - 1);
        return createRtfLink(href, text);
    }

    private static String createRtfLink(String href, String text) {
        return "<${RTF_TAG_NAME} href=\"${href}\">${text}</${RTF_TAG_NAME}>"
    }

    private static String createMdLink(String href, String text) {
        return "${MD_OPEN_TAG}${text}${MD_PART_DELIMITER_TAG}${href}${MD_CLOSE_TAG}"
    }
}
