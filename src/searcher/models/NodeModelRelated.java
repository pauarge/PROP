package searcher.models;

import common.domain.Node;
import common.domain.NodeType;
import common.domain.Relation;

public class NodeModelRelated extends NodeModel {
    private Relation edge;

    public NodeModelRelated(Node node, NodeType nodeType, Relation edge) {
        super(node, nodeType);
        this.edge = edge;
    }

    public Relation getEdge() {
        return edge;
    }
}
