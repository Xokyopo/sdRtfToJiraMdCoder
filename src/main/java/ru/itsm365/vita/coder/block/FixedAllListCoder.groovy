package ru.itsm365.vita.coder.block

import ru.itsm365.vita.coder.Coder
import ru.itsm365.vita.coder.model.RtfTag

import java.util.regex.Matcher
import java.util.regex.Pattern

class FixedAllListCoder implements Coder {
    private static final String MD_EMPTY_LINE = System.lineSeparator() * 2;
    private static final String MD_NEXT_LINE = System.lineSeparator();

    //1. Поиск списков.
    private static final String RTF_LIST_TAG_REGEXP_PATTERN = "(<[o,u]l[^><]*>)|(</[o,u]l>)";
    private static final String MD_LIST_REGEXP_PATTERN = "\\h*[\\*,\\#]+\\h[^\$]*";

    //3. Поиск элементов
    private static final String RTF_OPEN_ITEM_TAG_REGEXP_PATTERN = "<li[^><]*>";
    private static final String RTF_LIST_IN_ITEM_TAG_REGEXP_PATTERN = "(<[o,u]l[^><]*>)|(</[o,u]l>)|(</li>)";

    //4. Разделение на маркированный и нумированный.
    private static final ListTypeConfig MARKED_LIST_CONFIG = new ListTypeConfig("*", "ul");
    private static final ListTypeConfig NUMBERED_LIST_CONFIG = new ListTypeConfig("#", "ol");

    //5. Преобразование
    static final String RTF_LIST_ITEM_TAG_NAME = "li"
    static final String RTF_OPEN_ITEM_TAG = "<${RTF_LIST_ITEM_TAG_NAME}>";
    static final String RTF_CLOSE_ITEM_TAG = "</${RTF_LIST_ITEM_TAG_NAME}>";

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> blocks = s.split(MD_EMPTY_LINE);

