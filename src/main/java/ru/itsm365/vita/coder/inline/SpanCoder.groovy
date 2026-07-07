package ru.itsm365.vita.coder.inline

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class SpanCoder implements Coder {

    // Предварительно скомпилированные паттерны для ускорения обработки
    private final Pattern JIRA_COLOR_TAG_PATTERN = Pattern.compile("\\{color:(#[0-9a-fA-F]{6})\\}|\\{color\\}")
    private final Pattern RTF_COLOR_SPAN_PATTERN = Pattern.compile("(?s)<span[^>]*?style=[\\\"'][^>]*?color:\\s*(rgb\\(\\d+,\\s*\\d+,\\s*\\d+\\));?[^>]*?[\\\"'][^>]*?>(.*?)<\\/span>")
    private final Pattern RTF_CLEAN_SPAN_PATTERN = Pattern.compile("(?s)<span[^>]*?>(.*?)<\\/span>")

    @Override
    String toRtf(String text) {
        if (text == null || text.isEmpty()) return text

        StringBuilder result = new StringBuilder()
        Deque<String> openTags = new ArrayDeque<>()
        Matcher matcher = JIRA_COLOR_TAG_PATTERN.matcher(text)
        Integer cursor = 0

        while (matcher.find(cursor)) {
            if (cursor < matcher.start()) {
                result.append(text.substring(cursor, matcher.start()))
            }

            String hexColor = matcher.group(1)
            if (hexColor != null) {
                result.append('<span style="color: ').append(fromHexToRgb(hexColor)).append(';">')
                openTags.addLast(hexColor)
            } else {
                result.append("</span>")
                if (!openTags.isEmpty()) {
                    openTags.removeLast()
                }
            }

            cursor = matcher.end()
        }

        if (cursor < text.length()) {
            result.append(text.substring(cursor))
        }

        while (!openTags.isEmpty()) {
            result.append("</span>")
            openTags.removeLast()
        }

        return result.toString()
    }

    @Override
    String toMd(String text) {
        if (text == null || text.isEmpty()) return text

        String currentText = text

        // 1. Конвертируем span с цветом
        while (RTF_COLOR_SPAN_PATTERN.matcher(currentText).find()) {
            Matcher matcher = RTF_COLOR_SPAN_PATTERN.matcher(currentText)
            StringBuilder sb = new StringBuilder()
            while (matcher.find()) {
                String rgbValue = matcher.group(1)
                String content = matcher.group(2)
                String hexColor = fromRgbToHex(rgbValue)

                String replacement = "{color:${hexColor}}${content}{color}"
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement))
            }
            matcher.appendTail(sb)
            currentText = sb.toString()
        }

        // 2. Удаляем все оставшиеся span (без цвета)
        while (RTF_CLEAN_SPAN_PATTERN.matcher(currentText).find()) {
            Matcher matcher = RTF_CLEAN_SPAN_PATTERN.matcher(currentText)
            StringBuilder sb = new StringBuilder()
            while (matcher.find()) {
                String content = matcher.group(1)
                matcher.appendReplacement(sb, Matcher.quoteReplacement(content))
            }
            matcher.appendTail(sb)
            currentText = sb.toString()
        }

        return currentText
    }

    private String fromRgbToHex(String rgb) {
        // Находим все числа в строке rgb(r, g, b)
        def values = rgb.findAll(/\d+/)*.toInteger()
        int r = values[0]
        int g = values[1]
        int b = values[2]
        // Форматируем их в hex строку:
        // % - начало спецификатора
        // 02 - дополнить нулем до 2 символов
        // x - шестнадцатеричный формат
        return "#" + sprintf('%02x%02x%02x', r, g, b)
    }

    private String fromHexToRgb(String hexCode) {
        String hex = hexCode.startsWith("#") ? hexCode.substring(1) : hexCode
        int r = Integer.parseInt(hex.substring(0, 2), 16)
        int g = Integer.parseInt(hex.substring(2, 4), 16)
        int b = Integer.parseInt(hex.substring(4, 6), 16)
        return "rgb($r, $g, $b)"
    }

}

