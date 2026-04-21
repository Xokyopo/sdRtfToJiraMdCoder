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
    String toRtf(String text) {
        if (!text) return text

        // Паттерн с флагом DOTALL (?s), чтобы обрабатывать многострочный текст внутри тегов
        Pattern pattern = Pattern.compile("(?s)\\{color:(#[0-9a-fA-F]{6})}(.*?)\\{color}")
        Matcher matcher = pattern.matcher(text)

        StringBuilder sb = new StringBuilder()

        while (matcher.find()) {
            String hexColor = matcher.group(1)
            String content = matcher.group(2)
            String rgb = fromHexToRgb(hexColor)

            // Формируем замену
            String replacement = "<span style=\"color: ${rgb};\">${content}</span>"

            // appendReplacement экранирует спецсимволы ($ и \),
            // поэтому используем Matcher.quoteReplacement для безопасности содержимого
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement))
        }

        // Добавляем оставшуюся часть текста после последнего совпадения
        matcher.appendTail(sb)

        return sb.toString()
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

    private String fromHexToRgb(String hexCode) {
        String hex = hexCode.startsWith("#") ? hexCode.substring(1) : hexCode
        int r = Integer.parseInt(hex.substring(0, 2), 16)
        int g = Integer.parseInt(hex.substring(2, 4), 16)
        int b = Integer.parseInt(hex.substring(4, 6), 16)
        return "rgb($r, $g, $b)"
    }
}

