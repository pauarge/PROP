package searcher.models;

import common.domain.Node;


public class RelevanceModel {
    private Node origin;
    private Node destiny;
    private double relevance;

    public RelevanceModel(Node origin, Node destiny, double relevance){
        this.origin = origin;
        this.destiny = destiny;
        this.relevance = relevance;
    }

    public Node getOrigin() {
        return origin;
    }

    public Node getDestiny() {
        return destiny;
    }

    public double getRelevance() {
        return relevance;
    }
}
