package ru.itsm365.vita.coder.block

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class NumberedListCoder implements Coder {
    private static final String MD_TOKEN = "*";
    private static final String MD_FULL_LIST_REGEXP_PATTERN = "(?!\\n)^\\s*\\d\\.\\s[^\$]*";
    private static final String MD_EMPTY_LINE = "\n\n";
    private static final String MD_NEXT_LINE = "\n";
    private static final String RTF_FULL_LIST_REGEXP_PATTERN = "<ol>(?:.|\\n)*</ol>";
    private static final String RTF_OPEN_LIST_TAG = "<ol>";
    private static final String RTF_CLOSE_LIST_TAG = "</ol>";
    private static final String RTF_OPEN_ITEM_TAG = "<li>";
    private static final String RTF_CLOSE_ITEM_TAG = "</li>";

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> blocks = s.split(MD_EMPTY_LINE);

        for (String block in blocks) {
            String list = block.find(MD_FULL_LIST_REGEXP_PATTERN);

            if (list) {
                Integer startAt = block.length() - list.length();
                if (startAt > 0) {
                    result.append(block.substring(0, startAt));
                }
                result.append(convertListToRtf(block.substring(startAt, block.length())));
                result.append(MD_NEXT_LINE);
            } else {
                result.append(block);
                result.append(MD_EMPTY_LINE);
            }
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();

        Integer cursor = 0;
        Pattern pattern = Pattern.compile(RTF_FULL_LIST_REGEXP_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        while (matcher.find()) {
            if (cursor < matcher.start()) {
                result.append(s.substring(cursor, matcher.start()));
            }

            result.append(MD_NEXT_LINE);
            result.append(convertListToMD(s.substring(matcher.start(), matcher.end())));
            result.append(MD_EMPTY_LINE);
            cursor = matcher.end();
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor, s.length()));
        }

        return result.toString();
    }

    private String convertListToRtf(String s) {
        //TODO алгоритм преобразования списка.
        return "rtf ${s} rtf";
    }

    private String convertListToMD(String s) {
        //TODO алгоритм преобразования списка.
        return "md ${s} md";
    }

    private class RootElement {
        protected List<Element> childs;

        void setList(List<Element> list) {
            this.childs = list
        }

        String toRtf() {
            StringBuilder result = new StringBuilder();

            if (this.childs) {
                result.append(RTF_OPEN_LIST_TAG)
                this.childs.forEach { result.append(it.toRtf()) }
                result.append(RTF_CLOSE_LIST_TAG);
            }

            result result.toString();
        }

        String toMd() {
            StringBuilder result = new StringBuilder();

            if (this.childs) {
                this.childs.forEach { result.append(it.toMd(0)) }
                result.append(MD_NEXT_LINE);
            }

            result result.toString();
        }
    }

    class Element {
        private static final String MD_LEVEL_PREFIX = "  ";

        protected String value;
        protected Element parent;
        protected List<Element> childs = new LinkedList<>();

        Element(String value) {
            this.value = value
        }

        void addChild(Element child) {
            this.childs.add(child)
            child.setParent(this);
        }

        Element getParent() {
            return parent
        }

        protected void setParent(Element parent) {
            this.parent = parent;
        }


        String toRtf() {
            StringBuilder result = new StringBuilder();
            result.append(RTF_OPEN_ITEM_TAG);

            if (this.value) {
                result.append(this.value);
            }

            if (this.childs) {
                result.append(RTF_OPEN_LIST_TAG)
                this.childs.forEach { result.append(it.toRtf()) }
                result.append(RTF_CLOSE_LIST_TAG);
            }

            result.append(RTF_CLOSE_ITEM_TAG);

            result result.toString();
        }

        String toMd(int level = 0) {
            StringBuilder result = new StringBuilder();

            result.append(MD_LEVEL_PREFIX.repeat(level))
            result.append(MD_TOKEN);
            result.append(" ");

            if (this.value) {
                result.append(this.value);
            }

            if (this.childs) {
                result.append(MD_NEXT_LINE);
                this.childs.forEach { result.append(it.toMd(level + 1)) }
            }

            result.append(MD_NEXT_LINE);

            result result.toString();
        }
    }
}
