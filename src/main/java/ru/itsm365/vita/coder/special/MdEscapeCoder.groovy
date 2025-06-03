package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class MdEscapeCoder implements Coder {

    private String str = mapMdToEscapingMd.keySet().toString().replaceAll("[ ,]", "")
    private String REGEX = "(?<=>)[^<>]*${str}(?=[^<>]*<)"
    Pattern toMdPattern = Pattern.compile(REGEX)

    @Override
    String toMd(String s) {

        println(REGEX)
        StringBuilder example = new StringBuilder(s);
        Matcher toMdMather = toMdPattern.matcher(s)
        int shifter = 0;
        while (toMdMather.find()) {
            String line = toMdMather.group()
            int start = toMdMather.start()
            int end = toMdMather.end()
            int counter = end - start
            String result = line.replaceAll(str, { mapMdToEscapingMd.get(it.toString().trim()) ?: it })
            int resultCount = result.size();
            example.replace(start + shifter, end + shifter, result)
            shifter += resultCount - counter
        }
        return example.toString()
    }

    @Override
    String toRtf(String s) {
        return s
                .replaceAll("\\\\${mapEscapingMdToMd.keySet().toString().replaceAll("[ ,]", "")}", { mapEscapingMdToMd.get(it.toString().trim()) ?: it })
    }

    final static Map<String, String> mapMdToEscapingMd = [
            "_": "\\_",
            "|": "\\|",
            "#": "\\#",
            "*": "\\*",
            "-": "\\-",
            "~": "\\~",
            "+": "\\+"
    ]

    final static Map mapEscapingMdToMd = mapMdToEscapingMd.collectEntries { [it.value, it.key] }
}