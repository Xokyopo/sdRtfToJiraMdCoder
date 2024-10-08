package ru.itsm365.vita.coder.line


import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class ItalicTextCoder implements Coder {
    private static final String MD_TOKEN = "_";
    private static final String MD_OPEN_TAG_REGEXP_PATTERN = "_(?!\\s)(?!_)(?=.)";
    private static final String MD_CLOSE_TAG_REGEXP_PATTERN = "(?=.)(?<!\\s)(?<!_)_";
    private static final String MD_NEXT_LINE = "\n";
    private static final String RTF_OPEN_TAG = "<em>";
    private static final String RTF_CLOSE_TAG = "</em>";

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> lines = s.split(MD_NEXT_LINE);

        Pattern openMDToken = Pattern.compile(MD_OPEN_TAG_REGEXP_PATTERN);
        Pattern closeMDToken = Pattern.compile(MD_CLOSE_TAG_REGEXP_PATTERN);

        for (String line in lines) {
            Integer cursor = 0;

            Matcher openMatcher = openMDToken.matcher(line);
            Matcher closeMatcher = closeMDToken.matcher(line)

            while (openMatcher.find(cursor)) {
                if (cursor < openMatcher.start()) {
                    result.append(line.substring(cursor, openMatcher.start()));
                    cursor = openMatcher.start();
                }

                if (closeMatcher.find(openMatcher.end())) {
                    result.append(RTF_OPEN_TAG);
                    if (closeMatcher.start() > openMatcher.end()) {
                        result.append(line.substring(openMatcher.end(), closeMatcher.start()));
                    }
                    result.append(RTF_CLOSE_TAG);
                    cursor = closeMatcher.end();
                } else {
                    break;
                }
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
        String lowerCaseInputText = s.toLowerCase();

        Integer cursor = 0;
        Boolean open = false;
        int tagCursor = lowerCaseInputText.indexOf(RTF_OPEN_TAG, cursor);
        while (tagCursor >= 0) {
            if (open) {
                result.append(MD_TOKEN);
                if (cursor < tagCursor) {
                    result.append(s.substring(cursor + RTF_OPEN_TAG.length(), tagCursor));
                }
                result.append(MD_TOKEN);
                cursor = tagCursor + RTF_CLOSE_TAG.length();
                tagCursor = lowerCaseInputText.indexOf(RTF_OPEN_TAG, cursor);
                open = false;
            } else {
                if (cursor < tagCursor) {
                    result.append(s.substring(cursor, tagCursor));
                }
                cursor = tagCursor;
                tagCursor = lowerCaseInputText.indexOf(RTF_CLOSE_TAG, cursor);
                open = true
            }
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor, s.length()));
        }

        return result.toString();
    }
}