        for (int i = 0; i < blocks.size(); i++) {
            String block = blocks.get(i);
            String list = block.find(MD_LIST_REGEXP_PATTERN);

            if (list) {
                Integer startAt = block.length() - list.length();
                if (startAt > 0) {
                    result.append(block.substring(0, startAt));
                }

                String b = convertListToRtf(block.substring(startAt, block.length()));

                result.append(b);

                if (block.endsWith(MD_NEXT_LINE)) {
                    result.append(MD_NEXT_LINE)
                }
            } else {
                result.append(block);
            }

            if (i < blocks.size() - 1) {
                result.append(MD_EMPTY_LINE);
            }
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();

        int cursor = 0;

        Pattern pattern = Pattern.compile(RTF_LIST_TAG_REGEXP_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        while (matcher.find(cursor)) {
            int firstGroupCursor = matcher.start();

            RtfTag openListTag = new RtfTag(matcher.group().toLowerCase());
            if (openListTag.open) {
                Stack<RtfTag> stack = []
                stack.push(openListTag)
                while (!stack.isEmpty() && matcher.find(matcher.end())) {
                    RtfTag currentListTag = new RtfTag(matcher.group().toLowerCase());
                    if (!currentListTag.open) {
                        if (stack.isEmpty()) {
                            break;
                        }
                        stack.pop();
                    } else {
                        stack.push(currentListTag)
                    }
                }

                if (stack.isEmpty()) {
                    if (cursor < firstGroupCursor) {
                        String otherText = s.substring(cursor, firstGroupCursor);
                        result.append(otherText);
                    }

                    result.append(convertListToMD(s.substring(firstGroupCursor, matcher.end())));
                    cursor = matcher.end();
                }
            }
        }

        if (cursor < s.length()) {
            result.append(s.substring(cursor, s.length()));
        }

        return result.toString();
    }

    private String convertListToRtf(String s) {
        /*Element rootElement = new RootElement();

        Stack<Integer> levels = [];
        Element parent = rootElement;
        Element lastElement = parent;
        List<String> lines = s.split(MD_NEXT_LINE);

        for (String line in lines) {
            String currentValue = line.replaceFirst(setting.mdStartListItemRegexpPattern, "");
            Integer currentLevel = line.length() - currentValue.length();
            if (currentLevel > 0) {
                if (levels.isEmpty()) {
                    levels.push(currentLevel);
                } else {
                    if (levels.last() < currentLevel) {
                        levels.push(currentLevel);
                        parent = lastElement;
                    } else {
                        while (!levels.isEmpty() && levels.last() > currentLevel) {
                            levels.pop();
                            lastElement = parent;
                            parent = lastElement.parent;
                        }

                    }
                }
                lastElement = new Element(currentValue);
                parent.addChild(lastElement);
            } else if (lastElement) {
                lastElement.value += currentValue;
            }
        }

        return rootElement.toRtf();*/
        return s;
    }

    private String convertListToMD(String rtfList, int level = 0) {
        StringBuilder stringBuilder = new StringBuilder();
        ListTypeConfig listTypeConfig = rtfList.endsWithIgnoreCase(MARKED_LIST_CONFIG.rtfCloseListTag) ? MARKED_LIST_CONFIG : NUMBERED_LIST_CONFIG
        int cursor = 0;

        Pattern rtfOpenItemTagPattern = Pattern.compile(RTF_OPEN_ITEM_TAG_REGEXP_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher openItemTagMather = rtfOpenItemTagPattern.matcher(rtfList);
        Pattern rtfListInItemTagPattern = Pattern.compile(RTF_LIST_IN_ITEM_TAG_REGEXP_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher listTagMather = rtfListInItemTagPattern.matcher(rtfList);
        Pattern rtfListTagPattern = Pattern.compile(RTF_LIST_TAG_REGEXP_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher openCloseListTagMather = rtfListTagPattern.matcher(rtfList);

        while (openItemTagMather.find(cursor)) {
            listTagMather.find(openItemTagMather.end());

            stringBuilder.append(listTypeConfig.mdListTagName.repeat(level + 1));
            stringBuilder.append(" ");
            stringBuilder.append(rtfList.substring(openItemTagMather.end(), listTagMather.start()))
            cursor = listTagMather.end()

            RtfTag currentRtfTag = new RtfTag(listTagMather.group());
            if (currentRtfTag.name != RTF_LIST_ITEM_TAG_NAME) {

                Stack<RtfTag> openTags = [];
                openTags.push(currentRtfTag);
                while (!openTags.isEmpty() && openCloseListTagMather.find(cursor)) {
                    cursor = openCloseListTagMather.end();
                    currentRtfTag = new RtfTag(openCloseListTagMather.group());
                    if (currentRtfTag.open) {
                        openTags.push(currentRtfTag)
                    } else {
                        openTags.pop();
                    }
                }

                stringBuilder.append(MD_NEXT_LINE)
                stringBuilder.append(convertListToMD(rtfList.substring(listTagMather.start(), cursor), level + 1));
            }

            stringBuilder.append(MD_NEXT_LINE)
        }

        if (stringBuilder.endsWithAny(MD_NEXT_LINE)) {
            stringBuilder.setLength(stringBuilder.length() - MD_NEXT_LINE.length());
        }

        return stringBuilder.toString();
    }

//    private class RootElement extends Element {
//
//        RootElement() {
//            super(null)
//        }
//
//        @Override
//        String toRtf() {
//            StringBuilder result = new StringBuilder();
//
//            if (this.childs) {
//                result.append(setting.rtfOpenListTag)
//                this.childs.forEach { result.append(it.toRtf()) }
//                result.append(setting.rtfCloseListTag);
//            }
//
//            return result.toString();
//        }
//
//    }
//
//    class Element {
//        protected String value;
//        protected Element parent;
//        protected List<Element> childs = new LinkedList<>();
//
//        Element(String value) {
//            this.value = value
//        }
//
//        void addChild(Element child) {
//            this.childs.add(child)
//            child.setParent(this);
//        }
//
//        Element getParent() {
//            return parent
//        }
//
//        protected void setParent(Element parent) {
//            this.parent = parent;
//        }
//
//        boolean haveParent() {
//            return this.parent != null;
//        }
//
//        String toRtf() {
//            StringBuilder result = new StringBuilder();
//            result.append(setting.RTF_OPEN_ITEM_TAG);
//
//            if (this.value) {
//                result.append(this.value);
//            }
//
//            if (this.childs) {
//                result.append(setting.rtfOpenListTag)
//                this.childs.forEach { result.append(it.toRtf()) }
//                result.append(setting.rtfCloseListTag);
//            }
//
//            result.append(setting.RTF_CLOSE_ITEM_TAG);
//
//            return result.toString();
//        }
//    }

    static class ListTypeConfig {
        final String mdListTagName;
        final String rtfListTagName;
        final String rtfOpenListTag;
        final String rtfCloseListTag;

        ListTypeConfig(String mdListTagName, String rtfListTagName) {
            this.mdListTagName = mdListTagName
            this.rtfListTagName = rtfListTagName
            this.rtfOpenListTag = "<${rtfListTagName}>";
            this.rtfCloseListTag = "</${rtfListTagName}>";
        }
    }
}
