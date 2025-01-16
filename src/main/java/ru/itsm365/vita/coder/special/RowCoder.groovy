package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

/**
 * Обрабатывает строки, которые в RTF окружаются тегом div, а в MD выражаются переносом строки.
 * Вызывается после основных кодеров, и обрабатывает оставшиеся объекты как строки.
 */
class RowCoder implements Coder {
    private static final String MD_LINE_TAG = "\n";
    private static final String RTF_LINE_TAG = "div";

    @Override
    String toRtf(String s) {
        return null
    }

    @Override
    String toMd(String s) {
        return null
    }
}
