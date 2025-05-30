package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Класс призван обрабатывать nbsp при конвертации из rtf и md и обратно.
 * временный, пока не закончим полный конвертер.
 * В MD выполняется после XmlPrettifyCoder
 * В RTF выполняется перед XmlPrettifyCoder
 */
class RtfNBSPCoder implements Coder {
    private static final Pattern RTF_BSP_PATTERN = Pattern.compile("(?<=>\\r?\\n?) +| +(?=\\r?\\n?<)");
    private static final String RTF_NOT_BSP = "&nbsp;"
    private static final String RTF_BSP = " "

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        int cursor = 0;
        Matcher matcher = RTF_BSP_PATTERN.matcher(s);
        while (matcher.find(cursor)) {
            if (cursor < matcher.start()) {
                result.append(s.substring(cursor, matcher.start()));
            }
            String spaces = matcher.group()
            result.append(RTF_NOT_BSP.repeat(spaces.length()));
            cursor = matcher.end();
        }
        if (cursor < s.length()) {
            result.append(s.substring(cursor));
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        return s.replaceAll(RTF_NOT_BSP, RTF_BSP);
    }
}
