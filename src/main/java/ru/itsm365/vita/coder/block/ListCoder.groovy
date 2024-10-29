package ru.itsm365.vita.coder.block

import ru.itsm365.vita.coder.Coder

import java.util.regex.Matcher
import java.util.regex.Pattern

class ListCoder implements Coder {
    private final Coder numberedCoder;
    private final Coder markedCoder;

    {
        this.numberedCoder = new AllListCoder(AllListCoderSetting.NUMBERED);
        this.markedCoder = new AllListCoder(AllListCoderSetting.MARKED);
    }

    @Override
    String toRtf(String s) {
        String afterNumberedCoder = this.numberedCoder.toRtf(s);
        return this.markedCoder.toRtf(afterNumberedCoder);
    }

    @Override
    String toMd(String s) {
        String afterNumberedCoder = this.numberedCoder.toMd(s);
        return this.markedCoder.toMd(afterNumberedCoder);
    }

    private enum AllListCoderSetting {
        NUMBERED("ol", "#", "(?<!.)(?!\\n)\\s*#\\s[^\$]*", "^\\s*#\\s"),
        MARKED("ul", "*", "(?<!.)(?!\\n)\\s*\\*\\s[^\$]*", "^\\s*\\*\\s");

        final String rtfListTagName;
        final String mdListTag;
        final String mdListRegexpPattern;
        final String mdStartListItemRegexpPattern;

        AllListCoderSetting(
                String rtfListTagName,
                String mdListTag,
                String mdListRegexpPattern,
                String mdStartListItemRegexpPattern
        ) {
            this.rtfListTagName = rtfListTagName
            this.mdListTag = mdListTag
            this.mdListRegexpPattern = mdListRegexpPattern
            this.mdStartListItemRegexpPattern = mdStartListItemRegexpPattern
        }
    }

    private static class AllListCoder implements Coder {
        private static final String MD_EMPTY_LINE = "\n\n";
        private static final String MD_NEXT_LINE = "\n";
        private static final String RTF_LIST_ITEM_TAG_NAME = "li";
        private static final String RTF_OPEN_ITEM_TAG = "<${RTF_LIST_ITEM_TAG_NAME}>";
        private static final String RTF_CLOSE_ITEM_TAG = "</${RTF_LIST_ITEM_TAG_NAME}>";
        private final String rtfListTagName;
        private final String rtfOpenListTag;
        private final String rtfCloseListTag;
        private final String rtfListTagRegexpPattern;
        private final String mdListRegexpPattern;
        private final String mdStartListItemRegexpPattern;
        private final String mdListTag;

        AllListCoder(AllListCoderSetting setting) {
            this.mdListTag = setting.mdListTag;
            this.mdStartListItemRegexpPattern = setting.mdStartListItemRegexpPattern;
            this.mdListRegexpPattern = setting.mdListRegexpPattern;
            this.rtfListTagName = setting.rtfListTagName;
            this.rtfOpenListTag = "<${rtfListTagName}>";
            this.rtfCloseListTag = "</${rtfListTagName}>";
            this.rtfListTagRegexpPattern = "(<(?:${rtfListTagName}|li)>)|(</(?:${rtfListTagName}|li)>)";
        }

        @Override
        String toRtf(String s) {
            StringBuilder result = new StringBuilder();
            List<String> blocks = s.split(MD_EMPTY_LINE);

            def count = 0;
            for (String block in blocks) {
                count++;
                String list = block.find(mdListRegexpPattern);

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

            Pattern pattern = Pattern.compile(rtfListTagRegexpPattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(s);

            while (matcher.find()) {
                Integer firstGroupCursor = matcher.start();
                String group = matcher.group().toLowerCase();
                if (group == rtfOpenListTag) {
                    Stack<String> stack = [];
                    stack.push(group);
                    while (!stack.isEmpty() && matcher.find()) {
                        String currentTag = matcher.group().toLowerCase();
                        if (currentTag == rtfOpenListTag || currentTag == RTF_OPEN_ITEM_TAG) {
                            stack.push(currentTag);
                        } else if (currentTag == rtfCloseListTag) {
                            if (stack.last() == rtfOpenListTag) {
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

            Stack<Integer> levels = [];
            Element parent = rootElement;
            Element lastElement = parent;
            List<String> lines = s.split("\n");

            for (String line in lines) {
                String currentValue = line.replaceFirst(mdStartListItemRegexpPattern, "");
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
            NodeList rtfList = xmlList[rtfListTagName] ?: xmlList[rtfListTagName.toUpperCase()];
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
                        if (RTF_LIST_ITEM_TAG_NAME == tagName) {
                            String nodeName = getNodeName(element);
                            Element currentElement = new Element(nodeName);
                            rootElement.addChild(currentElement);
                            if (element.children().size() > 1) {
                                iteratorsStack.push(lastIterator);
                                lastIterator = element.iterator();
                                rootElement = currentElement;
                            }
                        } else if (rtfListTagName == tagName) {
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
                    result.append(rtfOpenListTag)
                    this.childs.forEach { result.append(it.toRtf()) }
                    result.append(rtfCloseListTag);
                }

                return result.toString();
            }

        }

        class Element {
            private static final String MD_LEVEL_PREFIX = " ".repeat(4);

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
                    result.append(rtfOpenListTag)
                    this.childs.forEach { result.append(it.toRtf()) }
                    result.append(rtfCloseListTag);
                }

                result.append(RTF_CLOSE_ITEM_TAG);

                return result.toString();
            }

            String toMd(int level = 0) {
                StringBuilder result = new StringBuilder();

                result.append(MD_LEVEL_PREFIX.repeat(level))
                result.append(mdListTag);
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
    }
}
