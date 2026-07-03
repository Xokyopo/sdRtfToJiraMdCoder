package ru.itsm365.vita.coder.model

import java.util.regex.Matcher
import java.util.regex.Pattern

class RtfTag {
    private static final String CLOSE_PREFIX = "</";
    private static final String SOLO_POSTFIX = "/>";
    private static final String NAME_TOKEN_STRING = "(?<=<\\/?)[^>\\/\\h]+";
    private static final Pattern NAME_TOKEN = Pattern.compile(NAME_TOKEN_STRING, Pattern.CASE_INSENSITIVE);

    private static final List<String> SOLO_TAGS = [
            "br", "hr", "input", "img"
    ]
    public final String tag;
    public final String name;
    public final boolean open;

    RtfTag(String rtfTag) {
        this.tag = rtfTag.trim().toLowerCase(Locale.ROOT);
        Matcher matcher = NAME_TOKEN.matcher(tag);
        this.name = matcher.find()
                ? matcher.group()
                : tag

        boolean closeTag = tag.startsWith(CLOSE_PREFIX) || tag.endsWith(SOLO_POSTFIX) || SOLO_TAGS.contains(this.name);
        this.open = !closeTag;
    }
}
