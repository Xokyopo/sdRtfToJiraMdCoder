package ru.itsm365.vita.coder.block

import ru.itsm365.vita.coder.Coder

class NumberedListCoder implements Coder {
    private static final String RTF_LIST_TAG_NAME = "ol";
    private static final String RTF_LIST_TAG_REGEXP_PATTERN = "(<(?:ol|li)[^><]*>)|(</(?:ol|li)>)";
    private static final String MD_LIST_TAG = "#";
    private static final String MD_LIST_REGEXP_PATTERN = "h*#\\h[^\$]*";
    private static final String MD_START_LIST_ITEM_REGEXP_PATTERN = "\\h*#\\h";

    private final Coder coder;

    {
        AllListCoder.Setting setting = new AllListCoder.Setting(
                RTF_LIST_TAG_NAME,
                RTF_LIST_TAG_REGEXP_PATTERN,
                MD_LIST_TAG,
                MD_LIST_REGEXP_PATTERN,
                MD_START_LIST_ITEM_REGEXP_PATTERN
        );
        this.coder = new AllListCoder(setting);
    }

    @Override
    String toRtf(String s) {
        return this.coder.toRtf(s);
    }

    @Override
    String toMd(String s) {
        return this.coder.toMd(s);
    }
}
