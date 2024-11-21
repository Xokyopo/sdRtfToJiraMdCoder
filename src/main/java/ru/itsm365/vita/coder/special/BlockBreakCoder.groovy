package ru.itsm365.vita.coder.special

import ru.itsm365.vita.coder.Coder

/**
 * В обоих случая работает только с RTF тестом
 * в методе toRtf должен заменить все переносы строк на специфические для rtf элементы, или удалить их.
 * в методе toMd должен добавить все переносы необходимые дял корректной работы в MD.
 */
class BlockBreakCoder implements Coder {

    @Override
    String toRtf(String s) {
        return null
    }

    @Override
    String toMd(String s) {
        return null
    }

}
