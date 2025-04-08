package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

class MdEscapeCoder implements Coder {

    @Override
    String toMd(String s) {
        return s
                .replaceAll(mapMdToEscapingMd.keySet().toString().replaceAll("[ ,]", ""), { mapMdToEscapingMd.get(it.toString().trim()) ?: it })
    }

    @Override
    String toRtf(String s) {
        return s
                .replaceAll("\\\\${mapEscapingMdToMd.keySet().toString().replaceAll("[ ,]", "")}", { mapEscapingMdToMd.get(it.toString().trim()) ?: it })
    }

    final static Map<String, String> mapMdToEscapingMd = [
            "_": "\\_",
            "|": "\\|",
            "#": "\\#",
            "*": "\\*",
            "-": "\\-",
            "~": "\\~",
            "+": "\\+"
    ]

    final static Map mapEscapingMdToMd = mapMdToEscapingMd.collectEntries { [it.value, it.key] }
}