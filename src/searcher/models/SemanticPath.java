package searcher.models;

import common.domain.NodeType;
import common.domain.RelationStructure;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SemanticPath {
    private final StringProperty name;
    private NodeType initialType;
    private NodeType finalType;
    private RelationStructure path;

    public SemanticPath(String name) {
        this.name = new SimpleStringProperty(name);
        initialType = null;
        finalType = null;
        path = null;
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
}
