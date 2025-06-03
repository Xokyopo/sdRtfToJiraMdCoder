package ru.itsm365.vita.coder

/**
 * Пустой кодер, который ничего не меняет, нужен как затычка.
 */
class VoidCoder implements Coder {
    @Override
    String toRtf(String s) {
        return s;
    }

    @Override
    String toMd(String s) {
        return s;
    }
}
