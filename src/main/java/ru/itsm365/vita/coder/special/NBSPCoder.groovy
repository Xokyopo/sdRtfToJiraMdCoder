package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

/**
 * Класс призван обрабатывать nbsp при конвертации из rtf и md и обратно.
 * Вынесен отдельно так как является важной составляющей преобразования в отличии от оставшихся спец символов.
 */
class NBSPCoder implements Coder {
    private final String RTF_NBSP = "&nbsp;"
    private final String MD_NBSP = " "

    @Override
    String toRtf(String s) {
        return s.replaceAll(MD_NBSP, RTF_NBSP);
    }

    @Override
    String toMd(String s) {
        return s.replaceAll(RTF_NBSP, MD_NBSP);
    }
}
