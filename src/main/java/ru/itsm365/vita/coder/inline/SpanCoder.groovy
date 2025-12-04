package ru.itsm365.vita.coder.inline

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class SpanCoder implements Coder {
    private static final String VOID = "";
    private static final String RTF_SPAN_TAGS_REGEXP_TOKEN = "<span(?:\\s[^><]*)?>|</span>";
    private static final String RTF_SPAN_TAGS_REGEX_FULL_SPAN = "<span(?:\\s[^><]*)?>.+?</span>";
    private static final String RTF_SPAN_TAGS_REGEXP_FIRST_SPAN = "<span(?:\\s[^><]*)?>";
    private static final String RTF_SPAN_TAGS_REGEX_COLOR = "style=\"color: rgb\\(\\d{1,3}, \\d{1,3}, \\d{1,3}\\)";
    private static final String RTF_SPAN_TAGS_REGEX_DIGITS = "\\d{1,3}";
    private static final String RTF_SPAN_TAGS_REGEX = "(?<=>)\\s*[^<]+(?=\\s*<\\/span>)";

    @Override
    String toRtf(String s) {
        return s;
    }

    @Override
    String toMd(String s) {

        StringBuilder a = new StringBuilder(s)
        Pattern patternFullSpan = Pattern.compile(RTF_SPAN_TAGS_REGEX_FULL_SPAN)
        Matcher matcherFullSpan = patternFullSpan.matcher(s)
        int shift = 0
        while (matcherFullSpan.find()) {
            String textFullSpan = matcherFullSpan.group()
            Integer start = matcherFullSpan.start()
            Integer end = matcherFullSpan.end()
            Pattern patternFirsSpan = Pattern.compile(RTF_SPAN_TAGS_REGEXP_FIRST_SPAN)
            Matcher matcherFirstSpan = patternFirsSpan.matcher(textFullSpan)
            if (matcherFirstSpan.find()) {
                String textFirstSpan = matcherFirstSpan.group()
                Pattern patternColor = Pattern.compile(RTF_SPAN_TAGS_REGEX_COLOR)
                Matcher matcherColor = patternColor.matcher(textFirstSpan)
                if (matcherColor.find()) {
                    String textColor = matcherFirstSpan.group()
                    Pattern patternDigits = Pattern.compile(RTF_SPAN_TAGS_REGEX_DIGITS)
                    Matcher matcherDigits = patternDigits.matcher(textColor)
                    List<Integer> list = new ArrayList<>()
                    while (matcherDigits.find()) {
                        list.add(matcherDigits.group() as Integer)
                    }
                    def result = getColor(list, textFullSpan)
                    a = a.replace(start - shift, end - shift, result);
                    shift += (end - start) - result.size()
                } else {
                    Pattern pattern = Pattern.compile(RTF_SPAN_TAGS_REGEX)
                    Matcher matcher = pattern.matcher(textFullSpan)
                    matcher.find()
                    String result = matcher.group()
                    a = a.replace(start - shift, end - shift, result);
                    shift += (end - start) - result.size()
                }
            }
        }
        return a
    }

    private static String getColor(List<Integer> list, String text) {
        String hexR = String.format("%s%s", list.get(0) < 16 ? "0" : "", Integer.toHexString(list.get(0)));
        String hexG = String.format("%s%s", list.get(1) < 16 ? "0" : "", Integer.toHexString(list.get(1)));
        String hexB = String.format("%s%s", list.get(2) < 16 ? "0" : "", Integer.toHexString(list.get(2)));
        return "{color:#${hexR}${hexG}${hexB}}${text.replaceAll(RTF_SPAN_TAGS_REGEXP_TOKEN, VOID)}{color}"
    }
}

