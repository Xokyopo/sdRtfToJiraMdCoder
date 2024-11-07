package ru.itsm365.vita.coder.line


import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class AllTextStyleCoder implements Coder {
    private static final String MD_NEXT_LINE = "\n";
    private final Settings settings;

    AllTextStyleCoder(Settings settings) {
        this.settings = settings
    }

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> lines = s.split(MD_NEXT_LINE);

        for (String line in lines) {
            Integer cursor = 0;

            Matcher openMatcher = settings.openMDToken.matcher(line);
            Matcher closeMatcher = settings.closeMDToken.matcher(line)

            while (openMatcher.find(cursor)) {
                if (cursor < openMatcher.start()) {
                    result.append(line.substring(cursor, openMatcher.start()));
                    cursor = openMatcher.start();
                }

                if (closeMatcher.find(openMatcher.end())) {
                    result.append(settings.rtfOpenTag);
                    if (closeMatcher.start() > openMatcher.end()) {
                        result.append(line.substring(openMatcher.end(), closeMatcher.start()));
                    }
                    result.append(settings.rtfCloseTag);
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

        Integer cursor = 0;

        Matcher openMatcher = settings.openRTFToken.matcher(s);
        Matcher closeMatcher = settings.closeRTFToken.matcher(s)

        while (openMatcher.find(cursor)) {
            if (cursor < openMatcher.start()) {
                result.append(s.substring(cursor, openMatcher.start()));
                cursor = openMatcher.start();
            }

            if (closeMatcher.find(openMatcher.end())) {
                result.append(settings.mdTag);
                if (closeMatcher.start() > openMatcher.end()) {
                    result.append(s.substring(openMatcher.end(), closeMatcher.start()));
                }
                result.append(settings.mdTag);
                cursor = closeMatcher.end();
            } else {
                break;
            }
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor, s.length()));
        }

        return result.toString();
    }

    protected static class Settings {
        final String rtfOpenTag;
        final String rtfCloseTag;
        final Pattern openRTFToken;
        final Pattern closeRTFToken;
        final String mdTag;
        final Pattern openMDToken;
        final Pattern closeMDToken;

        Settings(
                String rtfTagName,
                String mdTag,
                String mdOpenTagRegexpToken,
                String mdCloseTagRegexpToken
        ) {
            this.rtfOpenTag = "<${rtfTagName}>"
            this.rtfCloseTag = "</${rtfTagName}>"
            this.mdTag = mdTag
            this.openMDToken = Pattern.compile(mdOpenTagRegexpToken);
            this.closeMDToken = Pattern.compile(mdCloseTagRegexpToken);
            this.openRTFToken = Pattern.compile("<${rtfTagName}(?= |>)[^><]*>", Pattern.CASE_INSENSITIVE);
            this.closeRTFToken = Pattern.compile(rtfCloseTag, Pattern.CASE_INSENSITIVE);
        }
    }
}
