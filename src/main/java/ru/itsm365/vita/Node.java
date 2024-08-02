package ru.itsm365.vita;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Node implements Iterable<Node> {

    public final Node parent;
    private final Collection<Node> children = new LinkedList<>();

    public Node(Node parent) {
        this.parent = parent;
    }

    public void add(Node node) {
        this.children.add(node);
    }

    @Override
    public Iterator<Node> iterator() {
        return children.iterator();
    }
}
