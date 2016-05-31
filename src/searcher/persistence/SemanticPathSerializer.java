package searcher.persistence;

import common.domain.NodeType;
import common.domain.Relation;
import common.domain.RelationStructure;
import common.domain.RelationStructureException;
import searcher.Utils;
import searcher.models.SemanticPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;


class SemanticPathSerializer {

    private SemanticPath semanticPath;
    private String data;

    public SemanticPathSerializer(SemanticPath semanticPath) {
        this.semanticPath = semanticPath;
        this.deflate();
    }

    public SemanticPathSerializer(String data, Collection<Relation> relations) {
        this.data = data;
        this.inflate(relations);
    }

    public SemanticPath getSemanticPath() {
        return semanticPath;
    }

    public String getData() {
        return data;
    }

    private void inflate(Collection<Relation> allRelations) {
        StringTokenizer tokenizer = new StringTokenizer(data, "\t");
        if (tokenizer.countTokens() < 4) throw new RuntimeException("Serialized SemanticPath has incorrect format");

        String name = tokenizer.nextToken();
        NodeType first = Utils.getType(tokenizer.nextToken());
        NodeType last = Utils.getType(tokenizer.nextToken());

        ArrayList<Relation> relationPath = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String nextName = tokenizer.nextToken();
            Relation nextRel = allRelations.stream().filter(r -> r.getValue().equals(nextName))
                    .findFirst().orElseThrow(() -> new RuntimeException("EdgeType \"" + nextName + "\" does not exist"));
            relationPath.add(nextRel);
        }

        RelationStructure structure = null;
        try {
             structure = new RelationStructure(first, relationPath, last);
        } catch (RelationStructureException e) {
            throw new RuntimeException("RelationStructure failed and implementation gives no reason");
        }
        semanticPath = new SemanticPath(name, first, last, structure);
    }

    private void deflate() {
        StringBuilder builder = new StringBuilder();
        builder.append(semanticPath.getName()).append('\t');
        builder.append(Utils.getName(semanticPath.getInitialType())).append('\t');
        builder.append(Utils.getName(semanticPath.getFinalType())).append('\t');
        for (Relation r : semanticPath.getPath()) {
            builder.append(r.getName()).append('\t');
        }
        data = builder.toString();
    }

}
