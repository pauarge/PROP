package searcher.persistence;

import common.domain.Graph;
import common.domain.Relation;
import common.persistence.PersistenceController;
import searcher.models.SemanticPath;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static common.persistence.FileHandler.*;


public class ExtendedPersistenceController extends PersistenceController {

    private final String spFileName = "semanticPaths.txt";
    private final Collection<SemanticPath> semanticPaths;
    private final Collection<Relation> edgeTypes;

    public ExtendedPersistenceController(
            Graph graph, Collection<SemanticPath> semanticPaths, Collection<Relation> edgeTypes ) {
        super(graph);
        this.semanticPaths = semanticPaths;
        this.edgeTypes = edgeTypes;
    }

    public void exportSemanticPaths(String path) {
        path = handlePath(path) + spFileName;
        ArrayList<String> strings = new ArrayList<>();
        semanticPaths.stream().map(sp -> (new SemanticPathSerializer(sp)).getData()).forEach(strings::add);
        writeFile(path, strings, false);
    }

    public void importSemanticPaths(String path) {
        path = handlePath(path) + spFileName;
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            Graph graph = super.getGraph();
            List<String> serializedPath = readFile(path);
            for (String s : serializedPath) {
                SemanticPath semanticPath = (new SemanticPathSerializer(s, edgeTypes)).getSemanticPath();
                semanticPaths.add(semanticPath);
            }
        }
    }

    public void importDir(String path) {
        this.importNodes(path);
        this.importEdges(path);
        Iterator iter = this.getGraph().getRelationIterator();
        while (iter.hasNext()) {
            Relation r = (Relation) iter.next();
            if(!r.isDefault())
                edgeTypes.add(r);
        }
        this.importSemanticPaths(path);
    }

}
