package ru.itsm365.vita.coder.block

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class MarkedListCoder implements Coder {
    private static final String MD_FULL_LIST_REGEXP_PATTERN = "(?!\\n)\\s*\\d\\.\\s[^\$]*";
    private static final String MD_EMPTY_LINE = "\n\n";
    private static final String MD_NEXT_LINE = "\n";
    private static final String RTF_LIST_TAG_REGEXP_PATTERN = "(<(?:ol|li)>)|(</(?:ol|li)>)";
    private static final String RTF_OPEN_LIST_TAG = "<ol>";
    private static final String RTF_CLOSE_LIST_TAG = "</ol>";
    private static final String RTF_OPEN_ITEM_TAG = "<li>";
    private static final String RTF_CLOSE_ITEM_TAG = "</li>";

    @Override
    String toRtf(String s) {
        StringBuilder result = new StringBuilder();
        List<String> blocks = s.split(MD_EMPTY_LINE);

        def count = 0;
        for (String block in blocks) {
            count++;
            String list = block.find(MD_FULL_LIST_REGEXP_PATTERN);

            if (list) {
                Integer startAt = block.length() - list.length();
                if (startAt > 0) {
                    result.append(block.substring(0, startAt));
                }
                result.append(convertListToRtf(block.substring(startAt, block.length())));
            } else {
                result.append(block);
                if (count < blocks.size()) {
                    result.append(MD_EMPTY_LINE);
                }
            }
        }

        return result.toString();
    }

    @Override
    String toMd(String s) {
        StringBuilder result = new StringBuilder();

        Integer cursor = 0;

        Pattern pattern = Pattern.compile(RTF_LIST_TAG_REGEXP_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        while (matcher.find()) {
            Integer firstGroupCursor = matcher.start();
            String group = matcher.group().toLowerCase();
            if (group == RTF_OPEN_LIST_TAG) {
                Stack<String> stack = [];
                stack.push(group);
                while (!stack.isEmpty() && matcher.find()) {
                    String currentTag = matcher.group().toLowerCase();
                    if (currentTag == RTF_OPEN_LIST_TAG || currentTag == RTF_OPEN_ITEM_TAG) {
                        stack.push(currentTag);
                    } else if (currentTag == RTF_CLOSE_LIST_TAG) {
                        if (stack.last() == RTF_OPEN_LIST_TAG) {
                            stack.pop();
                        } else {
                            break;
                        }
                    } else if (currentTag == RTF_CLOSE_ITEM_TAG) {
                        if (stack.last() == RTF_OPEN_ITEM_TAG) {
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
                        if (!otherText.endsWith(MD_NEXT_LINE)) {
                            result.append(MD_NEXT_LINE);
                        }
                    }

                    result.append(convertListToMD(s.substring(firstGroupCursor, matcher.end())));
                    cursor = matcher.end();
                    if (cursor < s.length()) {
                        result.append(MD_EMPTY_LINE);
                    }
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

        String startPrefix = "^\\s*\\d*\\.\\s";
        Stack<Integer> levels = [];
        Element parent = rootElement;
        Element lastElement = parent;
        List<String> lines = s.split("\n");

        for (String line in lines) {
            String currentValue = line.replaceFirst(startPrefix, "");
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
        NodeList ols = xmlList.ol;
        Node ol = ols.find { it instanceof Node };
        Stack<Iterator<Node>> iteratorsStack = [];
        iteratorsStack.push(ol.iterator());

        Element rootElement = new RootElement();

        while (!iteratorsStack.isEmpty()) {
            Iterator<Node> lastIterator = iteratorsStack.pop();
            while (lastIterator.hasNext()) {
                Object element = lastIterator.next();
                if (element instanceof Node && element.name() == "li") {
                    String nodeName = getNodeName(element);
                    Element currentElement = new Element(nodeName);
                    rootElement.addChild(currentElement);
                    if (element.children().size() > 1) {
                        iteratorsStack.push(lastIterator);
                        lastIterator = element.iterator();
                        rootElement = currentElement;
                    }
                } else if (element instanceof Node && element.name() == "ol") {
                    iteratorsStack.push(lastIterator);
                    lastIterator = element.iterator();
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
                int count = 1;
                this.childs.forEach { result.append(it.toMd(0, count++)) }
            }

            //Удаляем последний перенос строки, так как это выгодней чем проверка в циклах.
            result.setLength(result.size() - MD_NEXT_LINE.size());
            return result.toString();
        }

        @Override
        String toRtf() {
            StringBuilder result = new StringBuilder();

            if (this.childs) {
                result.append(RTF_OPEN_LIST_TAG)
                this.childs.forEach { result.append(it.toRtf()) }
                result.append(RTF_CLOSE_LIST_TAG);
            }

            return result.toString();
        }

    }

    class Element {
        private static final String MD_LEVEL_PREFIX = " ".repeat(3);

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

            return result.toString();
        }

        String toMd(int level = 0, int number = 1) {
            StringBuilder result = new StringBuilder();

            result.append(MD_LEVEL_PREFIX.repeat(level))
            result.append(number);
            result.append(". ");

            if (this.value) {
                result.append(this.value);
            }

            if (this.childs) {
                result.append(MD_NEXT_LINE);
                int count = 1;
                this.childs.forEach { result.append(it.toMd(level + 1, count++)) }
            } else {
                result.append(MD_NEXT_LINE);
            }

            return result.toString();
        }
    }
}
