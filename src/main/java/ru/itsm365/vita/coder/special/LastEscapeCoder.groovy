package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder
import ru.itsm365.vita.coder.model.Escaper

/** При конвертации в обоих направления ставим в конец конвеера */
class LastEscapeCoder implements Coder {
    @Override
    String toRtf(String s) {
        return s
                .replaceAll("\\\\${Escaper.mapEscapingMdToMd.keySet().toString().replaceAll("[ ,]", "")}", { Escaper.mapEscapingMdToMd.get(it.toString().trim()) ?: it })
    }

    @Override
    String toMd(String s) {
        return s
                .replaceAll("&nbsp;", " ")
                .replaceAll("&[^ &]+?;", { Escaper.mapHtmlToMd.get(it.toString().trim()) ?: it })
    }
}