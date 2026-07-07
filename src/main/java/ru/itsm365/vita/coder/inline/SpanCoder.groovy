package ru.itsm365.vita.coder.inline

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class SpanCoder implements Coder {
    // Предварительно скомпилированные паттерны для ускорения обработки
    private final Pattern JIRA_COLOR_PATTERN = Pattern.compile("(?s)\\{color:(#[0-9a-fA-F]{6})\\}(.*?)\\{color\\}")
    private final Pattern RTF_COLOR_SPAN_PATTERN = Pattern.compile("(?s)<span[^>]*?style=[\\\"'][^>]*?color:\\s*(rgb\\(\\d+,\\s*\\d+,\\s*\\d+\\));?[^>]*?[\\\"'][^>]*?>(.*?)<\\/span>")
    private final Pattern RTF_CLEAN_SPAN_PATTERN = Pattern.compile("(?s)<span[^>]*?>(.*?)<\\/span>")

    @Override
    String toRtf(String text) {
        if (text == null || text.isEmpty()) return text

        final String openTag = "{color:"
        final String closeTag = "{color}"
        final int openTagLength = openTag.length()
        final int closeTagLength = closeTag.length()

        Closure<Integer> findColorClose = { String source, int from ->
            int depth = 0
            int index = from

            while (index <= source.length() - closeTagLength) {
                int nextOpen = source.indexOf(openTag, index)
                int nextClose = source.indexOf(closeTag, index)
                if (nextClose < 0) {
                    return -1
                }

                boolean hasNestedOpen = false
                if (nextOpen >= 0 && nextOpen < nextClose) {
                    int braceEnd = source.indexOf('}', nextOpen)
                    if (braceEnd > nextOpen && braceEnd < nextClose) {
                        String hexColor = source.substring(nextOpen + openTagLength, braceEnd)
                        hasNestedOpen = hexColor ==~ /#[0-9a-fA-F]{6}/
                    }
                }

                if (hasNestedOpen) {
                    depth++
                    index = source.indexOf('}', nextOpen) + 1
                } else {
                    if (depth == 0) {
                        return nextClose
                    }
                    depth--
                    index = nextClose + closeTagLength
                }
            }

            return -1
        }

        StringBuilder result = new StringBuilder()
        Integer cursor = 0

        while (cursor < text.length()) {
            int openIndex = text.indexOf(openTag, cursor)
            if (openIndex < 0) {
                result.append(text.substring(cursor))
                break
            }

            result.append(text.substring(cursor, openIndex))

            int hexStart = openIndex + openTagLength
            int hexEnd = text.indexOf('}', hexStart)
            if (hexEnd < 0) {
                result.append(text.substring(openIndex))
                break
            }

            String hexColor = text.substring(hexStart, hexEnd)
            if (!(hexColor ==~ /#[0-9a-fA-F]{6}/)) {
                result.append(text.substring(openIndex, hexEnd + 1))
                cursor = hexEnd + 1
                continue
            }

            int contentStart = hexEnd + 1
            int closeIndex = findColorClose(text, contentStart)
            if (closeIndex < 0) {
                result.append(text.substring(openIndex))
                break
            }

            result.append("<span style=\"color: ${fromHexToRgb(hexColor)};\">")
            result.append(toRtf(text.substring(contentStart, closeIndex)))
            result.append("</span>")

            cursor = closeIndex + closeTagLength
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

