package ru.itsm365.vita.coder.line

import ru.itsm365.vita.coder.Coder
import ru.itsm365.vita.coder.VoidCoder
import ru.itsm365.vita.coder.model.RtfTag

import java.util.regex.Matcher
import java.util.regex.Pattern

class QuoteCoder implements Coder {
    private static final Pattern JIRA_QUOTE_PATTERN = Pattern.compile("(?s)\\{quote\\}(.*?)\\{quote\\}")
    private static final Pattern ANY_TAG_PATTERN = Pattern.compile("(?s)<[^>]+>")
    public static final String BLOCKQUOTE_STYLE = 'border-color: rgb(153, 153, 153); border-left-width: 2px; border-left-style: solid; margin-left: 0px; padding-left: 5px; color: rgb(153, 153, 153);'

    private Coder inlineTexCoder = new VoidCoder()

    @Override
    String toRtf(String text) {
        if (text == null || text.isEmpty()) return text

        Matcher matcher = JIRA_QUOTE_PATTERN.matcher(text)
        StringBuilder sb = new StringBuilder()

        while (matcher.find()) {
            String content = matcher.group(1)
            // Рекурсивно обрабатываем инлайн-элементы внутри
            String processedContent = this.inlineTexCoder.toRtf(content)

            String replacement = "<blockquote style=\"${BLOCKQUOTE_STYLE}\">${processedContent}</blockquote>"
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement))
        }
        matcher.appendTail(sb)
        return sb.toString()
    }

    @Override
    String toMd(String text) {
        if (!text) return text

        // Паттерн для поиска любых HTML тегов
        Pattern tagPattern = ANY_TAG_PATTERN
        StringBuilder sb = new StringBuilder()
        int cursor = 0

        while (cursor < text.length()) {
            // Ищем начало потенциальной цитаты
            int startIdx = text.indexOf("<blockquote", cursor)
            if (startIdx == -1) break

            // Добавляем текст, который был до цитаты
            sb.append(text[cursor..<startIdx])

            // Ищем парный закрывающий тег с помощью RtfTag и стека
            int endIdx = -1
            int depth = 0
            int contentStart = -1

            Matcher matcher = tagPattern.matcher(text)
            matcher.region(startIdx, text.length())

            while (matcher.find()) {
                RtfTag rtfTag = new RtfTag(matcher.group())

                if (rtfTag.name == "blockquote") {
                    if (rtfTag.open) {
                        if (depth == 0) contentStart = matcher.end()
                        depth++
                    } else {
                        depth--
                    }
                }

                if (depth == 0 && contentStart != -1) {
                    endIdx = matcher.start()
                    break
                }
            }

            if (endIdx != -1) {
                String content = text.substring(contentStart, endIdx)
                sb.append("{quote}")
                sb.append(inlineTexCoder.toMd(content))
                sb.append("{quote}")
                cursor = endIdx + "</blockquote>".length()
            } else {
                // Если пара не нашлась, записываем символ и идем дальше (защита от битых тегов)
                sb.append(text[cursor])
                cursor++
            }
        }

        // Добавляем остаток текста
        if (cursor < text.length()) sb.append(text[cursor..-1])

        return sb.toString()
    }

    void setInlineTexCoder(Coder inlineTexCoder) {
        this.inlineTexCoder = inlineTexCoder
    }
}
