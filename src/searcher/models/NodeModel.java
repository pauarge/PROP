package searcher.models;

import javafx.beans.property.SimpleStringProperty;

public class NodeModel {
    private final SimpleStringProperty id = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty value = new SimpleStringProperty("");

    public NodeModel() {
        this("", "", "");
    }

    public NodeModel(int id, String type, String value) {
        this(String.valueOf(id), type, value);
    }

    public NodeModel(String id, String type, String value) {
        setId(id);
        setType(type);
        setValue(value);
    }

    public String getId() {
        return id.get();
    }

    public int getNumericId() {
        return Integer.parseInt(id.get());
    }

    public void setId(String fName) {
        id.set(fName);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String fName) {
        type.set(fName);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String fName) {
        value.set(fName);
    }
}