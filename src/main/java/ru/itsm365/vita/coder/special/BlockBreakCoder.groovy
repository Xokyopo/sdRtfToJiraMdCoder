package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * В обоих случая работает только с RTF тестом
 * в методе toRtf должен заменить все переносы строк на специфические для rtf элементы, или удалить их.
 * в методе toMd должен добавить все переносы необходимые дял корректной работы в MD.
 */
class BlockBreakCoder implements Coder {
    private static final MD_EMPTY_LINE = "\n\n";
    private static final List<String> BLOCK_TAG_NAME = [
            "ou", "ul", "table", "h1", "h2", "h3", "h4"
    ]
    private static final Pattern EMPTY_LINES_TOKEN = Pattern.compile("(?<=/(h\\d|ul|ol|table)>)\\s+", Pattern.CASE_INSENSITIVE);
    private static final Pattern ANY_BLOCK_TAG_PATTERN = Pattern.compile("</?(?:h\\d|ul|ol|table)(?:>| [^><]*>)", Pattern.CASE_INSENSITIVE);
    private static final Pattern TAG_NAME_TOKEN = Pattern.compile("(?<=</?)h\\d|ul|ol|table(?=>| )", Pattern.CASE_INSENSITIVE);
    private static final String END_BLOCK_TAG_PREFIX = "</";

    @Override
    String toRtf(String s) {
        return s.replaceAll(EMPTY_LINES_TOKEN, "");
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();
        Stack openBlocks = [];
        Matcher matcher = ANY_BLOCK_TAG_PATTERN.matcher(s);
        int cursor = 0;

        while (matcher.find()) {
            String tag = matcher.group();
            String tagName = getTagName(tag);

            if (isOpenTag(tag)) {
                openBlocks.push(tagName);
            } else if (openBlocks.size() > 0 && openBlocks.last() == tagName) {
                openBlocks.pop();
                if (openBlocks.isEmpty()) {
                    result.append(s.substring(cursor, matcher.end()));
                    if (matcher.end() < s.length()) {
                        result.append(MD_EMPTY_LINE);
                    }
                    cursor = matcher.end();
                }
            }
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor));
        }

        return result.toString();
    }

    private static boolean isOpenTag(String tag) {
        return !tag.startsWith(END_BLOCK_TAG_PREFIX);
    }

    private static String getTagName(String tag) {
        Matcher matcher = TAG_NAME_TOKEN.matcher(tag)
        if (matcher.find()) {
            return matcher.group();
        }
        return tag;
    }

}
