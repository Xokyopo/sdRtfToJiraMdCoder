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
    private String REGEX = "(?<=>)[^<>]*[|*+\\-_#~](?=[^<>]*<)"
    Pattern toMdPattern = Pattern.compile(REGEX)

    @Override
    String toMd(String s) {
        StringBuilder example = new StringBuilder(s);
        Matcher toMdMather = toMdPattern.matcher(s)
        int shifter = 0;
        while (toMdMather.find()) {
            String line = toMdMather.group()
            int start = toMdMather.start()
            int end = toMdMather.end()
            int counter = end - start

            String result = line;
            for (def entry in escapedMdByRtfTags) {
                result = result.replaceAll(entry.key, entry.value)
            }

            int resultCount = result.size();
            example.replace(start + shifter, end + shifter, result)
            shifter += resultCount - counter
        }
        return example.toString()
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
