package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder
import ru.itsm365.vita.coder.model.Escaper

class MdEscapeCoder implements Coder {

    @Override
    String toMd(String s) {
        return s
                .replaceAll(Escaper.mapMdToEscapingMd.keySet().toString().replaceAll("[ ,]", ""), { Escaper.mapMdToEscapingMd.get(it.toString().trim()) ?: it })
    }

    @Override
    String toRtf(String s) {
        return s
                .replaceAll("\\\\${Escaper.mapEscapingMdToMd.keySet().toString().replaceAll("[ ,]", "")}", { Escaper.mapEscapingMdToMd.get(it.toString().trim()) ?: it })
    }
}