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

        Pattern openMDToken = Pattern.compile(settings.mdOpenTagRegexpPattern);
        Pattern closeMDToken = Pattern.compile(settings.mdCloseTagRegexpPattern);

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
        String lowerCaseInputText = s.toLowerCase();

        Integer cursor = 0;
        Boolean open = false;
        int tagCursor = lowerCaseInputText.indexOf(settings.rtfOpenTag, cursor);
        while (tagCursor >= 0) {
            if (open) {
                result.append(settings.mdTag);
                if (cursor < tagCursor) {
                    result.append(s.substring(cursor + settings.rtfOpenTag.length(), tagCursor));
                }
                result.append(settings.mdTag);
                cursor = tagCursor + settings.rtfCloseTag.length();
                tagCursor = lowerCaseInputText.indexOf(settings.rtfOpenTag, cursor);
                open = false;
            } else {
                if (cursor < tagCursor) {
                    result.append(s.substring(cursor, tagCursor));
                }
                cursor = tagCursor;
                tagCursor = lowerCaseInputText.indexOf(settings.rtfCloseTag, cursor);
                open = true
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
        final String mdTag;
        final String mdOpenTagRegexpPattern;
        final String mdCloseTagRegexpPattern;

        Settings(
                String rtfTagName,
                String mdTag,
                String mdOpenTagRegexpPattern,
                String mdCloseTagRegexpPattern
        ) {
            this.rtfOpenTag = "<${rtfTagName}>"
            this.rtfCloseTag = "</${rtfTagName}>"
            this.mdTag = mdTag
            this.mdOpenTagRegexpPattern = mdOpenTagRegexpPattern
            this.mdCloseTagRegexpPattern = mdCloseTagRegexpPattern
        }
    }
}
