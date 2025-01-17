package ru.itsm365.vita.coder.inline

import ru.itsm365.vita.coder.Coder

class SuperScriptTextStyleCoder implements Coder {
    private static final String RTF_TAG_NAME = "sup";
    private static final String MD_TAG = "^";
    private static final String MD_OPEN_TAG_REGEXP_PATTERN = "\\^(?!\\s)(?!\\^)(?=.)";
    private static final String MD_CLOSE_TAG_REGEXP_PATTERN = "(?=.)(?<!\\s)(?<!\\^)\\^";
    private final Coder coder;

    {
        AllTextStyleCoder.Settings settings = new AllTextStyleCoder.Settings(
                RTF_TAG_NAME,
                MD_TAG,
                MD_OPEN_TAG_REGEXP_PATTERN,
                MD_CLOSE_TAG_REGEXP_PATTERN
        )
        this.coder = new AllTextStyleCoder(settings);
    }

    @Override
    String toRtf(String s) {
        return coder.toRtf(s);
    }

    @Override
    String toMd(String s) {
        return coder.toMd(s);
    }
}
