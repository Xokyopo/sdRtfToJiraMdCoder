package ru.itsm365.vita;

import ru.itsm365.vita.rules.Rule;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Node implements Iterable<Node> {
    public final NodeType nodeType;
    public final Rule rule;
    public final Node parent;

    private final Collection<Node> nodes = new LinkedList<>();

    public Node(NodeType nodeType, Rule rule, Node parent) {
        this.nodeType = nodeType;
        this.rule = rule;
        this.parent = parent;
    }

    public void add(Node node) {
        this.nodes.add(node);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }
}
