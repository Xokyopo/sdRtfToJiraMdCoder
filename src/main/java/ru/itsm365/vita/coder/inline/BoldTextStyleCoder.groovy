package ru.itsm365.vita.coder.inline

import ru.itsm365.vita.coder.Coder

class BoldTextStyleCoder implements Coder {
    private static final String RTF_TAG_NAME = "strong";
    private static final String MD_TAG = "*";
    private final Coder coder;

    {
        AllTextStyleCoder.Settings settings = new AllTextStyleCoder.Settings(RTF_TAG_NAME, MD_TAG)
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
