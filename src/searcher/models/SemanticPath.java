package searcher.models;

import common.domain.NodeType;
import common.domain.RelationStructure;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import searcher.Utils;

public class SemanticPath {
    private final StringProperty name;
    private NodeType initialType;
    private NodeType finalType;
    private RelationStructure path;

    public SemanticPath(String name, NodeType initialType, NodeType finalType, RelationStructure path) {
        this(name);
        this.initialType = initialType;
        this.finalType = finalType;
        this.path = path;
    }

    public SemanticPath(String name) {
        this.name = new SimpleStringProperty(name);
        initialType = null;
        finalType = null;
        path = null;
    }

    public SemanticPath(String name, NodeType[] path) {
        this.name = new SimpleStringProperty(name);
        this.initialType = path[0];
        this.finalType = path[path.length-1];
        this.path = null;
    }

    public String toString() {
        return getName() + ": " + Utils.convertToText(Utils.toTypeArray(this));
    }

    public String convertToExport() {
        String path = getName() + ": ";
        if (Utils.toTypeArray(this).length == 0) return path;
        for (NodeType nt : Utils.toTypeArray(this)) {
            path = path.concat(nt.toString() + " - ");
        }
        return path.substring(0, path.length() - 3);
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
