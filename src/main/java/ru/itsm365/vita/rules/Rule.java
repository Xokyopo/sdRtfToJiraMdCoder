package ru.itsm365.vita.rules;

import ru.itsm365.vita.Node;

import java.util.Collection;

public interface Rule<T extends Node> extends NodeRule, NodeAttributeRule, NodeChildrenRule, NodeConvertRule<T> {
    T getNodeType();
}

interface NodeRule {
    boolean isNodeStart(String text);
    boolean isNodeEnd(String test);
}

interface NodeAttributeRule {
    boolean isNodeAttributeStart(String text);

    boolean isNodeAttributeEnd(String test);
}

interface NodeChildrenRule {
    Collection<Class<Node>> availableChildren();
}

interface NodeConvertRule<T extends Node> {
    T createNode(Node parent);

    void saveNodeAttributeStart(String text, T target);

    String getPrefix(Node node);

    String getValue(Node node);

    String getPostfix(Node node);
}
