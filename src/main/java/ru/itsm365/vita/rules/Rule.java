package ru.itsm365.vita.rules;

import ru.itsm365.vita.Node;
import ru.itsm365.vita.NodeType;

import java.util.Collection;

public interface Rule<T extends Node> {
    NodeType getNodeType();

    boolean isNodeStart(String text);

    boolean isNodeEnd(String test);

    T createNode(Node parent, String value);

    T createNode(Node parent, Collection<Node> children);

    String getNodeSuffix(T node);

    String getNodePostfix(T node);

    String getNodeValue(T node);
}
