package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder
import ru.itsm365.vita.coder.model.Escaper

/** При конвертации в обоих напрвления ставим в самое начало конвеера */
class FirstEscapeCoder implements Coder {
    @Override
    String toRtf(String s) {
        return s
                .replaceAll(Escaper.mapMdToHtml.keySet().toString().replaceAll("[ ,]", ""), { Escaper.mapMdToHtml.get(it.toString().trim()) ?: it })
    }

    @Override
    String toMd(String s) {
        return s
                .replaceAll(Escaper.mapMdToEscapingMd.keySet().toString().replaceAll("[ ,]", ""), { Escaper.mapMdToEscapingMd.get(it.toString().trim()) ?: it })
    }
}