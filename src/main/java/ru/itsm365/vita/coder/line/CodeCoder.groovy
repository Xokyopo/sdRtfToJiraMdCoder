package ru.itsm365.vita.coder.line

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class CodeCoder implements Coder {

    // toRtf
    private final String CODE_START = "{code:java}";
    private final String CODE_END = "{code}";
    private final String CODE_REGEX = "(?msi)(?<=\\{code:java}).+?(?=\\{code})";
    private final String CODE_OPENING_TAG = "<pre fr-original-style=\"\" style=\"white-space: pre-wrap; overflow-wrap: break-word; overflow: visible;\">";
    private final String CODE_CLOSING_TAG = "</pre>";
    Pattern CODE_PATTERN = Pattern.compile(CODE_REGEX);

    // toMd
//    private final String BLOCKQUOTE_OPENING_TAG = "<blockquote";
//    private final String BLOCKQUOTE_CLOSING_END = "</blockquote>";
    private final String PRE_REGEX = "(?msi)<pre.+?>.+?</pre.*?>"
    private final String CHEVRONS_REGEX = "(?msi)(?<=>).*(?=<)"
//    private final String BLOCKQUOTE_TO_QUOTE = "{quote}"
    Pattern PRE_PATTERN = Pattern.compile(PRE_REGEX)
    Pattern CHEVRONS_PATTERN = Pattern.compile(CHEVRONS_REGEX)

    @Override
    String toRtf(String s) {
        Matcher CODE_MATCHER = CODE_PATTERN.matcher(s)
        StringBuilder resultRtf = new StringBuilder(s)
        Integer shift = 0;
        while (CODE_MATCHER.find()) {
            StringBuilder result = new StringBuilder()
            Integer start = CODE_MATCHER.start() - CODE_START.length()
            Integer end = CODE_MATCHER.end() + CODE_END.length()
            result.append(CODE_OPENING_TAG)
            result.append(CODE_MATCHER.group())
            result.append(CODE_CLOSING_TAG)
            resultRtf.replace(start + shift, end + shift, result.toString());
            shift += result.length() - (end - start);
        }
        return resultRtf
    }

    @Override
    String toMd(String s) {
        Matcher PRE_MATCHER = PRE_PATTERN.matcher(s);
        StringBuilder resultMd = new StringBuilder(s)
        Integer shift = 0;
        while (PRE_MATCHER.find()) {
            String str = PRE_MATCHER.group();
            Integer start = PRE_MATCHER.start()
            Integer end = PRE_MATCHER.end()
            Matcher CHEVRONS_MATCHER = CHEVRONS_PATTERN.matcher(str);
            if (CHEVRONS_MATCHER.find()) {
                StringBuilder result = new StringBuilder()
                result.append(CODE_START)
                result.append(CHEVRONS_MATCHER.group())
                result.append(CODE_END)
                resultMd.replace(start + shift, end + shift, result.toString());
                shift += result.length() - (end - start);
            }
        }
        return resultMd
    }
}
