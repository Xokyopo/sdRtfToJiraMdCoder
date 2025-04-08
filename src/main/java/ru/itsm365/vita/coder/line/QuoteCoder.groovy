package ru.itsm365.vita.coder.line

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class QuoteCoder implements Coder {

    // toRtf
    private final String QUOTE = "\\{quote}";
    private final String QUOTE_REGEX = "(?msi)(?<=\\{quote}).+?(?=\\{quote})";
    private final String QUOTE_OPENING_TAG = "<blockquote fr-original-style=\"\" style=\"border-color: rgb(153, 153, 153); border-left-width: 2px; border-left-style: solid; margin-left: 0px; padding-left: 5px; color: rgb(153, 153, 153);\">";
    private final String QUOTE_CLOSING_TAG = "</blockquote>";
    Pattern QUOTE_PATTERN = Pattern.compile(QUOTE_REGEX);

    // toMd
    private final String BLOCKQUOTE_REGEX = "(?msi)<blockquote[^>]*>.+?</blockquote>"
    private final String CHEVRONS_REGEX = "(?msi)(?<=>).*(?=<)"
    private final String BLOCKQUOTE_TO_QUOTE = "{quote}"
    Pattern BLOCKQUOTE_PATTERN = Pattern.compile(BLOCKQUOTE_REGEX)
    Pattern CHEVRONS_PATTERN = Pattern.compile(CHEVRONS_REGEX)

    @Override
    String toRtf(String s) {
        Matcher matcher = QUOTE_PATTERN.matcher(s)
        StringBuilder resultRtf = new StringBuilder(s)
        Integer shift = 0;
        Integer counter = Pattern.compile(QUOTE).matcher(s).count
        Integer oddStep = 0;
        while (counter % 2 == 0 && matcher.find()) {
            if (oddStep % 2 == 0) {
                StringBuilder result = new StringBuilder()
                Integer start = matcher.start() - QUOTE.length() + 1
                Integer end = matcher.end() + QUOTE.length() - 1
                result.append(QUOTE_OPENING_TAG)
                result.append(matcher.group())
                result.append(QUOTE_CLOSING_TAG)
                resultRtf.replace(start + shift, end + shift, result.toString());
                shift += result.length() - (end - start);
            }
            oddStep++;
        }
        return resultRtf
    }

    @Override
    String toMd(String s) {
        Matcher BLOCKQUOTE_MATCHER = BLOCKQUOTE_PATTERN.matcher(s);
        StringBuilder resultMd = new StringBuilder(s)
        Integer shift = 0;
        while (BLOCKQUOTE_MATCHER.find()) {
            String str = BLOCKQUOTE_MATCHER.group();
            Integer start = BLOCKQUOTE_MATCHER.start()
            Integer end = BLOCKQUOTE_MATCHER.end()
            Matcher CHEVRONS_MATCHER = CHEVRONS_PATTERN.matcher(str);
            if (CHEVRONS_MATCHER.find()) {
                StringBuilder result = new StringBuilder()
                result.append(BLOCKQUOTE_TO_QUOTE)
                result.append(CHEVRONS_MATCHER.group())
                result.append(BLOCKQUOTE_TO_QUOTE)
                resultMd.replace(start + shift, end + shift, result.toString());
                shift += result.length() - (end - start);
            }
        }
        return resultMd
    }
}
