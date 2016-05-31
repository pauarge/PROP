package searcher.persistence;

import common.domain.NodeType;
import common.domain.Relation;
import common.domain.RelationStructure;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import searcher.Utils;

import java.io.Serializable;
import java.util.ArrayList;


class SemanticPathSerializer implements Serializable {

    private String data;
    private String name;
    private RelationStructure path;
    private ArrayList<NodeType> types;
    private ArrayList<Relation> alr;


    private void inflate() {
        if (name == null || types.isEmpty() || path == null) {
            int m = data.indexOf(":");
            name = data.substring(0, m);
            for (String type : data.substring(m + 1, data.length()).split("-")) {
                types.add(NodeType.valueOf(type.trim()));
            }
            for (int i = 0; i < types.size() - 1; ++i) {
                alr.add(Utils.getDefaultRelation(types.get(i), types.get(i + 1)));
            }
            try{
                path = new RelationStructure(types.get(0), alr, types.get(types.size() - 1));
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public SemanticPathSerializer(String data) {
        this.data = data;
        this.types = new ArrayList<>();
        this.alr = new ArrayList<>();
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

    public RelationStructure getRelationStructure() {
        this.inflate();
        return path;
    }

}
