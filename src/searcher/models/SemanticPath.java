package searcher.models;

import common.domain.NodeType;
import common.domain.RelationStructure;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import searcher.Utils;

public class SemanticPath {
    private final StringProperty name;
    private final NodeType initialType;
    private final NodeType finalType;
    private final RelationStructure path;

    public SemanticPath(String name, NodeType initialType, NodeType finalType, RelationStructure path) {
        this.name = new SimpleStringProperty(name);
        this.initialType = initialType;
        this.finalType = finalType;
        this.path = path;
    }

    public String toString() {
        return getName() + ": " + Utils.convertToText(Utils.toTypeArray(this));
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public NodeType getInitialType() {
        return initialType;
    }

    public NodeType getFinalType() {
        return finalType;
    }

    public RelationStructure getPath() {
        return path;
    }
}
