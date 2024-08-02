package ru.itsm365.vita.nodes

abstract class Node implements Iterable<Node> {

    public final Node parent;
    private final Collection<Node> children = new LinkedList<>();

    Node(Node parent) {
        this.parent = parent;
    }

    void add(Node node) {
        this.children.add(node);
    }

    @Override
    Iterator<Node> iterator() {
        return children.iterator();
    }
}

