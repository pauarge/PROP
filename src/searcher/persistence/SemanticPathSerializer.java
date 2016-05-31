package searcher.persistence;

import common.domain.NodeType;

import java.util.ArrayList;


class SemanticPathSerializer {

    private final String data;
    private String name;
    private final ArrayList<NodeType> types;

    private void inflate() {
        if (name == null || types.isEmpty()) {
            int m = data.indexOf(":");
            name = data.substring(0, m);
            for (String type : data.substring(m + 1, data.length()).split("-")) {
                types.add(NodeType.valueOf(type.trim()));
            }
        }
    }

    public SemanticPathSerializer(String data) {
        this.data = data;
        this.types = new ArrayList<>();
    }

    public String getData() {
        this.inflate();
        return data;
    }

    public String getName() {
        this.inflate();
        return name;
    }

    public NodeType getInitialType() {
        this.inflate();
        return types.get(0);
    }

    public NodeType getFinalType() {
        this.inflate();
        return types.get(types.size() - 1);
    }

    public ArrayList<NodeType> getTypes(){
        this.inflate();
        return types;
    }

}
