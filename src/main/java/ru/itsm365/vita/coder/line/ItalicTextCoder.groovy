package ru.itsm365.vita.coder.line

import ru.itsm365.vita.coder.Coder

class ItalicTextCoder implements Coder {
    private static final String MD_TOKEN = "_";
    private static final String MD_NEXT_LINE = "\n";
    private static final String RTF_OPEN_TAG = "<em>";
    private static final String RTF_CLOSE_TAG = "</em>";

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> lines = s.split(MD_NEXT_LINE);

        for (String line in lines) {
            Integer lineCursor = 0;
            boolean open = false;

            Closure<Boolean> isCloseTag = { it > 0 && line[it - 1] != " " && line[it - 1] != "_" };
            Closure<Boolean> isOpen = { it < line.length() && line[it + 1] != " " && line[it + 1] != "_" };
            for (int cursor = line.indexOf(MD_TOKEN, lineCursor); cursor >= 0; cursor = line.indexOf(MD_TOKEN, cursor + 1)) {
                if (open && cursor > lineCursor + 2 && isCloseTag(cursor)) {
                    cursor += MD_TOKEN.length();
                    open = false;
                    result.append(RTF_OPEN_TAG);
                    result.append(line.substring(lineCursor + MD_TOKEN.length(), cursor - MD_TOKEN.length()));
                    result.append(RTF_CLOSE_TAG);
                    lineCursor = cursor;
                } else if (!open && isOpen(cursor)) {
                    open = true;
                    if (lineCursor < cursor) {
                        result.append(line.substring(lineCursor, cursor));
                        lineCursor = cursor;
                    }
                }
            }

            if (lineCursor < line.length()) {
                result.append(line.substring(lineCursor));
            }
            result.append(MD_NEXT_LINE);
        }

        //Исправляем перевод строки в последней.
        if (result.size() > 0) {
            result.setLength(result.size() - 1);
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
