package ru.itsm365.vita.coder.block

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class AllListCoder implements Coder {
    private static final String MD_EMPTY_LINE = "\n\n";
    private static final String MD_NEXT_LINE = "\n";

    private final Setting setting;

    AllListCoder(Setting setting) {
        this.setting = setting;
    }

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> blocks = s.split(MD_EMPTY_LINE);

        def count = 0;
        for (int i = 0; i < blocks.size(); i++) {
            String block = blocks.get(i);
            count++;
            String list = block.find(setting.mdListRegexpPattern);

            if (list) {
                Integer startAt = block.length() - list.length();
                if (startAt > 0) {
                    result.append(block.substring(0, startAt));
                }
                result.append(convertListToRtf(block.substring(startAt, block.length())));
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

        Integer cursor = 0;

        Pattern pattern = Pattern.compile(setting.rtfListTagRegexpPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        while (matcher.find()) {
            Integer firstGroupCursor = matcher.start();
            String group = matcher.group().toLowerCase();
            if (group.startsWith(setting.rtfListTagName, 1)) {
                Stack<String> stack = [];
                stack.push(group);
                while (!stack.isEmpty() && matcher.find()) {
                    String currentTag = matcher.group().toLowerCase();
                    if (currentTag.startsWith(setting.rtfListTagName, 1) || currentTag.startsWith(setting.RTF_LIST_ITEM_TAG_NAME, 1)) {
                        stack.push(currentTag);
                    } else if (currentTag == setting.rtfCloseListTag) {
                        if (stack.last().startsWith(setting.rtfListTagName, 1)) {
                            stack.pop();
                        } else {
                            break;
                        }
                    } else if (currentTag == setting.RTF_CLOSE_ITEM_TAG) {
                        if (stack.last().startsWith(setting.RTF_LIST_ITEM_TAG_NAME, 1)) {
                            stack.pop();
                        } else {
                            break;
                        }
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
        Element rootElement = new RootElement();

        Stack<Integer> levels = [];
        Element parent = rootElement;
        Element lastElement = parent;
        List<String> lines = s.split("\n");

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

        return rootElement.toRtf();
    }

    private String convertListToMD(String notFixedXml) {
        String s = "<root>${notFixedXml}</root>";
        Node xmlList = new XmlParser().parseText(s);
        NodeList rtfList = xmlList[setting.rtfListTagName] ?: xmlList[setting.rtfListTagName.toUpperCase()];
        Node firstList = rtfList.find { it instanceof Node };
        Stack<Iterator<Node>> iteratorsStack = [];
        iteratorsStack.push(firstList.iterator());

        Element rootElement = new RootElement();

        while (!iteratorsStack.isEmpty()) {
            Iterator<Node> lastIterator = iteratorsStack.pop();
            while (lastIterator.hasNext()) {
                Object element = lastIterator.next();
                if (element instanceof Node) {
                    String tagName = element.name()?.toLowerCase();
                    if (setting.RTF_LIST_ITEM_TAG_NAME == tagName) {
                        String nodeName = getNodeName(element);
                        Element currentElement = new Element(nodeName);
                        rootElement.addChild(currentElement);
                        if (element.children().size() > 1) {
                            iteratorsStack.push(lastIterator);
                            lastIterator = element.iterator();
                            rootElement = currentElement;
                        }
                    } else if (setting.rtfListTagName == tagName) {
                        iteratorsStack.push(lastIterator);
                        lastIterator = element.iterator();
                    }
                }
            }
            if (rootElement.haveParent()) {
                rootElement = rootElement.parent;
            }
        }

        return rootElement.toMd();
    }

    private String getNodeName(Node node) {
        Object nodeValue = node.value();
        if (nodeValue instanceof String) {
            return nodeValue;
        }

        if (nodeValue instanceof Collection) {
            Collection c = nodeValue;
            Object firstValue = c[0];
            if (firstValue instanceof String) {
                return firstValue;
            }
        }

        return "";
    }

    private class RootElement extends Element {

        RootElement() {
            super(null)
        }

        @Override
        String toMd(int level = 0) {
            StringBuilder result = new StringBuilder();

            if (this.childs) {
                this.childs.forEach { result.append(it.toMd(0)) }
            }

            //Удаляем последний перенос строки, так как это выгодней чем проверка в циклах.
            result.setLength(result.size() - MD_NEXT_LINE.size());
            return result.toString();
        }

        @Override
        String toRtf() {
            StringBuilder result = new StringBuilder();

            if (this.childs) {
                result.append(setting.rtfOpenListTag)
                this.childs.forEach { result.append(it.toRtf()) }
                result.append(setting.rtfCloseListTag);
            }

            return result.toString();
        }

    }

    class Element {
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

        boolean haveParent() {
            return this.parent != null;
        }

        String toRtf() {
            StringBuilder result = new StringBuilder();
            result.append(setting.RTF_OPEN_ITEM_TAG);

            if (this.value) {
                result.append(this.value);
            }

            if (this.childs) {
                result.append(setting.rtfOpenListTag)
                this.childs.forEach { result.append(it.toRtf()) }
                result.append(setting.rtfCloseListTag);
            }

            result.append(setting.RTF_CLOSE_ITEM_TAG);

            return result.toString();
        }

        String toMd(int level = 0) {
            StringBuilder result = new StringBuilder();

            result.append(setting.MD_LEVEL_PREFIX.repeat(level))
            result.append(setting.mdListTag);
            result.append(" ");

            if (this.value) {
                result.append(this.value);
            }

            if (this.childs) {
                result.append(MD_NEXT_LINE);
                this.childs.forEach { result.append(it.toMd(level + 1)) }
            } else {
                result.append(MD_NEXT_LINE);
            }

            return result.toString();
        }
    }

    protected static class Setting {
        static final String MD_LEVEL_PREFIX = " ".repeat(4);
        static final String RTF_LIST_ITEM_TAG_NAME = "li"
        static final String RTF_OPEN_ITEM_TAG = "<${RTF_LIST_ITEM_TAG_NAME}>";
        static final String RTF_CLOSE_ITEM_TAG = "</${RTF_LIST_ITEM_TAG_NAME}>";

        final String mdListTag;
        final String mdListRegexpPattern;
        final String mdStartListItemRegexpPattern;

        final String rtfListTagName;
        final String rtfListTagRegexpPattern;
        final String rtfOpenListTag;
        final String rtfCloseListTag;

        Setting(
                String rtfListTagName,
                String rtfListTagRegexpPattern,
                String mdListTag,
                String mdListRegexpPattern,
                String mdStartListItemRegexpPattern
        ) {
            this.rtfListTagName = rtfListTagName
            this.rtfListTagRegexpPattern = rtfListTagRegexpPattern
            this.mdListTag = mdListTag
            this.mdListRegexpPattern = mdListRegexpPattern
            this.mdStartListItemRegexpPattern = mdStartListItemRegexpPattern

            this.rtfOpenListTag = "<${rtfListTagName}>";
            this.rtfCloseListTag = "</${rtfListTagName}>";
        }
    }
}
