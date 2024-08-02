package ru.itsm365.vita

import ru.itsm365.vita.nodes.Node
import ru.itsm365.vita.nodes.RootBoxNode
import ru.itsm365.vita.rules.Rule


class Parser {
    Integer startCursor;
    Integer endCursor;

    Map<Class<Node>, Rule> nodeRules;
    Map<Class<Node>, Rule> availableNodeRules;

    Node currentNode;
    Rule currentRules;

    Parser(Map<Class<Node>, Rule> nodeRules) {
        this.nodeRules = nodeRules
        this.setCurrentNode(new RootBoxNode(null));
    }

    RootBoxNode parse(String text) {
        this.startCursor = 0;

        for (this.endCursor = 0; this.endCursor < text.length(); this.endCursor++) {
            if (this.currentRules.isNodeAttributeStart(text.substring(this.startCursor, this.endCursor))) {
                Integer startAttributesCursor = this.endCursor;
                for (this.endCursor; this.endCursor < text.length() || this.currentRules.isNodeAttributeEnd(text.substring(startAttributesCursor, this.endCursor)); this.endCursor++) {
                }
                this.currentRules.saveNodeAttributeStart(text.substring(startAttributesCursor, this.endCursor), this.currentNode);
            }


        }

        return null;
    }

    private void setCurrentNode(Node node) {
        this.currentNode = new RootBoxNode(null);
        this.currentRules = this.nodeRules.get(this.endCursor.class);
        this.availableNodeRules = this.currentRules.availableChildren().collect { this.nodeRules.get(it) } as Map<Class<Node>, Rule>;
    }
}

