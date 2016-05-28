package searcher.persistence;


import common.domain.Graph;
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

    private String spFileName = "semanticPaths.txt";
    private ObservableList<SemanticPath> semanticPaths;

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
            List<String> strings = readFile(path);
            for (String s : strings) {
                SemanticPathSerializer serializer = new SemanticPathSerializer(s);
                SemanticPath sp = new SemanticPath(
                        serializer.getName(), serializer.getInitialType(), serializer.getFinalType(), serializer.getRelationStructure());
                semanticPaths.add(sp);
            }
        }
    }

}
