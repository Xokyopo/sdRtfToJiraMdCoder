package ru.itsm365.vita.coder.line

import ru.itsm365.vita.coder.Coder

class ItalicTextCoder implements Coder {
    private static final String MD_TOKEN = "*";
    private static final String MD_EMPTY_LINE_TOKEN = "\n\n";
    private static final String RTF_OPEN_TAG = "<i>";
    private static final String RTF_CLOSE_TAG = "</i>";

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        StringBuilder current = new StringBuilder(s);

        Integer resultCursor = 0;
        Integer startCursor = current.indexOf(MD_TOKEN);
        Integer endCursor = current.indexOf(MD_TOKEN, startCursor + MD_TOKEN.length());

        if (startCursor > 0) {
            result.append(current.substring(resultCursor, startCursor));
            resultCursor = startCursor;
        }

        while (startCursor >= 0 && endCursor > startCursor) {
            String targetString = current.substring(startCursor, endCursor);
            if (targetString.contains(MD_EMPTY_LINE_TOKEN)) {
                result.append(targetString);
            } else {
                targetString = targetString.replaceAll("\n", "");
                result.append(RTF_OPEN_TAG);
                result.append(targetString.substring(1, targetString.length()));
                result.append(RTF_CLOSE_TAG);
            }

            resultCursor = endCursor;
            startCursor = current.indexOf(MD_TOKEN, endCursor + MD_TOKEN.length());
            endCursor = current.indexOf(MD_TOKEN, startCursor + MD_TOKEN.length());
        }

        if (resultCursor + 1 < current.length()) {
            result.append(current.substring(resultCursor + 1));
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder(s);
        StringBuilder current = new StringBuilder(s.toLowerCase());

        Map<Integer, String> matches = [:];
        String searchingTag = RTF_OPEN_TAG;
        Integer cursor = current.indexOf(RTF_OPEN_TAG);

        while (cursor >= 0) {
            matches.put(cursor, searchingTag);
            String nextTag = getNextTag(searchingTag);
            cursor = current.indexOf(nextTag, cursor + searchingTag.length());
            searchingTag = nextTag;
        }

        matches.reverseEach { result.replace(it.key, it.key + it.value.length(), MD_TOKEN) };

        return result.toString();
    }

    private static String getNextTag(String tag) {
        return RTF_OPEN_TAG == tag
                ? RTF_CLOSE_TAG
                : RTF_OPEN_TAG;
    }
}
