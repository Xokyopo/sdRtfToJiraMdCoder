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

            Closure<Boolean> isCloseToken = { it > 0 && line[it - 1] != " " && line[it - 1] != "_" };
            Closure<Boolean> isOpenToken = { it < line.length() && line[it + 1] != " " && line[it + 1] != "_" };
            for (int cursor = line.indexOf(MD_TOKEN, lineCursor); cursor >= 0; cursor = line.indexOf(MD_TOKEN, cursor + 1)) {
                if (open && cursor > lineCursor + 2 && isCloseToken(cursor)) {
                    cursor += MD_TOKEN.length();
                    open = false;
                    result.append(RTF_OPEN_TAG);
                    result.append(line.substring(lineCursor + MD_TOKEN.length(), cursor - MD_TOKEN.length()));
                    result.append(RTF_CLOSE_TAG);
                    lineCursor = cursor;
                } else if (!open && isOpenToken(cursor)) {
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
