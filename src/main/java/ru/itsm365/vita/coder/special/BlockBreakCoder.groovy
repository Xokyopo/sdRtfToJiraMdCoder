package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

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
    private static final Pattern EMPTY_LINES_TOKEN = Pattern.compile("(?<=/(h\\d|ul|li|table)>)\\s+", Pattern.CASE_INSENSITIVE);

    @Override
    String toRtf(String s) {
        return s.replaceAll(EMPTY_LINES_TOKEN, "");
    }

    @Override
    String toMd(String s) {
        return null
    }

}
