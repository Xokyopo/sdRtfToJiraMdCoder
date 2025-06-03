package ru.itsm365.vita.coder.inline


import ru.itsm365.vita.coder.Coder

class SpanCoder implements Coder {
    private static final String VOID = "";
    private static final String RTF_SPAN_TAGS_REGEXP_TOKEN = "<span(?:\\s[^><]*)?>|</span>";

    @Override
    String toRtf(String s) {
        return s;
    }

    @Override
    String toMd(String s) {
        return s.replaceAll(RTF_SPAN_TAGS_REGEXP_TOKEN, VOID);
    }
}

