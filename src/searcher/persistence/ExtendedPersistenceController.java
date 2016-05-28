package searcher.persistence;


import common.domain.Graph;
import common.persistence.PersistenceController;
import javafx.collections.ObservableList;
import searcher.models.SemanticPath;

import java.util.ArrayList;
import java.util.List;

import static common.persistence.FileHandler.handlePath;
import static common.persistence.FileHandler.readFile;
import static common.persistence.FileHandler.writeFile;


public class ExtendedPersistenceController extends PersistenceController {

    private String spFileName = "semanticPaths.txt";
    private ObservableList<SemanticPath> sp;

    public ExtendedPersistenceController(Graph graph) {
        super(graph);
    }

    public ExtendedPersistenceController(Graph graph, ObservableList<SemanticPath> sp) {
        super(graph);
        this.sp = sp;
    }

    public void exportSemanticPaths(String path) {
        path = handlePath(path) + spFileName;
        ArrayList<String> strings = new ArrayList<>();
        for (SemanticPath s : sp) {
            strings.add(s.toString());
        }
        writeFile(path, strings, false);
    }

    public void importSemanticPaths(String path) {
        path = handlePath(path) + spFileName;
        List<String> strings = readFile(path);
        for (String s : strings) {
            SemanticPathSerializer serializer = new SemanticPathSerializer(s);

        }
    }

}
