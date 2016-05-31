package searcher.persistence;

import common.domain.*;
import common.persistence.PersistenceController;
import javafx.collections.ObservableList;
import searcher.models.SemanticPath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static common.persistence.FileHandler.handlePath;
import static common.persistence.FileHandler.readFile;
import static common.persistence.FileHandler.writeFile;


public class ExtendedPersistenceController extends PersistenceController {

    private final String spFileName = "semanticPaths.txt";
    private final ObservableList<SemanticPath> semanticPaths;

    public ExtendedPersistenceController(Graph graph, ObservableList<SemanticPath> semanticPaths) {
        super(graph);
        this.semanticPaths = semanticPaths;
    }

    public void exportSemanticPaths(String path) {
        path = handlePath(path) + spFileName;
        ArrayList<String> strings = new ArrayList<>();
        for (SemanticPath s : semanticPaths) {
            strings.add(s.convertToExport());
        }
        writeFile(path, strings, false);
    }

    public void importSemanticPaths(String path) {
        path = handlePath(path) + spFileName;
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            Graph graph = super.getGraph();
            List<String> strings = readFile(path);
            for (String s : strings) {
                SemanticPathSerializer serializer = new SemanticPathSerializer(s);
                ArrayList<NodeType> types = serializer.getTypes();
                ArrayList<Relation> alr = new ArrayList<>();
                for (int i = 0; i < types.size() - 1; ++i) {
                    alr.add(graph.getOrCreateRelation(types.get(i), types.get(i+1), serializer.getName()));
                }
                try {
                    semanticPaths.add(new SemanticPath(
                            serializer.getName(), serializer.getInitialType(), serializer.getFinalType(),
                            new RelationStructure(types.get(0), alr, types.get(types.size() - 1))));
                } catch (RelationStructureException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
