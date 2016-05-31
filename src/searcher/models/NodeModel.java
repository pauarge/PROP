package searcher.models;

import common.domain.Node;
import common.domain.NodeType;

public class NodeModel {
    private final Node node;
    private final NodeType nodeType;

    public NodeModel(Node node, NodeType nodeType) {
        this.node = node;
        this.nodeType = nodeType;
    }

    public Node getNode() {
        return node;
    }

    public NodeType getNodeType() {
        return nodeType;
    }
}