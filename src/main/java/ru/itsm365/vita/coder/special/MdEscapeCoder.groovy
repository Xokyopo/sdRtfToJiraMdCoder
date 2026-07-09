package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class MdEscapeCoder implements Coder {
    final Map<String, String> rtfByEscapingMdTags = [
            "\\\\\\|": "|",
            "\\\\\\+": "+",
            "\\\\\\*": "*",
            "\\\\_"  : "_",
            "\\\\#"  : "#",
            "\\\\-"  : "-",
            "\\\\~"  : "~"
    ]
    final Map<String, String> escapedMdByRtfTags = [
            "\\|": "\\\\|",
            "\\*": "\\\\*",
            "\\+": "\\\\+",
            "_"  : "\\\\_",
            "#"  : "\\\\#",
            "-"  : "\\\\-",
            "~"  : "\\\\~",

    ]
    private String TEG_TOKEN = "<[^>]*>"
    private Pattern RTF_TAG_PATTERN = Pattern.compile(TEG_TOKEN)

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();
        Matcher toMdMather = RTF_TAG_PATTERN.matcher(s)
        int cursor = 0;
        while (toMdMather.find(cursor)) {
            if (cursor < toMdMather.start()) {
                String current = s.substring(cursor, toMdMather.start());
                for (def entry in escapedMdByRtfTags) {
                    current = current.replaceAll(entry.key, entry.value)
                }
                result.append(current);
            }

            result.append(s.substring(toMdMather.start(), toMdMather.end()))
            cursor = toMdMather.end()
        }

        if (cursor < s.length()) {
            String current = s.substring(cursor);
            for (def entry in escapedMdByRtfTags) {
                current = current.replaceAll(entry.key, entry.value)
            }
            result.append(current);
        }

        return result.toString()
    }

    @Override
    String toRtf(String s) {
        String result = s;
        println(result)
        println()
        for (def entry in rtfByEscapingMdTags) {
            result = result.replaceAll(entry.key, entry.value)
        }
        return result;
    }
}
