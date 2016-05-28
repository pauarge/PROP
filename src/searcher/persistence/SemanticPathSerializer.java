package searcher.persistence;

import common.domain.NodeType;

import java.io.Serializable;
import java.util.ArrayList;


public class SemanticPathSerializer implements Serializable {

    private String data;
    private String name;
    private ArrayList<NodeType> types;

    private void inflate() {

    }

    public SemanticPathSerializer(String data) {
        this.data = data;
    }

    public String getData() {
        this.inflate();
        return data;
    }

    public String getName() {
        this.inflate();
        return name;
    }

    public ArrayList<NodeType> getTypes() {
        this.inflate();
        return types;
    }
}
